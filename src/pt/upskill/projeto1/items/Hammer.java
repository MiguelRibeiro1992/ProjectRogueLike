package pt.upskill.projeto1.items;

import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.GameObject;
import pt.upskill.projeto1.rogue.utils.Position;

public class Hammer extends GameObject implements ImageTile {

    private final int pointsGiven = 15;

    public Hammer(){

    }

    public int getPointsGiven() {
        return pointsGiven;
    }

    private Position position;

    public Hammer(Position position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return "Hammer";
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
