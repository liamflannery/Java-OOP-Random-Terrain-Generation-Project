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

        //Makes sure the lowest cell near it isn't itself, and the lowest cell near it isn't already water, and the river isn't at its limit
        if(!(lowest.x == x && lowest.y == y) && !(lowest.getClass().getName().equals("Water")) && count > 0){
            //Converts the lowest cells position values to array values
            int nearLowestx = (lowest.x - 10) / 35;
            int nearLowesty = (lowest.y - 10) / 35;
            //Finds the lowest cell near the new cell
            Cell nextLowest = g.surroundingLowest(nearLowestx, nearLowesty);
            //Makes sure the lowest cell of the lowest cell near the current cell isn't the current cell
            //i.e. makes sure the cells don't pass water in between themselves
            //This shouldn't happen with the earlier water check but it's here for safety
            if(!(nextLowest.x == x && nextLowest.y == y)){
                g.cells[nearLowestx][nearLowesty] = new Water(10 + 35 * nearLowestx, 10 + 35 * nearLowesty, lowest.getElevation(), nextLowest, g, count - 1); 
            }
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
