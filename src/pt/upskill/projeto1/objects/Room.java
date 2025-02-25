package pt.upskill.projeto1.objects;

import pt.upskill.projeto1.enemies.Badguy;
import pt.upskill.projeto1.enemies.Bat;
import pt.upskill.projeto1.enemies.Skeleton;
import pt.upskill.projeto1.enemies.Thief;
import pt.upskill.projeto1.game.Engine;
import pt.upskill.projeto1.items.GoodMeat;
import pt.upskill.projeto1.items.Hammer;
import pt.upskill.projeto1.items.Key;
import pt.upskill.projeto1.items.Sword;
import pt.upskill.projeto1.rogue.utils.Position;
import pt.upskill.projeto1.scene.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static pt.upskill.projeto1.game.Engine.countIndex;
import static pt.upskill.projeto1.game.Engine.listOfTiles;

public class Room {

    //CREATE LIST OF ROOM FILES

    File room0 = new File("./rooms/room0.txt");
    File room1 = new File("./rooms/room1.txt");
    File room2 = new File("./rooms/room2.txt");



    List<File> roomList = new ArrayList<>();

        public Room() {
            roomList.add(room0);
            roomList.add(room1);
            roomList.add(room2);
        }



        //SCAN ROOM FILE

        public List<GameObject> roomScanner (File roomFile) {

            List<GameObject> imageTiles = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    imageTiles.add(new Floor(new Position(i, j)));
                }
            }
                try {
                    Scanner roomScanner = new Scanner(roomFile);
                    int line = 0;
                    while (roomScanner.hasNextLine()) {
                        String firstLine = roomScanner.nextLine();
                        if(firstLine.startsWith("#")){
                            continue;
                        }
                        int column = 0;
                        String[] object = firstLine.split("");
                        for (String objects : object) {
                            switch (objects) {
                                case ("W"):
                                    imageTiles.add(new Wall(new Position(column, line)));
                                    break;
                                case ("0"):
                                    if (roomFile.equals(room0)) {
                                        imageTiles.add(new DoorOpen(new Position(column, line)));
                                    } else if (roomFile.equals(room1)) {
                                        imageTiles.add(new DoorOpen(new Position(column, line)));
                                    } else if (roomFile.equals(room2)) {
                                        imageTiles.add(new DoorOpen(new Position(0, 8)));
                                        imageTiles.add(new DoorClosed(new Position(column, line)));
                                    }
                                    break;
                                case ("h"):
                                    imageTiles.add(new Hammer(new Position(column, line)));
                                    break;
                                case ("m"):
                                    imageTiles.add(new GoodMeat(new Position(column, line)));
                                    break;
                                case ("k"):
                                    imageTiles.add(new Key(new Position(column, line)));
                                    break;
                                case ("1"):
                                    if (roomFile.equals(room1)) {
                                        imageTiles.add(new DoorOpen(new Position(9, 8)));
                                        imageTiles.add(new DoorClosed(new Position(column, line)));
                                    } else if (roomFile.equals(room2)) {
                                        imageTiles.add(new DoorOpen(new Position(column, line)));
                                    }
                                    break;
                                case ("2"):
                                    imageTiles.add(new DoorWay(new Position(column, line)));
                                    break;
                                case ("S"):
                                    imageTiles.add(new Skeleton(new Position(column, line)));
                                    break;
                                case ("B"):
                                    imageTiles.add(new Bat(new Position(column, line)));
                                    break;
                                case ("G"):
                                    imageTiles.add(new Badguy(new Position(column, line)));
                                    break;
                                case ("s"):
                                    imageTiles.add(new Sword(new Position(column, line)));
                                    break;
                                case ("T"):
                                    imageTiles.add(new Thief(new Position(column, line)));
                                    break;
                                case ("g"):
                                    imageTiles.add(new Grass(new Position(column, line)));
                                    break;
                                case ("d"):
                                    imageTiles.add(new StairsDown(new Position(column, line)));
                                    break;
                                case ("u"):
                                    imageTiles.add(new StairsUp(new Position(column, line)));
                                    break;
                                case ("t"):
                                    imageTiles.add(new Trap(new Position(column, line)));
                                    break;
                                default:
                            }
                            column++;
                        }
                        line++;
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("File not found");
                }

            return imageTiles;
        }

        // SCAN LIST OF ROOMS AND SAVE IT

        public List<List<GameObject>> totalScanner() {
            List<List<GameObject>> allRoomsList = new ArrayList<>();
            for (File roomFile:roomList){
                List<GameObject> roomContents = roomScanner(roomFile);
                allRoomsList.add(roomContents);
            }
            return allRoomsList;
        }



    }