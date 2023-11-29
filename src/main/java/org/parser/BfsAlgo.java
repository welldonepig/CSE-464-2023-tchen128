package org.parser;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class BfsAlgo extends GraphSearchTemplate {

    private final Queue<String> queue;

    public BfsAlgo(DirectedGraph<String, DefaultEdge> graph) {
        super(graph);
        this.queue = new LinkedList<>();
    }

    @Override
    public void init(String src) {
        queue.add(src);
        visited.add(src);
    }

    @Override
    public boolean queueIsEmpty() {
        return queue.isEmpty();
    }

    @Override
    public String getNextNode() {
        return queue.poll();
    }

    @Override
    public void processNeighbors(String currentNode) {
        Set<DefaultEdge> outgoingEdges = graph.outgoingEdgesOf(currentNode);
        for (DefaultEdge edge : outgoingEdges) {
            String edgeTarget = graph.getEdgeTarget(edge);
            if (!visited.contains(edgeTarget)) {
                queue.add(edgeTarget);
                parentMap.put(edgeTarget, currentNode);
                visited.add(edgeTarget);
            }
        }
    }
}