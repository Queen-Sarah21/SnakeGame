import java.util.Scanner;

public class DeadSnakeThread implements Runnable{
    Game s;
    int option;
    public DeadSnakeThread(Game s){
        this.s= s;
    }
    public void run() {
        {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Choose one of the following options:");
            System.out.println("\t1 - Play again");
            System.out.println("\t2 - Replay recording of last game");
            System.out.println("\tOther - Quit");
            System.out.println("Type the chosen option: ");
            try{
                option = scanner.nextInt();
            }
            catch (Exception e){
            }

            if (option == 2){
                s.setReplaySnapshots(true);
            }
            else if (option == 1){
                s.setStartTime(System.currentTimeMillis());
                s.getSnake().setAlive(true);
                s.getSnake().resetSnake();
                s.getEnemy().resetSnake();
                s.setReplaySnapshots(false);
            }
            else {
                s.setReplaySnapshots(false);
                s.getSnake().setAlive(false);
            }
        }
    }
}
