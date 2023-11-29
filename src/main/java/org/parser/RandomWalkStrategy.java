package org.parser;

public class RandomWalkStrategy implements SearchStrategy {
    private GraphSearchTemplate template;

    public RandomWalkStrategy(GraphSearchTemplate template) {
        this.template = template;
    }

    @Override
    public Path executeSearch(String src, String dst) {
        return template.search(src, dst);
    }
}
