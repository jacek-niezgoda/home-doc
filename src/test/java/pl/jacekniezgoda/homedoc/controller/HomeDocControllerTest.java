package pl.jacekniezgoda.homedoc.controller;

import pl.jacekniezgoda.homedoc.model.HomeDocResource;
import pl.jacekniezgoda.homedoc.service.HomeDocService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.hateoas.Link;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class HomeDocControllerTest {

    private MockMvc mvc;
    @InjectMocks
    private HomeDocController sut;

    @Mock
    private HomeDocService homeDocService;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(sut)
                .build();
    }

    @org.junit.Test
    public void testHome() throws Exception {
        HomeDocResource resource = new HomeDocResource();
        resource.add(new Link("href", "rel"));

        when(homeDocService.homeDoc("http://localhost:8080")).thenReturn(resource);

        MvcResult result = mvc.perform(get("http://localhost:8080/home"))
                .andExpect(status().isOk())
                .andReturn();

        assertNotNull(result);
        assertNotNull(result.getResponse());
        assertNotNull(result.getResponse().getContentAsString());
        assertEquals("{\"links\":[{\"rel\":\"rel\",\"href\":\"href\"}]}", result.getResponse().getContentAsString());
    }
}