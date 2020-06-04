package pl.jacekniezgoda.homedoc.configuration;

import pl.jacekniezgoda.homedoc.annotation.HomeDoc;
import pl.jacekniezgoda.homedoc.model.HomeDocLink;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.security.RolesAllowed;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class HomeDocLinkFactory {
    private HomeDocMappingFactory homeDocMappingFactory;

    public HomeDocLinkFactory(HomeDocMappingFactory homeDocMappingFactory) {
        this.homeDocMappingFactory = homeDocMappingFactory;
    }
    
    public Stream<HomeDocLink> linksTo(Method method) {
        return homeDocLinksOf(
                method,
                homeDocMappingFactory.mappingOf(method.getDeclaringClass()),
                homeDocMappingFactory.mappingOf(method),
                homeDocsOf(method)
        );
    }

    private Stream<HomeDoc> homeDocsOf(Method method) {
        return Optional.ofNullable(AnnotationUtils.findAnnotation(method, HomeDoc.List.class))
                .map(HomeDoc.List::value)
                .map(Stream::of)
                .orElse(Optional.ofNullable(AnnotationUtils.findAnnotation(method, HomeDoc.class))
                                .map(Stream::of)
                                .orElse(Stream.empty())
                );
    }

    private Stream<HomeDocLink> homeDocLinksOf(
            Method method,
            RequestMapping classMapping,
            RequestMapping methodMapping,
            Stream<HomeDoc> homeDocs
    ) {
        return Stream.of(ArrayUtils.nullToEmpty(classMapping.value()))
                .flatMap(classUrl -> {
                            String[] methodUrls = methodMapping.path();

                            if (ArrayUtils.isEmpty(methodUrls)) {
                                return Stream.of(classUrl);
                            }

                            return Stream
                                    .of(methodUrls)
                                    .map(methodUrl -> methodUrl.replaceAll("/\\{.*\\}", ""))
                                    .map(methodUrl -> classUrl + methodUrl);
                        }
                )
                .flatMap(url -> homeDocs
                                .map(homeDoc -> homeDocLinkOf(
                                                url,
                                                method.getName(),
                                                classMapping,
                                                methodMapping,
                                                homeDoc,
                                                authoritiesOf(method, homeDoc)
                                        )
                                )
                );
    }

    private HomeDocLink homeDocLinkOf(
            String url,
            String methodName,
            RequestMapping classMapping,
            RequestMapping methodMapping,
            HomeDoc homeDoc,
            List<String> authorities
    ) {
        return HomeDocLink
                .builder()
                .href(url)
                .rel(StringUtils.isEmpty(homeDoc.value()) ? methodName : homeDoc.value())
                .template(homeDoc.template())
                .type(StringUtils.join(
                                ArrayUtils.isEmpty(methodMapping.produces()) ?
                                        classMapping.produces() :
                                        methodMapping.produces(),
                                ',')
                )
                .method(StringUtils.join(methodMapping.method(), ','))
                .describedBy(homeDoc.describedBy())
                .authorities(authorities)
                .build();
    }

    private List<String> authoritiesOf(Method method, HomeDoc homeDoc) {
        Optional<String[]> roles = Optional
                .of(homeDoc)
                .map(HomeDoc::authorities)
                .filter(authorities -> authorities.length > 0);

        if (!roles.isPresent()) {
            roles = Optional
                    .ofNullable(AnnotationUtils.findAnnotation(method, Secured.class))
                    .map(Secured::value)
                    .filter(authorities -> authorities.length > 0);
        }
        if (!roles.isPresent()) {
            roles = Optional
                    .ofNullable(AnnotationUtils.findAnnotation(method, RolesAllowed.class))
                    .map(RolesAllowed::value)
                    .filter(authorities -> authorities.length > 0);
        }

        Class clazz = method.getDeclaringClass();

        if (!roles.isPresent()) {
            roles = Optional
                    .ofNullable(AnnotationUtils.findAnnotation(clazz, Secured.class))
                    .map(Secured::value)
                    .filter(authorities -> authorities.length > 0);
        }
        if (!roles.isPresent()) {
            roles = Optional
                    .ofNullable(AnnotationUtils.findAnnotation(clazz, RolesAllowed.class))
                    .map(RolesAllowed::value)
                    .filter(authorities -> authorities.length > 0);
        }

        return roles
                .map(Stream::of)
                .orElse(Stream.empty())
                .collect(Collectors.toList());
    }
}
