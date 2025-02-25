package pt.upskill.projeto1.scene;

import pt.upskill.projeto1.game.Engine;
import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.items.Key;
import pt.upskill.projeto1.objects.GameObject;
import pt.upskill.projeto1.rogue.utils.Position;
import pt.upskill.projeto1.objects.Hero;

import static pt.upskill.projeto1.game.Engine.hero;
import static pt.upskill.projeto1.game.Engine.tiles;

public class DoorClosed extends GameObject implements ImageTile {

    private Position position;

    public DoorClosed(Position position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return "DoorClosed";
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public boolean isTransposable() {
        if (hero.hasKey()) {
            System.out.println("You used a door key! Next room unlocked");
            hero.manageInventory(Key.class, false);
            return true;
        }
        return false;
    }
}
