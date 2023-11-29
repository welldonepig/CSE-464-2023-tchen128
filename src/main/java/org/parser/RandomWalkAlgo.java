package org.parser;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class RandomWalkAlgo extends GraphSearchTemplate {
    private Random random;
    private String currentNode;
    private Set<String> currentNeighbors;

    public RandomWalkAlgo(DirectedGraph<String, DefaultEdge> graph) {
        super(graph);
        random = new Random();
        currentNeighbors = new HashSet<>();
    }

    @Override
    public void init(String src) {
        this.currentNode = src;
        visited.add(src);
    }

    @Override
    public boolean queueIsEmpty() {
        return false;
    }

    @Override
    public String getNextNode() {
        Set<String> neighbors = graph.outgoingEdgesOf(currentNode).stream()
                .map(graph::getEdgeTarget)
                .filter(neighbor -> !visited.contains(neighbor))
                .collect(Collectors.toSet());

        currentNeighbors.addAll(neighbors);

        for (String neighbor : neighbors) {
            parentMap.put(neighbor, currentNode);
        }

        if (currentNeighbors.isEmpty()) {
            return null;
        }
        int randomIndex = random.nextInt(currentNeighbors.size());
        Iterator<String> iterator = currentNeighbors.iterator();
        for (int i = 0; i < randomIndex; i++) {
            iterator.next();
        }
        String nextNode = iterator.next();
        currentNode = nextNode;
        currentNeighbors.remove(nextNode);
        visited.add(nextNode);
        System.out.println("Visit:" + nextNode);
        return nextNode;
    }

    @Override
    public void processNeighbors(String currentNode) {
        // In random walk, there's no specific processing of neighbors
        // The next node is determined in getNextNode()
    }
}