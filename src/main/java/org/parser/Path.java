package org.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Path {
    private List<String> nodes;

    public Path() {
        this.nodes = new ArrayList<>();
    }

    public void addNode(String node) {
        nodes.add(node);
    }

    public List<String> getNodes() {
        return nodes;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(" -> ");
        nodes.forEach(joiner::add);
        return joiner.toString();
    }
}

