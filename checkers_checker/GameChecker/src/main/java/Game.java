import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* The class to solve the whole problem.
* It is doing anything: takes start positions, generating checks, parsing input moves, moving checks according
* to them, throws exceptions if necessary and prints the result if we have got it.
 */
public class Game {
    private final static int boardSize = 8;
    private final static int masksNumber = 4;
    private final static Pattern posPattern = Pattern.compile("[a-hA-H][1-8]");
    private final static int[][] masks = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

    private Square[][] cells;

    /**
     * Game constructor. Just prepare places for checks, don't need any input.
     */
    public Game() {
        cells = new Square[boardSize][boardSize];
        for(int i = 0; i < boardSize; ++i) {
            for(int j = 0; j < boardSize; ++j) {
                cells[i][j] = new Square(i, j);
            }
        }
    }

    private static int getColumnFromSymbol(char t) {
        if (t >= 'A' && t <= 'H') {
            return (int)t - 'A';
        } else {
            return (int)t - 'a';
        }
    }
    private void addPositionsFromString(Color color, String stringWithPositions) {
        Matcher m = posPattern.matcher(stringWithPositions);
        int i;
        int posColumn, posString;
        boolean isKing;
        while(m.find()) {
            i = m.start();
            posColumn = getColumnFromSymbol(stringWithPositions.toCharArray()[i]);
            posString = stringWithPositions.toCharArray()[i + 1] - '0' - 1;
            isKing = stringWithPositions.toCharArray()[i] >= 'A' && stringWithPositions.toCharArray()[i] <= 'H';
            CheckItem itemInSquare;
            itemInSquare = new CheckItem(color, posString, posColumn, isKing);
            cells[posString][posColumn].addItem(itemInSquare);
        }

    }

    /**
     * Initialization function:
     * @param checkScan - a Scanner for input stream(file, stdin, etc...) reads white and black positions and sets inner
     * variables of class Game according to input. We believe that input string is correct and don't throw exceptions
     * in this place.
     */
    public void init(Scanner checkScan) {
        String whitePositions = checkScan.nextLine();
        addPositionsFromString(Color.white, whitePositions);
        String blackPositions = checkScan.nextLine();
        addPositionsFromString(Color.black, blackPositions);
    }

    private int getMask(int a, int b) {
        if(a > b) {
            return -1;
        } else {
            return 1;
        }
    }

    private void continueWay(CheckItem startedFrom, int wasString, int wasColumn, int differString, int differColumn,
                             ArrayList<Square> forMoves) {
        boolean meetSomeoneAgain = false;
        int curString = wasString + differString;
        int curColumn = wasColumn + differColumn;
        while (curString < boardSize && curString >= 0 && curColumn < boardSize && curColumn >= 0 && !meetSomeoneAgain) {
            if (!cells[curString][curColumn].isFull) {
                forMoves.add(cells[curString][curColumn]);
                curString += differString;
                curColumn += differColumn;
            } else {
                meetSomeoneAgain = true;
            }
        }
    }

    private boolean isBiting(CheckItem startItem, ArrayList<Square> forMoves) {
        //System.out.println("generating for " + startItem);
        if(startItem == null) {
            return false;
        }
        int curString;
        int curColumn;
        int nextString;
        int nextColumn;
        boolean meetSomeone;
        int isKingChecker;
        boolean foundFighter = false;

        forMoves.clear();
        for (int i = 0; i < masksNumber; ++i) {
            curString = startItem.xPos + masks[i][0];
            curColumn = startItem.yPos + masks[i][1];
            meetSomeone = false;
            if(startItem.isKing) {
                isKingChecker = 8;
            } else {
                isKingChecker = 1;
            }
            while (curString < boardSize && curString >= 0 && curColumn < boardSize && curColumn >= 0 && !meetSomeone
                    && isKingChecker > 0) {
                if (cells[curString][curColumn].isFull) {
                    meetSomeone = true;
                    CheckItem someone = cells[curString][curColumn].currentCheck;
                    //System.out.println("meet " + cells[curString][curColumn] + "\n");
                    if (someone != null && !someone.checkColor.equals(startItem.checkColor)) {
                        nextString = curString + masks[i][0];
                        nextColumn = curColumn + masks[i][1];
                        if (nextString < boardSize && nextString >= 0 && nextColumn < boardSize && nextColumn >= 0 &&
                                !cells[nextString][nextColumn].isFull) {
                            if (!foundFighter) {
                                foundFighter = true;
                                forMoves.clear();
                            }
                            forMoves.add(cells[nextString][nextColumn]);
                            if(startItem.isKing) {
                                continueWay(startItem, nextString, nextColumn, masks[i][0], masks[i][1], forMoves);
                            }
                        }
                    }
                } else {
                    if (!foundFighter) {
                        forMoves.add(cells[curString][curColumn]);
                    }
                }
                curString = curString + masks[i][0];
                curColumn = curColumn + masks[i][1];
                isKingChecker--;
            }
        }
        return foundFighter;
    }

