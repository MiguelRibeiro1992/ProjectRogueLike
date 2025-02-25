package pt.upskill.projeto1.objects;

import pt.upskill.projeto1.enemies.Bat;
import pt.upskill.projeto1.enemies.Enemy;
import pt.upskill.projeto1.game.Engine;
import pt.upskill.projeto1.gui.FireTile;
import pt.upskill.projeto1.rogue.utils.Position;

import java.util.List;

public class FireBall extends GameObject implements FireTile {

    private Position position;

    public FireBall(Position position) {
        this.position = position;
    }

    @Override
    public boolean isTransposable() {
        return true;
    }

    @Override
    public boolean validateImpact() {

        List <GameObject> tiles = Engine.tiles;


        for (GameObject obj: tiles){

            Position enemyPosition = obj.getPosition();
            Position heroPosition = Engine.hero.getPosition();

            int enemyPositionX = enemyPosition.getX();
            int enemyPositionY = enemyPosition.getY();
            int heroPositionX = heroPosition.getX();
            int heroPositionY = heroPosition.getY();

            if (obj instanceof Enemy && (enemyPositionX == heroPositionX || enemyPositionY == heroPositionY)){
                ((Enemy) obj).decrementHealth(100);
                Engine.playSE(8);
                tiles.remove(obj);
                return true;
            }
        }
        return false;
    }

    @Override
    public void setPosition(Position position) {
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

}


