import java.io.BufferedReader;

import java.io.InputStreamReader;

public class PositionThread implements Runnable {
    Game s;
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public PositionThread(Game s) {
        this.s = s;
    }

    public void run() {
        while(true) {
            try {
                if (!br.ready()) {
                    try {
                        Thread.sleep(1000);
                        continue;
                    } catch (InterruptedException e) {
                        return;
                    }
                }
                int input= (int)br.read();
                switch (input) {
                    case 97:
                        if (s.getSnake().getDirection() != Direction.RIGHT) {
                            s.getSnake().setDirection(Direction.LEFT);
                        }
                        break;
                    case 100:
                        if (s.getSnake().getDirection() != Direction.LEFT) {
                            s.getSnake().setDirection(Direction.RIGHT);
                        }
                    case 113:
                    default:
                        break;
                    case 115:
                        if (s.getSnake().getDirection() != Direction.UP) {
                            s.getSnake().setDirection(Direction.DOWN);
                        }
                        break;
                    case 119:
                        if (s.getSnake().getDirection() != Direction.DOWN) {
                            s.getSnake().setDirection(Direction.UP);
                        }
                }
            } catch (Exception var2) {
                var2.printStackTrace();
            }
        }
    }
}
