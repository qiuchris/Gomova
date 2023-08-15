package ui;

import com.formdev.flatlaf.FlatLightLaf;
import model.Board;
import model.Colour;
import model.Event;
import model.EventLog;
import model.Stone;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

// graphical UI class for Gomova

// uses code from
// https://docs.oracle.com/javase/tutorial/uiswing/examples/components/ButtonDemoProject/src/components/ButtonDemo.java

public class GomovaGUI implements ActionListener, WindowListener {
    private static final int DEFAULT_BOARD_SIZE = 15;
    private static final String JSON_PATH = "./data/board.json";
    private static final Color BOARD_COLOUR = new Color(180, 180, 180);

    private Board board;
    private JsonWriter writer;
    private JsonReader reader;

    private JFrame frame;
    private JPanel grid;
    private JPanel menu;

    private ArrayList<JButton> gridButtons;
    private ArrayList<JButton> menuButtons;

    private Image whiteImage;
    private Image blackImage;
    private ImageIcon scaledWhiteIcon;
    private ImageIcon scaledBlackIcon;

    // EFFECTS: instantiates necessary objects and starts the GUI
    public GomovaGUI() {
        board = new Board(DEFAULT_BOARD_SIZE);
        writer = new JsonWriter(JSON_PATH);
        reader = new JsonReader(JSON_PATH);
        frame = new JFrame("Gomova");
        try {
            whiteImage = ImageIO.read(new File("./data/white.png"));
            blackImage = ImageIO.read(new File("./data/black.png"));
        } catch (IOException e) {
            System.out.println("failed to load images");
        }
        javax.swing.SwingUtilities.invokeLater(this::initializeGUI);
    }

