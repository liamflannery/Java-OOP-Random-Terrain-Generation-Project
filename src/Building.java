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
        cellColour = new Color(102, 51, 0);
        //TODO Auto-generated constructor stub
    }
    public Color getColour(){
        return cellColour;
    }
    public int getElevation(){
        return -7000; //When asked about elevation, returns an impossibly small number 
    }
    
}
