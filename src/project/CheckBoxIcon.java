package project;

import javax.swing.*;
import java.awt.*;

class CheckBoxIcon implements Icon {

    private final int iconWidth = 20;
    private final int iconHeight = 20;

    public void paintIcon(Component component, Graphics g, int x, int y) {
        AbstractButton abstractButton = (AbstractButton) component;
        ButtonModel buttonModel = abstractButton.getModel();

        if (buttonModel.isEnabled()) {
            if (buttonModel.isSelected()) {
                g.setColor(Color.RED);
                g.fillRect(1, 1, iconWidth, iconHeight);
            } else {
                g.setColor(Color.BLACK);
                g.drawRect(1, 1, iconWidth, iconHeight);
            }
        } else {
            g.setColor(Color.GRAY);
            g.fillRect(1, 1, iconWidth, iconHeight);
        }
    }

    public int getIconWidth() {
        return iconWidth;
    }

    public int getIconHeight() {
        return iconHeight;
    }
}
