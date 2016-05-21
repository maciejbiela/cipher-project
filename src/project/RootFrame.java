package project;

import javax.swing.*;
import java.awt.*;

import static project.MenuState.CHOOSER;
import static project.MenuState.CIPHER_FILLER;

class RootFrame extends JFrame {

    private Grid grid;
    private Menu menu;

    RootFrame() {
        setLayout(new BorderLayout());
        setInnerLayout(new Chooser(4), CHOOSER);
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
        if (!(menuState == CIPHER_FILLER)) {
            setMenuPanel(menuState);
        }
        add(panel, BorderLayout.CENTER);
        revalidate();
    }

    private void setMenuPanel(MenuState menuState) {
        if (menu != null) {
            remove(menu);
        }
        menu = new Menu(this, grid.getBoardSize(), menuState);
        add(menu, BorderLayout.NORTH);
    }
}
