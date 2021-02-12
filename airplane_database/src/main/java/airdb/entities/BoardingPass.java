package airdb.entities;

public class BoardingPass {
    private final String ticketNumber;
    private final int flightId;
    private final int boardingNumber;
    private final String seatNumber;

    public BoardingPass(String ticketNumber, int flightId, int boardingNumber, String seatNumber) {
        this.ticketNumber = ticketNumber;
        this.flightId = flightId;
        this.boardingNumber = boardingNumber;
        this.seatNumber = seatNumber;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public int getFlightId() {
        return flightId;
    }

    public int getBoardingNumber() {
        return boardingNumber;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    @Override
    public String toString() {
        return "BoardingPass{" +
                "ticketNumber='" + ticketNumber + '\'' +
                ", flightId=" + flightId +
                ", boardingNumber=" + boardingNumber +
                ", seatNumber='" + seatNumber + '\'' +
                '}';
    }
}
