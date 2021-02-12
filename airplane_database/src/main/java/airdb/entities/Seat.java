package airdb.entities;

public class Seat {
    private final String aircraftCode;
    private final String seatNumber;
    private final String fareConditions;

    public Seat(String aircraftCode, String seatNumber, String fareConditions) {
        this.aircraftCode = aircraftCode;
        this.seatNumber = seatNumber;
        this.fareConditions = fareConditions;
    }

    public String getAircraftCode() {
        return aircraftCode;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public String getFareConditions() {
        return fareConditions;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "aircraftCode='" + aircraftCode + '\'' +
                ", seatNumber='" + seatNumber + '\'' +
                ", fareConditions='" + fareConditions + '\'' +
                '}';
    }
}
