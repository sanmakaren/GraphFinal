import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Implements a graphical canvas that displays a list of points.
 *
 * @author Nicholas R. Howe
 * @author Karen Santamaria
 * @version 7 December 2017
 */
class GraphCanvas extends JComponent {

    /**
     * The graph
     **/
    private Graph<VertexData, EdgeData> graph;

    /**
     * Diameter of vertex
     */
    public static final int DIAMETER = 15;

    /**
     * Default color for vertex
     */
    public static final Color DEFAULT_V_COLOR = new Color(67, 173, 201);

    /**
     * Default color for edge
     */
    public static final Color DEFAULT_E_COLOR = new Color(160, 217, 238);


    /**
     * Constructor
     */
    public GraphCanvas() {
        graph = new Graph<>();
        setMinimumSize(new Dimension(700, 600));
        setPreferredSize(new Dimension(700, 600));

    }


    /**
     * Imports a graph from file
     *
     * @param filename name of file
     */
    public void importGraph(String filename) {
        try {
            BufferedReader buff = new BufferedReader(new FileReader(filename));
            String currentLine;
            while ((currentLine = buff.readLine()) != null) {
                String[] currentSplit = currentLine.split("\\s");
                if (currentLine.startsWith("v")) {
                    addVertex(new Point(Integer.parseInt(currentSplit[1]), Integer.parseInt(currentSplit[2])), currentSplit[3]);
                } else if (currentLine.startsWith("e")) {
                    VertexData v1 = getVertexData(currentSplit[2]);
                    VertexData v2 = getVertexData(currentSplit[3]);
                    if (v1 != null && v2 != null) {
                        EdgeData newEdgeData = new EdgeData(Double.parseDouble(currentSplit[1]), DEFAULT_E_COLOR);
                        addEdge(newEdgeData, graph.getVertex(v1), graph.getVertex(v2));

                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Problem loading file");
        }
    }

    /**
     * Exports graph in canvas
     *
     * @param filename new file to write
     */
    public void exportGraph(String filename) {

        StringBuilder builder = new StringBuilder();

        for (Graph<VertexData, EdgeData>.Vertex v : graph.getVertices()) {
            builder.append("v");
            builder.append(" ");
            builder.append((int) v.getData().getPoint().getX());
            builder.append(" ");
            builder.append((int) v.getData().getPoint().getY());
            builder.append(" ");
            builder.append(v.getData().getString());
            builder.append("\n");
        }

        for (Graph<VertexData, EdgeData>.Edge e : graph.getEdges()) {
            builder.append("e");
            builder.append(" ");
            builder.append(e.getData().getLength());
            builder.append(" ");
            builder.append(e.getSourceVertex().getData().getString());
            builder.append(" ");
            builder.append(e.getTargetVertex().getData().getString());
            builder.append("\n");
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(builder.toString());
            writer.close();
        } catch (IOException e) {
            System.out.println("Problem writing file");
        }

    }

    /**
     * Get the vertices of the graph
     *
     * @return all the vertices
     */
    public ArrayList<Graph<VertexData, EdgeData>.Vertex> getVertices() {
        return graph.getVertices();
    }

    /**
     * Get the edges of the graph
     *
     * @return all the vertices
     */
    public ArrayList<Graph<VertexData, EdgeData>.Edge> getEdges() {
        return graph.getEdges();
    }

    /**
     * Accessor for VertexData
     *
     * @param name of vertex data
     * @return vertex data
     */
    private VertexData getVertexData(String name) {
        for (Graph<VertexData, EdgeData>.Vertex v : getVertices()) {
            if (v.getData().getString().equals(name)) {
                return v.getData();
            }
        }
        return null;
    }


    /**
     * Add a vertex to the graph in canvas
     * @param p position of vertex
     * @param name name of vertex
     */
    public void addVertex(Point p, String name) {
        VertexData newData = new VertexData(p, name, DEFAULT_V_COLOR);
        if (getVertexData(newData.getString()) == null) { //make sure no shared name
            graph.addVertex(newData);
            repaint();
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    /**
     * Remove a vertex from canvas
     *
     * @param vertex vertex to remove
     */
    public void removeVertex(Graph<VertexData, EdgeData>.Vertex vertex) {
        graph.removeVertex(vertex);
        repaint();
    }

    /**
     * Add an edge to the graph
     *
     * @param data cost of an edge
     * @param v1   starting vertex
     * @param v2   ending vertex
     */
    public void addEdge(EdgeData data, Graph<VertexData, EdgeData>.Vertex v1, Graph<VertexData, EdgeData>.Vertex v2) {
        graph.addEdge(data, v1, v2);
        repaint();
    }

    /**
     * Remove an edge from the graph
     *
     * @param v1 starting vertex
     * @param v2 ending vertex
     */
    public void removeEdge(Graph<VertexData, EdgeData>.Vertex v1, Graph<VertexData, EdgeData>.Vertex v2) {
        graph.removeEdge(v1, v2);
        repaint();
    }


    public Graph<VertexData, EdgeData>.Edge getEdgeRef(Graph<VertexData, EdgeData>.Vertex v1, Graph<VertexData, EdgeData>.Vertex v2) {
        return graph.getEdgeRef(v1, v2);
    }

    /**
     * Move a vertex
     *
     * @param vertex vertex to move
     * @param p      position to move to
     */
    public void moveVertex(Graph<VertexData, EdgeData>.Vertex vertex, Point p) {
        vertex.getData().setPoint(p);
        repaint();
    }


    /**
     * Change the name of a vertex
     *
     * @param vertex vertex to change
     * @param s      new name
     */
    public void changeVertexName(Graph<VertexData, EdgeData>.Vertex vertex, String s) {

        if (getVertexData(s) == null) {
            vertex.getData().setName(s);
            repaint();
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    /**
     * Change the cost of an edge
     *
     * @param v1 starting vertex
     * @param v2 ending vertex
     * @param s  new cost
     */
    public void changeEdgeData(Graph<VertexData, EdgeData>.Vertex v1, Graph<VertexData, EdgeData>.Vertex v2, String s) {
        Graph<VertexData, EdgeData>.Edge edgeChanged = graph.getEdgeRef(v1, v2);
        if (edgeChanged != null) {
            edgeChanged.getData().setLength(Double.parseDouble(s));
        }
        repaint();
    }

    /**
     * Traverse through the graph depth-first
     *
     * @param start beginning point for traversal
     */
    public void depthFirstTraversal(Graph<VertexData, EdgeData>.Vertex start) {
        ArrayList<Graph<VertexData, EdgeData>.Edge> traversedEdges = graph.depthFirstTraversal(start);
        System.out.println("---Depth First Traversal---");
        for (Graph<VertexData, EdgeData>.Edge e : traversedEdges) {
            System.out.println(e);
            colorTraversal(e);
        }
        System.out.println("\n");


    }

    /**
     * Traverse through the graph breadth-first
     *
     * @param start beginning point for traversal
     */
    public void breadFirstTraversal(Graph<VertexData, EdgeData>.Vertex start) {
        ArrayList<Graph<VertexData, EdgeData>.Edge> traversedEdges = graph.breadthFirstTraversal(start);
        System.out.println("---Breadth First Traversal---");
        for (Graph<VertexData, EdgeData>.Edge e : traversedEdges) {
            System.out.println(e);
            colorTraversal(e);
        }
        System.out.println("\n");
    }

    /**
     * Change color of edge when doing breadFirstTraversal and depthFirstTraversal
     *
     * @param e edge to color
     */
    private void colorTraversal(Graph<VertexData, EdgeData>.Edge e) {
        e.getData().setColor(Color.white);
        e.getSourceVertex().getData().setColor(Color.white);
        e.getTargetVertex().getData().setColor(Color.white);
        repaint();

    }

    /**
     * Set colors back to original color after traversing
     */
    public void clearTraversal() {
        for (Graph<VertexData, EdgeData>.Edge e : getEdges()) {
            e.getData().setColor(DEFAULT_E_COLOR);
        }

        for (Graph<VertexData, EdgeData>.Vertex v : getVertices()) {
            v.getData().setColor(DEFAULT_V_COLOR);
        }
        repaint();
    }

    /**
     * Use doDijkstra algorithm to find shortest shortestDistances from each vertex to the start
     *
     * @param start starting vertex
     */
    public void shortestDistances(Graph<VertexData, EdgeData>.Vertex start) {

        HashMap<Graph<VertexData, EdgeData>.Vertex, Graph<VertexData, EdgeData>.CostHomePair> dijkstra = graph.doDijkstra(start);
        HashMap<Graph<VertexData, EdgeData>.Vertex, Double> distances = graph.getDistances(dijkstra);

        System.out.println("---Distances---");

        for (Graph<VertexData, EdgeData>.Vertex v : distances.keySet()) {
            System.out.println(v + " " + dijkstra.get(v).getCost());
            v.getData().setColor(Color.white);
        }

        System.out.println("\n");

        repaint();

    }

    /**
     * Use doDijkstra algorithm to find and the shortest path between two vertices
     *
     * @param start starting point
     * @param end   ending point
     */
    public double shortestPath(Graph<VertexData, EdgeData>.Vertex start, Graph<VertexData, EdgeData>.Vertex end) {

        HashMap<Graph<VertexData, EdgeData>.Vertex, Graph<VertexData, EdgeData>.CostHomePair> dijkstra = graph.doDijkstra(end);

        double pathCost = dijkstra.get(start).getCost();

        if (pathCost != Double.POSITIVE_INFINITY) {
            ArrayList<Graph<VertexData, EdgeData>.Vertex> path = graph.getShortestPath(dijkstra, start, end);

            path.get(0).getData().setColor(Color.white);
            for (int i = 1; i < path.size(); i++) {
                getEdgeRef(path.get(i - 1), path.get(i)).getData().setColor(Color.white);
                path.get(i).getData().setColor(Color.white);
            }
        }
        repaint();

        return pathCost;
    }


    /**
     * Clear the graph of the canvas
     */
    public void clearGraph() {
        graph = new Graph<>();
        repaint();
    }


    /**
     * Paints the graph
     *
     * @param g The graphics object to draw with
     */
    public void paintComponent(Graphics g) {

        for (Graph<VertexData, EdgeData>.Edge e : graph.getEdges()) {
            Point p1 = e.getSourceVertex().getData().getPoint();
            Point p2 = e.getTargetVertex().getData().getPoint();
            Point midpoint = new Point((int) (p1.getX() + p2.getX()) / 2, (int) (p1.getY() + p2.getY()) / 2);

            g.setColor(e.getData().getColor());

            g.fillPolygon(new int[]{(int) p1.getX() + DIAMETER / 4, (int) p1.getX() - DIAMETER / 4, (int) p2.getX() - DIAMETER / 4,
                            (int) p2.getX() + DIAMETER / 4}, new int[]{(int) p1.getY(), (int) p1.getY(), (int) p2.getY(), (int) p2.getY()},
                    4);

            g.fillPolygon(new int[]{(int) p1.getX(), (int) p1.getX(), (int) p2.getX(), (int) p2.getX()}, new int[]{(int) p1.getY() + DIAMETER / 4,
                    (int) p1.getY() - DIAMETER / 4, (int) p2.getY() - DIAMETER / 4, (int) p2.getY() + DIAMETER / 4}, 4);

            g.setColor(Color.black);
            g.setFont(new Font("TimesRoman", Font.BOLD, 12));
            g.drawString(Double.toString(e.getData().getLength()), (int) midpoint.getX(), (int) midpoint.getY());

        }

        for (Graph<VertexData, EdgeData>.Vertex v : graph.getVertices()) {
            Point p = v.getData().getPoint();
            String s = v.getData().getString();
            g.setColor(v.getData().getColor());
            g.fillOval((int) p.getX() - DIAMETER / 2, (int) p.getY() - DIAMETER / 2, DIAMETER, DIAMETER);


            g.setColor(Color.black);
            g.setFont(new Font("TimesRoman", Font.BOLD, 12));
            g.drawString(s, (int) p.getX() - DIAMETER / 2, (int) p.getY() - 10);


        }
    }
}