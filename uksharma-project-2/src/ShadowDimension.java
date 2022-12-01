import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * SWEN20003 Project 2, Semester 2, 2022
 * The project 1 solutions were used as the foundations for this project
 * @author Upnishadd Ksharma
 */
public class ShadowDimension extends AbstractGame {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "SHADOW DIMENSION";
    private final static String WORLD_FILE_0 = "res/level0.csv";
    private final static String WORLD_FILE_1 = "res/level1.csv";
    private final Image BACKGROUND_IMAGE_0 = new Image("res/background0.png");
    private final Image BACKGROUND_IMAGE_1 = new Image("res/background1.png");

    private final static int TITLE_FONT_SIZE = 75;
    private final static int INSTRUCTION_FONT_SIZE = 40;
    private final static int TITLE_X = 260;
    private final static int TITLE_Y = 250;
    private final static int MIDDLE_X = 350;
    private final static int MIDDLE_Y = 350;
    private final static int INS_X_OFFSET = 90;
    private final static int INS_Y_OFFSET = 190;
    private final Font TITLE_FONT = new Font("res/frostbite.ttf", TITLE_FONT_SIZE);
    private final Font INSTRUCTION_FONT = new Font("res/frostbite.ttf", INSTRUCTION_FONT_SIZE);
    private final static String INSTRUCTION_MESSAGE = "PRESS SPACE TO START\nUSE ARROW KEYS TO FIND GATE";
    private final static String MIDDLE_MESSAGE = "PRESS SPACE TO START\nPRESS A TO ATTACK\nDEFEAT NAVEC TO WIN";
    private final static String END_MESSAGE = "GAME OVER!";
    private final static String LEVEL_COMPLETE_MESSAGE = "LEVEL COMPLETE";
    private final static String WIN_MESSAGE = "CONGRATULATIONS!";

    private final static int WALL_ARRAY_SIZE = 52;
    private final static int S_HOLE_ARRAY_SIZE = 5;
    private final static int TREE_ARRAY_SIZE = 15;
    private final static int DEMON_ARRAY_SIZE = 5;
    private final static Barrier[] walls = new Barrier[WALL_ARRAY_SIZE];
    private final static Barrier[] tree = new Barrier[TREE_ARRAY_SIZE];
    private final static Demon[] demons = new Demon[DEMON_ARRAY_SIZE];
    private final static Sinkhole[] sinkholes_0 = new Sinkhole[S_HOLE_ARRAY_SIZE];
    private final static Sinkhole[] sinkholes_1 = new Sinkhole[S_HOLE_ARRAY_SIZE];
    private Point topLeft_0;
    private Point bottomRight_0;
    private Player player_0;

    private Point topLeft_1;
    private Point bottomRight_1;
    private Player player_1;
    private Demon navec;
    private final static int NO_CHANGE_TIMESCALE = 0;
    private final static int INCREASE_TIMESCALE = 1;
    private final static int DECREASE_TIMESCALE = -1;
    private static int timeScale = NO_CHANGE_TIMESCALE;
    private final static int MIN_TIME_SCALE = -3;
    private final static int MAX_TIME_SCALE = 3;
    private static int change;
    private boolean hasStarted_0;
    private boolean hasStarted_1;
    private boolean level_0;
    private long LEVEL_COMPLETE_TIME;
    private final long LEVEL_COMPLETE_DURATION = 3000;
    private boolean gameOver;
    private boolean playerWin;

    public ShadowDimension() {
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
        readCSV();
        hasStarted_0 = false;
        hasStarted_1 = false;
        gameOver = false;
        playerWin = false;
        level_0 = true;
    }

