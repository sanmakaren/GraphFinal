import java.awt.*;

/**
 * Data Class for a Vertex to be presented in a GUI
 * @author Karen Santamaria
 * @version 14 December
 */
public class VertexData {

    /**
     * Position of vertex
     */
    private Point vertexLocation;

    /**
     * Name of vertex
     */
    private String vertexName;

    /**
     * Color of vertex
     */
    private Color vertexColor;

    /**
     * Constructor for a VertexData
     * @param vertexLocation position
     * @param vertexName name
     * @param vertexColor color
     */
    public VertexData(Point vertexLocation, String vertexName, Color vertexColor) {
        this.vertexLocation = vertexLocation;
        this.vertexName = vertexName;
        this.vertexColor = vertexColor;
    }

    /**
     * Accessor for point
     * @return point of vertex
     */
    public Point getPoint() {
        return vertexLocation;
    }

    /**
     * Accessor for string
     * @return string of vertex
     */
    public String getString() {
        return vertexName;
    }

    /**
     * Accessor for color
     * @return color of vertex
     */
    public Color getColor(){
        return vertexColor;
    }

    /**
     * Change the name of the vertex
     * @param vertexName new name
     */
    public void setName(String vertexName){this.vertexName =vertexName; }

    /**
     * Change the location of the vertex
     * @param vertexLocation new location
     */
    public void setPoint(Point vertexLocation) {this.vertexLocation = vertexLocation; }

    /**
     *  Change the color of a vertex
     * @param vertexColor new color of vertex
     */
    public void setColor(Color vertexColor){
        this.vertexColor = vertexColor;
    }

    public String toString(){
        return vertexName;
    }



}