    // MODIFIES: this
    // EFFECTS: creates the menu buttons and grid
    private void initializeGUI() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            frame.setIconImage(ImageIO.read(new File("./data/gomoku.png")));
        } catch (IOException e) {
            System.out.println("failed to load icon");
        } catch (Exception e) {
            System.out.println("failed to load look and feel");
        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(this);
        frame.setLayout(new BorderLayout());

        createMenu();
        createGrid();
        updateTitle();

        frame.pack();
        frame.setResizable(false);
        scaleIcons();

        frame.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: creates and adds menu buttons to bottom and adds action listeners
    private void createMenu() {
        menu = new JPanel();
        FlowLayout buttonsLayout = new FlowLayout();
        menu.setLayout(buttonsLayout);

        menuButtons = new ArrayList<>();
        menuButtons.add(new JButton("Undo"));
        menuButtons.add(new JButton("Clear"));
        menuButtons.add(new JButton("Change Size"));
        menuButtons.add(new JButton("Save"));
        menuButtons.add(new JButton("Load"));
        menuButtons.add(new JButton("Help"));

        for (JButton b : menuButtons) {
            b.setActionCommand(b.getText());
            b.addActionListener(this);
            menu.add(b);
        }

        frame.add(menu, BorderLayout.SOUTH);
        frame.pack();
    }

    // MODIFIES: this
    // EFFECTS: creates and adds grid for stones
    private void createGrid() {
        try {
            frame.remove(grid);
        } catch (NullPointerException e) {
            // not an issue
        }
        grid = new JPanel();
        GridLayout gridLayout = new GridLayout(board.getBoardSize(), board.getBoardSize());
        grid.setLayout(gridLayout);
        grid.setBackground(BOARD_COLOUR);
        grid.setPreferredSize(new Dimension(500, 500));

        gridButtons = new ArrayList<>();
        for (int i = 0; i < (board.getBoardSize() * board.getBoardSize()); i++) {
            JButton button = new JButton();
            button.setActionCommand(String.valueOf(i));
            button.addActionListener(this);
            gridButtons.add(button);
            grid.add(button);
        }

        gridLayout.layoutContainer(grid);
        frame.add(grid, BorderLayout.NORTH);
        frame.pack();
    }

    // MODIFIES: this
    // EFFECTS: scales icons to proper size for board
    private void scaleIcons() {
        JButton b = gridButtons.get(0);
        scaledBlackIcon = new ImageIcon(blackImage.getScaledInstance(b.getWidth(), b.getHeight(), 8));
        scaledWhiteIcon = new ImageIcon(whiteImage.getScaledInstance(b.getWidth(), b.getHeight(), 8));
    }

    // EFFECTS: handles action events from GUI buttons
    @Override
    public void actionPerformed(ActionEvent e) {
        if (Pattern.matches("\\d|(\\d\\d)|([1-3]\\d\\d)", e.getActionCommand())) {
            buttonPressed(Integer.parseInt(e.getActionCommand()));
        } else if (e.getActionCommand().equals("Undo")) {
            undo();
        } else if (e.getActionCommand().equals("Clear")) {
            clear();
        } else if (e.getActionCommand().equals("Change Size")) {
            changeSize();
        } else if (e.getActionCommand().equals("Save")) {
            save();
        } else if (e.getActionCommand().equals("Load")) {
            load();
        } else if (e.getActionCommand().equals("Help")) {
            help();
        }
    }


    // MODIFIES: this
    // EFFECTS: updates the button appearance at given index in buttons list
    //          and adds a Stone to this Board
    private void buttonPressed(int index) {
        if (board.getStones().get(index) == null) {
            JButton button = gridButtons.get(index);
            int x = index - (index / board.getBoardSize()) * board.getBoardSize();
            int y = (index - x) / board.getBoardSize();
            if (board.getCurrentTurn() == Colour.BLACK) {
                button.setIcon(scaledBlackIcon);
            } else {
                button.setIcon(scaledWhiteIcon);
            }
            board.addStone(new Stone(board.getCurrentTurn(), x, y));
            updateTitle();
        }
    }

    // MODIFIES: this
    // EFFECTS: undoes the last move and updates title
    private void undo() {
        if (board.getMoveList().size() != 0) {
            Stone s = board.getMoveList().get(board.getMoveList().size() - 1);
            int index = s.getY() * board.getBoardSize() + s.getX();
            gridButtons.get(index).setIcon(null);
            board.undoMove();
            updateTitle();
        } else {
            JOptionPane.showMessageDialog(frame, "No moves to undo", "Undo", 0);
        }
    }

    // MODIFIES: this
    // EFFECTS: clears the button labels, instantiates a board, and updates title
    private void clear() {
        createGrid();
        board = new Board(board.getBoardSize());
        updateTitle();
    }

    // MODIFIES: this
    // EFFECTS: instantiates a new board with the size from dialog and updates title
    private void changeSize() {
        String input = (String) JOptionPane.showInputDialog(frame,
                "Choose a size (default: 15, min: 5, max: 20)", "Change Size", JOptionPane.PLAIN_MESSAGE,
                null, null, null);
        if (input != null && Pattern.matches("((0*[5-9])|(0*1\\d)|(0*20))", input)) {
            try {
                board = new Board(Integer.parseInt(input));
                createGrid();
                scaleIcons();
                updateTitle();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid input", "Change Size", 0);
            }
        }
    }

    // EFFECTS: saves this board to a JSON file, displays dialog with status
    private void save() {
        try {
            writer.open();
            writer.write(board);
            writer.close();
            JOptionPane.showMessageDialog(frame, "Board state saved successfully", "Save", 1);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(frame, "Unable to save board state", "Save", 1);
        }
    }

    // MODIFIES: this
    // EFFECTS: reads a board from a JSON file, displays dialog with status
    private void load() {
        try {
            this.board = reader.read();
            createGrid();
            scaleIcons();
            for (Stone s : board.getMoveList()) {
                JButton button = gridButtons.get(s.getY() * board.getBoardSize() + s.getX());
                if (s.getColour() == Colour.BLACK) {
                    button.setIcon(scaledBlackIcon);
                } else {
                    button.setIcon(scaledWhiteIcon);
                }
            }
            updateTitle();
            JOptionPane.showMessageDialog(frame, "Board state loaded successfully", "Load", 1);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Unable to load board state", "Load", 1);
        }
    }

    // EFFECTS: displays a help dialog
    private void help() {
        StringBuilder str = new StringBuilder();
        str.append("Start the game by playing a black stone on the grid. Alternate turns between players, and try ");
        str.append("to match 5 of your pieces in a row either horizontally, vertically, or diagonally to win.");
        JOptionPane.showMessageDialog(frame, str, "Help", JOptionPane.INFORMATION_MESSAGE);
    }

    // MODIFIES: this
    // EFFECTS: updates the title of the program
    private void updateTitle() {
        if (board.getCurrentTurn() == Colour.BLACK) {
            frame.setTitle("Gomova - black's turn");
        } else {
            frame.setTitle("Gomova - white's turn");
        }
    }

    // EFFECTS: prints event log to console on window close
    @Override
    public void windowClosing(WindowEvent e) {
        for (Event event : EventLog.getInstance()) {
            System.out.println(event.toString());
        }
    }

    // EFFECTS: do nothing on window open
    @Override
    public void windowOpened(WindowEvent e) {
        // do nothing
    }

    // EFFECTS: do nothing when window closed
    @Override
    public void windowClosed(WindowEvent e) {
        // do nothing
    }

    // EFFECTS: do nothing on window iconify
    @Override
    public void windowIconified(WindowEvent e) {
        // do nothing
    }

    // EFFECTS: do nothing on window deiconify
    @Override
    public void windowDeiconified(WindowEvent e) {
        // do nothing
    }

    // EFFECTS: do nothing on window activation
    @Override
    public void windowActivated(WindowEvent e) {
        // do nothing
    }

    // EFFECTS: do nothing on window deactivation
    @Override
    public void windowDeactivated(WindowEvent e) {
        // do nothing
    }
}
