package pt.upskill.projeto1.items;

import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.GameObject;
import pt.upskill.projeto1.rogue.utils.Position;

public class Key extends GameObject implements ImageTile {

    private final int pointsGiven = 20;

    public Key(){

    }

    public int getPointsGiven() {
        return pointsGiven;
    }

    private Position position;

    public Key(Position position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return "Key";
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
