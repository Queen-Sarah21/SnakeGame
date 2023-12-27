import java.util.ArrayList;
import java.util.List;

public class Snake {
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
    public Snake() {
        this.direction = Direction.RIGHT;
        this.isAlive = true;
        resetSnake();
    }
    public void resetSnake() {
        this.body.clear();

        int middleRow=(Game.board.getNumberOfRows()/2);
        int middleColumn=(Game.board.getNumberOfColumns()/2);

        this.body.add(new int[]{middleRow, middleColumn});
        this.body.add(new int[]{middleRow, middleColumn-1});
        this.body.add(new int[]{middleRow, middleColumn-2});
        this.body.add(new int[]{middleRow, middleColumn-3});
        this.body.add(new int[]{middleRow, middleColumn-4});
    }

    public void moveSnake() {
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

    public void grow(int increase) {
        for(int i = 0; i < increase; i++) {
            int x = ((int[])this.body.get(this.body.size() - 1))[0];
            int y = ((int[])this.body.get(this.body.size() - 1))[1];
            this.body.add(new int[]{x, y});
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

    public boolean checkCollision(EnemySnake enemy) {
        for (int[] sCoord : this.body) {
            for ( int[] cell : enemy.body) { // check head does not collide with enemy
                if (sCoord[0] == cell[0] && sCoord[1] == cell[1]){
                    return false;
                }
            }
        }
        return true;
    }

    public void eatApple(Rectangle board) {
        if (this.body.get(0)[0] == board.apple.position[0] && this.body.get(0)[1] == board.apple.position[1]) {
            this.score++;
            this.grow(this.score);
            board.apple.placeRandom(board);
        }
    }
}

