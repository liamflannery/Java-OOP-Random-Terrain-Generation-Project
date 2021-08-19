import java.awt.Color;
public class Building extends Cell{

    int xPos;
    int yPos;
    Color cellColour; 
    int elevation;  

    public Building(int x, int y) {
        super(x, y);
        xPos = x;
        yPos = y;
        cellColour = Color.red;
        //TODO Auto-generated constructor stub
    }
    public Color getColour(){
        return cellColour;
    }
    public String getElevation(){
        return "";
    }
    
}
