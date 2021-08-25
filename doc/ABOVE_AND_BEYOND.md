I wanted to create natural looking terrain that would be generated as realistically as possible.
I decided the following rules needed to be consistent accross different generations of the files, these included:

**GENERAL RULES**
- Mountains would peak at 6000m
- Grass generated around mountains must slope down from the mountain in a realistic way (i.e. grass cells closer to a mountain would be higher in elevation, and then get lower as they moved further away).
- Oceans would be created where cells dipped below sea level, there should also be a high chance of oceans being created in each generation
- All water cells should flow down elevation (i.e. rivers from high points)
- Roads should avoid mountains and the ocean where possible and should connect buildings (rather than just being random)


In order to achieve these rules, I create the level in stages:

**STAGE 1**
