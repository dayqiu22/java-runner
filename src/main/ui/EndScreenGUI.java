package ui;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Represents the component of the GUI that will display end screen components
public class EndScreenGUI extends JPanel implements ActionListener {
    private static final String FONT_NAME = "Consolas";
    private static final Font REGULAR_TEXT = new Font(FONT_NAME, Font.PLAIN, 14);
    private static final int WIDTH_PX = 1000;
    private static final int HEIGHT_PX = 700;
    private final int centerX;
    private final MainWindow display;
    private final JLabel ggTime;

    // EFFECTS: constructs a panel to represent the GUI for the end screen,
    // initializes a label that can be changed to display game end time and
    // displays end screen text and buttons
    public EndScreenGUI(MainWindow display) {
        this.display = display;
        this.centerX = WIDTH_PX / 2;
        this.ggTime = new JLabel();
        this.setFocusable(false);
        drawEndScreen();
    }

    // MODIFIES: this
    // EFFECTS: add components to the end screen with a button to return to the main menu
    private void drawEndScreen() {
        this.setLayout(null);
        this.setPreferredSize(new Dimension(WIDTH_PX, HEIGHT_PX));
        JLabel gg = new JLabel("G A M E   O V E R");
        gg.setBounds(centerX - 150, 100, 500, 200);
        gg.setFont(new Font(FONT_NAME, Font.BOLD, 30));
        this.add(gg);

        JButton reButton = new JButton("Back to Menu");
        reButton.setActionCommand("menu");
        reButton.setBounds(centerX - 150, 350, 300, 40);
        reButton.setFont(REGULAR_TEXT);
        reButton.addActionListener(this);
        this.add(reButton);

        ggTime.setBounds(centerX - 160, 175, 500, 200);
        ggTime.setFont(new Font(FONT_NAME, Font.PLAIN, 24));
        this.add(ggTime);
    }

    public JLabel getGgTime() {
        return ggTime;
    }

    // EFFECTS: maps end screen button to display the menu panel when clicked
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("menu")) {
            this.display.getCardLayout().show(this.display.getMainPanel(), "menu");
        }
    }
}
