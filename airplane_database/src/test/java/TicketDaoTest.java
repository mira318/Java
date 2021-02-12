import airdb.JDBC.JDBCTemplate;
import airdb.dao.TicketDao;
import airdb.entities.Ticket;
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class TicketDaoTest {
    DataSource ConnectionPool = JdbcConnectionPool.create("jdbc:h2:mem:database;DB_CLOSE_DELAY=-1", "",
            "");
    JDBCTemplate source = new JDBCTemplate(ConnectionPool);
    private TicketDao dao = new TicketDao(source);

    @BeforeEach
    void intDB() throws ParseException, SQLException, IOException {
        Main.initializationFromFiles(ConnectionPool);
    }

    @AfterEach
    void shutDownDB() throws SQLException {
        source.statement(stmt ->{
            stmt.execute("drop all objects;");
        });
    }

    @Test
    void simpleNotEmptyTest() throws SQLException {
        Set<Ticket> allTicket = dao.getTickets();
        assertTrue(allTicket.isEmpty());
    }
}
