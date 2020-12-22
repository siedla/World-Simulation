package EnumClasses;

import javax.swing.*;

public enum Icon {

        ANIMAL_HIGH_ENERGY(new ImageIcon("src\\main\\resources\\animal_high_energy.png")),
        ANIMAL_LOW_ENERGY(new ImageIcon("src\\main\\resources\\animal_low_energy.png")),
        GRASS(new ImageIcon("src\\main\\resources\\grass.png"));


        public final ImageIcon icon;

        Icon(ImageIcon icon) {
            this.icon = icon;
        }

        public ImageIcon getIcon() {
            return icon;
        }

}
