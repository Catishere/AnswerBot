package com.cat.bots.util;

import java.awt.*;

public class Pallette {
    
    private Color red;
    private Color green;
    private Color blue;
    private Color gray;
    private Color yellow;
    private Color specialYellow;
    private Color deadYellow;
    private Color black;

    public Pallette() {
        this.red = new Color(255,63,63);
        this.green = new Color(0, 204, 0);
        this.blue = new Color(153, 204, 255);
        this.gray = new Color(204, 204, 204);
        this.yellow = new Color(255, 180, 0);
        this.specialYellow = new Color(255, 178, 0);
        this.deadYellow = new Color(255, 176, 0);
        this.black = new Color(0, 0, 0);
    }

    public Color getRed() {
        return red;
    }

    public Color getGreen() {
        return green;
    }

    public Color getBlue() {
        return blue;
    }

    public Color getGray() {
        return gray;
    }

    public Color getYellow() {
        return yellow;
    }

    public Color getSpecialYellow() {
        return specialYellow;
    }

    public Color getDeadYellow() {
        return deadYellow;
    }

    public Color getBlack() {
        return black;
    }

    public boolean hasColor(Color sample) {
        return getRed().equals(sample)
                || getGreen().equals(sample)
                || getBlue().equals(sample)
                || getYellow().equals(sample)
                || getGray().equals(sample)
                || getSpecialYellow().equals(sample);
    }

    public char serialize(Color color) {
        if (getRed().equals(color))
            return 'r';
        else if (getGreen().equals(color))
            return 'g';
        else if (getBlue().equals(color))
            return 'b';
        else if (getYellow().equals(color))
            return 'y';
        else if (getBlack().equals(color))
            return 'z';
        else if (getGray().equals(color))
            return 'w';
        else
            return 'n';
    }
}
