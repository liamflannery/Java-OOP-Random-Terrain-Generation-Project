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
This function starts with creating roads 



