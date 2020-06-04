package pl.jacekniezgoda.homedoc.controller;

import pl.jacekniezgoda.homedoc.model.HomeDocResource;
import pl.jacekniezgoda.homedoc.service.HomeDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@ResponseBody
public class HomeDocController {
    @Autowired
    private HomeDocService homeDocService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public HomeDocResource home(UriComponentsBuilder builder) {
        return homeDocService.homeDoc(builder.toUriString());
    }
}
