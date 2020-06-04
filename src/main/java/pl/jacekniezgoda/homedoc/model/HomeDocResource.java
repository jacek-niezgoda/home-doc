package pl.jacekniezgoda.homedoc.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HomeDocResource extends ResourceSupport {
    @Override
    @JsonProperty("links")
    public List<Link> getLinks() {
        return super.getLinks();
    }
}
