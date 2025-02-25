package pt.upskill.projeto1.scene;

import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.GameObject;
import pt.upskill.projeto1.rogue.utils.Position;

public class StairsUp extends GameObject implements ImageTile {

    private Position position;

    public StairsUp(Position position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return "StairsUp";
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public boolean isTransposable() {
        return true;
    }
}
