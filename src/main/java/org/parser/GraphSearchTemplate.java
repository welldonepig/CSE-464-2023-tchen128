package org.parser;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;

public abstract class GraphSearchTemplate {

    public DirectedGraph<String, DefaultEdge> graph;
    public Map<String, String> parentMap;
    public Set<String> visited;

    public GraphSearchTemplate(DirectedGraph<String, DefaultEdge> graph) {
        this.graph = graph;
        this.parentMap = new HashMap<>();
        this.visited = new HashSet<>();
    }

    public Path search(String src, String dst) {
        init(src);

        while (!queueIsEmpty()) {
            String currentNode = getNextNode();
            if (currentNode.equals(dst)) {
                return constructPath(currentNode);
            }

            processNeighbors(currentNode);
        }
        return null;
    }

    public abstract void init(String src);

    public abstract boolean queueIsEmpty();

    public abstract String getNextNode();

    public abstract void processNeighbors(String currentNode);

    public Path constructPath(String currentNode) {
        Path path = new Path();
        while (currentNode != null) {
            path.addNode(currentNode);
            currentNode = parentMap.get(currentNode);
        }
        Collections.reverse(path.getNodes());
        return path;
    }
}