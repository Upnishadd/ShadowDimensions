import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class gameEntity {
    protected Image currentImage;
    protected Point position;

    /**
     * Method that returns the bounding box of an image
     */
    public Rectangle getBoundingBox(){
        return new Rectangle(position, currentImage.getWidth(), currentImage.getHeight());
    }

    /**
     * Method that performs state update
     */
    public void update() {
        currentImage.drawFromTopLeft(this.position.x, this.position.y);
    }

}
