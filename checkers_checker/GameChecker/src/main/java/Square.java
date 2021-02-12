/**
 * Class to save a square from the game board. Stores information about current cell.
 */
public class Square {
    boolean isFull;
    final int string;
    final int column;
    CheckItem currentCheck;

    /**
     * Square constructor
     * @param string
     * @param column - coordinates of the square on the game board
     */
    Square(int string, int column) {
        isFull = false;
        this.string = string;
        this.column = column;
    }

    /**
     * Method to set an Item inside the Square
     * @param itemInSquare
     */
    void addItem(CheckItem itemInSquare) {
        currentCheck = itemInSquare;
        isFull = true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Square{isFull=").append(isFull).append("string = ").append(string);
        sb.append(", column = ").append(column).append(", currentCheck=");
        if(currentCheck != null){
            sb.append(currentCheck.toString());
        } else {
            sb.append("null");
        }
        sb.append("}");
        return sb.toString();
    }
}
