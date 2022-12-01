import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.Random;

public class Demon extends gameEntity implements livingEntity {
    private final Image demonLeft = new Image("res/demon/demonLeft.png");
    private final Image demonRight = new Image("res/demon/demonRight.png");
    private final Image demonInvincibleLeft = new Image("res/demon/demonInvincibleLeft.png");
    private final Image demonInvincibleRight = new Image("res/demon/demonInvincibleRight.png");
    private final Image demonFire = new Image("res/demon/demonFire.png");
    private final Image navecLeft = new Image("res/navec/navecLeft.png");
    private final Image navecRight = new Image("res/navec/navecRight.png");
    private final Image navecInvincibleLeft = new Image("res/navec/navecInvincibleLeft.png");
    private final Image navecInvincibleRight = new Image("res/navec/navecInvincibleRight.png");
    private final Image navecFire = new Image("res/navec/navecFire.png");


    private final static int MAX_HEALTH_POINTS_DEMON = 40;
    private final static int MAX_HEALTH_POINTS_NAVEC = 80;
    private final static int RANGE_DEMON = 150;
    private final static int RANGE_NAVEC = 200;
    private final static int DMG_DEMON = 10;
    private final static int DMG_NAVEC = 20;


    private final static int FONT_SIZE = 15;
    private final Font FONT = new Font("res/frostbite.ttf", FONT_SIZE);
    private final DrawOptions COLOUR = new DrawOptions();


    private Image fire;
    private int healthPoints;
    private int dmg;
    private int range;
    private double speed = 0;
    private boolean alive = true;


    Random rd = new Random();
    private final Boolean aggressive = rd.nextBoolean();
    private int direction = rd.nextInt(4);
    private final int LEFT = 0, RIGHT = 1, DOWN = 2, UP = 3;

    private Boolean navec;
    private final static int INCREASE_TIMESCALE = 1;
    private final static int DECREASE_TIMESCALE = -1;
    private final static double INCREASE_SPEED_FACTOR = 1.5;
    private final static double DECREASE_SPEED_FACTOR = 0.5;


    private long invinceStart;
    private boolean invince;

    public Demon(int startX, int startY, Boolean navec){
        this.navec = navec;
        if(navec) {
            if(direction == LEFT) {
                currentImage = navecLeft;
            }
            else {currentImage = navecRight;}
            healthPoints = MAX_HEALTH_POINTS_NAVEC;
            dmg = DMG_NAVEC;
            range = RANGE_NAVEC;
            fire = navecFire;
        }
        else {
            fire = demonFire;
            if(direction == LEFT && aggressive == true) {
                currentImage = demonLeft;
            }
            else {currentImage = demonRight;}
            range = RANGE_DEMON;
            healthPoints = MAX_HEALTH_POINTS_DEMON;
            dmg = DMG_DEMON;
        }
        if(navec || aggressive){
            speed = rd.nextDouble();
        }
        this.position = new Point(startX, startY);
        COLOUR.setBlendColour(GREEN);
        invince = false;
    }

    /**
     * Method that performs state update
     */
    public void update(int change) {
        if(alive) {
            // Change speed based on timescale
            if (change == INCREASE_TIMESCALE) {
                setSpeed(INCREASE_SPEED_FACTOR * getSpeed());
            } else if (change == DECREASE_TIMESCALE) {
                setSpeed(DECREASE_SPEED_FACTOR * getSpeed());
            }

            if(System.currentTimeMillis() - invinceStart > invinceLength) {
                invince = false;
            }
            // Change the direction if a collision occurs and continue moving demon
            if (direction == LEFT) {
                if (speed < 0) {
                    if (navec) {
                        currentImage = navecRight;
                    } else {
                        currentImage = demonRight;
                    }
                } else {
                    if (navec) {
                        currentImage = navecLeft;
                    } else {
                        currentImage = demonLeft;
                    }
                }
                move(-speed, 0);
            } else if (direction == RIGHT) {
                if (speed < 0) {
                    if (navec) {
                        currentImage = navecLeft;
                    } else {
                        currentImage = demonLeft;
                    }
                } else {
                    if (navec) {
                        currentImage = navecRight;
                    } else {
                        currentImage = demonRight;
                    }
                }
                move(speed, 0);
            } else if (direction == DOWN) {
                if (navec) {
                    currentImage = navecRight;
                } else {
                    currentImage = demonRight;
                }
                move(0, -speed);
            } else if (direction == UP) {
                if (navec) {
                    currentImage = navecRight;
                } else {
                    currentImage = demonRight;
                }
                move(0, speed);
            }
            if(invince) {
                fixInvinceImage();
            }
            if(healthPoints <= 0) {
                alive = false;
            }
            this.renderHealthPoints();
            currentImage.drawFromTopLeft(this.position.x, this.position.y);
        }
    }

    /**
     * Method that moves Demon given the direction
     */
    private void move(double xMove, double yMove){
        double newX = position.x + xMove;
        double newY = position.y + yMove;
        this.position = new Point(newX, newY);
    }

