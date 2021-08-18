import java.awt.Graphics;
import java.awt.Point;

class Grid {
    Cell[][] cells = new Cell[20][20];

    public Grid(){
        for(int i = 0; i < cells.length; i++){
            for(int j = 0; j < cells[i].length; j++){
                cells[i][j] = new Cell(10+35*i,10+35*j, setElevation(), setType());
            }
        }
    }
    private int setElevation(){
        return (int)(((Math.random() * 6500) - 500));
    }
    private String setType(){
        double typeSelector = Math.random();
        if(typeSelector < 0.4){
            return "grass";
        }
        if(typeSelector < 0.65){
            return "mountain";
        }
        if(typeSelector < 0.85){
            return "water";
        }
        if(typeSelector < 0.95){
            return "road";
        }
        else{
            return "building";
        }
    }
    public void paint(Graphics g, Point mousePos){
        for(int i = 0; i < cells.length; i++){
            for(int j = 0; j < cells[i].length; j++){
                cells[i][j].paint(g, mousePos);
            }
        }

    }
   

    public Cell cellAtColRow(int c, int r) {
        return cells[c][r];
    }
}