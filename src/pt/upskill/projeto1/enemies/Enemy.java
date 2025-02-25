package pt.upskill.projeto1.enemies;

import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.GameObject;
import pt.upskill.projeto1.objects.Hero;
import pt.upskill.projeto1.rogue.utils.Vector2D;

import java.util.List;

public abstract class Enemy extends GameObject implements ImageTile {

    public abstract boolean isTransposable();

    public abstract double getHealth();

    public abstract double getActualHealth();

    public abstract void setActualHealth(int health);

    public abstract void dealDamageToHero(Hero hero, List<GameObject> tiles);

    public abstract void decrementHealth(int damage);

}
