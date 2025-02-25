package pt.upskill.projeto1.objects;

import pt.upskill.projeto1.enemies.Badguy;
import pt.upskill.projeto1.enemies.Bat;
import pt.upskill.projeto1.enemies.Enemy;
import pt.upskill.projeto1.enemies.Skeleton;
import pt.upskill.projeto1.enemies.Thief;
import pt.upskill.projeto1.game.Engine;
import pt.upskill.projeto1.game.FireBallThread;
import pt.upskill.projeto1.gui.FireTile;
import pt.upskill.projeto1.gui.ImageMatrixGUI;
import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.items.GoodMeat;
import pt.upskill.projeto1.items.Hammer;
import pt.upskill.projeto1.items.Key;
import pt.upskill.projeto1.items.Sword;
import pt.upskill.projeto1.rogue.utils.Direction;
import pt.upskill.projeto1.rogue.utils.Position;
import pt.upskill.projeto1.scene.Fire;

import java.util.ArrayList;
import java.util.List;

import static pt.upskill.projeto1.game.Engine.*;

public class Hero extends GameObject implements ImageTile {

    private Position position;
    private final int totalHealth = 100;
    private int actualHealth;
    private int attackDamage;
    private final List<GameObject> inventory;
    private List <FireBall> fireBallInventory;
    private static final int maxInventorySize = 3;
    private int points;



    public Hero(Position position) {
        this.position = position;
        this.actualHealth = totalHealth;
        this.attackDamage = 15;
        this.inventory = new ArrayList<>();
        this.fireBallInventory = new ArrayList<>();
        for(int i=0; i<3; i++){
            fireBallInventory.add(new FireBall(position));
        }
        this.points = points;
    }

