package pt.upskill.projeto1.items;

import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.GameObject;
import pt.upskill.projeto1.rogue.utils.Position;

public class Sword extends GameObject implements ImageTile {

    private final int pointsGiven = 15;

    public Sword(){

    }

    public int getPointsGiven() {
        return pointsGiven;
    }


    private Position position;

    public Sword(Position position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return "Sword";
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
