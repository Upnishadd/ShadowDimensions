import bagel.Image;
import bagel.util.Point;

public class Sinkhole extends gameEntity {
    private final static Image SINKHOLE = new Image("res/sinkhole.png");
    private final static int DAMAGE_POINTS = 30;
    private boolean isActive;

    public Sinkhole(int startX, int startY){
        position = new Point(startX, startY);
        this.isActive = true;
        currentImage = SINKHOLE;
    }

    /**
     * Method that performs state update
     */
    public void update() {
        if (isActive){
            currentImage.drawFromTopLeft(position.x, position.y);
        }
    }

    public int getDamagePoints(){
        return DAMAGE_POINTS;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}