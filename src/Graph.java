import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Implements a directed graph with Vertices containing data V and edges containing data E
 *
 * @param <V> data in Vertices
 * @param <E> data in Edges
 * @author Karen Santamaria
 * @version 4 December 2017
 */
public class Graph<V, E> {

    /**
     * List of edges in graph
     */
    private ArrayList<Edge> edges;

    /**
     * List of vertices in graph
     */
    private ArrayList<Vertex> vertices;


    /**
     * Constructor
     */
    Graph() {
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
    }

    /**
     * Adds the specified edge to this graph, going from the source vertex to the target vertex,
     *
     * @param sourceVertex start vertex
     * @param targetVertex end vertex
     * @param data         data of edge to add
     */
    public Edge addEdge(E data, Vertex sourceVertex, Vertex targetVertex) {

        if (sourceVertex == targetVertex) {
            return null;
        }

        Edge newEdge = new Edge(data, sourceVertex, targetVertex);

        for (Edge e : edges) {
            if (e.equals(newEdge)) {
                return null;
            }
        }

        edges.add(newEdge);
        sourceVertex.addEdgeRef(newEdge);
        targetVertex.addEdgeRef(newEdge);
        return newEdge;
    }


    /**
     * Adds the specified vertex to this graph if not already present.
     *
     * @param data vertex added
     */
    public Vertex addVertex(V data) {

        for (Vertex v : vertices) {
            if (v.getData().equals(data)) {
                return null;
            }
        }

        Vertex newVertex = new Vertex(data);
        vertices.add(newVertex);
        return newVertex;
    }


    /**
     * Returns true if this graph contains the specified vertex
     *
     * @param vertex vertex to check
     * @return whether graph contains specified vertex
     */
    public boolean containsVertex(Vertex vertex) {
        return vertices.contains(vertex);
    }

    /**
     * Get the number of vertices
     *
     * @return number of vertices
     */
    public int numVertex() {
        return this.vertices.size();
    }

    /**
     * Get the number of edges
     *
     * @return number of edges
     */
    public int numEdge() {
        return this.edges.size();
    }

