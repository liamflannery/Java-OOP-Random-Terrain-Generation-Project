import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JLabel;

public class Stage {
    Grid grid;
    Actor train;
    Actor car;
    Actor boat;
    Cell currentCell;

    public Stage() {
        grid = new Grid();
        train = new Train(grid.cellAtColRow(0, 0));
        car = new Car(grid.cellAtColRow(0, 15));
        boat = new Boat(grid.cellAtColRow(12, 9));
    }

    public void paint(Graphics g, Point mouseLoc) {
        grid.paint(g, mouseLoc);
        /*train.paint(g);
        car.paint(g);
        boat.paint(g);*/
        Cell currentCell;
        Cell lowest; 
        ArrayList<Cell> surrounding;
        try{ //Will try the code if the mouse is on a cell, but do nothing if the mouse is not over a cell instead of throwing an error
            currentCell = grid.currentCell(mouseLoc);
            g.drawString(currentCell.toString(), 750, 50); //Prints current cell name
            if(currentCell.getElevation() > -1000){ //Doesn't include the building cells
                g.drawString("Elevation: " + currentCell.getElevation() + "", 750, 65); //Prints the current cells elevation
                g.drawString("Coordinates: (" + (currentCell.x - 10)/35  + ", " + (currentCell.y - 10)/35 + ")", 750, 80); //Prints coordinates of cell
               
               //The following code prints out the lowest cell next to the current cell and a list of the cells surrounding the current cel
               //I used it for debugging and left it in because it's interesting 

               /*
                lowest = grid.surroundingLowest((currentCell.x - 10)/35, (currentCell.y - 10)/35);
                g.drawString("Lowest: " + (lowest.x - 10)/35  + ", " + (lowest.y - 10)/35, 750, 95);
                surrounding = grid.surrounding((currentCell.x - 10)/35, (currentCell.y - 10)/35);
               
                g.drawString("Surrounding Cells", 750, 110);
                int positionOnPage = 125;
                
                for(Cell i: surrounding){
                    g.drawString((i.x - 10)/35  + ", " + (i.y - 10)/35, 750, positionOnPage);
                    positionOnPage += 15;
                }
               
                */
            }
            
        }
        catch(Exception e){
            //System.out.println("No cell");
        }
        

    }
}
