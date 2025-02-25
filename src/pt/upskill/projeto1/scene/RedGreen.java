package pt.upskill.projeto1.scene;

import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.rogue.utils.Position;

public class RedGreen implements ImageTile {

    private Position position;

    public RedGreen(Position position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return "RedGreen";
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
