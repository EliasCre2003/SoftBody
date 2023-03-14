package eliascregard.interactives;

import eliascregard.math.vectors.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;

public class IconButton extends Button {

    BufferedImage icon;
    Dimension iconSize;

    public IconButton(String label, Vector2D position, BufferedImage icon, Dimension iconSize) {
        super(label, position);
        this.icon = icon;
        this.iconSize = iconSize;
    }

    public IconButton(Vector2D position, BufferedImage icon, Dimension iconSize) {
        this("", position, icon, iconSize);
    }


}
