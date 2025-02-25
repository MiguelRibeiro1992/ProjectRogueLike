package pt.upskill.projeto1.enemies;

import pt.upskill.projeto1.game.Engine;
import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.GameObject;
import pt.upskill.projeto1.objects.Hero;
import pt.upskill.projeto1.rogue.utils.Direction;
import pt.upskill.projeto1.rogue.utils.Position;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import static pt.upskill.projeto1.game.Engine.hero;
import static pt.upskill.projeto1.game.Engine.playSE;

public class Bat extends Enemy {

    private Position position;
    private int totalHealth = 100;
    private int actualHealth;
    private final int attackDamage = 2;
    private boolean pauseChase = false;
    private int pointsGiven;

    public Bat(Position position) {
        this.position = position;
        this.actualHealth = totalHealth;
        this.pointsGiven = 20;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return "Bat";
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public boolean isTransposable() {
        return false;
    }

    @Override
    public double getHealth() {
        return totalHealth;
    }

    @Override
    public double getActualHealth() {
        return actualHealth;
    }

    @Override
    public void setActualHealth(int health) {
        this.actualHealth = health;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void decrementHealth(int damage) {
        this.actualHealth = Math.max(this.actualHealth - damage, 0);
    }

    public int getPointsGiven() {
        return pointsGiven;
    }

    public void setPointsGiven(int pointsGiven) {
        this.pointsGiven = pointsGiven;
    }

    public void dealDamageToHero(Hero hero, List<GameObject> tiles) {
        Position newPosition = moves(new ArrayList<>(tiles), hero.getPosition());

        int xDifference = Math.abs(newPosition.getX() - hero.getPosition().getX());
        int yDifference = Math.abs(newPosition.getY() - hero.getPosition().getY());

        if ((xDifference <= 1 && yDifference == 0) || (yDifference <= 1 && xDifference == 0)) {
            hero.setHealth(hero.getActualHealth() - this.attackDamage);
            playSE(10);
            System.out.println("Bat hit the Hero for " + this.attackDamage + " damage!");
            System.out.println("Hero's remaining health: " + hero.getActualHealth());
        }
    }


    boolean moveRight = true;

    public Position moves(List<GameObject> tiles, Position heroPosition) {
        Position currentPosition = getPosition();
        Position newPosition = currentPosition;


        int distanceToHero = Math.abs(heroPosition.getX() - currentPosition.getX()) +
                Math.abs(heroPosition.getY() - currentPosition.getY());

        if (pauseChase) {
            pauseChase = false;
            return currentPosition;
        }

        if (distanceToHero == 2) {
            pauseChase = true;
            return currentPosition;
        }


        if (distanceToHero <= 3) {
            if (heroPosition.getX() > currentPosition.getX()) {
                newPosition = currentPosition.plus(Direction.RIGHT.asVector());
            } else if (heroPosition.getX() < currentPosition.getX()) {
                newPosition = currentPosition.plus(Direction.LEFT.asVector());
            } else if (heroPosition.getY() > currentPosition.getY()) {
                newPosition = currentPosition.plus(Direction.DOWN.asVector());
            } else if (heroPosition.getY() < currentPosition.getY()) {
                newPosition = currentPosition.plus(Direction.UP.asVector());
            }
        } else {

            if (moveRight) {
                newPosition = currentPosition.plus(Direction.RIGHT.asVector());
            } else {
                newPosition = currentPosition.plus(Direction.LEFT.asVector());
            }
        }


        boolean canMove = true;
        for (GameObject tile : new ArrayList<>(tiles)) {
            if (tile.getPosition().equals(newPosition) && !tile.isTransposable()) {
                moveRight = !moveRight;
                canMove = false;
                break;
            }
        }


        if (canMove) {
            setPosition(newPosition);
        }

        return getPosition();
    }
}
