import java.util.Objects;
import java.util.Random;

public class Apple {
    public int[] position = new int[2];
    public Apple(Rectangle board) {
        this.placeRandom(board);
    }
    public void placeRandom(Rectangle board) {
        Objects.requireNonNull(board);
        int boundRow = board.getNumberOfRows();
        int boundColumn= board.getNumberOfColumns();
        Random random = new Random();
        this.position[0] = random.nextInt(1, boundRow - 2);
        this.position[1] = random.nextInt(1, boundColumn - 2);
    }
}
