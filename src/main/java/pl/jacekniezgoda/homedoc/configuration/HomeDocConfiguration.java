package pl.jacekniezgoda.homedoc.configuration;

import pl.jacekniezgoda.homedoc.annotation.HomeDoc;
import pl.jacekniezgoda.homedoc.controller.HomeDocController;
import pl.jacekniezgoda.homedoc.model.HomeDocLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@ConfigurationProperties("home-doc")
public class HomeDocConfiguration {
    private String homePath = "/home";
    private String selfRel = "self";
    private Map<String, String> rels = new HashMap<>();

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private RequestMappingHandlerMapping mapping;
    @Autowired
    private HomeDocController handler;

    @PostConstruct
    public void registerHomeDoc() throws NoSuchMethodException {
        RequestMappingInfo info = RequestMappingInfo
                .paths(homePath)
                .methods(RequestMethod.GET)
                .build();

        Method method = HomeDocController.class.getMethod("home", UriComponentsBuilder.class);

        mapping.unregisterMapping(info);
        mapping.registerMapping(info, handler, method);
    }

    @Bean
    public List<HomeDocLink> homeDocLinks() {
        List<HomeDocLink> links = applicationContext
                .getBeansWithAnnotation(RequestMapping.class)
                .entrySet()
                .stream()
                .filter(entry -> applicationContext.isSingleton(entry.getKey()))
                .map(Map.Entry::getValue)
                .map(Object::getClass)
                .map(Class::getDeclaredMethods)
                .flatMap(Stream::of)
                .filter(method -> AnnotationUtils.findAnnotation(method, HomeDoc.class) != null || AnnotationUtils.findAnnotation(method, HomeDoc.List.class) != null)
                .peek(method -> {
                    if (AnnotationUtils.findAnnotation(method, RequestMapping.class) == null) {
                        throw new IllegalArgumentException(HomeDoc.class.getSimpleName() + " can be used with " + RequestMapping.class.getSimpleName() + " (etc.) only");
                    }
                })
                .flatMap(homeDocLinkFactory()::linksTo)
                .sorted((o1, o2) -> o1.getRel().compareTo(o2.getRel()))
                .collect(Collectors.toList());

        links.add(0, selfHomeDocLink());

        return links;
    }

    private HomeDocLink selfHomeDocLink() {
        return HomeDocLink
                .builder()
                .rel(selfRel)
                .href(homePath)
                .authorities(Collections.emptyList())
                .build();
    }

    @Bean
    public HomeDocLinkFactory homeDocLinkFactory() {
        return new HomeDocLinkFactory(requestMappingFactory());
    }

    @Bean
    public HomeDocMappingFactory requestMappingFactory() {
        return new HomeDocMappingFactory();
    }

    @Bean
    public HomeDocRels homeDocRels() {
        return new HomeDocRels(rels);
    }

    public String getHomePath() {
        return homePath;
    }

    public void setHomePath(String homePath) {
        this.homePath = homePath;
    }

    public String getSelfRel() {
        return selfRel;
    }

    public void setSelfRel(String selfRel) {
        this.selfRel = selfRel;
    }

    public Map<String, String> getRels() {
        return rels;
    }

    public void setRels(Map<String, String> rels) {
        this.rels = rels;
    }
}
