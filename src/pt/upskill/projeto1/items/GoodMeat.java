package pt.upskill.projeto1.items;

import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.GameObject;
import pt.upskill.projeto1.rogue.utils.Position;

public class GoodMeat extends GameObject implements ImageTile {

    private final int pointsGiven = 10;

    public GoodMeat(){

    }

    public int getPointsGiven() {
        return pointsGiven;
    }

    private Position position;

    public GoodMeat(Position position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return "GoodMeat";
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
