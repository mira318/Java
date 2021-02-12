package airdb.dao;

import airdb.JDBC.JDBCTemplate;
import airdb.entities.Booking;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class BookingDao {
    private final JDBCTemplate source;

    public BookingDao(JDBCTemplate source) {
        this.source = source;
    }

    private Booking createBooking(ResultSet result) throws SQLException {
        Booking bookingFromDB = new Booking(
                result.getString("book_ref"),
                result.getDate("book_date"),
                result.getBigDecimal("total_amount"));
        return bookingFromDB;
    }

    public void saveBookings(Collection<Booking> bookings) throws SQLException {
        source.preparedStatement("insert into bookings (book_ref, book_date, total_amount) " +
                        "values (?, ?, ?)",
                newBooking -> {
                    //FileWriter toH2 = new FileWriter("output2.sql");
                    //SimpleDateFormat toParseToH2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


                    for(Booking currentBooking : bookings) {
                newBooking.setString(1, currentBooking.getId());
                newBooking.setTimestamp(2, new Timestamp(currentBooking.getBookingDate().getTime()));
                newBooking.setBigDecimal(3, currentBooking.getSumCost());
                newBooking.execute();
                //toH2.append("insert into bookings (book_ref, book_date, total_amount)  values ('" +
                  //      currentBooking.getId() + "', TIMESTAMP '" +
                    //    toParseToH2.format(currentBooking.getBookingDate()) + "', " +
                      //  currentBooking.getSumCost() + ");\n");

                    }
                    //toH2.close();
        });
    }

    public Set<Booking> getBookings() throws SQLException {
        return source.statement(sqlQvery -> {
            ResultSet resultSet = sqlQvery.executeQuery("select * from bookings");
            System.out.println("resultSetBookings = " + resultSet);
            Set<Booking> resultBookingSet = new HashSet<>();
            while (resultSet.next()) {
                resultBookingSet.add(createBooking(resultSet));
            }
            return resultBookingSet;
        });
    }
}
