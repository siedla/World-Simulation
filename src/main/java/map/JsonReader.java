package map;

import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.FileReader;


public class JsonReader {

    private final int width;
    private final int height;
    private final int jungleRatio;
    private final int grassEnergy;
    private final int dailyEnergy;
    private final int startAnimalsEnergy;

    public JsonReader(int width, int height, int jungleRatio, int grassEnergy, int dailyEnergy, int startAnimalsEnergy) {
        this.width = width;
        this.height = height;
        this.jungleRatio = jungleRatio;
        this.grassEnergy = grassEnergy;
        this.dailyEnergy = dailyEnergy;
        this.startAnimalsEnergy = startAnimalsEnergy;
    }


    static public JsonReader loadPropFromFile() throws FileNotFoundException,IllegalArgumentException {
        Gson gson = new Gson();

        return gson.fromJson(new FileReader("src\\main\\resources\\Parameters.json"), JsonReader.class);
    }


    public int getWidth(){
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getJungleRatio(){
        return jungleRatio;
    }

    public int getGrassEnergy() {
        return grassEnergy;
    }

    public int getDailyEnergy() {
        return dailyEnergy;
    }

    public int getStartAnimalsEnergy() {
        return startAnimalsEnergy;
    }
}
