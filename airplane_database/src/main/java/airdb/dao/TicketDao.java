package airdb.dao;

import airdb.JDBC.JDBCTemplate;
import airdb.entities.Ticket;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TicketDao {

    private final JDBCTemplate source;

    public TicketDao(JDBCTemplate source) {
        this.source = source;
    }

    private Ticket createTicket(ResultSet result) throws SQLException {
        Ticket ticketFromDB = new Ticket(
                result.getString("ticket_no"),
                result.getString("book_ref"),
                result.getString("passenger_id"),
                result.getString("passenger_name"),
                result.getString("contact_data"));
        return ticketFromDB;
    }

    public void saveTickets(Collection<Ticket> tickets) throws SQLException {
        source.preparedStatement("insert into tickets (ticket_no, book_ref, passenger_id, passenger_name," +
                        " contact_data) values (?, ?, ?, ?, ?)",
                newTicket -> {
            for(Ticket currentTicket : tickets) {
                newTicket.setString(1, currentTicket.getId());
                newTicket.setString(2, currentTicket.getBookingId());
                newTicket.setString(3, currentTicket.getPassengerId());
                newTicket.setString(4, currentTicket.getPassengerName());
                newTicket.setString(5, currentTicket.getContactData());
                newTicket.execute();
            }
        });
    }

    public Set<Ticket> getTickets() throws SQLException {
        return source.statement(sqlQvery -> {
            ResultSet resultSet = sqlQvery.executeQuery("select * from tickets");
            System.out.println("resultSetTickets = " + resultSet);
            Set<Ticket> resultTicketSet = new HashSet<>();
            while (resultSet.next()) {
                resultTicketSet.add(createTicket(resultSet));
            }
            return resultTicketSet;
        });
    }
}
