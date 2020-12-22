package Visualiser;

import map.JungleMap;
import simulation.WorldSimulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

public class InfoPanel extends JPanel implements ActionListener {
    private final JungleMap map;
    private final WorldSimulation simulation;
    private final JCheckBox addMap;
    private final JButton pauseButton;
    private final JButton startButton;
    private final JButton exportButton;

    public InfoPanel(JungleMap map, WorldSimulation simulation) {
        setFont(new Font("Arial", Font.BOLD, 12));
        setLayout(null);
        this.map = map;
        this.simulation = simulation;
        addMap = new JCheckBox("Show second map");
        addMap.addActionListener(this);
        addMap.setBounds(10,600,150,20);
        add(addMap);
        pauseButton = new JButton("Pause Simulation");
        pauseButton.addActionListener(this);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(0, 500, (int) (simulation.frame.getWidth() * 0.4),50);
        buttonPanel.add(pauseButton);
        add(buttonPanel);

        startButton = new JButton("Start Simulation");
        startButton.addActionListener(this);
        buttonPanel.add(startButton);

        exportButton = new JButton("Export Info");
        exportButton.addActionListener(this);
        JPanel exportPanel = new JPanel();
        exportPanel.setBounds(10, 550, 100,50);
        exportPanel.add(exportButton);

        add(exportPanel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setSize((int) (simulation.frame.getWidth() * 0.4), simulation.frame.getHeight() - 38); //38 is toolbar size
        this.setLocation(0, 0);
        int height = getHeight();

        int simulationDay = simulation.getSimulationDay();
        int animalsSum = map.getAnimals().size();

        g.drawString("Number of animals on the map: " + animalsSum, 20, 50);

        int grassSum = map.getGrass().size();
        g.drawString("Number of grass on the map: " + grassSum, 20,70);

        int averageAge = map.averageAge();
        g.drawString("Average life length: " + averageAge, 20, 90);

        int averageEnergy = map.averageEnergy();
        g.drawString("Average animals energy: " + averageEnergy, 20, 110);

        double averageNumberOfChildren = map.averageChildrenNumber();
        g.drawString("Average number of children: " + averageNumberOfChildren, 20, 130);

        int theMostPopularGene = map.getMostPopularGene();
        g.drawString("Dominant gene: " + theMostPopularGene, 20, 150);



        if(simulation.pointedAnimal != null){
            g.drawString("Pointed animal info", 20, 210);
            g.drawString("Energy: " + simulation.pointedAnimal.getEnergy(), 20, 230);
            g.drawString("Genome: ", 20, 250);
            g.drawString(""+simulation.pointedAnimal.genes.showGenome(), 20, 270);
            g.drawString("Number of children: "+simulation.pointedAnimal.getNumberOfChildren(), 20, 290);
            if(simulation.pointedAnimal.isAlive()){
                g.drawString("Pointed animal is alive", 20, 310);
            }
            else{
                g.drawString("The day of animal's death: "+simulation.deathDay, 20, 310);
            }
        }
        else{
            g.drawString("Click on the animal on the first map to see", 20, 210);
            g.drawString(" details ", 20, 230);
        }

        g.drawString("Day: " + simulationDay, 15, height - 30);


    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Object clickedButton = e.getSource();
        if(clickedButton == startButton){
            simulation.resumeSimulation();
        }
        else if(clickedButton == pauseButton){
            simulation.pauseSimulation();
        }

        if(clickedButton == addMap){
            assert addMap != null;
            if(addMap.isSelected()){
                simulation.showSecondMap();
            }
            else{
                simulation.hideSecondMap();
            }
        }
        if(clickedButton == exportButton){
            try {
                FileWriter myWriter = new FileWriter("src\\main\\resources\\exportedInfo.txt");
                myWriter.write("Day number: " + simulation.getSimulationDay());
                myWriter.write("\nNumber of animals on the map: " + map.getAnimals().size());
                myWriter.write("\nNumber of grass on the map: "+map.getGrass().size());
                myWriter.write("\nDominant gene: "+map.getMostPopularGene());
                myWriter.write("\nAverage animals energy: "+map.getStartAnimalsEnergy());
                myWriter.write("\nAverage life length: "+map.averageAge());
                myWriter.write("\nAverage number of children: "+map.averageChildrenNumber());
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException ex) {
                System.out.println("An error occurred.");
                ex.printStackTrace();
            }
        }
    }

}
