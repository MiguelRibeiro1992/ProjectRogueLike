package pt.upskill.projeto1.game;

import pt.upskill.projeto1.enemies.*;
import pt.upskill.projeto1.gui.FireTile;
import pt.upskill.projeto1.gui.ImageMatrixGUI;
import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.items.Hammer;
import pt.upskill.projeto1.items.Key;
import pt.upskill.projeto1.items.Sword;
import pt.upskill.projeto1.objects.FireBall;
import pt.upskill.projeto1.objects.GameObject;
import pt.upskill.projeto1.objects.Hero;
import pt.upskill.projeto1.objects.Room;
import pt.upskill.projeto1.rogue.utils.Direction;
import pt.upskill.projeto1.rogue.utils.Position;
import pt.upskill.projeto1.scene.*;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Engine {

    //GAME STATE

    public boolean gameIsOver = true;
    private boolean isMusicPlaying = false;

    //GAME SOUND

    public void playMusic (int i){
        sound.setFile(i);
        sound.play();
        sound.loop();
    }

    public void stopMusic() {
        sound.stop();
        isMusicPlaying = false;
    }

    public static void playSE(int i){
        sound.setFile(i);
        sound.play();
    }

    public void playGameOver(int i){
        sound.setFile(i);
        sound.play();
    }

    static Sound sound = new Sound();

    public void startGame() {
        if (!isMusicPlaying) {
            playMusic(0);
            isMusicPlaying = true;
        }
        gameIsOver = false;
    }

    //CREATING HERO:

    public static Hero hero = new Hero(new Position(5, 5));


    //CREATE LIST OF TILES VARIABLES AND LIST OF STATUS IMAGES

    public static List<List<GameObject>> listOfTiles = new Room().totalScanner();
    public static List<GameObject> tiles;

    static List <ImageTile> statusImage = statusImages();

    //IMAGE MATRIX
    public static ImageMatrixGUI gui = ImageMatrixGUI.getInstance();

    //ADD HEROES TO ALL TILES AUTOMATICALLY

    public void addHeroToAllTiles() {
        for (List<GameObject> tiles : listOfTiles) {
            if (!tiles.contains(hero)) {
                tiles.add(hero);
            }
        }
    }

    //INIT METHOD

    public void init() {

        startGame();

        if(hero.getActualHealth() > 0) {
            addHeroToAllTiles();
        } else {
            tiles.remove(hero);
        }

        tiles = listOfTiles.get(0);

        gui.setEngine(this);
        gui.newImages(tiles);
        gui.go();
        gui.newStatusImages(statusImage);


        while (!gameIsOver && hero.getActualHealth() > 0) {
            gui.update();


            if (hero.getActualHealth() <= 0) {
                gameOver();
            }
        }

    }

    //CREATE ENEMIES AND IMPORT THEIR MOVEMENT

    private void moveEnemies(List<GameObject> currentTile, Position heroPosition) {

        for (GameObject enemy : currentTile) {
            if (enemy instanceof Bat) {
               ((Bat) enemy).moves(tiles, heroPosition);
            }
        }
        for (GameObject enemy : currentTile) {
            if (enemy instanceof Skeleton) {
                ((Skeleton) enemy).moves(tiles, heroPosition);
            }

        }
        for (GameObject enemy : currentTile) {
            if (enemy instanceof Badguy) {
                ((Badguy) enemy).moves(tiles, heroPosition);
            }
        }
        for (GameObject enemy : currentTile) {
            if (enemy instanceof Thief) {
                ((Thief) enemy).moves(tiles, heroPosition);
            }
        }
    }

    //METHOD TO MOVE BETWEEN ROOMS

    public static int countIndex = 0;

    public boolean loadRoom(Position newHeroPosition) {
        List<GameObject> roomIndex = listOfTiles.get(countIndex);

        for (GameObject roomObject : roomIndex) {

            if ((roomObject instanceof DoorOpen && roomObject.getPosition().equals(hero.getPosition()))) {
                if (countIndex == 0) {
                    countIndex++;
                    tiles = listOfTiles.get(countIndex);
                    gui.newImages(tiles);
                    hero.setPosition(new Position(4, 9));
                    refreshStatusImages();
                    return true;
                } else if (countIndex == 1 && roomObject.getPosition().equals(new Position(4, 0))) {
                    countIndex++;
                    tiles = listOfTiles.get(countIndex);
                    gui.newImages(tiles);
                    hero.setPosition(new Position(5, 9));
                    refreshStatusImages();
                    return true;
                } else if (countIndex == 2 && roomObject.getPosition().equals(new Position(5, 9))) {
                    countIndex--;
                    tiles = listOfTiles.get(countIndex);
                    gui.newImages(tiles);
                    hero.setPosition(new Position(4, 0));
                    refreshStatusImages();
                    return true;
                }

            } else if (roomObject instanceof DoorClosed && roomObject.getPosition().equals(hero.getPosition())) {
                System.out.println("Transitioning to previous room from DoorClosed.");
                if (countIndex == 2 && roomObject.getPosition().equals(new Position(0, 8))) {
                    tiles.remove(roomObject);
                    countIndex--;
                    tiles = listOfTiles.get(countIndex);
                    gui.newImages(tiles);
                    tiles.remove(roomObject);
                    hero.setPosition(new Position(8, 8));
                    refreshStatusImages();
                    return true;
                } else if (countIndex == 1 && roomObject.getPosition().equals(new Position(9, 8))) {
                    countIndex++;
                    tiles = listOfTiles.get(countIndex);
                    gui.newImages(tiles);
                    hero.setPosition(new Position(1, 8));
                    refreshStatusImages();
                    return true;
                }

                } else if (roomObject instanceof DoorWay && roomObject.getPosition().equals(hero.getPosition())) {
                    if (countIndex == 1 && roomObject.getPosition().equals(new Position(4,9))) {
                        countIndex--;
                        tiles = listOfTiles.get(countIndex);
                        gui.newImages(tiles);
                        hero.setPosition(new Position(4,0));
                        refreshStatusImages();
                        return true;
                    }
               }
           }
        if (countIndex >= listOfTiles.size()) {
            countIndex = listOfTiles.size() - 1;
        }

        return false;
    }


    //CONFIRM HERO ONLY MOVES INSIDE WALLS

    public boolean characterMoveConfirm(Position newHeroPosition) {

        List<GameObject> tilesCopy = new ArrayList<>(tiles);

        for (GameObject tile : tilesCopy) {
            if (tile.getPosition().equals(newHeroPosition) && !tile.isTransposable()) {
                return false;
            }
        }
        return true;
    }

    //IN-GAME ACTIONS:

    public void notify(int keyPressed) {

        Position newHeroPosition = hero.getPosition();

        // Handle hero movement keys
        if (keyPressed == KeyEvent.VK_DOWN) newHeroPosition = hero.getPosition().plus(Direction.DOWN.asVector());
        if (keyPressed == KeyEvent.VK_UP) newHeroPosition = hero.getPosition().plus(Direction.UP.asVector());
        if (keyPressed == KeyEvent.VK_LEFT) newHeroPosition = hero.getPosition().plus(Direction.LEFT.asVector());
        if (keyPressed == KeyEvent.VK_RIGHT) newHeroPosition = hero.getPosition().plus(Direction.RIGHT.asVector());

        // Check for inventory actions
        if (keyPressed == KeyEvent.VK_1) hero.manageInventory(Hammer.class, false);
        if (keyPressed == KeyEvent.VK_2) hero.manageInventory(Sword.class, false);
        if (keyPressed == KeyEvent.VK_3) hero.manageInventory(Key.class, false);
        if (keyPressed == KeyEvent.VK_P) hero.picksItem(hero.getPosition(), tiles, hero);
        if (keyPressed == KeyEvent.VK_F) hero.throwFireBall();

        // Check for enemies at the target position
        boolean enemyAtTarget = false;
        for (GameObject obj : new ArrayList<>(tiles)) {
            if (obj.getPosition().equals(newHeroPosition) && obj instanceof Enemy) {
                enemyAtTarget = true;
                hero.dealsDamage(newHeroPosition, tiles, hero);
                break;
            }
        }

        // Move hero if no enemy is blocking and movement is confirmed
        if (!enemyAtTarget && characterMoveConfirm(newHeroPosition)) {
            hero.setPosition(newHeroPosition);
            hero.takePoints(1);
            System.out.println("Current points: " + hero.getPoints());
        }

        Trap.dealsDamageToHero(hero,tiles,newHeroPosition);


        // Move enemies and load the room
        moveEnemies(tiles, newHeroPosition);
        loadRoom(newHeroPosition);
        gui.newImages(tiles);

        // Allow enemies to attack the hero
        for (GameObject obj : new ArrayList<>(tiles)) {
            if (obj instanceof Enemy) {
                ((Enemy) obj).dealDamageToHero(hero, tiles);
            }
        }

        gui.setStatus("Current points: "+ hero.getPoints());

        gameOver();

        gameIsCompleted();

    }

    public boolean isPromptActive = false;

    // Placeholder for game-over logic
    public void gameOver() {
        if (hero.getActualHealth() <= 0) {
            gameIsOver = true;
            stopMusic();
            playSE(1);
            List<Integer> points = leaderboardScanner(new File("./rooms/leaderboard.txt"));
            points.add(hero.getPoints());
            points.sort(Collections.reverseOrder());
            leaderBoard(points);
            ImageMatrixGUI.getInstance().setStatus("GameOver");
            ImageMatrixGUI.getInstance().showMessage("You died! Try again", "LeaderBoards: " + points);
            restart();
        }
    }


    public void gameIsCompleted() {

        if (hero.getActualHealth() > 0) {


            List<GameObject> lastRoomTiles = listOfTiles.get(2);

            boolean allEnemiesDefeated = true;
            for (GameObject obj : lastRoomTiles) {
                if (obj instanceof Enemy) {
                    allEnemiesDefeated = false;
                    break;
                }
            }

            if (allEnemiesDefeated) {
                System.out.println("Congratulations! You have completed the game.");
                stopMusic();
                playSE(5);
                List<Integer> points = leaderboardScanner(new File("./rooms/leaderboard.txt"));
                points.add(hero.getPoints());
                points.sort(Collections.reverseOrder());
                leaderBoard(points);
                ImageMatrixGUI.getInstance().setStatus("Game Completed");
                ImageMatrixGUI.getInstance().showMessage("Victory!", "LeaderBoards: " + points);
                restart();
            }
        }
     }

    public void restart(){

        Position heroNewPosition = new Position(5,5);
        hero.setPosition(heroNewPosition);
        hero.setHealth(hero.getTotalHealth());


        Engine engine = new Engine();
        engine.init();
    }


    public void leaderBoard(List<Integer> sortedScores){
        try (PrintWriter leaderBoardWriter = new PrintWriter(new File("./rooms/leaderboard.txt"))) {
            for (int i = 0; i < Math.min(sortedScores.size(), 3); i++) { // Top 3 scores
                leaderBoardWriter.println((i + 1) + ". " + sortedScores.get(i));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Creating file not possible");
        }
    }

    public List<Integer> leaderboardScanner(File leaderboardFile) {
        List<Integer> scores = new ArrayList<>();
        try (Scanner leaderBoardScanner = new Scanner(leaderboardFile)) {
            while (leaderBoardScanner.hasNextLine()) {
                String line = leaderBoardScanner.nextLine().trim();
                String[] parts = line.split("\\. ");
                if (parts.length > 1) {
                    try {
                        scores.add(Integer.parseInt(parts[1].trim()));
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid score format in leaderboard file: " + parts[1]);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found, creating new leaderboard");
        }
        return scores;
    }

    public static List<ImageTile> statusImages() {
        List<ImageTile> statusTiles = new ArrayList<>();
        int j = 0;
        int fireBallCount = hero.getFireBallCounts();
        for (int i = 0; i < 10; i++) {
            if (i <= 2) {
                if (fireBallCount > 0) {
                    statusTiles.add(new Black(new Position(i, j)));
                    statusTiles.add(new FireBall(new Position(i, j)));
                    fireBallCount--;
                } else {
                    statusTiles.add(new Black(new Position(i, j)));
                }
            }
        }

        if (hero.getActualHealth() > 75) {
            for (int i = 3; i <= 6; i++) {
                statusTiles.add(new Green(new Position(i, j)));
            }
        } else if (hero.getActualHealth() > 50) {
            for (int i = 3; i <= 5; i++) {
                statusTiles.add(new Green(new Position(i, j)));
            }
            statusTiles.add(new Red(new Position(6, j)));
        } else if (hero.getActualHealth() > 25) {
            for (int i = 3; i <= 4; i++) {
                statusTiles.add(new Green(new Position(i, j)));
            }
            for (int i = 5; i <= 6; i++) {
                statusTiles.add(new Red(new Position(i, j)));
            }
        } else if (hero.getActualHealth() > 0) {
            statusTiles.add(new Green(new Position(3, j)));
            for (int i = 4; i <= 6; i++) {
                statusTiles.add(new Red(new Position(i, j)));
            }
        } else {
            for (int i = 3; i <= 6; i++) {
                statusTiles.add(new Red(new Position(i, j)));
            }
        }

        int hammer = hero.getHammerCount(hero.getInventory());
        int sword = hero.getSwordCount(hero.getInventory());
        int key = hero.getKeyCount(hero.getInventory());

        for (int i = 7; i < 8; i++) {
            if (hammer > 0) {
                statusTiles.add(new Hammer(new Position(i, j)));
                hammer--;
            } else {
                statusTiles.add(new Black(new Position(i, j)));
            }
        }

        for (int i = 8; i < 9; i++) {
            if (sword > 0) {
                statusTiles.add(new Sword(new Position(i, j)));
                sword--;
            } else {
                statusTiles.add(new Black(new Position(i, j)));
            }
        }

        for (int i = 9; i < 10; i++) {
            if (key > 0) {
                statusTiles.add(new Key(new Position(i, j)));
                key--;
            } else {
                statusTiles.add(new Black(new Position(i, j)));
            }
        }

        return statusTiles;
    }

    public static void refreshStatusImages() {
        statusImage = statusImages();
        gui.newStatusImages(statusImage);
    }


    public static void main(String[] args) {
        Engine engine = new Engine();
        engine.init();
    }
}




