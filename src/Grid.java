import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

class Grid {
    public Cell[][] cells = new Cell[20][20];
    float probMountainT; //probability of mountain tops
    float probMountainC; //probability of mountain bodies not directly next to a mountain top
    float probMountainCT; //probability of mountain bodies directly next to a mountain top 
    float probRiverS; //probability of rivers
    float probRoad; //pribability of roads
    int mountainTopCount; //max number of mountain tops in the map
    int elSmooth; //how steep/shallow the terrain is coming down from a mountain
    int riverLength; //how long rivers can be
    int mountainSize;  //how far away from a mountain top mountain bodies can generate
    ArrayList<String> doNotReplaceMT = new ArrayList<String>(); //cells moutain tops cannot generate over
    ArrayList<String> doNotReplaceRS = new ArrayList<String>(); //cells rivers cannot generate over
    ArrayList<String> doNotReplaceRo = new ArrayList<String>(); //cells roads cannot generate over
    ArrayList<String> doNotReplaceB = new ArrayList<String>(); //cells buildings cannot generate over
    ArrayList<Cell> mountainTops = new ArrayList<Cell>(); //list of all mountain tops on map
    int[][] directionPoints; // list of directions used in the surrounding() function 

    public Grid(){
        //For each type of new cell, defines which cells they cannot overwrite
        doNotReplaceMT.add("MountainTop");
        
        doNotReplaceRS.add("MountainTop");
        doNotReplaceRS.add("Water");
        doNotReplaceRS.add("Mountain");
        
        doNotReplaceRo.add("Mountain");
        doNotReplaceRo.add("MountainTop");
        doNotReplaceRo.add("Water");

        doNotReplaceB.add("MountainTop");
        doNotReplaceB.add("Water");
        
        probMountainT = 0.05f;
        mountainTopCount = 1;
        probMountainC = 0.8f;
        probMountainCT = 0.95f;
        probRiverS = 0.05f;
        probRoad = 0.01f;
        
        riverLength = 100;
        elSmooth = 5;
        mountainSize = 8;
    

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

    //Adds in mountain tops or grass
    private Cell mountainTop(int x, int y){
        double typeSelector = Math.random();
        Cell firstMountain;
        if(mountainTops.size() > 0){ 
            firstMountain = mountainTops.get(0); //finds possible existing mountain tops
        }
        else{
            firstMountain = new Cell(toGrid(100), toGrid(100));
        }

        //The following two seperate if else statements are based on whether a moutain top is already on the map

        if(distance(new Cell(toGrid(x), toGrid(y)), firstMountain) < 5){ //constrains mountain tops to be close to original mountain top
            if(surroundingContains(x, y, "MountainTop") == false && typeSelector < probMountainT){ //doesn't generate mountain tops directly next to each other
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

    //Creates mountain bodies
    private Cell mountainCont(int x, int y){
        double typeSelector = Math.random();
        Cell firstMountain;
        //Finds existing mountain tops
        if(mountainTops.size() > 0){
            firstMountain = mountainTops.get(0);
        }
        else{
            firstMountain = new Cell(toGrid(100), toGrid(100));
        }
        //Creates moutains either right next to a moutain top or right next to a mountain 
        if(surroundingContains(x, y, "MountainTop") && typeSelector < probMountainCT){
            return new Mountain(toGrid(x), toGrid(y), (int) (6000 * (Math.random() * 0.3 + 0.7)));
        }
        else if(surroundingContains(x, y, "Mountain") && typeSelector < probMountainC && distance(new Cell(toGrid(x), toGrid(y)), firstMountain) < mountainSize){
            return new Mountain(toGrid(x), toGrid(y), (int) (6000 * (Math.random() * 0.5 + 0.5)));
        }
        else{
            return cells[x][y];
        }
    }

    //Calculates elevation of grass based on its distance to closest mountain top
    private int grassElevation(int x, int y){
        Cell closestMT = closestMT(x, y); //Finds closest mountain top
        int mtDistance = distance(new Cell(toGrid(x),toGrid(y)), closestMT); //Calculates distance to that mountain top
        double sinVal = Math.PI/2 * mtDistance/elSmooth; //Converts that distance to equivalent x distance on a sin curve, based on the elSmooth variable
        double elevation = Math.sin(sinVal) * 3250 + 2750; //Calculates elevation based on sinVal with the mountainTop (6000m) being the peak and -500m being the bottom
        return (int) elevation;
    }
    
    //Creates rivers
    private Cell riverSource(int x, int y){
        double typeSelector = Math.random();
        //Creates water cells either below sea level or randomly throughout the terrain 
        if(cells[x][y].getElevation() < 0 || typeSelector < probRiverS){
            return new Water(toGrid(x), toGrid(y), cells[x][y].getElevation(), surroundingLowest(x, y), this, riverLength);
        }
        else{
            return cells[x][y];
        }
    }       
   
    //Creates roads
    private Cell addRoad(int x, int y){
        double typeSelector = Math.random();
        //Creates roads randomly or right next to other roads
        if(typeSelector < probRoad){
            return new Road(toGrid(x), toGrid(y));
        }
        if(cellCountDirect(x, y, "Road") == 1){ //If there is only one road cell directly next to current cell
            return new Road(toGrid(x), toGrid(y));
        }
        else{
            return cells[x][y];
        }
    }

    //Creates buildings at the end of roads
    private Cell addBuildings(int x, int y){
        int numRoads = cellCountDirect(x, y, "Road");
        //Creates buildings at the end of roads (see ABOVE_AND_BEYOND.md for a proper explanation)
        if(cellCountAll(x, y, "Road") < 3 && numRoads < 3 && numRoads > 0 &&surroundingContains(x, y, "Building") == false && cells[x][y].getElevation() > 0){
            return new Building(toGrid(x), toGrid(y));
        }
        else{
            return cells[x][y];
        }
    }

    //Returns an array of all cells surrounding the current cell
    public ArrayList<Cell> surrounding(int x, int y){
        ArrayList<Cell> surroundingCells = new ArrayList<Cell>();
        //Each item in 'direction' is a pivot, this is applied to the current cell and the result of the pivot is added to the surroundingCells list.
        for(int[] direction: directionPoints){
            int cx = x + direction[0]; //Adds x pivot
            int cy = y + direction[1]; //Adds y pivot
            if(cy >=0 && cy < cells.length){ //Makes sure resulting cell is within the bounds of the screen (for y)
                if(cx >= 0 && cx < cells[cy].length){ //Same as above but for x
                    surroundingCells.add(cells[cx][cy]); //Adds new cell to list
                }
            }
        }
        
        return surroundingCells;
    }
  
    //Returns if there is a cell of a certain type surrounding the current cell
    public boolean surroundingContains(int x, int y, String name){
        ArrayList<Cell> surroundingCells = surrounding(x,y); //Finds surrounding cells
        for(Cell i: surroundingCells){
            if(i != null){
                if(i.getClass().getName().equals(name)){ //If a cell surrounding is equal to the cell that is being looked for, return true
                    return true;
                }
            }
        }
        return false;
    }

    //Finds the lowest cell surrounding the current cell
    public Cell surroundingLowest(int x, int y){
        ArrayList<Cell> surroundingCells = surrounding(x,y); //Finds surrounding cells
        Cell currentLowest = new Grass(0,0,6001); //Creates placeholder cell with impossibly high elevation
       
        for(Cell i: surroundingCells){
            if(i != null){
                if(i.getElevation() < currentLowest.getElevation()){ //Compares each cell to previous cell to see if elevation is lower
                    currentLowest = i;
                }
            }
        }
        return currentLowest; //Returns lowest cell from surrounding
    }
  
    //Counts cells of a certain type that are directly next to the current cell (i.e. not diagonal)
    private int cellCountDirect(int x, int y, String name){
        ArrayList<Cell> surroundingCells = surrounding(x,y); //Finds surrounding cells
        int cellCount = 0;
        for(Cell i: surroundingCells){
            if(toCells(i.x) == x || toCells(i.y) == y){ //Looks at cells that are either the same on the x or the y axis (i.e. not diagonal)
                if(i.getClass().getName().equals(name)){
                    cellCount++; //adds to the count if they are the same as the input name
                }
            }
        }
        return cellCount;
    }

    //Counts cells of a certain type surrounding the current cell (i.e. including diagonal)
    private int cellCountAll(int x, int y, String name){
        ArrayList<Cell> surroundingCells = surrounding(x,y); //Finds surrounding cells
        int cellCount = 0;
        for(int i = 0; i < surroundingCells.size() ; i++){
            if(surroundingCells.get(i) != null){
                if(surroundingCells.get(i).getClass().getName().equals(name)){ //Loops through surrounding cells and adds to the count if they are equal to the input name
                        cellCount++;
                }
            }
        }
        return cellCount;
    }
  
    //Finds the closest mountain top to the current cell
    private Cell closestMT(int x, int y){
        ArrayList<Cell> mtCopy = mountainTops; //Creates new list
        if(mtCopy.size() > 0){ //Empty check 
            Cell currentClosest = mtCopy.get(0);
            int currentClosestD = distance(currentClosest, new Cell(toGrid(x), toGrid(y)));
            for(Cell i: mtCopy){
                if(distance(i , new Cell(toGrid(x),toGrid(y))) < currentClosestD){ //Loops through all mountainTops and finds one with the least distance to the current cell
                    currentClosest = i;
                }
            }
            return currentClosest;
        }
        return null;
    }

    //Finds the distance between two cells
    private int distance(Cell cell1, Cell cell2){
        if(cell1 != null && cell2 != null){ //null check 
            //pythagoras theorem a^2 + b^2 = c^2
            int xDistance = toCells(cell1.x) - toCells(cell2.x); //finds a
            int yDistance = toCells(cell1.y) - toCells(cell2.y); //find b 
            return (int)(Math.sqrt(xDistance * xDistance + yDistance * yDistance)); //returns c
        }
        else{
            return 600;
        }
    }

    //Paints each cell
    public void paint(Graphics g, Point mousePos){
        for(int i = 0; i < cells.length; i++){
            for(int j = 0; j < cells[i].length; j++){
                cells[i][j].paint(g, mousePos, cells[i][j].getColour());
            }
        }
    }

    //Returns the cell that the mouse is over
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
   
    //Converts position in array to position on screen 
    public int toGrid(int input){ 
        return 10 + 35 * input;
    }
    //Converts position on screen to position in array
    public int toCells (int input){
        return (input - 10)/35;
    }
   

    public Cell cellAtColRow(int c, int r) {
        return cells[c][r];
    }
    
}