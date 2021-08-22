import java.awt.Color;
public class Road extends Cell{

    int xPos;
    int yPos;
    Color cellColour; 
    int elevation;  

    public Road(int x, int y) {
        super(x, y);
        xPos = x;
        yPos = y;
        cellColour = Color.gray;
        //TODO Auto-generated constructor stub
    }
    public Color getColour(){
        return elevationPaint(elevation, cellColour);
    }
    public int getElevation(){
        return this.elevation;
    }
    
}
