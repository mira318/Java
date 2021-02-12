import java.util.Scanner;

public class GameChecker {

    public static void main(String[] args) throws WhiteCellException, BusyCellException, InvalidMoveException {
        Game game = new Game();
        try(Scanner checkScan = new Scanner(System.in)) {
            game.init(checkScan);
            try {
                while(checkScan.hasNext()){
                    game.nextMoveForBothColors(checkScan);
                }
                game.writeResult();
            } catch (BusyCellException | WhiteCellException | InvalidMoveException e) {
                e.printStackTrace();
            }
        }
    }
}
