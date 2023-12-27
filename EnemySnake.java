import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemySnake {
    public List<int[]> body = new ArrayList();
    private Direction direction;
    private boolean isAlive;
    private int moves = 1;
    private int score = 0;

    public int getScore() {
        return score;
    }
    public Direction getDirection() {
        return this.direction;
    }
    public int getLength(){
        return this.body.size();
    }
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
    public boolean isAlive() {
        return this.isAlive;
    }
    public void setAlive(boolean alive) {
        this.isAlive = alive;
    }
    public EnemySnake() {
        this.direction = Direction.RIGHT;
        this.isAlive = true;
        resetSnake();
    }
    public void resetSnake() {
        this.body.clear();

        Random random = new Random();
        int rand = random.nextInt(100);

        int startRow;
        int startColumn;

        if (rand < 33) {
            startRow=(Game.board.getNumberOfRows() - 2);
            startColumn=(Game.board.getNumberOfColumns() - 15);
            this.direction = Direction.UP;
        }else if(rand < 66) {
            startRow=(Game.board.getNumberOfRows() -6);
            startColumn=(Game.board.getNumberOfColumns() - 15);
            this.direction = Direction.RIGHT;
        }
        else {
            startRow=(Game.board.getNumberOfRows() -15);
            startColumn=(Game.board.getNumberOfColumns() - 15);
            this.direction = Direction.DOWN;
        }



        this.body.add(new int[]{startRow, startColumn});
        this.body.add(new int[]{startRow, startColumn-1});
        this.body.add(new int[]{startRow, startColumn-2});
        this.body.add(new int[]{startRow, startColumn-3});
        this.body.add(new int[]{startRow, startColumn-4});
    }

    public void moveSnake() {
        Random random = new Random();
        int rand = random.nextInt(100);

        if (rand < 50) {
        } else if (rand < 63) {
            if (!(this.direction == Direction.DOWN))
                this.direction = Direction.UP;
        } else if (rand < 76) {
            if (!(this.direction == Direction.LEFT))
                this.direction = Direction.RIGHT;
        } else if (rand < 89) {
            if (!(this.direction == Direction.UP))
                this.direction = Direction.DOWN;
        } else {
            if (!(this.direction == Direction.RIGHT))
                this.direction = Direction.LEFT;
        }

        //This first part takes care of the head
        int[] newPos= new int[2];
        int[] oldPos= new int[2];
        newPos[0]=this.body.get(0)[0];
        newPos[1]=this.body.get(0)[1];

        switch (this.direction) {
            case UP:
                newPos[0] -= moves;
                break;
            case DOWN:
                newPos[0] += moves;
                break;
            case RIGHT:
                newPos[1] += moves;
                break;
            case LEFT:
                newPos[1] -= moves;
                break;
        }

        for(int i = 0; i<this.body.size(); i++){
            oldPos[0]=this.body.get(i)[0];
            oldPos[1]=this.body.get(i)[1];
            this.body.get(i)[0]=newPos[0];
            this.body.get(i)[1]=newPos[1];
            newPos[0]=oldPos[0];
            newPos[1]=oldPos[1];
        }
    }

    public boolean checkCollision(Rectangle board) {
        int i = 0;

        for (int[] sCoord : body) {
            if (i != 0){
                if (sCoord[0] == body.get(0)[0] && sCoord[1] == body.get(0)[1]){ // check head does not have the same
                    return false;                                                  // position as any other part of the body
                }
            }
            for (int[] border : board.getBorders()) { // check head does not collide with any border
                if (sCoord[0] == border[0] && sCoord[1] == border[1]){
                    return false;
                }
            }
            i++;
        }
        return true;
    }
}
