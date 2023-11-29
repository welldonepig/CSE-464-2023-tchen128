package org.parser;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Set;

public class DfsAlgo extends GraphSearchTemplate {

    private final Deque<String> stack;

    public DfsAlgo(DirectedGraph<String, DefaultEdge> graph) {
        super(graph);
        this.stack = new LinkedList<>();
    }

    @Override
    public void init(String src) {
        stack.push(src);
        visited.add(src);
    }

    @Override
    public boolean queueIsEmpty() {
        return stack.isEmpty();
    }

    @Override
    public String getNextNode() {
        return stack.pop();
    }

    @Override
    public void processNeighbors(String currentNode) {
        Set<DefaultEdge> outgoingEdges = graph.outgoingEdgesOf(currentNode);
        for (DefaultEdge edge : outgoingEdges) {
            String edgeTarget = graph.getEdgeTarget(edge);
            if (!visited.contains(edgeTarget)) {
                stack.push(edgeTarget);
                parentMap.put(edgeTarget, currentNode);
                visited.add(edgeTarget);
            }
        }
    }
}