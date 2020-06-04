package pl.jacekniezgoda.homedoc.configuration;

import java.util.HashMap;
import java.util.Map;

public class HomeDocRels {
    private Map<String, String> rels;

    public HomeDocRels(Map<String, String> rels) {
        this.rels = rels==null ? new HashMap<>() : rels;
    }

    public String relOf(String rel) {
        return rels.get(rel)==null ? rel : rels.get(rel);
    }
}
