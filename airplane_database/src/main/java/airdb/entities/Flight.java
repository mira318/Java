package airdb.entities;


import java.util.Date;

public class Flight {
    private final long id;
    private final String flightNumber;
    private final Date scheduledDeparture;
    private final Date scheduledArrival;
    private final String departureAirport;
    private final String arrivalAirport;
    private final String status;
    private final String aircraftCode;
    private final Date actualDeparture;
    private final Date actualArrival;

    public Flight(long id, String flightNumber, Date scheduledDeparture, Date scheduledArrival, String departureAirport,
                  String arrivalAirport, String status, String aircraftCode, Date actualDeparture, Date actualArrival) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.scheduledDeparture = scheduledDeparture;
        this.scheduledArrival = scheduledArrival;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.status = status;
        this.aircraftCode = aircraftCode;
        this.actualDeparture = actualDeparture;
        this.actualArrival = actualArrival;
    }

    public long getId() {
        return id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public Date getScheduledDeparture() {
        return scheduledDeparture;
    }

    public Date getScheduledArrival() {
        return scheduledArrival;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public String getStatus() {
        return status;
    }

    public String getAircraftCode() {
        return aircraftCode;
    }

    public Date getActualDeparture() {
        return actualDeparture;
    }

    public Date getActualArrival() {
        return actualArrival;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "id=" + id +
                ", flightNumber='" + flightNumber + '\'' +
                ", scheduledDeparture=" + scheduledDeparture +
                ", scheduledArrival=" + scheduledArrival +
                ", departureAirport='" + departureAirport + '\'' +
                ", arrivalAirport='" + arrivalAirport + '\'' +
                ", status='" + status + '\'' +
                ", aircraftCode='" + aircraftCode + '\'' +
                ", actualDeparture=" + actualDeparture +
                ", actualArrival=" + actualArrival +
                '}';
    }
}
