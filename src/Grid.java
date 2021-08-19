import java.awt.Graphics;
import java.awt.Point;

class Grid {
    Cell[][] cells = new Cell[20][20];

    public Grid(){
        for(int i = 0; i < cells.length; i++){
            for(int j = 0; j < cells[i].length; j++){
                cells[i][j] = setCell(10+35*i,10+35*j);
            }
        }
    }
    private Cell setCell(int x, int y){
        double typeSelector = Math.random();
        if(typeSelector < 0.4){
            return new Grass(x, y);
        }
        if(typeSelector < 0.65){
            return new Mountain(x, y);
        }
        if(typeSelector < 0.85){
            return new Water(x, y);
        }
        if(typeSelector < 0.95){
            return new Road(x, y);
        }
        else{
            return new Building(x, y);
        }
    }

    public void paint(Graphics g, Point mousePos){
        for(int i = 0; i < cells.length; i++){
            for(int j = 0; j < cells[i].length; j++){
                cells[i][j].paint(g, mousePos, cells[i][j].getColour());
            }
        }
    }

    public Cell currentCell(Point mousePos){
        for(int i = 0; i < cells.length; i++){
            for(int j = 0; j < cells[i].length; j++){
                if(cells[i][j].contains(mousePos)){
                    return cells[i][j];
                }
            }
        }
        return null;
    }
   
   

    public Cell cellAtColRow(int c, int r) {
        return cells[c][r];
    }
    
}