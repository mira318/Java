package airdb.entities;

import java.math.BigDecimal;
import java.util.Date;

public class Booking {
    private final String id;
    private  final Date bookingDate;
    private final BigDecimal sumCost;

    public Booking(String id, Date bookingDate, BigDecimal sumCost) {
        this.id = id;
        this.bookingDate = bookingDate;
        this.sumCost = sumCost;
    }

    public String getId() {
        return id;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public BigDecimal getSumCost() {
        return sumCost;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id='" + id + '\'' +
                ", bookingDate=" + bookingDate +
                ", sumCost=" + sumCost +
                '}';
    }
}
