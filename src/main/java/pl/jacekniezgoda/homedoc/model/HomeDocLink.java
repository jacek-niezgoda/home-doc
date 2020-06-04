package pl.jacekniezgoda.homedoc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.hateoas.Link;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HomeDocLink extends Link {
    private String type;
    private String template;
    private String method;
    @JsonProperty("describedby")
    private String describedBy;
    @JsonIgnore
    private List<String> authorities;

    private HomeDocLink(Builder builder) {
        super(builder.href, builder.rel);
        this.template = builder.template;
        this.type = builder.type;
        this.method = builder.method;
        this.describedBy = builder.describedBy;
        this.authorities = builder.authorities;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getTemplate() {
        return template;
    }

    public String getType() {
        return type;
    }

    public String getMethod() {
        return method;
    }

    public String getDescribedBy() {
        return describedBy;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof HomeDocLink)) {
            return false;
        }

        HomeDocLink that = (HomeDocLink) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(type, that.type)
                .append(template, that.template)
                .append(method, that.method)
                .append(describedBy, that.describedBy)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(type)
                .append(template)
                .append(method)
                .append(describedBy)
                .toHashCode();
    }

    public static final class Builder {
        private String href;
        private String rel;
        private String template;
        private String type;
        private String method;
        private String describedBy;
        private List<String> authorities;

        public Builder href(String href) {
            this.href = href;
            return this;
        }

        public Builder rel(String rel) {
            this.rel = rel;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder template(String template) {
            this.template = template;
            return this;
        }

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder describedBy(String describedBy) {
            this.describedBy = describedBy;
            return this;
        }

        public Builder authorities(List<String> authorities) {
            this.authorities = authorities;
            return this;
        }

        public HomeDocLink build() {
            return new HomeDocLink(this);
        }
    }
}
