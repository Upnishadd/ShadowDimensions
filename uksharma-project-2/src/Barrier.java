import bagel.Image;
import bagel.util.Point;


public class Barrier extends gameEntity {
    private final static Image TREE = new Image("res/tree.png");
    private final static Image WALL = new Image("res/wall.png");

    public Barrier(int startX, int startY, Boolean tree){
        position = new Point(startX, startY);
        if (tree) { currentImage = TREE; }
        else { currentImage = WALL; }
    }

}