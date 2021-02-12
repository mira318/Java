package airdb.entities;

public class Ticket {
    private final String id;
    private final String bookingId;
    private final String passengerId;
    private final String passengerName;
    private final String contactData;

    public Ticket(String id, String bookingId, String passengerId, String passengerName, String contactData) {
        this.id = id;
        this.bookingId = bookingId;
        this.passengerId = passengerId;
        this.passengerName = passengerName;
        this.contactData = contactData;
    }

    public String getId() {
        return id;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public String getContactData() {
        return contactData;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id='" + id + '\'' +
                ", bookingId='" + bookingId + '\'' +
                ", passengerId='" + passengerId + '\'' +
                ", passengerName='" + passengerName + '\'' +
                ", contactData='" + contactData + '\'' +
                '}';
    }
}
