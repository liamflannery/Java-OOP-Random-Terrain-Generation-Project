import java.awt.Color;
public class Water extends Cell{

    int xPos;
    int yPos;
    Color cellColour; 
    int elevation;  

    public Water(int x, int y) {
        super(x, y);
        xPos = x;
        yPos = y;
        cellColour = Color.blue;
        elevation = setElevation();
        //TODO Auto-generated constructor stub
    }
    public Color getColour(){
        return elevationPaint(elevation, cellColour);
    }
    public String getElevation(){
        return this.elevation + "";
    }
}
