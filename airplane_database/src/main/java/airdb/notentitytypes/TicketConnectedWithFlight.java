package airdb.notentitytypes;

import java.math.BigDecimal;

public class TicketConnectedWithFlight {
    private final String ticketId;
    private final int flightId;
    private final String conditions;
    private final BigDecimal flightCost;

    public TicketConnectedWithFlight(String ticketId, int flightId, String conditions, BigDecimal flightCost) {
        this.ticketId = ticketId;
        this.flightId = flightId;
        this.conditions = conditions;
        this.flightCost = flightCost;
    }

    public String getTicketId() {
        return ticketId;
    }

    public int getFlightId() {
        return flightId;
    }

    public String getConditions() {
        return conditions;
    }

    public BigDecimal getFlightCost() {
        return flightCost;
    }

    @Override
    public String toString() {
        return "TicketConnectedWithFlight{" +
                "ticketId='" + ticketId + '\'' +
                ", flightId=" + flightId +
                ", conditions='" + conditions + '\'' +
                ", flightCost=" + flightCost +
                '}';
    }
}
