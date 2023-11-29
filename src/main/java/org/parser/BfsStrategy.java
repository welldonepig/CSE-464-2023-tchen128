package org.parser;

public class BfsStrategy implements SearchStrategy {
    private GraphSearchTemplate template;

    public BfsStrategy(GraphSearchTemplate template) {
        this.template = template;
    }

    @Override
    public Path executeSearch(String src, String dst) {
        return template.search(src, dst);
    }
}
