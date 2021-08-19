import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

class Grid {
    Cell[][] cells = new Cell[20][20];
    ArrayList<String> doNotReplace = new ArrayList<String>();

    public Grid(){
        doNotReplace.add("MountainTop");
        setCell();
    }

    private void setCell(){
        //Everything starts as grass
        for(int i = 0; i < cells.length; i++){
            for(int j = 0; j < cells[i].length; j++){
                cells[i][j] = new Grass(10+35*i,10+35*j);
                
            }
        } 
        //Add in peaks of mountains
        for(int i = 0; i < cells.length; i++){
            for(int j = 0; j < cells[i].length; j++){
                cells[i][j] = mountainTop(i,j);
                
            }
        } 
        //Form mountains around the peaks
       for(int i = 0; i < cells.length; i++){
            for(int j = 0; j < cells[i].length; j++){
                if(doNotReplace.contains(cells[i][j].getClass().getName()) != true){
                    cells[i][j] = mountainCont(i,j);
                }
                
            }
        }
       
    }
    private Cell mountainTop(int x, int y){
        double typeSelector = Math.random();
        if(surroundingContains(x, y, "MountainTop") == false && typeSelector < 0.02){
            return new MountainTop(10 + 35 * x, 10 + 35 * y);
        }
        else{
            return new Grass(10 + 35 * x, 10 + 35 * y);
        }
    }
    private Cell mountainCont(int x, int y){
        double typeSelector = Math.random();
        if(surroundingContains(x, y, "MountainTop")){
            if(typeSelector < 0.95){
                return new Mountain(10 + 35 * x, 10 + 35 * y, (int) (6000 * (Math.random() * 0.3 + 0.7)));
            }
            else{
                return new Grass(10 + 35 * x, 10 + 35 * y);
            }
        }
        if(surroundingContains(x, y, "Mountain")){
            if(typeSelector < 0.3){
                return new Mountain(10 + 35 * x, 10 + 35 * y, (int) (5000 * (Math.random() * 0.5 + 0.5)));
            }
            else{
                return new Grass(10 + 35 * x, 10 + 35 * y);
            }
        }
        else{
            return new Grass(10 + 35 * x, 10 + 35 * y);
        }
    }
    
    private ArrayList<Cell> surrounding(int x, int y){
        ArrayList<Cell> surroundingCells = new ArrayList<Cell>();
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(x + (i - 1) < 0 || x + (i - 1) > 19 || y + (j - 1) < 0 || y + (j - 1) > 19){
                    surroundingCells.add(null);
                }
                else{
                    surroundingCells.add(cells[x + (i-1)][y + (j -1)]);
                }
            }
        }
        return surroundingCells;
    }
    private boolean surroundingContains(int x, int y, String name){
        ArrayList<Cell> surroundingCells = surrounding(x,y);
        for(Cell i: surroundingCells){
            if(i != null){
                if(i.getClass().getName().equals(name)){
                    return true;
                }
            }
        }
        return false;
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