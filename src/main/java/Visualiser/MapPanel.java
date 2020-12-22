package Visualiser;

import EnumClasses.Icon;
import map.JungleMap;
import map.Vector2d;
import mapElements.Animal;
import mapElements.Grass;
import simulation.WorldSimulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MapPanel extends JPanel implements MouseListener {

    public JungleMap map;
    public WorldSimulation simulation;
    private final int mapHeight;
    private int widthScale;
    private int heightScale;

    public MapPanel(JungleMap map, WorldSimulation simulation) {
        this.map = map;
        this.simulation = simulation;
        this.mapHeight = simulation.frame.getHeight() - 38;
        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setSize((int) (simulation.frame.getWidth() * 0.6), mapHeight);
        this.setLocation((int) (0.4 * simulation.frame.getWidth()), 0);
        int width = this.getWidth();
        int height = this.getHeight();
        widthScale = width / map.width;
        heightScale = height / (map.height*2);


        g.setColor(new Color(217, 222, 109));
        g.fillRect(0, 0, width, height/2);


        g.setColor(new Color(16, 76, 15));
        g.fillRect(map.getJungleLowerLeft().x * widthScale, map.getJungleLowerLeft().y * heightScale,
                map.jungleWidth * widthScale, map.jungleHeight * heightScale);


        ImageIcon grassImage = Icon.GRASS.getIcon();
        Image image = grassImage.getImage();
        Image newimg = image.getScaledInstance(widthScale, heightScale, Image.SCALE_SMOOTH);
        grassImage = new ImageIcon(newimg);

        for (Grass grass : map.getGrass()) {
            int y = grass.getPosition().y * heightScale;
            int x = grass.getPosition().x * widthScale;
            grassImage.paintIcon(this, g, x, y);

        }

        ImageIcon lowEnergyIcon = Icon.ANIMAL_LOW_ENERGY.getIcon();
        image = lowEnergyIcon.getImage();
        newimg = image.getScaledInstance(widthScale, heightScale,  java.awt.Image.SCALE_SMOOTH);
        lowEnergyIcon = new ImageIcon(newimg);

        ImageIcon highEnergyIcon = Icon.ANIMAL_HIGH_ENERGY.getIcon();
        image = highEnergyIcon.getImage();
        newimg = image.getScaledInstance(widthScale, heightScale,  java.awt.Image.SCALE_SMOOTH);
        highEnergyIcon = new ImageIcon(newimg);

        for (Animal animal : map.getAnimals()) {
            int y = animal.getPosition().y * heightScale;
            int x = animal.getPosition().x * widthScale;

            if(animal.getEnergy() < map.getStartAnimalsEnergy()*0.5){
                lowEnergyIcon.paintIcon(this, g, x, y);
            }
            else {
                highEnergyIcon.paintIcon(this, g, x, y);
            }

        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX() / widthScale;
        int y = e.getY() / heightScale;
        simulation.setPointedAnimal(new Vector2d(x, y));
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}