package simulation;

import Visualiser.InfoPanel;
import Visualiser.MapPanel;
import map.JungleMap;
import map.Vector2d;
import mapElements.Animal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class WorldSimulation implements ActionListener {

    public final int delay = 100;
    public JungleMap map;
    public JungleMap map2;
    public int startNumOfAnimals;

    public JFrame frame;
    public MapPanel mapPanel;
    public Timer timer;
    public InfoPanel infoPanel;

    public MapPanel mapPanel2;

    public JPanel firstMap;
    public JPanel secondMap;

    public int simulationDay;
    public Animal pointedAnimal;
    public int deathDay;

    public WorldSimulation(JungleMap map, JungleMap map2) {

        this.map = map;
        this.map2 = map2;
        this.startNumOfAnimals = 20;
        simulationDay = 0;

        timer = new Timer(delay, this);

        frame = new JFrame("Evolution Simulator");
        frame.setLayout(null);
        frame.setSize(700, 715);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        firstMap = new JPanel();
        firstMap.setBounds(0,0,700,350);
        frame.add(firstMap);

        secondMap = new JPanel();
        secondMap.setBounds(0,frame.getHeight()/2,frame.getWidth(),frame.getHeight()/2);
        frame.add(secondMap);

        mapPanel = new MapPanel(map, this);
        mapPanel.setSize(new Dimension(1, 1));

        mapPanel2 = new MapPanel(map2, this);
        mapPanel2.setSize(new Dimension(1, 1));
        infoPanel = new InfoPanel(map,this);
        infoPanel.setSize(1, 1);

        firstMap.add(mapPanel);
        secondMap.add(mapPanel2);
        mapPanel2.setVisible(false);
        frame.add(infoPanel);


    }

    public void startSimulation() {
        map.placeStartAnimal(startNumOfAnimals);
        map2.placeStartAnimal(startNumOfAnimals);
        for(int i=0; i<map.width*map.height/10; i++){
            map.addNewGrass();
            map2.addNewGrass();
        }
        timer.start();

    }

    public void pauseSimulation() {
        timer.stop();
    }

    public void resumeSimulation() {
        timer.start();
    }

    public void showSecondMap() {
        mapPanel2.setVisible(true);
    }

    public void hideSecondMap() {
        mapPanel2.setVisible(false);

    }

    public int getSimulationDay(){
        return simulationDay;
    }

    public void setPointedAnimal(Vector2d position) {
        pointedAnimal = map.getAnimalAtPosition(position);
        deathDay = 0;
        infoPanel.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        simulationDay++;
        mapPanel.repaint();
        infoPanel.repaint();
        mapPanel2.repaint();

        map.clearDeadAnimals();
        map.moveAnimals();
        map.consumeGrassByAnimals();
        map.reproduce();
        map.addNewGrass();
        map.reduceEnergyForAll();

        map2.clearDeadAnimals();
        map2.moveAnimals();
        map2.consumeGrassByAnimals();
        map2.reproduce();
        map2.addNewGrass();
        map2.reduceEnergyForAll();
        if(pointedAnimal != null && !pointedAnimal.isAlive() && deathDay==0){
            deathDay = simulationDay;
        }

    }
}
