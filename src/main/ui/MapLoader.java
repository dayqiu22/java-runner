package ui;

import model.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MapLoader {

    public MapLoader() {}

    public void loadMap(Game game) {
        try {
            InputStream is = getClass().getResourceAsStream("/maps/testmap.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            for (int row = 0; row < GameGUI.MAX_ROW; row++) {
                String line = br.readLine();
                String[] labels = line.split(",");
                for (int col = 0; col < labels.length; col++) {
                    switch (labels[col]) {
                        case "1":
                            game.addBlock(new Block(col * GameGUI.GRID_UNIT, row * GameGUI.GRID_UNIT));
                            break;
                        case "2":
                            game.addBlock(new Hazard(col * GameGUI.GRID_UNIT, row * GameGUI.GRID_UNIT));
                            break;
                        case "3":
                            game.addBlock(new PowerUp(col * GameGUI.GRID_UNIT,
                                    row * GameGUI.GRID_UNIT,
                                    Game.SPEED));
                            break;
                        case "4":
                            game.addBlock(new PowerUp(col * GameGUI.GRID_UNIT,
                                    row * GameGUI.GRID_UNIT,
                                    Game.INVULNERABLE));
                            break;
                        case "5":
                            game.addBlock(new FinishLine(col * GameGUI.GRID_UNIT, row * GameGUI.GRID_UNIT));
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
