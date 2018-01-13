import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Implements a GUI for inputting points.
 *
 * @author Nicholas R. Howe
 * @author Karen Santamaria
 * @version 7 December 2017
 */
public class GraphGUI {
    /**
     * The graph to be displayed
     */
    private static GraphCanvas canvas;

    /**
     * Label for the input mode instructions
     */
    private  JLabel instr;

    /**
     * The input mode
     */
    private InputMode mode = InputMode.ADD_EDIT_POINTS;


    /**
     * Textfield in GUI
     */
    private TextField textField;


    /**
     * Keep track of clicks
     */
    private int numClick = 0;

    /**
     * Remembers point where last mousedown event occurred
     */
    private Graph<VertexData, EdgeData>.Vertex vertexUnderMouse = null;

    /**
     * Schedules a job for the event-dispatching thread
     * creating and showing this application's GUI.
     */
    public static void main(String[] args) {
        final GraphGUI GUI = new GraphGUI();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GUI.createAndShowGUI();
            }
        });
    }

    /**
     * Sets up the GUI window
     */
    private void createAndShowGUI() {
        // Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        // Create and set up the window.
        JFrame frame = new JFrame("Graph GUI");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // Add components
        createComponents(frame);

        // Display the window.
        frame.pack();
        frame.setVisible(true);


    }

    /**
     * Puts content in the GUI window
     */
    private void createComponents(JFrame frame) {

       Color b = new Color (255, 220, 240);
        // graph display
        Container pane = frame.getContentPane();
        pane.setBackground(b);
        pane.setLayout(new FlowLayout());
        JPanel panel1 = new JPanel();
        panel1.setBackground(b);
        panel1.setLayout(new BorderLayout());
        canvas = new GraphCanvas();
        PointMouseListener pml = new PointMouseListener();
        canvas.addMouseListener(pml);
        canvas.addMouseMotionListener(pml);
        panel1.add(canvas);
        instr = new JLabel("Click to add new points; drag to move.");
        //instr.setForeground(Color.white);
        instr.setFont(instr.getFont().deriveFont(20.0f));
        panel1.add(instr, BorderLayout.NORTH);
        pane.add(panel1);
        textField = new TextField();
        textField.addTextListener(new GraphTextListener());
        panel1.add(textField, BorderLayout.SOUTH);

        // controls
        JPanel panel2 = new JPanel();
        panel2.setBackground(b);
        panel2.setLayout(new GridLayout(11, 1));
        JButton addPointButton = new JButton("Add/Move/Name Vertices");
        panel2.add(addPointButton);
        addPointButton.addActionListener(new AddPointListener());
        JButton rmvPointButton = new JButton("Remove Vertices");
        panel2.add(rmvPointButton);
        rmvPointButton.addActionListener(new RmvPointListener());
        JButton addEdgeButton = new JButton("Add/Edit Edges");
        panel2.add(addEdgeButton);
        addEdgeButton.addActionListener(new AddEditEdgeListener());
        JButton removeEdgeButton = new JButton("Remove Edges");
        panel2.add(removeEdgeButton);
        removeEdgeButton.addActionListener(new RemoveEdgeListener());
        JButton dftButton = new JButton("Do DFT");
        panel2.add(dftButton);
        dftButton.addActionListener(new DFTListener());
        JButton bftButton = new JButton("Do BFT");
        panel2.add(bftButton);
        bftButton.addActionListener(new BFTListener());
        JButton distanceButton = new JButton("Get Shortest Distances");
        panel2.add(distanceButton);
        distanceButton.addActionListener(new DistanceListener());
        JButton shortPathButton = new JButton("Get Shortest Path");
        panel2.add(shortPathButton);
        shortPathButton.addActionListener(new ShortPathListener());
        JButton importButton = new JButton("Import Graph");
        panel2.add(importButton);
        importButton.addActionListener(new ImportListener());
        JButton exportButton = new JButton("Export Graph");
        panel2.add(exportButton);
        exportButton.addActionListener(new ExportListener());
        JButton clearButton = new JButton("Clear Graph");
        panel2.add(clearButton);
        clearButton.addActionListener(new ClearGraphListener());

        pane.add(panel2);
    }

    /**
     * Returns a point found within the drawing radius of the given location,
     * or null if none
     *
     * @param x the x coordinate of the location
     * @param y the y coordinate of the location
     * @return a point from the canvas if there is one covering this location,
     * or a null reference if not
     */
    private Graph<VertexData, EdgeData>.Vertex findNearbyVertex(int x, int y) {
        Graph<VertexData, EdgeData>.Vertex v = null;
        for (Graph<VertexData, EdgeData>.Vertex vertex : canvas.getVertices()) {
            Point p = vertex.getData().getPoint();
            if (p.distance(x, y) < GraphCanvas.DIAMETER) {
                v = vertex;
            }
        }

        return v;
    }

    /**
     * Constants for recording the input mode
     */
    enum InputMode {
        ADD_EDIT_POINTS, RMV_POINTS, ADD_EDIT_EDGES, RMV_EDGES, DFT, BFT, DISTANCE, SHORT_PATH
    }

    /**
     * Listener for AddPoint button
     */
    private class AddPointListener implements ActionListener {
        /**
         * Event handler for AddPoint button
         */
        public void actionPerformed(ActionEvent event) {
            mode = InputMode.ADD_EDIT_POINTS;
            instr.setText("Click to add new vertices or change their location.");
            canvas.clearTraversal();
        }
    }

    /**
     * Listener for RmvPoint button
     */
    private class RmvPointListener implements ActionListener {
        /**
         * Event handler for RmvPoint button
         */
        public void actionPerformed(ActionEvent event) {
            mode = InputMode.RMV_POINTS;
            instr.setText("Click on vertex to remove it");
            canvas.clearTraversal();
        }
    }

    /**
     * Listener for Add/Edit Edge button
     */
    private class AddEditEdgeListener implements ActionListener {
        /**
         * Event handler for Add/Edit button
         */
        public void actionPerformed(ActionEvent event) {
            mode = InputMode.ADD_EDIT_EDGES;
            instr.setText("Click on two vertices to create or edit an edge");
            canvas.clearTraversal();

        }

    }

    /**
     * Listener for Remove Edge button
     */
    private class RemoveEdgeListener implements ActionListener {
        /**
         * Event handler for Remove Edge button
         */
        public void actionPerformed(ActionEvent event) {
            mode = InputMode.RMV_EDGES;
            instr.setText("Click on two vertices to remove edge between them");
            canvas.clearTraversal();

        }
    }
    /**
     * Listener for breadFirstTraversal traversal button
     */
    private class BFTListener implements ActionListener {
        /**
         * Event handler for breadFirstTraversal traversal button
         */
        public void actionPerformed(ActionEvent event) {
            mode = InputMode.BFT;
            instr.setText("Click on a vertex to do Breadth-First Traversal");
            canvas.clearTraversal();

        }
    }

    /**
     * Listener for depthFirstTraversal traversal button
     */
    private class DFTListener implements ActionListener {
        /**
         * Event handler for depthFirstTraversal traversal button
         */
        public void actionPerformed(ActionEvent event) {
            mode = InputMode.DFT;
            instr.setText("Click on a vertex to do Depth-First Traversal");
            canvas.clearTraversal();

        }
    }

    /**
     * Listener for Dijkstra shortest shortestDistances button
     */
    private class DistanceListener implements ActionListener {
        /**
         * Event handler for Shortest Path traversal button
         */
        public void actionPerformed(ActionEvent event) {
            mode = InputMode.DISTANCE;
            instr.setText("Click on a vertex get shortest distances");
            canvas.clearTraversal();

        }
    }

    /**
     * Listener for Shortest Path traversal button
     */
    private class ShortPathListener implements ActionListener {
        /**
         * Event handler for Shortest Path traversal button
         */
        public void actionPerformed(ActionEvent event) {
            mode = InputMode.SHORT_PATH;
            instr.setText("Click on two vertices to get shortest path between");
            canvas.clearTraversal();

        }
    }

    /**
     * Listener for importing graph
     */
    private class ImportListener implements ActionListener {

        /**
         * Event handler for importing graph
         */
        public void actionPerformed(ActionEvent event) {
            instr.setText("Type desired input filename and then click on 'Import Graph'");
            canvas.clearTraversal();

            if (textField.getText().endsWith(".txt")) {
                canvas.importGraph(textField.getText());
                textField.setText("");
                instr.setText("Now click on a different button to do something");
            }
        }
    }

    /**
     * Listener for exporting graph
     */
    private class ExportListener implements ActionListener {
        /**
         * Event handler for exporting graph
         */
        public void actionPerformed(ActionEvent event) {
            instr.setText("Type desired output filename and then click on 'Export Graph'");
            canvas.clearTraversal();
            if (textField.getText().endsWith(".txt")) {
                canvas.exportGraph(textField.getText());
                textField.setText("");
                instr.setText("Now click on a different button to do something");
            }
        }
    }

    /**
     * Listener clearing graph
     */
    private class ClearGraphListener implements ActionListener {
        /**
         * Event handler for clearing graph
         */
        public void actionPerformed(ActionEvent event) {
            instr.setText("Graph cleared");
            canvas.clearGraph();
        }
    }


    /**
     * Listener for text input
     */
    private static class GraphTextListener implements TextListener {
        /**
         * Event handler for text input
         */
        public void textValueChanged(TextEvent e) {

        }
    }


    /**
     * Mouse listener for PointCanvas element
     */
    private class PointMouseListener extends MouseAdapter
            implements MouseMotionListener {

        /**
         * Responds to click event depending on mode
         */
        public void mouseClicked(MouseEvent event) {
            switch (mode) {
                case ADD_EDIT_POINTS:
                    vertexUnderMouse = findNearbyVertex(event.getX(), event.getY());
                    //add vertex
                    if (vertexUnderMouse == null) {
                        String name = Integer.toString(canvas.getVertices().size() + 1);
                        if (!textField.getText().equals("")) {
                            name = textField.getText();
                            textField.setText("");
                        }
                        canvas.addVertex(new Point(event.getX(), event.getY()), name);

                        //edit vertex
                    } else if (!textField.getText().equals("")) {
                        canvas.changeVertexName(vertexUnderMouse, textField.getText());
                        textField.setText("");

                    } else {
                        Toolkit.getDefaultToolkit().beep();
                    }
                    break;
                case RMV_POINTS:
                    if (findNearbyVertex(event.getX(), event.getY()) != null) {
                        vertexUnderMouse = findNearbyVertex(event.getX(), event.getY());
                        canvas.removeVertex(vertexUnderMouse);
                        vertexUnderMouse = null;

                    } else {
                        Toolkit.getDefaultToolkit().beep();
                    }
                    break;
                case ADD_EDIT_EDGES:
                    if (numClick == 1) {
                        Graph<VertexData, EdgeData>.Vertex target = findNearbyVertex(event.getX(), event.getY());
                        if (target != null) {

                            //add edge
                            if (canvas.getEdgeRef(vertexUnderMouse, target) == null) {
                                double distance = 0;
                                if (textField.getText().matches("-?\\d+(\\.\\d+)?")) {
                                    distance = Double.parseDouble(textField.getText());
                                }
                                EdgeData eData = new EdgeData(distance, GraphCanvas.DEFAULT_E_COLOR);
                                canvas.addEdge(eData, vertexUnderMouse, target);

                                //edit edge
                            } else if (textField.getText().matches("-?\\d+(\\.\\d+)?")) {
                                canvas.changeEdgeData(vertexUnderMouse, target, textField.getText());

                            } else{
                                Toolkit.getDefaultToolkit().beep();
                            }

                        } else {
                            Toolkit.getDefaultToolkit().beep();
                        }

                        numClick = 0;
                        vertexUnderMouse = null;
                        textField.setText("");

                    } else if (numClick == 0) {
                        vertexUnderMouse = findNearbyVertex(event.getX(), event.getY());
                        if (vertexUnderMouse != null) {
                            numClick = 1;
                        } else {
                            Toolkit.getDefaultToolkit().beep();
                        }
                    }

                    break;
                case RMV_EDGES:
                    if (numClick == 1) {

                        //remove edge
                        Graph<VertexData, EdgeData>.Vertex target = findNearbyVertex(event.getX(), event.getY());
                        if (target != null) {
                            canvas.removeEdge(vertexUnderMouse, target);
                        } else {
                            Toolkit.getDefaultToolkit().beep();
                        }

                        vertexUnderMouse = null;
                        numClick = 0;

                    } else if (numClick == 0) {
                        vertexUnderMouse = findNearbyVertex(event.getX(), event.getY());
                        if (vertexUnderMouse != null) {
                            numClick += 1;
                        } else {
                            Toolkit.getDefaultToolkit().beep();
                        }
                    }
                    break;
                case BFT:
                    if (findNearbyVertex(event.getX(), event.getY()) != null) {
                        canvas.breadFirstTraversal(findNearbyVertex(event.getX(), event.getY()));
                        instr.setText("View edges in console");
                    }
                    break;
                case DFT:
                    if (findNearbyVertex(event.getX(), event.getY()) != null) {
                        canvas.depthFirstTraversal(findNearbyVertex(event.getX(), event.getY()));
                        instr.setText("View edges in console");
                    }
                    break;
                case DISTANCE:
                    if (findNearbyVertex(event.getX(), event.getY()) != null) {
                        canvas.shortestDistances(findNearbyVertex(event.getX(), event.getY()));
                        instr.setText("View shortest distances in console");
                    }
                    break;
                case SHORT_PATH:
                    if (numClick == 1) {
                        //do shortest path
                        Graph<VertexData, EdgeData>.Vertex target = findNearbyVertex(event.getX(), event.getY());
                        if (target != null) {
                            double cost = canvas.shortestPath(vertexUnderMouse, target);
                            instr.setText("Shortest path cost: " + cost);
                        } else {
                            Toolkit.getDefaultToolkit().beep();
                        }
                        vertexUnderMouse = null;
                        numClick = 0;
                    } else if (numClick == 0 && vertexUnderMouse == null) {
                        vertexUnderMouse = findNearbyVertex(event.getX(), event.getY());
                        if (vertexUnderMouse != null) {
                            numClick += 1;
                        } else {
                            Toolkit.getDefaultToolkit().beep();
                        }
                    }

            }

        }

        /**
         * Records point under press event in anticipation of possible drag
         */
        public void mousePressed(MouseEvent event) {
            //  Record point under mouse, if any
            if (mode == InputMode.ADD_EDIT_POINTS && textField.getText().equals("")) {
                vertexUnderMouse = findNearbyVertex(event.getX(), event.getY());
            }
        }

        /**
         * Responds to release event
         */
        public void mouseReleased(MouseEvent event) {
            if ((mode != InputMode.ADD_EDIT_EDGES) && (mode != InputMode.RMV_EDGES) && (mode != InputMode.SHORT_PATH)){
                vertexUnderMouse = null;
            }
        }

        /**
         * Responds to mouse drag event
         */
        public void mouseDragged(MouseEvent event) {
            if (vertexUnderMouse != null && mode == InputMode.ADD_EDIT_POINTS && textField.getText().equals("")) {
                canvas.moveVertex(vertexUnderMouse, new Point(event.getX(), event.getY()));
            }

        }

        /**
         * Empty but necessary to comply with MouseMotionListener interface.
         */
        public void mouseMoved(MouseEvent event) {
        }
    }


}