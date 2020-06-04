package pl.jacekniezgoda.homedoc.service;

import pl.jacekniezgoda.homedoc.configuration.HomeDocRels;
import pl.jacekniezgoda.homedoc.model.HomeDocLink;
import pl.jacekniezgoda.homedoc.model.HomeDocResource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class HomeDocServiceTest {

    @InjectMocks
    private HomeDocService sut;

    @Mock
    private HomeDocRels homeDocRels;
    @Mock
    private List<HomeDocLink> homeDocLinks;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        initMocks(this.homeDocRels);

        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        when(authentication.getAuthorities()).thenReturn((Collection) authorities);
        when(homeDocRels.relOf(anyString())).thenAnswer(invocation -> invocation.getArguments()[0]);
    }

    @Test
    public void homeDocShouldReturnLinkWhenNoAuthorities() throws Exception {
        when(homeDocLinks.stream()).thenReturn(
                Collections.singletonList(
                        HomeDocLink
                                .builder()
                                .rel("rel")
                                .href("/api")
                                .method("GET")
                                .type("application/json")
                                .describedBy("/described-by-url")
                                .template("/template-url")
                                .authorities(null)
                                .build()
                ).stream()
        );

        HomeDocResource result = sut.homeDoc("http://localhost:8080");

        assertNotNull(result);
        assertNotNull(result.getLinks());
        assertEquals(1, result.getLinks().size());
        assertTrue(result.getLinks().get(0) instanceof HomeDocLink);

        HomeDocLink link = (HomeDocLink) result.getLinks().get(0);

        assertEquals("rel", link.getRel());
        assertEquals("http://localhost:8080/api", link.getHref());
        assertEquals("GET", link.getMethod());
        assertEquals("http://localhost:8080/described-by-url", link.getDescribedBy());
        assertEquals("http://localhost:8080/api/template-url", link.getTemplate());
        assertEquals("application/json", link.getType());
        assertNull(link.getAuthorities());
    }

    @Test
    public void homeDocShouldReturnLinkWhenAuthorizedToAuthority() throws Exception {
        when(homeDocLinks.stream()).thenReturn(
                Collections.singletonList(
                        HomeDocLink
                                .builder()
                                .rel("rel")
                                .href("/api")
                                .method("GET")
                                .authorities(Collections.singletonList("ROLE_ADMIN"))
                                .build()
                ).stream()
        );

        HomeDocResource result = sut.homeDoc("http://localhost:8080");

        assertNotNull(result);
        assertNotNull(result.getLinks());
        assertEquals(1, result.getLinks().size());
        assertTrue(result.getLinks().get(0) instanceof HomeDocLink);

        HomeDocLink link = (HomeDocLink) result.getLinks().get(0);

        assertEquals("rel", link.getRel());
        assertEquals("http://localhost:8080/api", link.getHref());
        assertEquals("GET", link.getMethod());
        assertNull(link.getDescribedBy());
        assertNull(link.getTemplate());
        assertNull(link.getType());
        assertEquals(Collections.singletonList("ROLE_ADMIN"), link.getAuthorities());
    }

    @Test
    public void homeDocShouldReturnNoLinkWhenNotAuthorized() throws Exception {
        when(homeDocLinks.stream()).thenReturn(
                Collections.singletonList(
                        HomeDocLink
                                .builder()
                                .rel("rel")
                                .href("/api")
                                .method("GET")
                                .authorities(Collections.singletonList("ROLE_SUPER_ADMIN"))
                                .build()
                ).stream()
        );

        HomeDocResource result = sut.homeDoc("http://localhost:8080");

        assertNotNull(result);
        assertNotNull(result.getLinks());
        assertEquals(0, result.getLinks().size());
    }
}