package io.github.maciejbiela;

import javax.swing.*;
import java.awt.*;

class RootFrame extends JFrame {

    private Grid grid;
    private io.github.maciejbiela.Menu menu;

    RootFrame() {
        setLayout(new BorderLayout());
        setInnerLayout(new Chooser(4), MenuState.CHOOSER);
        pack();
    }

    Grid getGrid() {
        return grid;
    }

    void setInnerLayout(Grid panel, MenuState menuState) {
        if (grid != null) {
            remove(grid);
        }
        grid = panel;
        setMenuPanel(menuState);
        add(panel, BorderLayout.CENTER);
        revalidate();
    }

    private void setMenuPanel(MenuState menuState) {
        if (menu != null) {
            remove(menu);
        }
        menu = new io.github.maciejbiela.Menu(this, grid.getBoardSize(), menuState);
        add(menu, BorderLayout.NORTH);
    }
}
