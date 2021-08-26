import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

class Grid {
    public Cell[][] cells = new Cell[20][20];
    float probMountainT;
    float probMountainC;
    float probMountainCT;
    float probRiverS;
    float probRoad;
    float probBuilding;
    int mountainTopCount;
    int elSmooth;
    int riverLength;
    ArrayList<String> doNotReplaceMT = new ArrayList<String>();
    ArrayList<String> doNotReplaceRS = new ArrayList<String>();
    ArrayList<String> doNotReplaceRo = new ArrayList<String>();
    ArrayList<String> doNotReplaceB = new ArrayList<String>();
    ArrayList<Cell> mountainTops = new ArrayList<Cell>();
    int[][] directionPoints;

    public Grid(){
        //For each type of new cell, defines which cells they cannot overwrite
        doNotReplaceMT.add("MountainTop");
        
        doNotReplaceRS.add("MountainTop");
        
        doNotReplaceRo.add("Mountain");
        doNotReplaceRo.add("MountainTop");
        doNotReplaceRo.add("Water");

        doNotReplaceB.add("MountainTop");
        doNotReplaceB.add("Water");
        
        probMountainT = 0.03f;
        mountainTopCount = 1;
        probMountainC = 0.01f;
        probMountainCT = 0.95f;
        probRiverS = 0.05f;
        probRoad = 0.005f;
        probBuilding = 0.05f;
        riverLength = 100;
        elSmooth = 5;

        directionPoints = new int[][]{{-1,-1}, {-1,0}, {-1,1},  {0,-1},{0,1}, {1,-1},  {1,0},  {1,1}, {0,0}};

        setCell();
    }

