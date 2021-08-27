I wanted to create natural looking terrain that would be generated as realistically as possible.
I decided the following rules needed to be consistent accross different generations of the files, these included:

**GENERAL RULES**
- Mountains would peak at 6000m
- Grass generated around mountains must slope down from the mountain in a realistic way (i.e. grass cells closer to a mountain would be higher in elevation, and then get lower as they moved further away).
- Oceans would be created where cells dipped below sea level, there should also be a high chance of oceans being created in each generation
- All water cells should flow down elevation (i.e. rivers from high points)
- Roads should avoid mountains and the ocean where possible and should connect buildings (rather than just being random)

**TERRAIN GENERATION OVERVIEW**
My terrain gets generated in stages:
	1. Every cell is filled in with either grass (-500m) or a mountain top (6000m)
	2. Around each moutain top, other moutain cells are created
	3. Each grass cell's elevation is set based on its distance from a mountain top
	4. River source cells are added in, these then recursively create more water cells at the lowest elevation near them.
	5. Roads are created not on mountains or rivers
	6. Buildings are added at the end of roads

*Quick note about the probabilities of cells*
The assignment specs asks for specific probabilities of each type of cell. 
In my original completion of the assignment I had these but with my current generation it's a bit hard to have specific probabilites of certain cells. This is because for alot of cells, the source cell is created and other cells are made off that. The probability of the source cells can be changed but this doesn't guarentee the probability of following cells. 

If we needed the specific probabilities to get marks for the pass level this was my old code: 
	
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

**STRUCTURE OF MY PROGRAM**
In my grid function, my level is created in stages. Each stage is called one after the other in the setCell() method, in which the array of cells is looped through multiple times and different types of cell are created/set. 
The sequence of my program is as follows:

Grid() -> setCell()
                    -> mountainTop/grass loop -> mountainTop()
					-> rest of mountains loop -> mountainCont()
					-> elevation of grass loop -> grassElevation()
					-> river cells loop -> riverSource()
					-> create roads loop -> addRoad()
					-> create buildings loop -> addBuildings()
					
**mountainTop()** 
This function was originally meant to return a cell based on simple rules; it would either return a grass cell or a mountainTop cell depending on the probMountainT variable.
It would also not create two mountainTops directly next to each other. However, you'll notice that this function is a bit more complicated than that now. 

Essentially, the rest of the code in this function ensures that mountains will only get generated together, i.e. there won't be two seperate mountain ranges. 
This will be explained in further detail in the grassElevation() section but because I was limited on time I couldn't have the program calculate elevation based on two 
mountainTops. Keeping the mountain tops together makes this alot less noticeable. 

**mountainCont()**
This function is fairly straightforward, it will add mountian bodies around mountain tops depending on the probMountainCT and probMountainC variables. 
If a mountain body is being generated right next to a mountain top it will be higher than mountain bodies not right next to a mountain top. 

**grassElevation()**
This function essentially creates a sin wave with the peak of the sin wave being the closest mountain top to the grass cell. 
It first finds the closest mountainTop to the cell, then calculates the distance from that mountainTop it is. 
The elevation of the cell is then calculated essentially using this distance as the x value. 

One limitation I had with this function was that it only takes in input from one mountainTop and ignores all others. 
This meant that when there were mountainTops far away from one another, the cells in between them would  have weird jumps in elevation. 

If I had more time, I would create a function that does this sin calculation with every mountainTop on the map and averages them out. 
This would hopefully smooth out the cells and create more natural looking terrain but I would have to experiment to find out.

**riverSource()**
 Like all the other functions, this code creates water cells based on the probRiverS variable. 
 However, water cells are different to other cells. When creating a water cell I pass in a reference to the lowest cell next to it along with its x and y values. 
 
 The water class then creates another water cell in the lowest cell next to it, passing in the lowest cell next to that cell.
 This continues until the new cell is at the bottom of a valley (i.e. there are no cells lower than it surrounding it).  
 
 I created this using recursion as creating it using a loop in the grid would result in rivers not being able to travel up the screen. 
 
 I got some problems with this creating infinite loops occasionally and I ran out of time to investigate further so I added a riverLength variable that determines how long this recrusion can last. 
 
 **addRoad()**
This function starts with creating roads randomly on the grid, similar to the water source blocks. It then runs again with the cellCountDirect function.
This function looks at the cells that are directly next to a cell (i.e. not diagonal to it) and counts the cells that are of a particular type. 
If there is a cell that has exactly 1 road cell directly next to it, it will place another road cell there. 
Because water and mountain cells are not passed into this function, roads will stop at water and mountains. 

**addBuildings()**
This function will create buildings at the end of roads. It does this with the following rules. 
	1. cellCountAll() -> Surrounding the cell, there cannot be more than 3 road cells (so buildings aren't generated in the middle of a road)
	2. cellCountDirect() -> Directly next to the cell there has to be less than 3 but at least 1 road cells. This means that buildings will get created at the end of a road (it used to be exactly 1 but two roads sometimes end on the same cell).
	3. Buildings can only get created above 0. This is because sometimes buildings were getting generated right next to the river and it looked weird.  


**THINGS THAT STILL NEED WORK**
- The elevation is only calculated based on one mountain top, the mountain bodies elevations aren't calculated from this either so there is often grass cells next to mountain cells that are taller than the mountain cells. 
- Because the elevation is calulated from sin, the elevation comes back up towards the ends of the map. I left this is because it creates cool rivers that I liked the look of. This probably isn't realistic however, in the future I would make the sin curve flatten at -500 (i.e. not come back up). I'd like to do some more research into how actual terrain is formed to see if I can make this better somehow. 
- Because cells are generated top left down, mountains have a much higher chance of generating in the top left of the screen. Because there can only be one mountain, the terrain ends up looking pretty similar on each generation as the mountain starts in a roughly similar place. 
- Roads sometimes don't go anywhere, ideally I'd like to have roads always end in a building. I'd also like to have them generate in more straight lines because at the moment they look kind of random. It would be interesting to generate buildings first, then use a function that finds the surrounding cell that is closest to its nearest building and generate a road there. This function would be similar to the water function but would go to cells based on distance to a target rather than the lowest elevation. 
- I'd love to impliment a seed function so you could see cool generations again but I'd have to do some research to figure out how to go about implementing that. I don't think my code is ready for that at the moment. 