    public Point getPosition() {
        return position;
    }

    public double getSpeed() {
        return speed;
    }
    /**
     * Applies damage to demon if not invincible
     */
    public void doDmg(int dmgTaken) {
        if(!invince && alive) {
            healthPoints -= dmgTaken;
            invince = true;
            invinceStart = System.currentTimeMillis();
        }
    }
    /**
     * Determines if a point is within the demon's reange
     */
    private boolean inRange(Point point, Point centre) {
        double distance = Math.hypot(point.x - centre.x, point.y - centre.y);
        if(distance <= range) {
            return true;
        }
        return false;
    }
    /**
     * Determines which direction a point lies in from the demons centre
     */
    private int quadrant(Point point) {
        Point centre = getBoundingBox().centre();
        if(inRange(point, centre)) {
            if(point.x <= centre.x) {
                if(point.y <= centre.y){
                    return 1;
                }
                else { return 2; }
            }
            if(point.y > centre.y) {
                return 3;
            }
            return 4;
        }
        return 0;
    }
    /**
     * Coordinates the attack damage that will be applied to the player by the demon
     */
    public int attack(Rectangle player) {
        int dmgDone = 0;
        if(alive) {
            Point point = player.centre();
            int quad = quadrant(point);
            DrawOptions fireDrawing = new DrawOptions();
            fireDrawing.setRotation(Math.PI);
            Rectangle fireBox = fire.getBoundingBox();
            if (quad != 0) {
                if(quad == 1) {
                    Point posit = new Point(getBoundingBox().topLeft().x - fire.getWidth(),
                            getBoundingBox().topLeft().y - fire.getHeight());
                    fireBox.moveTo(posit);
                    fireDrawing.setRotation(-2*Math.PI);
                    fire.drawFromTopLeft(posit.x, posit.y, fireDrawing);
                    if(player.intersects(fireBox)) {
                        dmgDone = this.dmg;
                    };
                }
                else if(quad == 2) {
                    Point posit = new Point(getBoundingBox().bottomLeft().x - fire.getWidth(),
                            getBoundingBox().bottomLeft().y);
                    fireBox.moveTo(posit);
                    fireDrawing.setRotation(3 * Math.PI / 2);
                    fire.drawFromTopLeft(posit.x, posit.y, fireDrawing);
                }
                else if(quad == 3) {
                    Point posit = new Point(getBoundingBox().bottomRight().x,
                            getBoundingBox().bottomRight().y);
                    fireBox.moveTo(posit);
                    fire.drawFromTopLeft(posit.x, posit.y, fireDrawing);
                }
                else if(quad == 4) {
                    Point posit = new Point(getBoundingBox().topRight().x,
                            getBoundingBox().topRight().y - fire.getHeight());
                    fireBox.moveTo(posit);
                    fireDrawing.setRotation(- 3 * Math.PI / 2);
                    fire.drawFromTopLeft(posit.x, posit.y, fireDrawing);
                }
                if(player.intersects(fireBox)) {
                    dmgDone = this.dmg;
                };
            }
        }
        return dmgDone;
    }
    /**
     * Converts the invincible image of a demon to its normal one
     */
    private void fixInvinceImage() {
        if (direction > RIGHT) {
            if (navec) {
                currentImage = navecInvincibleRight;
            } else {
                currentImage = demonInvincibleRight;
            }
        } else if (direction == LEFT) {
            if (speed < 0) {
                if (navec) {
                    currentImage = navecInvincibleRight;
                } else {
                    currentImage = demonInvincibleRight;
                }
            } else {
                if (navec) {
                    currentImage = navecInvincibleLeft;
                } else {
                    currentImage = demonInvincibleLeft;
                }
            }
        } else {
            if (speed < 0) {
                if (navec) {
                    currentImage = navecInvincibleLeft;
                } else {
                    currentImage = demonInvincibleLeft;
                }
            } else {
                if (navec) {
                    currentImage = navecInvincibleRight;
                } else {
                    currentImage = demonInvincibleRight;
                }
            }
        }
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
    /**
     * Health of a demon will be calculated as a percentage and rendered with the appropriate colour
     */
    public void renderHealthPoints(){
        double percentageHP;
        if(navec) {
            percentageHP= ((double) healthPoints / MAX_HEALTH_POINTS_NAVEC) * 100;
        } else {percentageHP = ((double) healthPoints / MAX_HEALTH_POINTS_DEMON) * 100;}

        if (percentageHP <= RED_BOUNDARY){
            COLOUR.setBlendColour(RED);
        } else if (percentageHP <= ORANGE_BOUNDARY){
            COLOUR.setBlendColour(ORANGE);
        }
        FONT.drawString(Math.round(percentageHP) + "%", this.position.x, this.position.y - 6, COLOUR);
    }

    public boolean isAlive() {
        return alive;
    }

    public int getHealthPoints() {
        return healthPoints;
    }
}