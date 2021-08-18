import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;


class Cell extends Rectangle {
    static int size = 35;
    //int elevation;
    //String cellType;

    public Cell(int x, int y){
        super(x, y, size, size);
     /*  if(type != "building"){
        elevation = z;
       }
        cellType = type;*/
    }
   

    void paint(Graphics g, Point mousePos, Color colour){
        if(contains(mousePos)){
            g.setColor(Color.GRAY);
        } 
        else {
            g.setColor(colour);            
        }
        
        g.fillRect(x,y,size,size);
        g.setColor(Color.BLACK);
        g.drawRect(x,y,size,size);
    }

    @Override
    public boolean contains(Point p){
        if (p != null){
            return(super.contains(p));
        } 
        else {
            return false;
        }
    }
    public Color getColour(){
        return Color.black;
    }
    public Color elevationPaint(int elevation, Color colour){
        float normElevation = (float)(elevation * 1.0 + 500) / 6500;
        if(normElevation < 0.3){
            normElevation = 0.3f;
        }
        float red = (float) (colour.getRed() / 255.0);
        float green = (float) (colour.getGreen() / 255.0);
        float blue = (float) (colour.getBlue() / 255.0);
        return new Color(red * normElevation, green * normElevation, blue * normElevation);
    }
}