    /**
     * Method used to read file and create objects
     */
    private void readCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader(WORLD_FILE_0))) {

            String line;
            int currentWallCount = 0;
            int currentSinkholeCount = 0;

            while ((line = reader.readLine()) != null) {
                String[] sections = line.split(",");
                switch (sections[0]) {
                    case "Player":
                        player_0 = new Player(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                        break;
                    case "Wall":
                        walls[currentWallCount] = new Barrier(Integer.parseInt(sections[1]),
                                Integer.parseInt(sections[2]), false);
                        currentWallCount++;
                        break;
                    case "Sinkhole":
                        sinkholes_0[currentSinkholeCount] = new Sinkhole(Integer.parseInt(sections[1]),
                                Integer.parseInt(sections[2]));
                        currentSinkholeCount++;
                        break;
                    case "TopLeft":
                        topLeft_0 = new Point(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                        break;
                    case "BottomRight":
                        bottomRight_0 = new Point(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(WORLD_FILE_1))) {

            String line;
            int currentTreeCount = 0;
            int currentSinkholeCount_1 = 0;
            int currentDemonCount = 0;

            while ((line = reader.readLine()) != null) {
                String[] sections = line.split(",");
                switch (sections[0]) {
                    case "Player":
                        player_1 = new Player(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                        break;
                    case "Navec":
                        navec = new Demon(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]), true);
                        break;
                    case "Tree":
                        tree[currentTreeCount] = new Barrier(Integer.parseInt(sections[1]),
                                Integer.parseInt(sections[2]), true);
                        currentTreeCount++;
                        break;
                    case "Sinkhole":
                        sinkholes_1[currentSinkholeCount_1] = new Sinkhole(Integer.parseInt(sections[1]),
                                Integer.parseInt(sections[2]));
                        currentSinkholeCount_1++;
                        break;
                    case "Demon":
                        demons[currentDemonCount] = new Demon(Integer.parseInt(sections[1]),
                                Integer.parseInt(sections[2]), false);
                        currentDemonCount++;
                        break;
                    case "TopLeft":
                        topLeft_1 = new Point(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                        break;
                    case "BottomRight":
                        bottomRight_1 = new Point(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDimension game = new ShadowDimension();
        game.run();
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    public void update(Input input) {
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        if (!hasStarted_0) {
            drawStartScreen();
            if (input.wasPressed(Keys.SPACE)) {
                hasStarted_0 = true;
            }
        } else if (!hasStarted_1) {
            if(System.currentTimeMillis() - LEVEL_COMPLETE_TIME <= LEVEL_COMPLETE_DURATION) {
                drawMessage(LEVEL_COMPLETE_MESSAGE);
            }
            else {
                drawMiddleScreen();
                if (input.wasPressed(Keys.SPACE)) {
                    hasStarted_1 = true;
                }
            }
        }

        if (gameOver) {
            drawMessage(END_MESSAGE);
        } else if (playerWin) {
            drawMessage(WIN_MESSAGE);
        }

        // Set timescale
        change = NO_CHANGE_TIMESCALE;

        if (input.wasPressed(Keys.K)) {
            timeScale++;
            if (timeScale > MAX_TIME_SCALE) {
                timeScale--;
            } else {
                change = INCREASE_TIMESCALE;
                System.out.println("Sped up, Speed: "+ timeScale);
            }
        } else if (input.wasPressed(Keys.L)) {
            timeScale--;
            if (timeScale < MIN_TIME_SCALE) {
                timeScale++;
            } else {
                change = DECREASE_TIMESCALE;
                System.out.println("Slowed down, Speed: "+ timeScale);
            }
        }

        // game is running
        if (hasStarted_0 && !gameOver && !playerWin) {
            if (level_0) {
                BACKGROUND_IMAGE_0.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);

                for (Barrier current : walls) {
                    current.update();
                }
                for (Sinkhole current : sinkholes_0) {
                    current.update();
                }
                player_0.update(input, this);
                checkCollisions(player_0, true);
                checkOutOfBounds(player_0, true);

                if (player_0.isDead()) {
                    gameOver = true;
                }

                if (player_0.reachedGate()) {
                    level_0 = false;
                    LEVEL_COMPLETE_TIME = System.currentTimeMillis();
                }
            } else if (hasStarted_1 && !gameOver && !playerWin) {
                BACKGROUND_IMAGE_1.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
                for (Barrier current : tree) {
                    current.update();
                }
                for (Sinkhole current : sinkholes_1) {
                    current.update();
                }
                for (Demon current : demons) {
                    current.update(change);
                    demonCollisions(current);
                }
                navec.update(change);
                demonCollisions(navec);
                player_1.update(input, this);
                checkCollisions(player_1, false);
                checkOutOfBounds(player_1, false);

                attackingPhase(player_1, navec, demons);

                if (player_1.isDead()) {
                    gameOver = true;
                }
                if (!navec.isAlive()) {
                    playerWin = true;
                }
            }
        }
    }

    /**
     * Method that checks for collisions between Fae and the other entities, and performs
     * corresponding actions.
     */
    public void checkCollisions(Player player, Boolean level_0) {
        Barrier[] barrier;
        Sinkhole[] sinkholes;

        if (level_0) {
            barrier = walls;
            sinkholes = sinkholes_0;
        } else {
            barrier = tree;
            sinkholes = sinkholes_1;
        }

        Rectangle faeBox = new Rectangle(player.getPosition(), player.getCurrentImage().getWidth(),
                player.getCurrentImage().getHeight());
        for (Barrier current : barrier) {
            Rectangle wallBox = current.getBoundingBox();
            if (faeBox.intersects(wallBox)) {
                player.moveBack();
            }
        }

        for (Sinkhole hole : sinkholes) {
            Rectangle holeBox = hole.getBoundingBox();
            if (hole.isActive() && faeBox.intersects(holeBox) && !player.isInvince()) {
                player.setHealthPoints(Math.max(player.getHealthPoints() - hole.getDamagePoints(), 0));
                player.moveBack();
                hole.setActive(false);
                System.out.println("Sinkhole inflicts " + hole.getDamagePoints() + " damage points on Fae. " +
                        "Fae's current health: " + player.getHealthPoints() + "/" + Player.getMaxHealthPoints());
            }
        }
    }

    /**
     * Method that checks if Fae has gone out-of-bounds and performs corresponding action
     */
    public void checkOutOfBounds(Player player, Boolean level_0) {
        Point bottomRight, topLeft;
        if (level_0) {
            bottomRight = bottomRight_0;
            topLeft = topLeft_0;
        } else {
            bottomRight = bottomRight_1;
            topLeft = topLeft_1;
        }

        Point currentPosition = player.getPosition();
        if ((currentPosition.y > bottomRight.y) || (currentPosition.y < topLeft.y) || (currentPosition.x < topLeft.x)
                || (currentPosition.x > bottomRight.x)) {
            player.moveBack();
        }
    }

    /**
     * Checks if demon has collided with any non-player/demon object
     * and if collision occurs the demon turns around
     *
     * @param demon
     */
    private void demonCollisions(Demon demon) {
        Barrier[] barrier;
        Sinkhole[] sinkholes;
        Point bottomRight, topLeft;

        barrier = tree;
        sinkholes = sinkholes_1;
        bottomRight = bottomRight_1;
        topLeft = topLeft_1;

        Rectangle demonBox = demon.getBoundingBox();
        for (Barrier current : barrier) {
            Rectangle wallBox = current.getBoundingBox();
            if (demonBox.intersects(wallBox)) {
                demon.setSpeed(-demon.getSpeed());
            }
        }
        for (Sinkhole current : sinkholes) {
            Rectangle sinkholeBox = current.getBoundingBox();
            if (demonBox.intersects(sinkholeBox)) {
                demon.setSpeed(-demon.getSpeed());
            }
        }
        Point currentPosition = demon.getPosition();
        if ((currentPosition.y > bottomRight.y) || (currentPosition.y < topLeft.y) || (currentPosition.x < topLeft.x)
                || (currentPosition.x > bottomRight.x)) {
            demon.setSpeed(-demon.getSpeed());
        }
    }

    /**
     * All characters that can attack during this turn will attack, allowing damages to be calculated
     */
    private void attackingPhase(Player player, Demon boss, Demon[] demon) {
        Rectangle playerBox = player.getBoundingBox();
        if (playerBox.intersects(boss.getBoundingBox())) {
            if (player.getState()) {
                boss.doDmg(player.getDmg());
                System.out.println("Fae inflicts 20 damage points on Navec. Navec’s current health:"
                        + boss.getHealthPoints());
            }
            player.doDmg(boss.attack(playerBox));
            if(boss.attack(playerBox) != 0) {
                System.out.println("Navec inflicts 20 damage points on Fae. Fae’s current health:"
                        + player.getHealthPoints());
            }
        }
        for (Demon current : demon) {
            if (playerBox.intersects(current.getBoundingBox())) {
                if (player.getState()) {
                    current.doDmg(player.getDmg());
                    System.out.println("Fae inflicts 20 damage points on Demon. Demon’s current health:"
                            + current.getHealthPoints());
                }

                player.doDmg(current.attack(playerBox));
                if(current.attack(playerBox) != 0) {
                    System.out.println("Demon inflicts 10 damage points on Fae. Fae’s current health:"
                            + player.getHealthPoints());
                }
            }
        }
    }

    /**
     * Method used to draw the start screen title and instructions
     */
    private void drawStartScreen() {
        TITLE_FONT.drawString(GAME_TITLE, TITLE_X, TITLE_Y);
        INSTRUCTION_FONT.drawString(INSTRUCTION_MESSAGE, TITLE_X + INS_X_OFFSET, TITLE_Y + INS_Y_OFFSET);
    }

    /**
     * Method used to draw middle screen between level 0 and level 1
     */
    private void drawMiddleScreen() {
        INSTRUCTION_FONT.drawString(MIDDLE_MESSAGE, MIDDLE_X, MIDDLE_Y);
    }

    /**
     * Method used to draw end screen messages
     */
    private void drawMessage(String message) {
        TITLE_FONT.drawString(message, (Window.getWidth() / 2.0 - (TITLE_FONT.getWidth(message) / 2.0)),
                (Window.getHeight() / 2.0 + (TITLE_FONT_SIZE / 2.0)));
    }
}