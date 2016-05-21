package project;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static project.ButtonState.ENABLED;
import static project.ButtonState.INVISIBLE;
import static project.MenuState.CHOOSER;
import static project.MenuState.CIPHER_FILLER;
import static project.MenuState.OPENED_FROM_FILE;

class Menu extends JPanel {

    private final RootFrame frame;
    private final int size;
    private final JTextField boardSizeTextField = new JTextField();
    private final JButton openButton = new JButton("Open");
    private final JButton doneButton = new JButton("Done");
    private final JButton rotateRightButton = new JButton("Rotate");
    private final JButton saveButton = new JButton("Save");

    private Map<JButton, ButtonState> buttonStateMap = new HashMap<>();

    Menu(RootFrame frame, int size, MenuState menuState) {
        this.frame = frame;
        this.size = size;
        setUpElements();
        addElements();
        initializeButtonsState(menuState);
    }

    private void setUpElements() {
        setUpOpenButton();
        setUpBoardSizeTextField();
        setUpDoneButton();
        setUpRotateRightButton();
        setUpSaveButton();
    }

    private void initializeButtonsState(MenuState menuState) {
        if (menuState == CHOOSER) {
            buttonStateMap.put(openButton, ENABLED);
            buttonStateMap.put(doneButton, ENABLED);
            buttonStateMap.put(rotateRightButton, INVISIBLE);
            buttonStateMap.put(saveButton, INVISIBLE);
        } else if (menuState == OPENED_FROM_FILE) {
            buttonStateMap.put(openButton, ENABLED);
            buttonStateMap.put(doneButton, INVISIBLE);
            buttonStateMap.put(rotateRightButton, INVISIBLE);
            buttonStateMap.put(saveButton, INVISIBLE);
        }
        setButtonsState();
    }

    private void addElements() {
        this.add(openButton);
        this.add(boardSizeTextField);
        this.add(doneButton);
        this.add(rotateRightButton);
        this.add(saveButton);
    }

    private void setButtonsState() {
        List<JButton> buttons = Arrays.asList(openButton, doneButton, rotateRightButton, saveButton);
        for (JButton button : buttons) {
            ButtonState state = buttonStateMap.get(button);
            switch (state) {
                case ENABLED:
                    button.setVisible(true);
                    button.setEnabled(true);
                    break;
                case DISABLED:
                    button.setVisible(true);
                    button.setEnabled(false);
                    break;
                case INVISIBLE:
                    button.setVisible(false);
                    button.setEnabled(false);
                    break;
            }
        }
    }

    private void setUpBoardSizeTextField() {
        boardSizeTextField.setText(String.valueOf(size));
        boardSizeTextField.setColumns(10);
        boardSizeTextField.addActionListener(e -> {
            String text = boardSizeTextField.getText();
            int n = Integer.parseInt(text);
            frame.setInnerLayout(new Chooser(n), CHOOSER);
        });
        boardSizeTextField.setHorizontalAlignment(SwingConstants.CENTER);
    }


    private void setUpOpenButton() {
        openButton.addActionListener(e -> {
            JFileChooser c = new JFileChooser();
            int option = c.showOpenDialog(Menu.this);
            if (option == JFileChooser.APPROVE_OPTION) {
                Path path = Paths.get(c.getCurrentDirectory().getAbsolutePath(), c.getSelectedFile().getName());
                if (Files.exists(path)) {
                    try {
                        final String cipher = new String(Files.readAllBytes(path));
                        frame.setInnerLayout(new CipherFiller(cipher), OPENED_FROM_FILE);
                        buttonStateMap.put(doneButton, INVISIBLE);
                        buttonStateMap.put(rotateRightButton, INVISIBLE);
                        buttonStateMap.put(saveButton, INVISIBLE);
                        setButtonsState();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    private void setUpDoneButton() {
        doneButton.addActionListener(e -> {
            final MainLayoutPanel mainLayoutPanel = frame.getMainLayoutPanel();
            if (!mainLayoutPanel.isFull()) {
                return;
            }
            if (mainLayoutPanel instanceof Chooser) {
                boolean[][] matrix = mainLayoutPanel.getFieldStatusMatrix();
                int n = frame.getMainLayoutPanel().getBoardSize();
                frame.setInnerLayout(new CipherFiller(n), CIPHER_FILLER);
                frame.getMainLayoutPanel().setFieldStatusMatrix(matrix);
                buttonStateMap.put(rotateRightButton, ENABLED);
                setButtonsState();
            } else {
                buttonStateMap.put(saveButton, ENABLED);
                setButtonsState();
            }
        });
    }

    private void setUpRotateRightButton() {
        rotateRightButton.addActionListener(e -> {
            final CipherFiller cipherFiller = (CipherFiller) frame.getMainLayoutPanel();
            cipherFiller.rotate();
        });
    }

    private void setUpSaveButton() {
        saveButton.addActionListener(e -> {
            final CipherFiller cipherFiller = (CipherFiller) frame.getMainLayoutPanel();
            if (cipherFiller.isFull()) {
                final String cipher = cipherFiller.getCipher();
                JFileChooser c = new JFileChooser();
                int option = c.showSaveDialog(Menu.this);
                if (option == JFileChooser.APPROVE_OPTION) {
                    Path path = Paths.get(c.getCurrentDirectory().getAbsolutePath(), c.getSelectedFile().getName());
                    if (!Files.exists(path)) {
                        try {
                            Files.createFile(path);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                    try {
                        Files.write(path, cipher.getBytes());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }
}