    @Override
    public String getName() {
        return "Hero";
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void heroMoveUp() {
        setPosition(getPosition().plus(Direction.UP.asVector()));
    }

    public void heroMoveDown() {
        setPosition(getPosition().plus(Direction.DOWN.asVector()));
    }

    public void heroMoveRight() {
        setPosition(getPosition().plus(Direction.RIGHT.asVector()));
    }

    public void heroMoveLeft() {
        setPosition(getPosition().plus(Direction.LEFT.asVector()));
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public boolean isTransposable() {
        return false;
    }

    public int getTotalHealth() {
        return totalHealth;
    }

    public void setHealth(int health) {
        this.actualHealth = Math.max(health, 0);
        Engine.refreshStatusImages();
    }

    public int getActualHealth() {
        return actualHealth;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void takePoints(int pointsTaken){
        this.points = Math.max(getPoints() - pointsTaken,0);
    }

    public void pointsGained (int pointsGained){
        this.points = getPoints() + pointsGained;
    }

    public List<GameObject> getInventory() {
        return inventory;
    }


    private boolean hammerPickedUp = true;
    private boolean swordPickedUp = true;


    public boolean manageInventory(Class<?> itemType, boolean add) {
        if (add) {
            // Add item logic here if needed
            return false;
        } else {
            // Check if the inventory contains the item type
            boolean itemExists = inventory.stream().anyMatch(item -> item.getClass().equals(itemType));
            if (!itemExists) {
                System.out.println("Inventory does not contain " + itemType.getSimpleName() + " to drop.");
                return false;
            }

            // Remove one instance of the item from the inventory
            inventory.removeIf(item -> item.getClass().equals(itemType));
            System.out.println(itemType.getSimpleName() + " removed from inventory.");

            // Adjust attack damage and flags based on item type
            if (itemType.equals(Hammer.class)) {
                hero.attackDamage -= 20;
                hammerPickedUp = false;
                System.out.println("Hero's attack damage decreased by 20.");
            } else if (itemType.equals(Sword.class)) {
                hero.attackDamage -= 15;
                swordPickedUp = false;
                System.out.println("Hero's attack damage decreased by 15.");
            }

            // Drop the item on the current tile only if it’s safe to do so
            boolean canPlaceItem = tiles.stream()
                    .noneMatch(obj -> obj.getClass().equals(itemType) && obj.getPosition().equals(hero.getPosition()));

            if (canPlaceItem) {

                try {
                    GameObject newItem = (GameObject) itemType.getConstructor(Position.class).newInstance(hero.getPosition());
                    tiles.add(newItem);
                    gui.newImages(tiles);
                } catch (Exception e) {
                    System.out.println("Error placing item on tile: " + e.getMessage());
                }
            }

            // Refresh status images to update the inventory UI
            Engine.refreshStatusImages();
            return true;
        }
    }

    public boolean picksItem(Position newHeroPosition, List<GameObject> room, Hero hero) {
        for (GameObject obj : room) {
            if ((obj instanceof Hammer || obj instanceof Sword || obj instanceof GoodMeat || obj instanceof Key)
                && (obj.getPosition().equals(newHeroPosition))) {
                if (inventory.size() < 3) {
                    if (obj instanceof GoodMeat) {
                        hero.actualHealth += 30;
                        Engine.playSE(2);
                        hero.pointsGained(((GoodMeat) obj).getPointsGiven());
                        int meatPoints = ((GoodMeat) obj).getPointsGiven();
                        System.out.println("Hero picked up a juicy meat...yummy" + "\n" + "Points gained: " + meatPoints);
                        System.out.println("Current points: " + hero.getPoints());
                    } else {
                        getInventory().add(obj);
                        if (obj instanceof Hammer) {
                            hero.attackDamage += 20;
                            Engine.playSE(4);
                            hero.pointsGained(((Hammer) obj).getPointsGiven());
                            int hammerPoints = ((Hammer) obj).getPointsGiven();
                            System.out.println("Hero picked up a Hammer" + "\n" + "Points gained: " + hammerPoints);
                            System.out.println("Current points: " + hero.getPoints());
                        } else if (obj instanceof Sword) {
                            hero.attackDamage += 15;
                            Engine.playSE(4);
                            hero.pointsGained(((Sword) obj).getPointsGiven());
                            int swordPoints = ((Sword) obj).getPointsGiven();
                            System.out.println("Hero picked up a Sword"  + "\n" + "Points gained: " + swordPoints);
                            System.out.println("Current points: " + hero.getPoints());
                        } else {
                            Engine.playSE(4);
                            hero.pointsGained(((Key) obj).getPointsGiven());
                            int keyPoints = ((Key) obj).getPointsGiven();
                            System.out.println("Hero picked up a Key" + "\n" + "Points gained: " + keyPoints);
                            System.out.println("Current points: " + hero.getPoints());
                        }
                    }
                    System.out.println("Hero added object: " + obj.getClass().getSimpleName() + " to inventory");
                    room.remove(obj);
                    Engine.refreshStatusImages();
                    return true;
                } else {
                    System.out.println("Inventory full! Cannot pick up" + obj.getClass().getSimpleName());
                }
            }
        }
        return false;
    }

    public boolean hasKey() {
        List<GameObject> inventory = getInventory();
        for (GameObject obj : inventory) {
            if (obj instanceof Key)
                return true;
        }
        return false;
    }

    public boolean hasHammer() {
        List<GameObject> inventory = getInventory();
        for (GameObject obj : inventory) {
            if (obj instanceof Hammer)
                return true;
        }
        return false;
    }

    public boolean hasFire() {
        List<GameObject> inventory = getInventory();
        for (GameObject obj : inventory) {
            if (obj instanceof Fire)
                return true;
        }
        return false;
    }

    public boolean hasSword() {
        List<GameObject> inventory = getInventory();
        for (GameObject obj : inventory) {
            if (obj instanceof Hammer)
                return true;
        }
        return false;
    }

    private int hammerCount = 0;
    private int swordCount = 0;
    private int fireBallCount = 3;
    private int keyCount = 0;

    public int getFireBallSlots() {
        return fireBallInventory.size();
    }

    private boolean heroThrowsFireball;

    public int getFireBallCounts() {
        if (heroThrowsFireball){
            hero.throwFireBall();
            fireBallCount--;
        }
        return fireBallCount;
    }

    public int getSwordCount(List<GameObject> inventory) {
        int swordCount = 0;
        for (GameObject item : inventory) {
            if (item instanceof Sword) {
                swordCount++;
            }
        }
        return swordCount;
    }

    public void collectSword() {
        swordCount++;
    }

    public int getKeyCount(List<GameObject> inventory) {
        int keyCount = 0;
        for (GameObject item : inventory) {
            if (item instanceof Key) {
                keyCount++;
            }
        }
        return keyCount;
    }

    public void collectKey() {
        keyCount++;
    }

    public int getHammerCount(List<GameObject> inventory) {
        int hammerCount = 0;
        for (GameObject item : inventory) {
            if (item instanceof Hammer) {
                hammerCount++;
            }
        }
        return hammerCount;
    }

    public void collectHammer() {
        hammerCount++;
    }

    public boolean dealsDamage(Position newHeroPosition, List<GameObject> tile, Hero hero) {

        boolean canDealDamage = false;

        List<GameObject> enemiesToRemove = new ArrayList<>();

        for (GameObject obj : tile) {

            Position enemyPosition = obj.getPosition();

            if (newHeroPosition.equals(obj.getPosition()) &&
                    (obj instanceof Skeleton || obj instanceof Thief || obj instanceof Bat || obj instanceof Badguy)) {

                System.out.println("Hero's attack damage: " + hero.getAttackDamage());

                if (obj instanceof Skeleton) {
                    ((Skeleton) obj).decrementHealth(hero.getAttackDamage());
                } else if (obj instanceof Thief) {
                    ((Thief) obj).decrementHealth(hero.getAttackDamage());
                } else if (obj instanceof Bat) {
                    ((Bat) obj).decrementHealth(hero.getAttackDamage());
                } else {
                    ((Badguy) obj).decrementHealth(hero.getAttackDamage());
                }

                Engine.playSE(7);

                if (((Enemy) obj).getActualHealth() > 0) {
                    canDealDamage = true;
                    System.out.println("Player hit " + obj.getClass().getSimpleName() + " for " + hero.getAttackDamage() + "!");
                    System.out.println("Enemy HP: " + ((Enemy) obj).getActualHealth());
                } else {
                    if (obj instanceof Skeleton) {
                        int skeletonPoints = ((Skeleton) obj).getPointsGiven();
                        hero.pointsGained(skeletonPoints);
                        System.out.println("Player has defeated a " + obj.getClass().getSimpleName());
                        System.out.println("Points gained: " + skeletonPoints);
                        System.out.println("Current points: " + hero.getPoints());
                    } else if (obj instanceof Thief) {
                        int thiefPoints = ((Thief) obj).getPointsGiven();
                        hero.pointsGained(thiefPoints);
                        System.out.println("Player has defeated a " + obj.getClass().getSimpleName());
                        System.out.println("Points gained: " + thiefPoints);
                        System.out.println("Current points: " + hero.getPoints());
                    } else if (obj instanceof Bat) {
                        int batPoints = ((Bat) obj).getPointsGiven();
                        hero.pointsGained(batPoints);
                        System.out.println("Player has defeated a " + obj.getClass().getSimpleName());
                        System.out.println("Points gained: " + batPoints);
                        System.out.println("Current points: " + hero.getPoints());
                    } else {
                        int badGuyPoints = ((Badguy) obj).getPointsGiven();
                        hero.pointsGained(badGuyPoints);
                        System.out.println("Player has defeated a " + obj.getClass().getSimpleName());
                        System.out.println("Points gained: " + badGuyPoints);
                        System.out.println("Current points: " + hero.getPoints());
                    }
                    enemiesToRemove.add(obj);
                }
                break;
            }
        }

        tiles.removeAll(enemiesToRemove);

        if (!enemiesToRemove.isEmpty()) {
            gui.newImages(tiles);
        }

        return canDealDamage;
    }


    public boolean throwFireBall() {

        if (fireBallInventory.isEmpty()) {
            System.out.println("No fireballs left!");
            return false;
        }

        FireBall fireBall = fireBallInventory.remove(0);

        FireBallThread fireBallThread = null;

        List<GameObject> tiles = Engine.tiles;

        for (GameObject obj : tiles) {
            if (obj instanceof Enemy) {
                Position enemyPosition = obj.getPosition();
                int newHeroPositionX = Engine.hero.getPosition().getX();
                int newHeroPositionY = Engine.hero.getPosition().getY();
                int newEnemyPositionX = enemyPosition.getX();
                int newEnemyPositionY = enemyPosition.getY();

                fireBall.setPosition(hero.getPosition());

                // Fireball direction logic
                if (newHeroPositionX == newEnemyPositionX && newHeroPositionY > newEnemyPositionY) {
                    fireBallThread = new FireBallThread(Direction.UP, fireBall);
                } else if (newHeroPositionX == newEnemyPositionX && newHeroPositionY < newEnemyPositionY) {
                    fireBallThread = new FireBallThread(Direction.DOWN, fireBall);
                } else if (newHeroPositionX > newEnemyPositionX && newHeroPositionY == newEnemyPositionY) {
                    fireBallThread = new FireBallThread(Direction.LEFT, fireBall);
                } else if (newHeroPositionX < newEnemyPositionX && newHeroPositionY == newEnemyPositionY) {
                    fireBallThread = new FireBallThread(Direction.RIGHT, fireBall);
                }

                if (fireBallThread != null) {
                    System.out.println("You've thrown a fire ball! No points received.");
                    ImageMatrixGUI.getInstance().addImage(fireBall);
                    fireBallThread.run();
                    fireBallCount--;
                    // Decrease fireball count after firing
                    refreshStatusImages();
                    break;
                }
            }
        }

        if (fireBallThread == null) {
            return false;
        }
        return true;
    }

}




