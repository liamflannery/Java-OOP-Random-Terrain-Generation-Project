import java.awt.Color;
public class Grass extends Cell{

    int xPos;
    int yPos;
    Color cellColour; 
    int elevation;  

    public Grass(int x, int y) {
        super(x, y);
        xPos = x;
        yPos = y;
        cellColour = Color.green;
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
