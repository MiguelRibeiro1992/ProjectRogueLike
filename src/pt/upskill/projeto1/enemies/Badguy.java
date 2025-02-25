package pt.upskill.projeto1.enemies;

import pt.upskill.projeto1.objects.GameObject;
import pt.upskill.projeto1.objects.Hero;
import pt.upskill.projeto1.rogue.utils.Position;
import pt.upskill.projeto1.rogue.utils.Vector2D;
import pt.upskill.projeto1.scene.DoorOpen;

import java.util.List;
import java.util.Random;

import static pt.upskill.projeto1.game.Engine.*;

public class Badguy extends Enemy {

    private Position position;
    private final int totalHealth = 100;
    private int actualHealth;
    private final int attackDamage = 4;
    private boolean pauseChase;
    private int pointsGiven;


    public Badguy(Position position) {
        this.position = position;
        this.actualHealth = totalHealth;
        this.pointsGiven = 50;
    }

    public void setPosition(Position position) {
        this.position = position;
    }


    @Override
    public String getName() {
        return "BadGuy";
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
        Vector2D moveVector = moves(tiles, hero.getPosition());


        Position currentPosition = getPosition();
        Position newPosition = currentPosition.plus(moveVector);

        int xDifference = Math.abs(newPosition.getX() - hero.getPosition().getX());
        int yDifference = Math.abs(newPosition.getY() - hero.getPosition().getY());


        if ((xDifference <= 1 && yDifference == 0) || (yDifference <= 1 && xDifference == 0)) {
            hero.setHealth(hero.getActualHealth() - this.attackDamage);
            playSE(10);
            System.out.println("Badguy hit the Hero for " + this.attackDamage + " damage!");
            System.out.println("Hero's remaining health: " + hero.getActualHealth());
        }
    }

    public Vector2D randomVectors(){

        Vector2D [] vectors = new Vector2D[8];

        vectors [0]  = new Vector2D(0,-1);
        vectors [1]  = new Vector2D(0,1);
        vectors [2]  = new Vector2D(-1,0);
        vectors [3]  = new Vector2D(1,0);
        vectors [4]  = new Vector2D(1,-1);
        vectors [5]  = new Vector2D(-1,-1);
        vectors [6]  = new Vector2D(1,1);
        vectors [7]  = new Vector2D(-1,-1);

        Random random = new Random();

        int randomIndex = random.nextInt(vectors.length);
        return vectors[randomIndex];
    }


    public Vector2D moves(List<GameObject> tiles, Position heroPosition) {

        Position currentPosition = getPosition();
        Vector2D moveVector;


        int distanceToHero = Math.abs(heroPosition.getX() - currentPosition.getX()) +
                Math.abs(heroPosition.getY() - currentPosition.getY());

        if (pauseChase) {
            pauseChase = false;
            return new Vector2D(0, 0);
        }

        if (distanceToHero == 2) {
            pauseChase = true;
            return new Vector2D(0, 0);
        }


        if (distanceToHero <= 3) {

            if (heroPosition.getX() > currentPosition.getX()) {
                moveVector = new Vector2D(1, 0);
            } else if (heroPosition.getX() < currentPosition.getX()) {
                moveVector = new Vector2D(-1, 0);
            } else if (heroPosition.getY() > currentPosition.getY()) {
                moveVector = new Vector2D(0, 1);
            } else {
                moveVector = new Vector2D(0, -1);
            }
        } else {

            moveVector = randomVectors();
        }


        Position nextPosition = currentPosition.plus(moveVector);


        boolean canMove = true;
        for (GameObject tile : tiles) {
            if (tile.getPosition().equals(nextPosition) && (!tile.isTransposable() || tile instanceof DoorOpen)) {
                canMove = false;
                break;
            }
        }


        if (canMove) {
            setPosition(nextPosition);

            return moveVector;
        } else {

            return new Vector2D(0, 0);
        }
    }
}