    private void setCell(){
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
        
        
        //Grass elevation set
       for(int i = 0; i < cells.length; i++){
        for(int j = 0; j < cells[i].length; j++){
            if(doNotReplaceRS.contains(cells[i][j].getClass().getName()) != true){
                cells[i][j].setElevation(grassElevation(i,j));
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
        
      
        //Add in roads
        for(int i = 0; i < cells.length; i++){
            for(int j = 0; j < cells[i].length; j++){
                if(doNotReplaceRo.contains(cells[i][j].getClass().getName()) != true){   
                    cells[i][j] = addRoad(i,j);
                }
                
            }
        }
        //Add in buildings
        for(int i = 0; i < cells.length; i++){
            for(int j = 0; j < cells[i].length; j++){
                if(doNotReplaceB.contains(cells[i][j].getClass().getName()) != true){
                    cells[i][j] = addBuildings(i,j);
                }
                
            }
        }
        
   
    }
    private Cell mountainTop(int x, int y){
        double typeSelector = Math.random();
        Cell firstMountain;
        if(mountainTops.size() > 0){
            firstMountain = mountainTops.get(0);
        }
        else{
            firstMountain = new Cell(toGrid(100), toGrid(100));
        }
        if(distance(new Cell(toGrid(x), toGrid(y)), firstMountain) < 10){
            if(surroundingContains(x, y, "MountainTop") == false && typeSelector < probMountainT){
                mountainTops.add(new MountainTop(toGrid(x), toGrid(y)));
                return new MountainTop(toGrid(x), toGrid(y));
            }
            else{
                return new Grass(toGrid(x), toGrid(y),-500); 
            }
        }
        else if(surroundingContains(x, y, "MountainTop") == false && typeSelector < probMountainT && mountainTopCount > 0){
            mountainTops.add(new MountainTop(toGrid(x), toGrid(y)));
            mountainTopCount--;
            return new MountainTop(toGrid(x), toGrid(y));
        }
        else{
            return new Grass(toGrid(x), toGrid(y),-500);
        }
    }
    private Cell mountainCont(int x, int y){
        double typeSelector = Math.random();
        if(surroundingContains(x, y, "MountainTop") && typeSelector < probMountainCT){
            return new Mountain(toGrid(x), toGrid(y), (int) (6000 * (Math.random() * 0.3 + 0.7)));
        }
        else if(surroundingContains(x, y, "Mountain") && typeSelector < probMountainC){
            return new Mountain(toGrid(x), toGrid(y), (int) (5000 * (Math.random() * 0.5 + 0.5)));
        }
        else{
            return cells[x][y];
        }
    }
    private int grassElevation(int x, int y){
        Cell closestMT = closestMT(x, y);
        int mtDistance = distance(new Cell(toGrid(x),toGrid(y)), closestMT);
        double sinVal = Math.PI/2 * mtDistance/elSmooth;
        double elevation = Math.sin(sinVal) * 3250 + 2750;
        return (int) elevation;
    }
    
    private Cell riverSource(int x, int y){
        double typeSelector = Math.random();
        if(cells[x][y].getElevation() < 0 || typeSelector < probRiverS){
            if(!(cells[x][y].getClass().getName().equals("Water"))){
                return new Water(toGrid(x), toGrid(y), cells[x][y].getElevation(), surroundingLowest(x, y), this, riverLength);
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
        if(typeSelector < probRoad){
            return new Road(toGrid(x), toGrid(y));
        }
        if(cellCountDirect(x, y, "Road") == 1){
            return new Road(toGrid(x), toGrid(y));
        }
        else{
            return cells[x][y];
        }
    }
    private Cell addBuildings(int x, int y){
        if(cellCountAll(x, y, "Road") < 3 && cellCountDirect(x, y, "Road") == 1 &&surroundingContains(x, y, "Building") == false){
            return new Building(toGrid(x), toGrid(y));
        }
        else{
            return cells[x][y];
        }
    }
    public ArrayList<Cell> surrounding(int x, int y){
        ArrayList<Cell> surroundingCells = new ArrayList<Cell>();
        for(int[] direction: directionPoints){
            int cx = x + direction[0];
            int cy = y + direction[1];
            if(cy >=0 && cy < cells.length){
                if(cx >= 0 && cx < cells[cy].length){
                    surroundingCells.add(cells[cx][cy]);
                }
            }
        }
        
        return surroundingCells;
    }
  
    public boolean surroundingContains(int x, int y, String name){
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
    public Cell surroundingLowest(int x, int y){
        ArrayList<Cell> surroundingCells = surrounding(x,y);
        Cell currentLowest = new Grass(0,0,6001);
       
        for(Cell i: surroundingCells){
            if(i != null){
                if(i.getElevation() < currentLowest.getElevation()){
                    currentLowest = i;
                }
            }
        }
        return currentLowest;
    }
  
    private int cellCountDirect(int x, int y, String name){
        ArrayList<Cell> surroundingCells = surrounding(x,y);
        int cellCount = 0;
        for(Cell i: surroundingCells){
            if(toCells(i.x) == x || toCells(i.y) == y){
                if(i.getClass().getName().equals(name)){
                    cellCount++;
                }
            }
        }
        return cellCount;
    }
    private int cellCountAll(int x, int y, String name){
        ArrayList<Cell> surroundingCells = surrounding(x,y);
        int cellCount = 0;
        for(int i = 0; i < surroundingCells.size() ; i++){
            if(surroundingCells.get(i) != null){
                if(surroundingCells.get(i).getClass().getName().equals(name)){
                        cellCount++;
                }
            }
        }
        return cellCount;
    }
  
    private Cell closestMT(int x, int y){
        ArrayList<Cell> mtCopy = mountainTops;
        if(mtCopy.size() > 0){
            Cell currentClosest = mtCopy.get(0);
            int currentClosestD = distance(currentClosest, new Cell(toGrid(x), toGrid(y)));
            for(Cell i: mtCopy){
                if(distance(i , new Cell(toGrid(x),toGrid(y))) < currentClosestD){
                    currentClosest = i;
                }
            }
            return currentClosest;
        }
        return null;
    }
    private int distance(Cell cell1, Cell cell2){
        if(cell1 != null && cell2 != null){
            int xDistance = toCells(cell1.x) - toCells(cell2.x);
            int yDistance = toCells(cell1.y) - toCells(cell2.y);
            return (int)(Math.sqrt(xDistance * xDistance + yDistance * yDistance));
        }
        else{
            return 600;
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
   
    public int toGrid(int input){
        return 10 + 35 * input;
    }

    public int toCells (int input){
        return (input - 10)/35;
    }
   

    public Cell cellAtColRow(int c, int r) {
        return cells[c][r];
    }
    
}