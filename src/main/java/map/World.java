package map;

import Visualiser.StartSimulation;

import java.io.IOException;

public class World {

    public static void main(String[] args) throws IOException {

        JsonReader properties = JsonReader.loadPropFromFile();
        Integer[] defaultMapProperties = {
                properties.getWidth(),
                properties.getHeight(),
                properties.getJungleRatio(),
                properties.getGrassEnergy(),
                properties.getDailyEnergy(),
                properties.getStartAnimalsEnergy(),
        };
        new StartSimulation(defaultMapProperties);

    }
}
