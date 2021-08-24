import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

class Grid {
    Cell[][] cells = new Cell[20][20];
    float probMountainT;
    float probMountainC;
    float probMountainCT;
    float probRiverS;
    float probRoad;
    float probBuilding;
    ArrayList<String> doNotReplaceMT = new ArrayList<String>();
    ArrayList<String> doNotReplaceRS = new ArrayList<String>();
    ArrayList<String> doNotReplaceRo = new ArrayList<String>();
    int[][] directionPoints;

    public Grid(){
        //For each type of new cell, defines which cells they cannot overwrite
        doNotReplaceMT.add("MountainTop");
        doNotReplaceRS.add("MountainTop");
        //doNotReplaceRS.add("Water");
        doNotReplaceRo.add("Mountain");
        doNotReplaceRo.add("MountainTop");
        doNotReplaceRo.add("Water");
        
        probMountainT = 0.005f;
        probMountainC = 0.00f;
        probMountainCT = 1f;
        probRiverS = 0.02f;
        probRoad = 0.01f;
        probBuilding = 0.005f;

        directionPoints = new int[][]{{-1,-1}, {-1,0}, {-1,1},  {0,-1},{0,1}, {1,-1},  {1,0},  {1,1}};

        setCell();
    }

    private void setCell(){
        //Add in peaks of mountains
        for(int i = 0; i < cells.length; i++){
            for(int j = 0; j < cells[i].length; j++){
                cells[i][j] = mountainTop(i,j);
                
            }
        } 
        /*
        //Form mountains around the peaks
       for(int i = 0; i < cells.length; i++){
            for(int j = 0; j < cells[i].length; j++){
                if(doNotReplaceMT.contains(cells[i][j].getClass().getName()) != true){
                    cells[i][j] = mountainCont(i,j);
                }
                
            }
        }  
        
        /*
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
        /*
        //Add in rivers
        for(int i = 0; i < cells.length; i++){
            for(int j = 0; j < cells[i].length; j++){
                if(cells[i][j].getClass().getName().equals("Water")){
                    addRiver(i, j);
                }
                
            }
        }
      /*
        //Add in roads
        for(int i = 0; i < cells.length; i++){
            for(int j = 0; j < cells[i].length; j++){
                if(doNotReplaceRo.contains(cells[i][j].getClass().getName()) != true){   
                    cells[i][j] = addRoad(i,j);
                }
                
            }
        }/*
        //Add in buildings
        for(int i = 0; i < cells.length; i++){
            for(int j = 0; j < cells[i].length; j++){
                if(doNotReplaceRS.contains(cells[i][j].getClass().getName()) != true){
                    cells[i][j] = addBuildings(i,j);
                }
                
            }
        }
        */
   
    }
    private Cell mountainTop(int x, int y){
        double typeSelector = Math.random();
        if(surroundingContains(x, y, "MountainTop") == false && typeSelector < probMountainT){
            return new MountainTop(10 + 35 * x, 10 + 35 * y);
        }
        else{
            return new Grass(10 + 35 * x, 10 + 35 * y, -500);
        }
    }
    private Cell mountainCont(int x, int y){
        double typeSelector = Math.random();
        if(surroundingContains(x, y, "MountainTop") && typeSelector < probMountainCT){
            return new Mountain(10 + 35 * x, 10 + 35 * y, (int) (6000 * (Math.random() * 0.3 + 0.7)));
        }
        else if(surroundingContains(x, y, "Mountain") && typeSelector < probMountainC){
            return new Mountain(10 + 35 * x, 10 + 35 * y, (int) (5000 * (Math.random() * 0.5 + 0.5)));
        }
        else{
            return cells[x][y];
        }
    }
    private int grassElevation(int x, int y){
        if(surroundingContains(x, y, "Mountain")){
            return (int)((Math.random() * 0.1 + 0.9) * 5000);
        }
        else{
            return surroundingHeight(x, y);
        }
    }
    private Cell riverSource(int x, int y){
        double typeSelector = Math.random();
        if(cells[x][y].getElevation() < 0 || typeSelector < probRiverS){
            return new Water(10 + 35 * x, 10 + 35 * y, cells[x][y].getElevation());
        }
        else{
            return cells[x][y];
        }
    }
   private void addRiver(int x, int y){
        Cell nearLowest;
        int nearLowestx = 0;
        int nearLowesty = 0;
        nearLowest = surroundingLowest(x, y);
        nearLowestx = (nearLowest.x - 10) / 35;
        nearLowesty = (nearLowest.y - 10) / 35;
        cells[nearLowestx][nearLowesty] = new Water(10 + 35 * nearLowestx, 10 + 35 * nearLowesty, cells[nearLowestx][nearLowesty].getElevation()); 
    }
            
   
    private Cell addRoad(int x, int y){
        double typeSelector = Math.random();
        if(typeSelector < probRoad){
            return new Road(10 + 35 * x, 10 + 35 * y);
        }
        if(cellCountDirect(x, y, "Road") == 1){
            return new Road(10 + 35 * x, 10 + 35 * y);
        }
        else{
            return cells[x][y];
        }
    }
    private Cell addBuildings(int x, int y){
        double typeSelector = Math.random();
        if(cellCountAll(x, y, "Road") < 3 && cellCountDirect(x, y, "Road") == 1 &&surroundingContains(x, y, "Building") == false){
            return new Building(10 + 35 * x, 10 + 35 * y);
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
    private int cellCountAll(int x, int y, String name){
        ArrayList<Cell> surroundingCells = surrounding(x,y);
        int cellCount = 0;
        for(int i = 0; i < 9 ; i++){
            if(surroundingCells.get(i) != null){
                if(surroundingCells.get(i).getClass().getName().equals(name)){
                        cellCount++;
                }
            }
        }
        return cellCount;
    }
    private int surroundingHeight(int x, int y){
        ArrayList<Cell> surroundingCells = surrounding(x,y);
        int averageHeight = 0;
        int numtoAverage = 9;
        int currentHeight;
        int averageCubes = 0;
        
        //Find average of surrounding cells
        for(Cell i: surroundingCells){
            if(i != null){
                currentHeight = i.getElevation();
                averageHeight += currentHeight; 
            }
        }
        averageHeight = averageHeight/9;

        //Subtract average from each cell, then set it to the power of 5, then find average of 
        for(Cell i: surroundingCells){
            if(i != null){
                currentHeight = i.getElevation() - averageHeight;
                currentHeight = (int) Math.pow(currentHeight, 2); 
                averageCubes += currentHeight;
            }
        }
        averageCubes = averageCubes/9;
        averageCubes = (int) Math.pow(averageCubes, 1.0/2);
        averageHeight += averageCubes;
        return averageHeight;
    }
    private Cell surroundingType(int x, int y, String name){
        ArrayList<Cell> surroundingCells = surrounding(x,y);
        for(int i = 0; i < 9 ; i++){
            if(surroundingCells.get(i) != null){
                if(surroundingCells.get(i).getClass().getName().equals(name)){
                        return surroundingCells.get(i);
                }
            }
        }
        return null;
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