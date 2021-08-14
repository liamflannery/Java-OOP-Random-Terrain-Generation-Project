import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

class Cell extends Rectangle {
    static int size = 35;
    int elevation;
    String cellType;

    public Cell(int x, int y, int z, String type){
        super(x, y, size, size);
       if(type != "building"){
        elevation = z;
       }
        cellType = type;
    }
   

    void paint(Graphics g, Point mousePos){
        if(contains(mousePos)){
            g.setColor(Color.GRAY);
        } else {
            g.setColor(getColour());
            
        }
        
        g.fillRect(x,y,size,size);
        g.setColor(Color.BLACK);
        g.drawRect(x,y,size,size);
    }

    @Override
    public boolean contains(Point p){
        if (p != null){
            return(super.contains(p));
        } else {
            return false;
        }
    }
    public Color getColour(){
       // int zNum = (int) (((this.elevation + 500)/6500.0) * 255.0);
      //  return new Color(zNum, zNum, zNum);
      if(cellType == "road"){
          return Color.gray;
      }
      if(cellType == "water"){
        return Color.blue;
        }
      if(cellType == "grass"){
        return Color.green;
        }
      if(cellType == "mountain"){
        return Color.yellow;
        }
      else{
        return Color.red;
        }
    }
}