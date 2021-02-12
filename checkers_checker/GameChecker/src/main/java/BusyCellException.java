public class BusyCellException extends Exception {
    String message = "This cell is busy";
    public BusyCellException(String message){
        super(message);
    }
}
