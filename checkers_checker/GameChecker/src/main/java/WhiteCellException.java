public class WhiteCellException extends Exception{
    String message = "You trying to move to white cell it is forbidden by rules";
    public WhiteCellException(String message){
        super(message);
    }
}