    /**
     * Access vertices
     */
    public ArrayList<Vertex> getVertices() {
        return vertices;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    /**
     * Accessor for specific edge
     *
     * @param sourceVertex start
     * @param targetVertex end
     * @return edge reference
     */
    public Edge getEdgeRef(Vertex sourceVertex, Vertex targetVertex) {
        for (Edge e : edges) {
            if (e.getSourceVertex() == sourceVertex &&
                    e.getTargetVertex() == targetVertex) {
                return e;
            }

            if (e.getSourceVertex() == targetVertex &&
                    e.getTargetVertex() == sourceVertex) {
                return e;
            }
        }
        return null;
    }

    /**
     * Accessor for edge using index
     *
     * @param i location in list
     * @return edge
     */
    public Edge getEdge(int i) {
        return edges.get(i);
    }

    /**
     * Accessor for vertex by index
     *
     * @param i location of vertex in list
     * @return vertex
     */
    public Vertex getVertex(int i) {
        return vertices.get(i);
    }

    public Vertex getVertex(V data) {
        for (Vertex v : vertices) {
            if (v.getData().equals(data)) {
                return v;
            }
        }
        return null;
    }


    /**
     * Breadth-first traversal of graph
     *
     * @param start start
     * @return all the edges traversed
     */
    public ArrayList<Edge> breadthFirstTraversal(Vertex start) {
        ArrayList<Vertex> queue = new ArrayList<>();
        ArrayList<Edge> traversedEdges = new ArrayList<>();
        HashSet<Vertex> visitedVertices = new HashSet<>();

        queue.add(start);
        visitedVertices.add(start);

        while (!queue.isEmpty()) {
            Vertex currentVertex = queue.get(0);
            queue.remove(0);

            for (Vertex v : currentVertex.getNeighbors()) {
                if (!visitedVertices.contains(v)) {

                    queue.add(v); //add to queue
                    visitedVertices.add(v); //mark v
                    traversedEdges.add(currentVertex.edgeTo(v));
                }
            }
        }

        return traversedEdges;
    }


    /**
     * Depth-first traversal of graph
     *
     * @param currentVertex current place
     * @return edges visited
     */
    public ArrayList<Edge> depthFirstTraversal(Vertex currentVertex, ArrayList<Edge> traversedEdges, HashSet<Vertex> visitedVertices) {

        visitedVertices.add(currentVertex);

        for (Vertex v : currentVertex.getNeighbors()) {
            if (!visitedVertices.contains(v)) {

                depthFirstTraversal(v, traversedEdges, visitedVertices);
                traversedEdges.add(0, currentVertex.edgeTo(v));
            }
        }

        return traversedEdges;
    }

    /**
     * Entry method for depthFirstTraversal traversal
     *
     * @param startVertex starting node
     * @return edges visited
     */
    public ArrayList<Edge> depthFirstTraversal(Vertex startVertex) {

        ArrayList<Edge> traversedEdges = new ArrayList<>();
        HashSet<Vertex> visited = new HashSet<>();
        traversedEdges = depthFirstTraversal(startVertex, traversedEdges, visited);

        return traversedEdges;
    }

    public HashMap<Vertex, CostHomePair> doDijkstra(Vertex startVertex) {
        HashSet<Vertex> queue = new HashSet<>();
        HashMap<Vertex, CostHomePair> vertexCostHome = new HashMap<>();

        for (Vertex v : getVertices()) {
            queue.add(v);
            vertexCostHome.put(v, new CostHomePair(Double.POSITIVE_INFINITY, null));
        }
        vertexCostHome.put(startVertex, new CostHomePair(0, startVertex));

        while (!queue.isEmpty()) {
            Vertex u = getClosest(vertexCostHome, queue);
            queue.remove(u);
            for (Vertex uNeighbor : u.getNeighbors()) {
                if (queue.contains(uNeighbor)) {
                    double alt = vertexCostHome.get(u).getCost() + u.edgeTo(uNeighbor).getNumData();
                    if (alt < vertexCostHome.get(uNeighbor).getCost()) {
                        CostHomePair currentCostHomePair = vertexCostHome.get(uNeighbor);
                        currentCostHomePair.setCostHome(alt, u);
                    }
                }
            }
        }


        return vertexCostHome;
    }

    private Vertex getClosest(HashMap<Vertex, CostHomePair> dijkstraMap, HashSet<Vertex> queue) {
        Vertex closestVertex = null;
        double minValue = Double.POSITIVE_INFINITY;
        for (Vertex v : queue) {
            double value = dijkstraMap.get(v).getCost();
            if (value < minValue || value == minValue) {
                minValue = value;
                closestVertex = v;
            }
        }
        return closestVertex;
    }


    public HashMap<Vertex, Double> getDistances(HashMap<Vertex, CostHomePair> master) {
        HashMap<Vertex, Double> distances = new HashMap<>();
        for (Vertex v : master.keySet()) {
            distances.put(v, master.get(v).getCost());
        }
        return distances;
    }


    public ArrayList<Vertex> getShortestPath(HashMap<Vertex, CostHomePair> master, Vertex start, Vertex end) {
        Vertex current = start;
        ArrayList<Vertex> path = new ArrayList<>();
        while (current != end){
            path.add(current);
            current = master.get(current).getHome();
        }
        path.add(end);
        return path;
    }




    /**
     * Given a set of vertices, return the other vertices that have not been included
     *
     * @param group vertices that are in list
     * @return nodes not on a given list
     */
    public HashSet<Vertex> otherVertices(HashSet<Vertex> group) {

        HashSet<Vertex> otherVertices = new HashSet<>();

        for (Vertex v : vertices) {
            if (!group.contains(v)) {
                otherVertices.add(v);
            }
        }

        return otherVertices;
    }


    /**
     * Returns nodes that are endpoints of a list of edges
     *
     * @param edges endpoints of edges
     * @return endpoints
     */
    public HashSet<Vertex> endpoints(Set<Edge> edges) {

        HashSet<Vertex> endpoints = new HashSet<>();

        for (Edge e : edges) {
            endpoints.add(e.getTargetVertex());

        }
        return endpoints;
    }

    /**
     * Returns nodes that are endpoints of a list of edges
     *
     * @param edges endpoints of edges
     * @return endpoints
     */
    public HashSet<Vertex> startpoints(Set<Edge> edges) {

        HashSet<Vertex> startpoints = new HashSet<>();

        for (Edge e : edges) {
            startpoints.add(e.getSourceVertex());

        }
        return startpoints;
    }


    /**
     * Remove a vertex
     *
     * @param vertex vertex to remove
     */
    public void removeVertex(Vertex vertex) {
        ArrayList<Edge> forRemove = new ArrayList<>(); //prevent concurrent modification
        for (Edge e : edges) {
            if (e.getSourceVertex() == vertex || e.getTargetVertex() == vertex) {
                forRemove.add(e);
                e.getSourceVertex().removeEdgeRef(e);
                e.getTargetVertex().removeEdgeRef(e);
            }

        }
        vertices.remove(vertex);
        edges.removeAll(forRemove);
    }


    /**
     * Remove an edge
     *
     * @param edge edge to remove
     */
    public void removeEdge(Edge edge) {
        edge.getSourceVertex().removeEdgeRef(edge);
        edge.getTargetVertex().removeEdgeRef(edge);


        edges.remove(edge);
    }

    /**
     * Remove an edge
     *
     * @param sourceVertex start
     * @param targetVertex end
     */
    public void removeEdge(Vertex sourceVertex, Vertex targetVertex) {
        Edge e = getEdgeRef(sourceVertex, targetVertex);
        if (e != null) {
            removeEdge(e);
        }
    }


    /**
     * Prints a representation of the graph
     */
    public void print() {
        System.out.println(vertices);
        System.out.println(edges);
    }


    /**
     * Contains data of type V
     */
    public class Vertex {

        /**
         * Data contained within vertex
         */
        private V data;

        /**
         * List of edges that the vertex contains
         */
        private HashSet<Edge> edges;


        /**
         * Constructor for Vertex
         *
         * @param data V
         */
        private Vertex(V data) {
            this.data = data;
            this.edges = new HashSet<>();
        }


        /**
         * @return data
         */
        public V getData() {
            return data;
        }

        /**
         * @param data V
         */
        public void setData(V data) {
            this.data = data;
        }

        /**
         * @param edge reference to add
         */
        private void addEdgeRef(Edge edge) {
            edges.add(edge);
        }

        /**
         * Remove reference of edge from vertex
         *
         * @param edge edge to remove
         */
        private void removeEdgeRef(Edge edge) {
            this.edges.remove(edge);
        }

        /**
         * Find the edge containing a specified neighbor
         *
         * @param vertex one vertex connected to another vertex by edge
         * @return edge containing neighbor
         */
        public Edge edgeTo(Vertex vertex) {
            for (Edge e : edges) {
                if (e.getTargetVertex() == vertex) {
                    return e;
                }
                if (e.getSourceVertex() == vertex) {
                    return e;
                }
            }
            return null;
        }

        /**
         * Get the neighbors of a vertex
         *
         * @return neighbors of a vertex
         */
        public HashSet<Vertex> getNeighbors() {
            HashSet<Vertex> neighbors = new HashSet<>();
            for (Edge e : edges) {
                if (e.getTargetVertex().getData() != data) {
                    neighbors.add(e.getTargetVertex());
                } else if (e.getSourceVertex().getData() != data) {
                    neighbors.add(e.getSourceVertex());
                }
            }


            return neighbors;
        }


        /**
         * Check if vertex has any edges
         *
         * @return true if vertex has edges
         */
        private boolean hasEdge() {
            return !edges.isEmpty();
        }


        /**
         * toString for a vertex
         *
         * @return vertex as string
         */
        @Override
        public String toString() {
            return getData().toString();
        }

    }

    /**
     * Edge which contains a two vertices and a data of type E
     */
    public class Edge {

        /**
         * Starting vertex
         */
        private Vertex sourceVertex;

        /**
         * Ending vertex
         */
        private Vertex targetVertex;

        /**
         * Data within an edge
         */
        private E data;

        /**
         * Constructor for new edge
         *
         * @param sourceVertex start
         * @param targetVertex end
         */
        private Edge(E data, Vertex sourceVertex, Vertex targetVertex) {
            this.data = data;
            this.sourceVertex = sourceVertex;
            this.targetVertex = targetVertex;
        }


        /**
         * Get the start vertex
         *
         * @return source vertex
         */
        public Vertex getSourceVertex() {

            return sourceVertex;
        }

        /**
         * Get the end vertex
         *
         * @return end
         */
        public Vertex getTargetVertex() {
            return targetVertex;
        }


        /**
         * Get the opposite of a vertex on an edge
         *
         * @param vertex node to look for opposite
         * @return opposite vertex
         */
        private Vertex oppositeTo(Vertex vertex) {

            if (vertex == sourceVertex) {
                return targetVertex;
            } else {
                return sourceVertex;
            }
        }

        /**
         * Access data in edge
         *
         * @return edge data
         */
        public E getData() {
            return data;
        }

        /**
         * Access numerical data to find shortest path
         *
         * @return numerical edge data
         */
        private double getNumData() {
            double numData = 0;
            if (data instanceof Number) {
                numData = ((Number) data).doubleValue();
            }
            return numData;
        }

        /**
         * Set data in edge
         *
         * @param data information for edge
         */
        public void setData(E data) {
            this.data = data;
        }


        /**
         * Override hashcode method
         *
         * @return hashCode number
         */
        @Override
        public int hashCode() {
            return sourceVertex.hashCode() + targetVertex.hashCode();
        }


        /**
         * Override equal method so that edges with different data are considered equal
         *
         * @param o any object to compare against "this"
         * @return boolean that states equality
         */
        @Override
        public boolean equals(Object o) {

            boolean equal = false;

            if (o == this) {
                equal = true;

            }

            else if(o instanceof Graph.Edge) {

                @SuppressWarnings("unchecked")
                Edge e = (Edge) o;

                if( e.getTargetVertex().equals(sourceVertex) && e.getSourceVertex().equals(targetVertex) ||
                        e.getTargetVertex().equals(targetVertex) && e.getSourceVertex().equals(sourceVertex)){
                    equal = true;
                }
            }
            return equal;
        }


        /**
         * @return Edge as string
         */
        public String toString() {
            return (getSourceVertex().toString() + " - " + getTargetVertex().toString());
        }

    }


    /**
     * CostHomePair keeps a cost and a homeward vertex together
     * Specifically used for Dijkstra Algorithm
     */
    public class CostHomePair {

        /**
         * Vertex to the start point of the Dijkstra Algorithm
         */
        private Vertex home;

        /**
         * Cost to the start point of the Dijkstra Algorithm
         */
        private Double cost;


        /**
         * Constructor for CostHomePair
         * @param cost cost to get to the start
         * @param home homeward vertex
         */
        public CostHomePair(double cost, Vertex home) {
            this.cost = cost;
            this.home = home;
        }

        /**
         * Accessor for cost
         * @return cost
         */
        public double getCost() {
            return cost;
        }

        /**
         * Accessor for homeward vertex
         * @return home
         */
        public Vertex getHome() {
            return home;
        }

        /**
         * Manipulator for cost and homeward vertex
         * @param newCost new cost
         * @param newHome new homeward vertex
         */
        private void setCostHome(double newCost, Vertex newHome) {
            home = newHome;
            cost = newCost;
        }


    }
}
