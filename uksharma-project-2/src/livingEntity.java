import bagel.util.Colour;
/**
 * Interface used on living entities
 */
public interface livingEntity {
    int ORANGE_BOUNDARY = 65;
    int RED_BOUNDARY = 35;
    Colour GREEN = new Colour(0, 0.8, 0.2);
    Colour ORANGE = new Colour(0.9, 0.6, 0);
    Colour RED = new Colour(1, 0, 0);
    long invinceLength = 3000;

    /**
     * Method that draws healthpoints of entity
     */
    default void renderHealthPoints() {

    }
    default void doDmg(int dmgTaken) {

    }
}
