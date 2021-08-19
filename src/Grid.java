import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

class Grid {
    Cell[][] cells = new Cell[20][20];
    ArrayList<String> doNotReplaceMT = new ArrayList<String>();
    ArrayList<String> doNotReplaceRS = new ArrayList<String>();
    ArrayList<String> doNotReplaceRo = new ArrayList<String>();

    public Grid(){
        doNotReplaceMT.add("MountainTop");
        doNotReplaceRS.add("MountainTop");
        doNotReplaceRS.add("Mountain");
        doNotReplaceRo.add("Mountain");
        doNotReplaceRo.add("MountainTop");
        doNotReplaceRo.add("Water");
        doNotReplaceRo.add("WaterSource");

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
                if(doNotReplaceMT.contains(cells[i][j].getClass().getName()) != true){
                    cells[i][j] = mountainCont(i,j);
                }
                
            }
        }
        //Add in river source cells
        for(int i = 0; i < cells.length; i++){
            for(int j = 0; j < cells[i].length; j++){
                if(doNotReplaceRS.contains(cells[i][j].getClass().getName()) != true){
                    cells[i][j] = riverSource(i,j);
                }
                
            }
        }
        //Add in rivers
        for(int i = 0; i < cells.length; i++){
            for(int j = 0; j < cells[i].length; j++){
                if(doNotReplaceRS.contains(cells[i][j].getClass().getName()) != true){
                    cells[i][j] = addRiver(i,j);
                }
                
            }
        }
        //Add in buildings
        for(int i = 0; i < cells.length; i++){
            for(int j = 0; j < cells[i].length; j++){
                if(doNotReplaceRS.contains(cells[i][j].getClass().getName()) != true){
                    cells[i][j] = addBuildings(i,j);
                }
                
            }
        }
        //Add in roads
        for(int i = 0; i < cells.length; i++){
            for(int j = 0; j < cells[i].length; j++){
                if(doNotReplaceRo.contains(cells[i][j].getClass().getName()) != true){   
                    cells[i][j] = addRoad(i,j);
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
    private Cell riverSource(int x, int y){
        double typeSelector = Math.random();
        if(surroundingContains(x, y, "MountainTop") == false && surroundingContains(x, y, "WaterSource") == false&& typeSelector < 0.02){
            return new WaterSource(10 + 35 * x, 10 + 35 * y);
        }
        else{
            return cells[x][y];
        }
    }
    private Cell addRiver(int x, int y){
        double typeSelector = Math.random();
        if(surroundingContains(x, y, "MountainTop") == false){
            if(below(x, y, "WaterSource") | below(x,y, "Water")){
                return new Water(10 + 35 * x, 10 + 35 * y);
            }
            else{
                return cells[x][y];
            }
            
        }
        else{
            return cells[x][y];
        }
    }
    private Cell addRoad(int x, int y){
        double typeSelector = Math.random();
        if(surroundingContains(x, y, "Building") && typeSelector < 0.5){
            return new Road(10 + 35 * x, 10 + 35 * y);
        }
        if(cellCount(x, y, "Road") == 1){
            return new Road(10 + 35 * x, 10 + 35 * y);
        }
        else{
            return cells[x][y];
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
    private Cell addBuildings(int x, int y){
        double typeSelector = Math.random();
        if(surroundingContains(x, y, "MountainTop") == false && surroundingContains(x, y, "Water") == false && surroundingContains(x, y, "WaterSource") == false && typeSelector < 0.05){
            return new Building(10 + 35 * x, 10 + 35 * y);
        }
        else{
            return cells[x][y];
        }
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
    private boolean below(int x, int y, String name){
        ArrayList<Cell> surroundingCells = surrounding(x,y);
        for(int i = 2; i < 9; i++){
            if(i == 2 || i == 5 || i == 8){
            if(surroundingCells.get(i) != null){
                if(surroundingCells.get(i).getClass().getName().equals(name)){
                    return true;
                }
            }
        }
        }
        return false;
    }
  
    private int cellCount(int x, int y, String name){
        ArrayList<Cell> surroundingCells = surrounding(x,y);
        int cellCount = 0;
        for(int i = 0; i < 9 ; i++){
            if(i % 2 != 0){
                if(surroundingCells.get(i) != null){
                    if(surroundingCells.get(i).getClass().getName().equals(name)){
                        cellCount++;
                    }
                }
            }
        }
        return cellCount;
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