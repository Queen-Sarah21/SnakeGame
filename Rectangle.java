import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Rectangle {
    private final int numberOfRows = 16;
    private final int numberOfColumns = 20;
    private String[][] board;
    private List<int[]> borders = new ArrayList();
    public Apple apple = new Apple(this);
    private final String empty = "   ";
    private final String sammyFilled = " # ";
    private final String enemyFilled = " $ ";
    private final String head = " @ ";


    public void setBoard(String[][] board) {
        this.board = board;
    }
    public int getNumberOfRows() {
        return numberOfRows;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public Rectangle() {
        board = new String[numberOfRows][numberOfColumns];
    }
    public Rectangle(String[][] board) {
        this.board = board;
    }

    public String[][] getBoard() {
        return this.board;
    }

    public List<int[]> getBorders() {
        return this.borders;
    }

    public void setRectangle() {
        for(int y = 1; y < numberOfRows-1; y++) {
            for(int x = 1; x < numberOfColumns-1; x++) {
                this.board[y][x] = empty;
            }
        }

        for(int i=1; i<numberOfRows-1;i++){
            this.board[i][0] =" | ";
            this.board[i][numberOfColumns-1]= " | ";
            this.borders.add(new int[]{i, 0});
            this.borders.add(new int[]{i, numberOfColumns-1});
        }

        for(int i=0; i<numberOfColumns;i++){
            this.board[0][i] = " — ";
            this.board[numberOfRows-1][i]= " — ";
            this.borders.add(new int[]{0, i});
            this.borders.add(new int[]{numberOfRows-1, i});
        }
    }

    public void displayRectangle() {
        for(int i=0;i<numberOfRows;i++){
            for(int j=0;j<numberOfColumns;j++){
                System.out.print(this.board[i][j]);
            }
            System.out.println();
        }
    }

    public void placeSnake(List<int[]> snake, boolean isSammy) {
        //{row,column}
        if (isSammy){

            for (int[] coord : snake) {
                if (coord[0] == snake.get(0)[0] && coord[1] == snake.get(0)[1]) //for head
                    board[coord[0]][coord[1]] = head;
                else
                    board[coord[0]][coord[1]] = sammyFilled;
            }
        }
        else {
            for (int[] coord : snake) {
                if (coord[0] == snake.get(0)[0] && coord[1] == snake.get(0)[1]) //for head
                    board[coord[0]][coord[1]] = head;
                else
                    board[coord[0]][coord[1]] = enemyFilled;
            }
        }
    }

    public void placeApple() {
        this.board[this.apple.position[0]][this.apple.position[1]] = " % ";
    }
}
