package pl.jacekniezgoda.homedoc.service;

import pl.jacekniezgoda.homedoc.configuration.HomeDocRels;
import pl.jacekniezgoda.homedoc.model.HomeDocResource;
import pl.jacekniezgoda.homedoc.model.HomeDocLink;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class HomeDocService {
    @Resource
    private List<HomeDocLink> homeDocLinks;
    @Autowired
    private HomeDocRels homeDocRels;

    public HomeDocResource homeDoc(String uri) {
        List<String> authorities = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getAuthorities)
                .map(Collection::stream)
                .orElse(Stream.empty())
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        HomeDocResource resource = new HomeDocResource();

        homeDocLinks
                .stream()
                .map(link -> linkWithUri(uri, (HomeDocLink) link))
                .filter(link -> CollectionUtils.isEmpty(link.getAuthorities()) || CollectionUtils.containsAny(authorities, link.getAuthorities()))
                .forEach(resource::add);

        return resource;
    }

    private HomeDocLink linkWithUri(String uri, HomeDocLink link) {
        return HomeDocLink
                .builder()
                .rel(homeDocRels.relOf(link.getRel()))
                .href(uri + link.getHref())
                .template(StringUtils.isEmpty(link.getTemplate()) ? null : uri + link.getHref() + link.getTemplate())
                .type(StringUtils.isEmpty(link.getType()) ? null : link.getType())
                .method(link.getMethod())
                .describedBy(StringUtils.isEmpty(link.getDescribedBy()) ? null : uri + link.getDescribedBy())
                .authorities(link.getAuthorities())
                .build();
    }
}
