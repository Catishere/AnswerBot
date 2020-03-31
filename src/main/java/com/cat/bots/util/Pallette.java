package com.cat.bots.util;

import java.awt.*;

public class Pallette {

    public static Color getRed() {
        return new Color(255,63,63);
    }

    public static Color getGreen() {
        return new Color(0, 204, 0);
    }

    public static Color getBlue() {
        return new Color(153, 204, 255);
    }

    public static Color getGray() {
        return new Color(204, 204, 204);
    }

    public static Color getYellow() {
        return new Color(255, 180, 0);
    }

    public static Color getBlack() {
        return new Color(0, 0, 0);
    }

    public static boolean hasColor(Color sample) {
        return getRed().equals(sample)
                || getGreen().equals(sample)
                || getBlue().equals(sample)
                || getYellow().equals(sample)
                || getGray().equals(sample);
    }

    public static char serialize(Color color) {
        if (Pallette.getRed().equals(color))
            return 'r';
        else if (Pallette.getGreen().equals(color))
            return 'g';
        else if (Pallette.getBlue().equals(color))
            return 'b';
        else if (Pallette.getYellow().equals(color))
            return 'y';
        else if (Pallette.getBlack().equals(color))
            return 'z';
        else if (Pallette.getGray().equals(color))
            return 'w';
        else
            return 'n';
    }
}
