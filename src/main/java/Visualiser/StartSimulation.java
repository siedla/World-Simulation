package Visualiser;

import map.JungleMap;
import simulation.WorldSimulation;

public class StartSimulation {


    int width;
    int height;
    int startEnergy;
    int jungleRatio;
    int grassEnergy;
    int dailyEnergy;

    public StartSimulation(Integer[] defaultMapProperties) {
        width = defaultMapProperties[0];
        height = defaultMapProperties[1];
        jungleRatio = defaultMapProperties[2];
        grassEnergy = defaultMapProperties[3];
        dailyEnergy = defaultMapProperties[4];
        startEnergy = defaultMapProperties[5];

        JungleMap map = new JungleMap(width, height,jungleRatio, grassEnergy, dailyEnergy, startEnergy);
        JungleMap map2 = new JungleMap(width, height,jungleRatio, grassEnergy, dailyEnergy, startEnergy);
        WorldSimulation simulation = new WorldSimulation(map, map2);
        simulation.startSimulation();

    }


}