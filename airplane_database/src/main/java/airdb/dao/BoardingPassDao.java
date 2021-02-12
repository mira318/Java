package airdb.dao;

import airdb.JDBC.JDBCTemplate;
import airdb.entities.BoardingPass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class BoardingPassDao {
    private final JDBCTemplate source;

    public BoardingPassDao(JDBCTemplate source) {
        this.source = source;
    }

    private BoardingPass createBoardingPass(ResultSet result) throws SQLException {
        BoardingPass passFromDB = new BoardingPass(
                result.getString("ticket_no"),
                result.getInt("flight_id"),
                result.getInt("boarding_no"),
                result.getString("seat_no"));
        return passFromDB;
    }

    public void saveBoardingPasses(Collection<BoardingPass> passes) throws SQLException {
        source.preparedStatement("insert into boarding_passes (ticket_no, flight_id, boarding_no, seat_no)" +
                        " values (?, ?, ?, ?)",
                newPass -> {

            for(BoardingPass currentBoardingPass : passes) {
                newPass.setString(1, currentBoardingPass.getTicketNumber());
                newPass.setInt(2, currentBoardingPass.getFlightId());
                newPass.setInt(3, currentBoardingPass.getBoardingNumber());
                newPass.setString(4, currentBoardingPass.getSeatNumber());
                newPass.execute();
            }
        });
    }

    public Set<BoardingPass> getPasses() throws SQLException {
        return source.statement(sqlQvery -> {
            ResultSet resultSet = sqlQvery.executeQuery("select * from boarding_passes");
            Set<BoardingPass> resultPassSet = new HashSet<>();
            while (resultSet.next()) {
                resultPassSet.add(createBoardingPass(resultSet));
            }
            return resultPassSet;
        });
    }
}
