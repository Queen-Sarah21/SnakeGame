import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.*;

public class Game {

    static final Rectangle board = new Rectangle();
    private List<String[][]> gameSnapshots = new ArrayList<>();
    private Snake sammy = new Snake();
    private EnemySnake enemy = new EnemySnake();
    private long startTime;
    private boolean replaySnapshots = false;
    public void setReplaySnapshots(boolean replaySnapshots) {
        this.replaySnapshots = replaySnapshots;
    }

    public static final String DB_NAME = "snakeDB.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\database\\" + DB_NAME;

    public static final String TABLE_SNAPSHOTS = "snapshots";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DATA = "data";

    private static void saveListToDatabase(Connection connection, List<String[][]> list) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO " + TABLE_SNAPSHOTS +
                                                                            " (" + COLUMN_DATA + ") VALUES (?)")) {
            for (String[][] array : list) {
                // Convert String[][] to byte[] and store in BLOB column
                statement.setBytes(1, serializeArray(array));
                statement.executeUpdate();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<String[][]> retrieveListFromDatabase(Connection connection) throws SQLException, IOException, ClassNotFoundException {
        List<String[][]> retrievedList = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT " + COLUMN_DATA + " FROM " + TABLE_SNAPSHOTS)) {

            while (resultSet.next()) {
                // Convert byte[] from BLOB column to String[][]
                String[][] array = deserializeArray(resultSet.getBytes(COLUMN_DATA));
                retrievedList.add(array);
            }
        }

        return retrievedList;
    }

    private static byte[] serializeArray(String[][] array) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(array);
            return byteArrayOutputStream.toByteArray();
        }
    }

    private static String[][] deserializeArray(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            return (String[][]) objectInputStream.readObject();
        }
    }
    Thread positionThread;
    public Thread runnable = new Thread(new Runnable() {
        public void run() {

            try{
                Connection conn = DriverManager.getConnection(CONNECTION_STRING);
                Statement statement = conn.createStatement();



                new Scanner(System.in);
                Game.this.positionThread = Game.this.createNewPositionThread();
                Game.this.positionThread.start();

                startTime = System.currentTimeMillis();
                while(sammy.isAlive()) {
                    statement.execute("DROP TABLE IF EXISTS " + TABLE_SNAPSHOTS);

                    statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_SNAPSHOTS
                            + " (" + COLUMN_ID + " integer primary key autoincrement, "
                            + COLUMN_DATA + " blob"
                            + ")");

                    Game.clearConsole();
                    board.setRectangle();
                    board.placeSnake(sammy.body, true);
                    board.placeSnake(enemy.body, false);
                    board.placeApple();
                    board.displayRectangle();
                    sammy.eatApple(board);

                    if (!sammy.checkCollision(board) || !sammy.checkCollision(enemy)) {
                        saveListToDatabase(conn, gameSnapshots);
                        long endTime = System.currentTimeMillis();
                        System.out.println("Game Over!");
                        System.out.println("Score: " + Game.this.sammy.getScore()*5);
                        System.out.println("Your snake was "+Game.this.sammy.getLength() + " units long!");
                        System.out.println("You played for "+((endTime-startTime)/1000)+" seconds!");
                        sammy.setAlive(false);
                        gameSnapshots.clear();
                        Game.this.positionThread.interrupt();
                        Game.this.createNewDeadSnakeThread();
                        while(replaySnapshots){
                            List<String[][]> snapshots = retrieveListFromDatabase(conn);
                            replayLastGame(snapshots);
                            Game.this.createNewDeadSnakeThread();
                        }
                        if (sammy.isAlive()) {
                            Game.this.positionThread = Game.this.createNewPositionThread();
                            Game.this.positionThread.start();
                            sammy.resetSnake();
                            sammy.setDirection(Direction.RIGHT);
                        }
                    }
                    if (!enemy.checkCollision(board)){
                        enemy.resetSnake();
                    }

                    String[][] obj = new String[16][20];

                    for(int i=0;i<16;i++){
                        for(int j=0;j<20;j++){
                            obj[i][j] = board.getBoard()[i][j];
                        }
                    }
                    gameSnapshots.add(obj);

                    sammy.moveSnake();
                    enemy.moveSnake();
                    Thread.sleep(1000L);
                }
                conn.close();
                System.out.println("Thank you for playing");
            }catch(SQLException | InterruptedException e){
                System.out.println("Something went wrong: " + e.getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    });

    public Game() {
        String temp;
        System.out.println("\nWelcome To Our Snake Game!");
        System.out.println("\n++++++++++++++++++++++++ GAME RULES ++++++++++++++++++++++++\n");
        System.out.println("\tUse the W, A, S and D keys + Enter to move around!");
        System.out.println("\tW: UP");
        System.out.println("\tA: LEFT");
        System.out.println("\tS: DOWN");
        System.out.println("\tW: RIGHT");
        System.out.println("\tNote that YOUR snake is the one with '#' body!!");
        System.out.println("\tDon't get caught by the enemy snake!!");
        System.out.println("\nPress enter to start");
        Scanner scanner = new Scanner(System.in);
        temp = scanner.nextLine();
    }

    public static void clearConsole() {
        System.out.print("\u001bc");
    }

    public void createNewDeadSnakeThread() {
        DeadSnakeThread thread = new DeadSnakeThread(this);
        Thread thread1 = new Thread(thread);
        thread1.start();

        try {
            thread1.join();
        } catch (InterruptedException var4) {
            throw new RuntimeException(var4);
        }
    }

    public Thread createNewPositionThread() {
        PositionThread thread = new PositionThread(this);
        Thread thread1 = new Thread(thread);
        return thread1;
    }

    public void setStartTime(long time){this.startTime = time;}
    public  Snake getSnake() {
        return sammy;
    }
    public  EnemySnake getEnemy() {
        return enemy;
    }

    public void replayLastGame(List<String[][]> snapshots) throws InterruptedException {
        for (String[][] board : snapshots) {
            for(int i=0; i < 16; i++){
                for(int j=0; j < 20; j++){
                    System.out.print(board[i][j]);
                }
                System.out.println();
            }
            Thread.sleep(300);
        }
    }

}
