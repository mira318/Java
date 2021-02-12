package airdb.dao;

import airdb.JDBC.JDBCTemplate;
import airdb.entities.Seat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SeatDao {
    private final JDBCTemplate source;

    public SeatDao(JDBCTemplate source) {
        this.source = source;
    }

    private Seat createSeat(ResultSet result) throws SQLException {
        Seat seatFromDB = new Seat(
                result.getString("aircraft_code"),
                result.getString("seat_no"),
                result.getString("fare_conditions"));
        return seatFromDB;
    }

    public void saveSeats(Collection<Seat> seats) throws SQLException {
        source.preparedStatement("insert into seats (aircraft_code, seat_no, fare_conditions) " +
                "values (?, ?, ?)",
                newSeat -> {
            for(Seat currentSeat : seats) {
                newSeat.setString(1, currentSeat.getAircraftCode());
                newSeat.setString(2, currentSeat.getSeatNumber());
                newSeat.setString(3, currentSeat.getFareConditions());
                newSeat.execute();
            }
        });
    }

    public Set<Seat> getSeats() throws SQLException {
        return source.statement(sqlQvery -> {
            ResultSet resultSet = sqlQvery.executeQuery("select * from seats");
            System.out.println("resultSetSeats = " + resultSet);
            Set<Seat> resultSeatSet = new HashSet<>();
            while (resultSet.next()) {
                resultSeatSet.add(createSeat(resultSet));
            }
            return resultSeatSet;
        });
    }

}
