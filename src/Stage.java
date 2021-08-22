import java.awt.Graphics;
import java.awt.Point;

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
        try{
            g.drawString(grid.currentCell(mouseLoc).toString(), 750, 50);
            if(grid.currentCell(mouseLoc).getElevation() > -1000){
                g.drawString(grid.currentCell(mouseLoc).getElevation() + "", 750, 60);
            }
        }
        catch(Exception e){
            //System.out.println("No cell");
        }
        

    }
}
