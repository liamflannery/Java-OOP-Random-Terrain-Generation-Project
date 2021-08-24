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
        try{
            currentCell = grid.currentCell(mouseLoc);
            g.drawString(currentCell.toString(), 750, 50);
            if(currentCell.getElevation() > -1000){
                g.drawString(currentCell.getElevation() + "", 750, 65);
                g.drawString((currentCell.x - 10)/35  + ", " + (currentCell.y - 10)/35, 750, 80);
                lowest = grid.surroundingLowest((currentCell.x - 10)/35, (currentCell.y - 10)/35);
                g.drawString("Lowest: " + (lowest.x - 10)/35  + ", " + (lowest.y - 10)/35, 750, 95);
                surrounding = grid.surrounding((currentCell.x - 10)/35, (currentCell.y - 10)/35);
                int positionOnPage = 110;
                for(Cell i: surrounding){
                    g.drawString((i.x - 10)/35  + ", " + (i.y - 10)/35, 750, positionOnPage);
                    positionOnPage += 15;
                }
                /*
                surrounding = grid.surrounding(15, 10);
                positionOnPage = 110;
                for(Cell i: surrounding){
                    g.drawString((i.x - 10)/35  + ", " + (i.y - 10)/35, 780, positionOnPage);
                    positionOnPage += 15;
                }*/

            }
        }
        catch(Exception e){
            //System.out.println("No cell");
        }
        

    }
}
