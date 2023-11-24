package ui;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EndScreenGUI extends JPanel implements ActionListener {
    private static final String FONT_NAME = "Consolas";
    private static final Font REGULAR_TEXT = new Font(FONT_NAME, Font.PLAIN, 14);
    private static final int WIDTH_PX = 1000;
    private static final int HEIGHT_PX = 700;
    private final int centerX;
    private final MainWindow display;

    public EndScreenGUI(MainWindow display) {
        this.display = display;
        this.centerX = WIDTH_PX / 2;
        this.setFocusable(true);
        drawEndScreen();
    }

    // MODIFIES: this
    // EFFECTS: draws the main menu with options to start a new game or load a saved game
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
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("menu")) {
            this.display.getCardLayout().show(this.display.getMainPanel(), "menu");
        }
    }
}
