import java.awt.Color;
public class Mountain extends Cell{

    int xPos;
    int yPos;
    Color cellColour; 
    int elevation;  

    public Mountain(int x, int y, int inElevation) {
        super(x, y);
        xPos = x;
        yPos = y;
        cellColour = Color.gray;
        elevation = inElevation;
        //TODO Auto-generated constructor stub
    }

    public Color getColour(){
        return elevationPaint(elevation, cellColour);
    }
    public int getElevation(){
        return this.elevation;
    }
  
}
