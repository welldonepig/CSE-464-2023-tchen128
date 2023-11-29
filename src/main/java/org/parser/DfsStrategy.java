package org.parser;

public class DfsStrategy implements SearchStrategy {
    private GraphSearchTemplate template;

    public DfsStrategy(GraphSearchTemplate template) {
        this.template = template;
    }

    @Override
    public Path executeSearch(String src, String dst) {
        return template.search(src, dst);
    }
}
