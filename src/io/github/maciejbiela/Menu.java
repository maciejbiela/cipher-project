package io.github.maciejbiela;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        setUpElements(menuState);
        addElements();
        initializeButtonsState(menuState);
    }

    private void setUpElements(MenuState menuState) {
        setUpOpenButton();
        setUpBoardSizeTextField();
        setUpDoneButton(menuState);
        setUpRotateRightButton();
        setUpSaveButton();
    }

    private void initializeButtonsState(MenuState menuState) {
        buttonStateMap.put(openButton, ButtonState.ENABLED);
        buttonStateMap.put(doneButton, ButtonState.ENABLED);
        if (menuState == MenuState.CIPHER_FILLER) {
            buttonStateMap.put(rotateRightButton, ButtonState.ENABLED);
        } else {
            buttonStateMap.put(rotateRightButton, ButtonState.INVISIBLE);
        }
        buttonStateMap.put(saveButton, ButtonState.INVISIBLE);
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
            SwingUtilities.invokeLater(() -> {
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
            });
        }
    }

    private void setUpBoardSizeTextField() {
        boardSizeTextField.setText(String.valueOf(size));
        boardSizeTextField.setColumns(10);
        boardSizeTextField.addActionListener(e -> {
            String text = boardSizeTextField.getText();
            int n = Integer.parseInt(text);
            frame.setInnerLayout(new Chooser(n), MenuState.CHOOSER);
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
                        frame.setInnerLayout(new Chooser(cipher), MenuState.OPENED_FROM_FILE);
                        buttonStateMap.put(doneButton, ButtonState.INVISIBLE);
                        buttonStateMap.put(rotateRightButton, ButtonState.INVISIBLE);
                        buttonStateMap.put(saveButton, ButtonState.INVISIBLE);
                        setButtonsState();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    private void setUpDoneButton(MenuState menuState) {
        doneButton.addActionListener(e -> {
            final Grid grid = frame.getGrid();
            if (!grid.isFull()) {
                return;
            }
            switch (menuState) {
                case CHOOSER:
                    boolean[][] matrix = grid.getFieldStatusMatrix();
                    int n = frame.getGrid().getBoardSize();
                    frame.setInnerLayout(new CipherFiller(n), MenuState.CIPHER_FILLER);
                    frame.getGrid().setFieldStatusMatrix(matrix);
                    buttonStateMap.put(rotateRightButton, ButtonState.ENABLED);
                    setButtonsState();
                    break;
                case OPENED_FROM_FILE:
                    buttonStateMap.put(rotateRightButton, ButtonState.ENABLED);
                    setButtonsState();
                    frame.getGrid().highlightFields();
                    break;
                case CIPHER_FILLER:
                    buttonStateMap.put(saveButton, ButtonState.ENABLED);
                    setButtonsState();
                    break;
            }
        });
    }

    private void setUpRotateRightButton() {
        rotateRightButton.addActionListener(e -> {
            final Grid cipherFiller = frame.getGrid();
            cipherFiller.rotate();
        });
    }

    private void setUpSaveButton() {
        saveButton.addActionListener(e -> {
            final CipherFiller cipherFiller = (CipherFiller) frame.getGrid();
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
