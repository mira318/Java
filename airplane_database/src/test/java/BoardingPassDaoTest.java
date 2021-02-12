import airdb.JDBC.JDBCTemplate;
import airdb.dao.BoardingPassDao;
import airdb.entities.BoardingPass;
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

public class BoardingPassDaoTest {

    DataSource ConnectionPool = JdbcConnectionPool.create("jdbc:h2:mem:database;DB_CLOSE_DELAY=-1", "",
            "");
    JDBCTemplate source = new JDBCTemplate(ConnectionPool);
    private BoardingPassDao dao = new BoardingPassDao(source);

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
        Set<BoardingPass> allPasses = dao.getPasses();
        assertTrue(allPasses.isEmpty());
    }
}

