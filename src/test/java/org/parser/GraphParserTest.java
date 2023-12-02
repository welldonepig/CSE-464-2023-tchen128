package org.parser;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class GraphParserTest {
    private GraphParser parser;

    @BeforeEach
    public void setup() {
        parser = new GraphParser();
    }

    @Test
    public void testParseGraph() {
        DirectedGraph<String, DefaultEdge> graph = parser.parseGraph(System.getProperty("user.dir") + "/src/test/resources/test.dot");
        assertNotNull(graph);
    }

    @Test
    public void testOutputGraph() throws IOException {
        String expectedFilePath = System.getProperty("user.dir")  + "/src/test/resources/expected.txt";

        String inputFilePath = System.getProperty("user.dir")  + "/src/test/resources/input.dot";
        parser.parseGraph(inputFilePath); // Load the test graph
        // Execute the outputGraph method
        String outputFilePath = System.getProperty("user.dir")  + "/src/test/resources/output.txt";
        parser.outputGraph(outputFilePath);

        // Read the actual output from the generated "output.dot" file
        String actualOutput = readFileContent(outputFilePath);

        // Read the expected output from the "expected.dot" file
        String expectedOutput = readFileContent(expectedFilePath);

        // Compare the expected and actual outputs
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testGetNumberOfNodes() {
        String filePath = System.getProperty("user.dir")  + "/src/test/resources/test.dot";
        parser.parseGraph(filePath); // Load a test graph
        int numberOfNodes = parser.getNumberOfNodes();
        assertEquals(5, numberOfNodes);
    }

    @Test
    public void testGetNodeLabels() {
        String filePath = System.getProperty("user.dir")  + "/src/test/resources/test.dot";
        parser.parseGraph(filePath); // Load a test graph
        Set<String> nodeLabels = parser.getNodeLabels();
        assertTrue(nodeLabels.contains("A"));
        assertTrue(nodeLabels.contains("B"));
    }

    @Test
    public void testGetNumberOfEdges() {
        String filePath = System.getProperty("user.dir")  + "/src/test/resources/test.dot";
        parser.parseGraph(filePath); // Load a test graph
        int numberOfEdges = parser.getNumberOfEdges();
        assertEquals(6, numberOfEdges);
    }
    @Test
    public void testGetEdges() {
        String filePath = System.getProperty("user.dir")  + "/src/test/resources/test.dot";
        System.out.println(filePath);
        parser.parseGraph(filePath); // Load a test graph
        Set<String> edges = parser.getEdges();
        assertTrue(edges.contains("A -> B"));
        assertTrue(edges.contains("B -> D"));
    }

    @Test
    public void testAddNode() {
        parser.addNode("A");
        parser.addNode("B");
        parser.addNode("C");

        assertEquals(3, parser.getNumberOfNodes()); // Three nodes added
        assertTrue(parser.getNodeLabels().contains("A"));
        assertTrue(parser.getNodeLabels().contains("B"));
        assertTrue(parser.getNodeLabels().contains("C"));
    }

    @Test
    public void testAddNodeWithDuplicate() {
        parser.addNode("X");
        parser.addNode("Y");
        parser.addNode("X"); // Attempt to add a duplicate

        assertEquals(2, parser.getNumberOfNodes()); // Only two unique nodes should be added
        assertTrue(parser.getNodeLabels().contains("X"));
        assertTrue(parser.getNodeLabels().contains("Y"));
    }

    @Test
    public void testAddNodes() {
        String[] nodes = {"A", "B", "C", "D"};
        parser.addNodes(nodes);

        assertEquals(4, parser.getNumberOfNodes()); // Four nodes added
        assertTrue(parser.getNodeLabels().contains("A"));
        assertTrue(parser.getNodeLabels().contains("B"));
        assertTrue(parser.getNodeLabels().contains("C"));
        assertTrue(parser.getNodeLabels().contains("D"));
    }

    @Test
    public void testAddNodesWithDuplicates() {
        String[] nodes = {"X", "Y", "X", "Z"};
        parser.addNodes(nodes);

        assertEquals(3, parser.getNumberOfNodes()); // Only three unique nodes should be added
        assertTrue(parser.getNodeLabels().contains("X"));
        assertTrue(parser.getNodeLabels().contains("Y"));
        assertTrue(parser.getNodeLabels().contains("Z"));
    }

    @Test
    public void testAddEdge() {
        GraphParser parser = new GraphParser();
        parser.addNode("A");
        parser.addNode("B");
        parser.addNode("C");

        // Test adding a valid edge
        parser.addEdge("A", "B");
        assertTrue(parser.getGraph().containsEdge("A", "B"));

        // Test adding an edge that already exists
        parser.addEdge("A", "B");
        assertTrue(parser.getGraph().containsEdge("A", "B"));

        // Test adding an edge with a missing source vertex
        parser.addEdge("X", "A");
        assertFalse(parser.getGraph().containsEdge("X", "A"));

        // Test adding an edge with a missing destination vertex
        parser.addEdge("A", "Y");
        assertFalse(parser.getGraph().containsEdge("A", "Y"));

        // Test adding an edge with both missing vertices
        parser.addEdge("X", "Y");
        assertFalse(parser.getGraph().containsEdge("X", "Y"));
    }


    @Test
    public void testOutputDOTGraph() throws IOException {
        String expectedFilePath = System.getProperty("user.dir")  + "/src/test/resources/expected.dot";

        String inputFilePath = System.getProperty("user.dir")  + "/src/test/resources/input.dot";
        parser.parseGraph(inputFilePath); // Load the test graph
        // Execute the outputDOTGraph method
        String outputFilePath = System.getProperty("user.dir")  + "/src/test/resources/output.dot";
        parser.outputGraph(outputFilePath);

        // Read the actual output from the generated "output.dot" file
        String actualOutput = readFileContent(outputFilePath);

        // Read the expected output from the "expected.dot" file
        String expectedOutput = readFileContent(expectedFilePath);

        // Compare the expected and actual outputs
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void testRemoveNode() {
        String[] nodes = {"A", "B", "C", "D"};
        parser.addNodes(nodes);
        parser.removeNode("A");
        assertEquals(3, parser.getNumberOfNodes());
        assertFalse(parser.getNodeLabels().contains("A"));
        assertTrue(parser.getNodeLabels().contains("B"));
        assertTrue(parser.getNodeLabels().contains("C"));
        assertTrue(parser.getNodeLabels().contains("D"));
    }

    @Test
    public void testRemoveNonExistentNode() {
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> parser.removeNode("C"));
        assertEquals("Node with label 'C' does not exist in the graph.", exception.getMessage());
    }

    @Test
    void testRemoveNodes() {
        String[] nodes = {"A", "B", "C", "D"};
        String[] removeNodes = {"A", "D"};
        parser.addNodes(nodes);
        parser.removeNodes(removeNodes);
        assertEquals(2, parser.getNumberOfNodes());
        assertFalse(parser.getNodeLabels().contains("A"));
        assertFalse(parser.getNodeLabels().contains("D"));
        assertTrue(parser.getNodeLabels().contains("B"));
        assertTrue(parser.getNodeLabels().contains("C"));
    }

    @Test
    public void testRemoveNonExistentNodeInListNodes() {
        String[] nodes = {"A", "B", "C", "D"};
        String[] removeNodes = {"E", "K"};
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> parser.removeNodes(removeNodes));
        assertEquals("Node with label 'E' does not exist in the graph.", exception.getMessage());
    }

    @Test
    void removeEdge() {
        parser.addNode("A");
        parser.addNode("B");
        parser.addEdge("A", "B");
        parser.removeEdge("A", "B");
        assertFalse(parser.getGraph().containsEdge("A", "B"));
        assertEquals(2, parser.getGraph().vertexSet().size());
        assertEquals(0, parser.getGraph().edgeSet().size());
    }

    @Test
    public void testRemoveNonExistentEdge() {
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> parser.removeEdge("A", "C"));
        assertEquals("One or both of the nodes do not exist in the graph.", exception.getMessage());
    }

    @Test
    public void testRemoveNonExistentEdgeBetweenExistingNodes() {
        parser.addNode("A");
        parser.addNode("B");
        parser.addNode("C");
        parser.addEdge("A", "B");
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> parser.removeEdge("A", "C"));
        assertEquals("Edge (A -> C) does not exist in the graph.", exception.getMessage());
    }

    private String readFileContent(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }
}
