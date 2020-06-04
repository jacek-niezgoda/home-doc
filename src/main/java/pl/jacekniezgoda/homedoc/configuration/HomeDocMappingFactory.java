package pl.jacekniezgoda.homedoc.configuration;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

class HomeDocMappingFactory {
    public RequestMapping mappingOf(Class clazz) {
        return AnnotationUtils.findAnnotation(clazz, RequestMapping.class);
    }

    public RequestMapping mappingOf(Method method) {
        GetMapping getMapping = AnnotationUtils.findAnnotation(method, GetMapping.class);
        if (getMapping!=null) {
            return new GetMappingAdapter(getMapping);
        }
        PostMapping postMapping = AnnotationUtils.findAnnotation(method, PostMapping.class);
        if (postMapping!=null) {
            return new PostMappingAdapter(postMapping);
        }
        PutMapping putMapping = AnnotationUtils.findAnnotation(method, PutMapping.class);
        if (putMapping!=null) {
            return new PutMappingAdapter(putMapping);
        }
        DeleteMapping deleteMapping = AnnotationUtils.findAnnotation(method, DeleteMapping.class);
        if (deleteMapping!=null) {
            return new DeleteMappingAdapter(deleteMapping);
        }
        PatchMapping patchMapping = AnnotationUtils.findAnnotation(method, PatchMapping.class);
        if (patchMapping!=null) {
            return new PatchMappingAdapter(patchMapping);
        }

        return AnnotationUtils.findAnnotation(method, RequestMapping.class);
    }

    private final class GetMappingAdapter implements RequestMapping {
        private GetMapping getMapping;

        public GetMappingAdapter(GetMapping getMapping) {
            this.getMapping = getMapping;
        }

        @Override
        public String name() {
            return getMapping.name();
        }

        @Override
        public String[] value() {
            return getMapping.value();
        }

        @Override
        public String[] path() {
            return getMapping.path();
        }

        @Override
        public RequestMethod[] method() {
            return new RequestMethod[] {RequestMethod.GET};
        }

        @Override
        public String[] params() {
            return getMapping.params();
        }

        @Override
        public String[] headers() {
            return getMapping.headers();
        }

        @Override
        public String[] consumes() {
            return getMapping.consumes();
        }

        @Override
        public String[] produces() {
            return getMapping.produces();
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return RequestMapping.class;
        }
    }

    private final class PostMappingAdapter implements RequestMapping {
        private PostMapping postMapping;

        public PostMappingAdapter(PostMapping postMapping) {
            this.postMapping = postMapping;
        }

        @Override
        public String name() {
            return postMapping.name();
        }

        @Override
        public String[] value() {
            return postMapping.value();
        }

        @Override
        public String[] path() {
            return postMapping.path();
        }

        @Override
        public RequestMethod[] method() {
            return new RequestMethod[] {RequestMethod.POST};
        }

        @Override
        public String[] params() {
            return postMapping.params();
        }

        @Override
        public String[] headers() {
            return postMapping.headers();
        }

        @Override
        public String[] consumes() {
            return postMapping.consumes();
        }

        @Override
        public String[] produces() {
            return postMapping.produces();
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return RequestMapping.class;
        }
    }

    private final class PutMappingAdapter implements RequestMapping {
        private PutMapping putMapping;

        public PutMappingAdapter(PutMapping putMapping) {
            this.putMapping = putMapping;
        }

        @Override
        public String name() {
            return putMapping.name();
        }

        @Override
        public String[] value() {
            return putMapping.value();
        }

        @Override
        public String[] path() {
            return putMapping.path();
        }

        @Override
        public RequestMethod[] method() {
            return new RequestMethod[] {RequestMethod.PUT};
        }

        @Override
        public String[] params() {
            return putMapping.params();
        }

        @Override
        public String[] headers() {
            return putMapping.headers();
        }

        @Override
        public String[] consumes() {
            return putMapping.consumes();
        }

        @Override
        public String[] produces() {
            return putMapping.produces();
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return RequestMapping.class;
        }
    }

    private final class DeleteMappingAdapter implements RequestMapping {
        private DeleteMapping deleteMapping;

        public DeleteMappingAdapter(DeleteMapping deleteMapping) {
            this.deleteMapping = deleteMapping;
        }

        @Override
        public String name() {
            return deleteMapping.name();
        }

        @Override
        public String[] value() {
            return deleteMapping.value();
        }

        @Override
        public String[] path() {
            return deleteMapping.path();
        }

        @Override
        public RequestMethod[] method() {
            return new RequestMethod[] {RequestMethod.DELETE};
        }

        @Override
        public String[] params() {
            return deleteMapping.params();
        }

        @Override
        public String[] headers() {
            return deleteMapping.headers();
        }

        @Override
        public String[] consumes() {
            return deleteMapping.consumes();
        }

        @Override
        public String[] produces() {
            return deleteMapping.produces();
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return RequestMapping.class;
        }
    }

    private final class PatchMappingAdapter implements RequestMapping {
        private PatchMapping patchMapping;

        public PatchMappingAdapter(PatchMapping patchMapping) {
            this.patchMapping = patchMapping;
        }

        @Override
        public String name() {
            return patchMapping.name();
        }

        @Override
        public String[] value() {
            return patchMapping.value();
        }

        @Override
        public String[] path() {
            return patchMapping.path();
        }

        @Override
        public RequestMethod[] method() {
            return new RequestMethod[] {RequestMethod.PATCH};
        }

        @Override
        public String[] params() {
            return patchMapping.params();
        }

        @Override
        public String[] headers() {
            return patchMapping.headers();
        }

        @Override
        public String[] consumes() {
            return patchMapping.consumes();
        }

        @Override
        public String[] produces() {
            return patchMapping.produces();
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return RequestMapping.class;
        }
    }
}
