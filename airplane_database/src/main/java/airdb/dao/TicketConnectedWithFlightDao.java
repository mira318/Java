package airdb.dao;

import airdb.JDBC.JDBCTemplate;
import airdb.notentitytypes.TicketConnectedWithFlight;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TicketConnectedWithFlightDao {
    private final JDBCTemplate source;

    public TicketConnectedWithFlightDao(JDBCTemplate source) {
        this.source = source;
    }

    private TicketConnectedWithFlight createTicketConnectedWithFlight(ResultSet result) throws SQLException {
        TicketConnectedWithFlight connectionFromDB = new TicketConnectedWithFlight(
                result.getString("ticket_no"),
                result.getInt("flight_id"),
                result.getString("fare_conditions"),
                result.getBigDecimal("amount"));
        return connectionFromDB;
    }
    public void saveConnections(Collection<TicketConnectedWithFlight> connections) throws SQLException {
        source.preparedStatement("insert into ticket_flights (ticket_no, flight_id, fare_conditions, amount)" +
                        " values (?, ?, ?, ?)",
                newConnection -> {
            for(TicketConnectedWithFlight currentConnection : connections) {
                newConnection.setString(1, currentConnection.getTicketId());
                newConnection.setInt(2, currentConnection.getFlightId());
                newConnection.setString(3, currentConnection.getConditions());
                newConnection.setBigDecimal(4, currentConnection.getFlightCost());
                newConnection.execute();
            }
        });
    }

    public Set<TicketConnectedWithFlight> getConnections() throws SQLException {
        return source.statement(sqlQvery -> {
            ResultSet resultSet = sqlQvery.executeQuery("select * from ticket_flights");
            System.out.println("resultSetConnections = " + resultSet);
            Set<TicketConnectedWithFlight> resultConnectionSet = new HashSet<>();
            while (resultSet.next()) {
                resultConnectionSet.add(createTicketConnectedWithFlight(resultSet));
            }
            return resultConnectionSet;
        });
    }
}