    private  boolean generateAllAvailableMoves(Color color, HashMap<CheckItem, ArrayList<Square> > moves) {
        boolean haveToBit = false;
        boolean obligatoryBiting = false;
        for(int i = 0; i < boardSize; ++i) {
            for(int j = 0; j < boardSize; ++j) {
                if(cells[i][j].isFull && cells[i][j].currentCheck.checkColor.equals(color)) {
                    CheckItem startItem = cells[i][j].currentCheck;
                    ArrayList<Square> currentItemMoves = new ArrayList<Square>();
                    haveToBit = isBiting(startItem, currentItemMoves);
                    if(haveToBit) {
                        if(!obligatoryBiting) {
                            obligatoryBiting = true;
                            moves.clear();
                        }
                        moves.put(startItem, currentItemMoves);
                    } else if(!obligatoryBiting) {
                        moves.put(startItem, currentItemMoves);
                    }
                }
            }
        }
        return obligatoryBiting;
    }

    private CheckItem nextGoTo(ArrayList<Square> victims, Square from, Square to, Color color,
                               ArrayList<Square> possibleMoves, Boolean obligatoryBiting) throws InvalidMoveException,
            WhiteCellException, BusyCellException {
        //System.out.println("nextGoTo: from = " + from + ", to = " + to + "\n");
        //System.out.println("possibleMoves = " + possibleMoves + "\n");
        if(to.isFull) {
            throw new BusyCellException("The cell you are going to is busy\n");
        }
        if((to.string + to.column) % 2 == 1) {
            throw new WhiteCellException("white cell\n");
        }
        if(possibleMoves != null && possibleMoves.contains(to)) {
            to.currentCheck = from.currentCheck;
            to.isFull = true;
            from.isFull = false;
            from.currentCheck = null;
            to.currentCheck.xPos = to.string;
            to.currentCheck.yPos = to.column;
            if((color.equals(Color.white) && to.string == 7) || (color.equals(Color.black) && to.string == 0)) {
                to.currentCheck.isKing = true;
            }
            if(obligatoryBiting) {
                int diffString = getMask(from.string, to.string);
                int diffColumn = getMask(from.column, to.column);
                int midString = from.string;
                int midColumn = from.column;
                midString += diffString;
                midColumn += diffColumn;
                while(midString < boardSize && midString >= 0 && midColumn < boardSize && midColumn >= 0) {
                    if(cells[midString][midColumn].isFull) {
                        cells[midString][midColumn].currentCheck = null;
                        victims.add(cells[midString][midColumn]);
                        return to.currentCheck;
                    }
                    midString += diffString;
                    midColumn += diffColumn;
                }
            } else {
                return null;
            }
        } else {
            if(obligatoryBiting) {
                throw new InvalidMoveException("Biting is obligatory\n");
            } else {
                throw new InvalidMoveException("It is too far\n");
            }
        }
        return null;
    }

