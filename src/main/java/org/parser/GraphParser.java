package org.parser;

import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.jgrapht.io.*;


import java.io.*;
import java.util.*;

public class GraphParser {

    private DirectedGraph<String, DefaultEdge> graph;

    public GraphParser() {
        this.graph = new DefaultDirectedGraph<>(DefaultEdge.class);
    }
    public DirectedGraph<String, DefaultEdge> parseGraph(String filePath) {
       this.graph = new DefaultDirectedGraph<>(DefaultEdge.class);

        File file = new File(filePath);

        try {
            DOTImporter<String, DefaultEdge> importer = new DOTImporter<>(new VertexProvider<String>() {
                @Override
                public String buildVertex(String label, Map<String, Attribute> attributes) {
                    return label;
                }
            }, new EdgeProvider<String, DefaultEdge>() {
                @Override
                public DefaultEdge buildEdge(String from, String to, String label, Map<String, Attribute> attributes) {
                    return new DefaultEdge();
                }
            });
            printDotFileContents(file);
            importer.importGraph(graph, file);
        } catch(NoSuchElementException e) {
            e.printStackTrace();
        } catch (ImportException e) {
            e.printStackTrace();
        }

        return graph;
    }

    public void outputGraph(String filePath) {
        File file = new File(filePath);

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("digraph G {");
            for (DefaultEdge edge : graph.edgeSet()) {
                String source = graph.getEdgeSource(edge);
                String target = graph.getEdgeTarget(edge);
                writer.println("    " + source + " -> " + target + ";");
            }
            writer.println("}");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void printDotFileContents(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getNumberOfNodes() {
        return graph.vertexSet().size();
    }

    public Set<String> getNodeLabels() {
        return graph.vertexSet();
    }

    public int getNumberOfEdges() {
        return graph.edgeSet().size();
    }

    public Set<String> getEdges() {
        Set<String> edges = new HashSet<>();
        for (DefaultEdge edge : graph.edgeSet()) {
            String source = graph.getEdgeSource(edge);
            String target = graph.getEdgeTarget(edge);
            edges.add(source + " -> " + target);
        }
        return edges;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Number of Nodes: ").append(getNumberOfNodes()).append("\n");
        result.append("Node Labels: ").append(getNodeLabels()).append("\n");
        result.append("Number of Edges: ").append(getNumberOfEdges()).append("\n");
        result.append("Edge Direction: ").append(getEdges()).append("\n");
        return result.toString();
    }

    public void addNode(String label) {
        if (!graph.containsVertex(label)) {
            graph.addVertex(label);
        } else {
            System.out.println("Node with label '" + label + "' already exists.");
        }
    }

    // Add a list of nodes
    public void addNodes(String[] labels) {
        for (String label : labels) {
            addNode(label);
        }
    }

    // Remove node
    public void removeNode(String label) throws NoSuchElementException {
        if (graph.containsVertex(label)) {
            graph.removeVertex(label);
        } else {
            throw new NoSuchElementException("Node with label '" + label + "' does not exist in the graph.");
        }
    }

    // Remove list of nodes
    public void removeNodes(String[] labels) throws NoSuchElementException {
        for (String label : labels) {
            removeNode(label);
        }
    }

    // Add an edge and check for duplicate edges
    public void addEdge(String srcLabel, String dstLabel) {
        if (graph.containsVertex(srcLabel) && graph.containsVertex(dstLabel)) {
            DefaultEdge newEdge = new DefaultEdge();

            if (graph.addEdge(srcLabel, dstLabel, newEdge)) {
                System.out.println("Edge (" + srcLabel + " -> " + dstLabel + ") added.");
            } else {
                System.out.println("Edge (" + srcLabel + " -> " + dstLabel + ") already exists.");
            }
        } else {
            System.out.println("One or both of the nodes do not exist in the graph.");
        }
    }

    // Remove edge node
    public void removeEdge(String srcLabel, String dstLabel) {
        if (graph.containsVertex(srcLabel) && graph.containsVertex(dstLabel)) {
            DefaultEdge edgeToRemove = graph.getEdge(srcLabel, dstLabel);
            if (edgeToRemove != null) {
                graph.removeEdge(edgeToRemove);
                System.out.println("Edge (" + srcLabel + " -> " + dstLabel + ") removed.");
            } else {
                throw new NoSuchElementException("Edge (" + srcLabel + " -> " + dstLabel + ") does not exist in the graph.");
            }
        } else {
            throw new NoSuchElementException("One or both of the nodes do not exist in the graph.");
        }
    }

    public Path graphSearch(String src, String dst, Algorithm algo) {
        if(algo == Algorithm.BFS) {
            Queue<String> queue = new LinkedList<>();
            Map<String, String> parentMap = new HashMap<>();
            Set<String> visited = new HashSet<>();
            queue.add(src);
            visited.add(src);

            while (!queue.isEmpty()) {
                String currentNode = queue.poll();
                if (currentNode.equals(dst)) {
                    Path path = new Path();
                    while (currentNode != null) {
                        path.addNode(currentNode);
                        currentNode = parentMap.get(currentNode);
                    }
                    Collections.reverse(path.getNodes());
                    return path;
                }

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
            return null;
        } else if (algo == Algorithm.DFS) {
            Deque<String> stack = new LinkedList<>();
            Map<String, String> parentMap = new HashMap<>();
            Set<String> visited = new HashSet<>();

            stack.push(src);
            visited.add(src);

            while (!stack.isEmpty()) {
                String currentNode = stack.pop();
                if (currentNode.equals(dst)) {
                    Path path = new Path();
                    while (currentNode != null) {
                        path.addNode(currentNode);
                        currentNode = parentMap.get(currentNode);
                    }
                    Collections.reverse(path.getNodes());
                    return path;
                }

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
            return null;
        }
        return null;
    }

    public Graph getGraph() {
        return this.graph;
    }

    public void outputDOTGraph(String filePath) {
        File file = new File(filePath);

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("digraph G {");
            for (DefaultEdge edge : graph.edgeSet()) {
                String source = graph.getEdgeSource(edge);
                String target = graph.getEdgeTarget(edge);
                writer.println("    " + source + " -> " + target + ";");
            }
            writer.println("}");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void outputGraphics(String dotPath, String pngPath) throws IOException {
        String command = "dot -Tpng " + dotPath + " -o " + pngPath;
        Process process = Runtime.getRuntime().exec(command);

        // Wait for the process to complete
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{

        System.out.println("");

        GraphParser parser = new GraphParser();
        parser.parseGraph("input.dot");

        parser.outputGraph("output.txt");

        System.out.println(parser.toString());

        // Add a single node
        parser.addNode("X");

        // Add a list of nodes
        String[] newNodes = {"Y", "Z", "X"}; // "X" is a duplicate
        parser.addNodes(newNodes);

        System.out.println(parser.toString());


        // Add a single edge
        parser.addEdge("X", "Y");
        parser.addEdge("X", "Y"); // This will print a message about a duplicate edge

        System.out.println(parser.toString());

        parser.outputDOTGraph("output.dot");

        parser.outputGraphics("output.dot", "output.png");

    }
}
