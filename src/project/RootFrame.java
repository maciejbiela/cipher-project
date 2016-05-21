package project;

import javax.swing.*;
import java.awt.*;

import static project.MenuState.CHOOSER;
import static project.MenuState.CIPHER_FILLER;

class RootFrame extends JFrame {

    private MainLayoutPanel mainLayoutPanel;
    private Menu menu;

    RootFrame() {
        setLayout(new BorderLayout());
        setInnerLayout(new Chooser(4), CHOOSER);
        pack();
    }

    MainLayoutPanel getMainLayoutPanel() {
        return mainLayoutPanel;
    }

    void setInnerLayout(MainLayoutPanel panel, MenuState menuState) {
        if (mainLayoutPanel != null) {
            remove(mainLayoutPanel);
        }
        mainLayoutPanel = panel;
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
        menu = new Menu(this, mainLayoutPanel.getBoardSize(), menuState);
        add(menu, BorderLayout.NORTH);
    }
}
