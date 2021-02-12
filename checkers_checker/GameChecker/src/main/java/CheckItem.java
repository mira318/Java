import java.util.Objects;

/**
 * class for one check. All checks used by game have this type. Stores current position color and boolean that shows,
 *  is it king, or not. Maybe null in some cases.
 */
public class CheckItem {
    Color checkColor;
    int xPos = 0;
    int yPos = 0;
    boolean isKing = false;

    /**
     * CheckItem constructor
     * @param checkColor
     * @param xPos
     * @param yPos
     * use it when the check is not king
     */
    CheckItem(Color checkColor, int xPos, int yPos) {
        this.checkColor = checkColor;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    /**
     * Second CheckItem constructor
     * @param checkColor
     * @param xPos
     * @param yPos
     * @param isKing
     * You may use it whether the check is king or not. Just write the last argument according to this information.
     */
    CheckItem(Color checkColor, int xPos, int yPos, boolean isKing) {
        this.checkColor = checkColor;
        this.xPos = xPos;
        this.yPos = yPos;
        this.isKing = isKing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckItem checkItem = (CheckItem) o;
        return xPos == checkItem.xPos &&
                yPos == checkItem.yPos &&
                isKing == checkItem.isKing &&
                checkColor == checkItem.checkColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(checkColor, xPos, yPos, isKing);
    }

    @Override
    public String toString() {
        return "CheckItem{" +
                "checkColor=" + checkColor +
                ", xPos=" + xPos +
                ", yPos=" + yPos +
                ", isKing=" + isKing +
                '}';
    }
}
