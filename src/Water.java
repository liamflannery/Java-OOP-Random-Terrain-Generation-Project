import java.awt.Color;
public class Water extends Cell{

    int xPos;
    int yPos;
    Color cellColour; 
    int elevation;  

    public Water(int x, int y, int inElevation, Cell lowest, Grid g, int count) {
        super(x, y);
        xPos = x;
        yPos = y;
        cellColour = Color.blue;
        elevation = setElevation(inElevation);
        if(!(lowest.x == x && lowest.y == y) && !(lowest.getClass().getName().equals("Water"))){
            System.out.println(lowest.x + "," + lowest.y );
            int nearLowestx = (lowest.x - 10) / 35;
            int nearLowesty = (lowest.y - 10) / 35;
            Cell nextLowest = g.surroundingLowest(nearLowestx, nearLowesty);
            g.cells[nearLowestx][nearLowesty] = new Water(10 + 35 * nearLowestx, 10 + 35 * nearLowesty, lowest.getElevation(), nextLowest, g, count - 1); 
        }
        //TODO Auto-generated constructor stub
    }
    public Color getColour(){
        return elevationPaint(elevation, cellColour);
    }
    public int getElevation(){
        return this.elevation;
    }
}
