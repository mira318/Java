package airdb.dao;

import airdb.JDBC.JDBCTemplate;
import airdb.entities.Aircraft;

import java.sql.ResultSet;
import java.util.Collection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class AircraftDao {
    private final JDBCTemplate source;

    public AircraftDao(JDBCTemplate source) {
        this.source = source;
    }

    private Aircraft createAircraft(ResultSet result) throws SQLException {
        Aircraft aircraftFromDB = new Aircraft(
                result.getString("aircraft_code"),
                result.getString("model"),
                result.getInt("range"));
        return aircraftFromDB;
    }

    public void saveAircrafts(Collection<Aircraft> aircrafts) throws SQLException {
        source.preparedStatement("insert into aircrafts (aircraft_code, model, range) values (?, ?, ?)",
                newAircraft -> {
            for(Aircraft currentAircraft : aircrafts) {
                newAircraft.setString(1, currentAircraft.getCode());
                newAircraft.setString(2, currentAircraft.getDesc());
                newAircraft.setInt(3, currentAircraft.getRange());
                newAircraft.execute();
            }
        });
    }

    public Set<Aircraft> getAircrafts() throws SQLException {
        return source.statement(sqlQvery -> {
            ResultSet resultSet = sqlQvery.executeQuery("select * from aircrafts");
            Set<Aircraft> resultAircraftSet = new HashSet<>();
            while (resultSet.next()) {
                resultAircraftSet.add(createAircraft(resultSet));
            }
            return resultAircraftSet;
        });
    }

}
