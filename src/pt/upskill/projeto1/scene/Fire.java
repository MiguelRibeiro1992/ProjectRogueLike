package pt.upskill.projeto1.scene;

import pt.upskill.projeto1.enemies.Enemy;
import pt.upskill.projeto1.game.Engine;
import pt.upskill.projeto1.gui.FireTile;
import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.GameObject;
import pt.upskill.projeto1.rogue.utils.Position;

import java.util.List;

public class Fire extends GameObject implements ImageTile {

    private Position position;

    public Fire(Position position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return "Fire";
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