    private void nextMoveOneColor(String moveLine, Matcher m, Color color) throws InvalidMoveException,
            WhiteCellException, BusyCellException {
        boolean obligatoryBiting = false;
        HashMap<CheckItem, ArrayList<Square> > startMoves = new HashMap<CheckItem, ArrayList<Square>>();
        obligatoryBiting = generateAllAvailableMoves(color, startMoves);
        boolean foundend = false;
        int i;
        int posString, posColumn;
        int startString, startColumn;
        if(m.find()) {
            i = m.start();
            startColumn = getColumnFromSymbol(moveLine.toCharArray()[i]);
            startString = (int)moveLine.toCharArray()[i + 1] - '0' - 1;
            if((startString + startColumn) % 2 == 1) {
                throw new WhiteCellException("white cell");
            }
            if(!cells[startString][startColumn].isFull) {
                throw new InvalidMoveException("You start from an empty cell\n");
            }
            CheckItem startItem = cells[startString][startColumn].currentCheck;
            if(moveLine.toCharArray()[i + 2] == ' ') {
                throw new IllegalArgumentException();
            }
            ArrayList<Square> victims = new ArrayList<Square>();
            ArrayList<Square> possibleMoves = startMoves.get(startItem);
            while(!foundend && startItem != null) {
                if(m.find()){
                    i = m.start();
                    posColumn = getColumnFromSymbol(moveLine.toCharArray()[i]);
                    posString = (int)moveLine.toCharArray()[i + 1] - '0' - 1;
                    startItem = nextGoTo(victims, cells[startItem.xPos][startItem.yPos], cells[posString][posColumn],
                            color, possibleMoves, obligatoryBiting);
                    if(moveLine.toCharArray().length <= i + 2 || moveLine.toCharArray()[i + 2] == ' ' ||
                            moveLine.toCharArray()[i + 2] == '\n') {
                        foundend = true;
                    }
                    obligatoryBiting = isBiting(startItem, possibleMoves);
                } else {
                    throw new IllegalArgumentException();
                }
            }
            if(obligatoryBiting) {
                throw new InvalidMoveException("Biting is obligatory");
            }
            if(!foundend) {
                throw new IllegalArgumentException("You should stop, but you continue");
            }
            //System.out.println(victims);
            for(Square q : victims) {
                q.isFull = false;
            }
        } else {
            throw new IllegalArgumentException();
        }

    }

    public void nextMoveForBothColors(Scanner checkScan) throws BusyCellException, WhiteCellException,
            InvalidMoveException, IllegalArgumentException {
        String moveLine = checkScan.nextLine();
        Matcher m = posPattern.matcher(moveLine);
        nextMoveOneColor(moveLine, m, Color.white);
        nextMoveOneColor(moveLine, m, Color.black);
    }

    /**
     * Method to present the result properly: first string with white positions, than string with black positions;
     * if checks have the same color, the are in one string and king is going first; if they similar in both ways,
     * uses the alphabet order. Don't need any additional information, except the condition of the Game
     */
    public void writeResult(){
        StringBuilder sbWhiteSimple = new StringBuilder();
        StringBuilder sbBlackSimple = new StringBuilder();
        StringBuilder sbWhiteKing = new StringBuilder();
        StringBuilder sbBlackKing = new StringBuilder();
        for(int i = 0; i < boardSize; ++i) {
            for(int j = 0; j < boardSize; ++j) {
                if(cells[j][i].isFull) {
                    CheckItem currentItem = cells[j][i].currentCheck;
                    if(Color.white.equals(currentItem.checkColor)) {
                        if(currentItem.isKing)
                        {
                            sbWhiteKing.append((char)(i + 'A')).append(j + 1).append(" ");
                        } else {
                            sbWhiteSimple.append((char)(i + 'a')).append(j + 1).append(" ");
                        }
                    } else {
                        if(currentItem.isKing)
                        {
                            sbBlackKing.append((char)(i + 'A')).append(j + 1).append(" ");
                        } else {
                            sbBlackSimple.append((char)(i + 'a')).append(j + 1).append(" ");
                        }
                    }
                }
            }
        }
        System.out.println(sbWhiteKing.toString() + sbWhiteSimple.toString());
        System.out.println(sbBlackKing.toString() + sbBlackSimple.toString());
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Board{" + "cells=");
        for(int i = 0; i < boardSize; ++i) {
            for(int j = 0; j < boardSize; ++j) {
                sb.append(cells[i][j].toString()).append("\n");
            }
        }
        sb.append('}');
        return sb.toString();
    }
}
