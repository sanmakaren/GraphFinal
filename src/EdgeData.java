import java.awt.*;

/**
 * Data class that keeps a color and the length of an edge
 * @author Karen Santamaria
 * @version 14 December 2017
 */
public class EdgeData extends Number {

    /**
     * Length of edge
     */
    private double edgeLength;

    /**
     *  Color of edge
     */
    private Color edgeColor;

    /**
     * Constructor for new EdgeData
     * @param edgeLength length of edge
     * @param edgeColor color of edge
     */
    public EdgeData(double edgeLength, Color edgeColor){
        this.edgeLength = edgeLength;
        this.edgeColor = edgeColor;
    }

    /**
     * Accessor for color of edge
     * @return color
     */
    public Color getColor(){
        return edgeColor;
    }

    /**
     * Accessor for length of edge
     * @return size
     */
    public double getLength(){
        return edgeLength;
    }

    /**
     * Set the color of an edge
     * @param edgeColor new color
     */
    public void setColor(Color edgeColor){
        this.edgeColor = edgeColor;
    }

    /**
     * Set the length of an edge
     * @param edgeLength new length
     */
    public void setLength(Double edgeLength){
        this.edgeLength = edgeLength;
    }

    public String toString(){
        return Double.toString(edgeLength);
    }

    /**
     * Override doubleValue in Number
     * @return EdgeData as double
     */
    @Override
    public double doubleValue() {
        return edgeLength;
    }

    /**
     * Override intValue in Number
     * @return EdgeData as int
     */
    @Override
    public int intValue() {
        return (int) edgeLength;
    }

    /**
     * Override floatValue in Number
     * @return EdgeData as float
     */
    @Override
    public float floatValue() {
        return (float) edgeLength;
    }

    /**
     * Override longValue in Number
     * @return EdgeData as long
     */
    @Override
    public long longValue() {
        return (long) edgeLength;
    }


}
