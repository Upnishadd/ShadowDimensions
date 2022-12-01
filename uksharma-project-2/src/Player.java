import bagel.*;
import bagel.util.Point;

public class Player extends gameEntity  implements livingEntity {
    private final static String FAE_LEFT = "res/fae/faeLeft.png";
    private final static String FAE_RIGHT = "res/fae/faeRight.png";
    private final static String FAE_ATTACK_LEFT = "res/fae/faeAttackLeft.png";
    private final static String FAE_ATTACK_RIGHT = "res/fae/faeAttackRight.png";
    private final static int MAX_HEALTH_POINTS = 100;
    private final static double MOVE_SIZE = 2;
    private final static int WIN_X = 950;
    private final static int WIN_Y = 670;

    private final static int HEALTH_X = 20;
    private final static int HEALTH_Y = 25;
    private final static int FONT_SIZE = 30;
    private final Font FONT = new Font("res/frostbite.ttf", FONT_SIZE);
    private final static DrawOptions COLOUR = new DrawOptions();
    private Point prevPosition;
    private int healthPoints;
    private boolean facingRight;
    private boolean attack = false;
    private int dmg = 0;
    private long lastAttack;
    private long startAttack;
    private long invinceStart;
    private boolean invince;
    private final long attackDuration = 1000;
    private final long attackCooldown = 2000;

    public Player(int startX, int startY){
        position = new Point(startX, startY);
        healthPoints = MAX_HEALTH_POINTS;
        currentImage = new Image(FAE_RIGHT);
        facingRight = true;
        COLOUR.setBlendColour(GREEN);
        lastAttack = System.currentTimeMillis();
    }

    /**
     * Method that performs state update
     */
    public void update(Input input, ShadowDimension gameObject) {
        if(System.currentTimeMillis() - invinceStart > invinceLength) {
            invince = false;
        }
        if(attack && (System.currentTimeMillis() - startAttack > attackDuration)) {
            attack = false;
            lastAttack = System.currentTimeMillis();
            dmg = 0;

            if(!facingRight){
                currentImage = new Image(FAE_LEFT);
            } else { currentImage = new Image(FAE_RIGHT); }
        }
        if (input.isDown(Keys.A) && (System.currentTimeMillis() - lastAttack > attackCooldown)) {
            attack = true;
            startAttack = System.currentTimeMillis();
            dmg = 20;

            if(!facingRight){
                currentImage = new Image(FAE_ATTACK_LEFT);
            } else { currentImage = new Image(FAE_ATTACK_RIGHT); }
        }

        else if (input.isDown(Keys.UP)){
            setPrevPosition();
            move(0, -MOVE_SIZE);
        } else if (input.isDown(Keys.DOWN)){
            setPrevPosition();
            move(0, MOVE_SIZE);
        } else if (input.isDown(Keys.LEFT)){
            setPrevPosition();
            move(-MOVE_SIZE,0);
            if (facingRight) {
                if(attack) {
                    currentImage = new Image(FAE_ATTACK_LEFT);
                }
                else { currentImage = new Image(FAE_LEFT); }
                facingRight = false;
            }
        } else if (input.isDown(Keys.RIGHT)){
            setPrevPosition();
            move(MOVE_SIZE,0);
            if (!facingRight) {
                if(attack) {
                    currentImage = new Image(FAE_ATTACK_RIGHT);
                }
                else { currentImage = new Image(FAE_RIGHT); }
                facingRight = true;
            }
        }
        currentImage.drawFromTopLeft(position.x, position.y);
        renderHealthPoints();
    }

    /**
     * Method that stores Fae's previous position
     */
    private void setPrevPosition(){
        prevPosition = new Point(position.x, position.y);
    }

    /**
     * Method that moves Fae back to previous position
     */
    public void moveBack(){
        position = prevPosition;
    }

    /**
     * Method that moves Fae given the direction
     */
    private void move(double xMove, double yMove){
        double newX = position.x + xMove;
        double newY = position.y + yMove;
        position = new Point(newX, newY);
    }

    /**
     * Method that renders the current health as a percentage on screen
     */
    public void renderHealthPoints(){
        double percentageHP = ((double) healthPoints/MAX_HEALTH_POINTS) * 100;
        if (percentageHP <= RED_BOUNDARY){
            COLOUR.setBlendColour(RED);
        } else if (percentageHP <= ORANGE_BOUNDARY){
            COLOUR.setBlendColour(ORANGE);
        }
        FONT.drawString(Math.round(percentageHP) + "%", HEALTH_X, HEALTH_Y, COLOUR);
    }

    /**
     * Method that checks if Fae's health has depleted
     */
    public boolean isDead() {
        COLOUR.setBlendColour(GREEN);
        return healthPoints <= 0;
    }

    /**
     * Method that checks if Fae has found the gate
     */
    public boolean reachedGate(){
        return (position.x >= WIN_X) && (position.y >= WIN_Y);
    }

    /**
     * Applies damage to Fae if not invincible
     */
    public void doDmg(int dmgTaken) {
        if(!invince) {
            healthPoints -= dmgTaken;
            invince = true;
            invinceStart = System.currentTimeMillis();
        }
    }

    public Point getPosition() {
        return position;
    }

    public Image getCurrentImage() {
        return currentImage;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public static int getMaxHealthPoints() {
        return MAX_HEALTH_POINTS;
    }

    public boolean isInvince() {
        return invince;
    }

    public boolean getState() {
        return attack;
    }

    public int getDmg() {
        return dmg;
    }
}