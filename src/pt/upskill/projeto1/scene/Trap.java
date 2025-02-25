package pt.upskill.projeto1.scene;

import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.GameObject;
import pt.upskill.projeto1.objects.Hero;
import pt.upskill.projeto1.rogue.utils.Position;

import java.util.List;

import static pt.upskill.projeto1.game.Engine.playSE;

public class Trap extends GameObject implements ImageTile {

    private Position position;

    public Trap(Position position) {
        this.position = position;
    }

    public static void dealsDamageToHero (Hero hero, List<GameObject> tiles, Position newHeroPosition){
        for(GameObject obj:tiles){
            if(obj instanceof Trap && obj.getPosition().equals(newHeroPosition)){
                hero.setHealth(hero.getActualHealth()-5);
                playSE(9);
                hero.takePoints(2);
                System.out.println("Hero's remaining health: " + hero.getActualHealth());
                System.out.println("Hero has stepped on a trap!" + "\n" + "Points taken: 2");
                System.out.println("Current Points " + hero.getPoints());
            }
        }
    }

    @Override
    public String getName() {
        return "Trap";
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
