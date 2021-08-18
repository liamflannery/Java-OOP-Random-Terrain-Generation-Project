import java.awt.Color;
public class Mountain extends Cell{

    int xPos;
    int yPos;
    Color cellColour; 
    int elevation;  

    public Mountain(int x, int y) {
        super(x, y);
        xPos = x;
        yPos = y;
        cellColour = Color.yellow;
        elevation = setElevation();
        //TODO Auto-generated constructor stub
    }

    public Color getColour(){
        return elevationPaint(elevation, cellColour);
    }

    public int setElevation(){
        return (int)(((Math.random() * 6500) - 500));
    }
    
}
