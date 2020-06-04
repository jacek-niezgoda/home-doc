package pl.jacekniezgoda.homedoc.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(HomeDoc.List.class)
public @interface HomeDoc {
    String value() default "";
    String template() default "";
    String describedBy() default "";
    String[] authorities() default {};

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    @interface List {
        HomeDoc[] value();
    }
}
