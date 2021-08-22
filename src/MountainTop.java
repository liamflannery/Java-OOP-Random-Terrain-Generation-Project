import java.awt.Color;
public class MountainTop extends Cell{

    int xPos;
    int yPos;
    Color cellColour; 
    int elevation;  

    public MountainTop(int x, int y) {
        super(x, y);
        xPos = x;
        yPos = y;
        cellColour = Color.white;
        elevation = 6000;
        //TODO Auto-generated constructor stub
    }

    public Color getColour(){
        return elevationPaint(elevation, cellColour);
    }
    public int getElevation(){
        return this.elevation;
    }
  
}
