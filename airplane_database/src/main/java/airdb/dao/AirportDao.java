package airdb.dao;

import airdb.JDBC.JDBCTemplate;
import airdb.entities.Airport;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class AirportDao {
    private final JDBCTemplate source;

    public AirportDao(JDBCTemplate source) {
        this.source = source;
    }

    private Airport createAirport(ResultSet result) throws SQLException {
        Airport airportFromDB = new Airport(
                result.getString("airport_code"),
                result.getString("airport_name"),
                result.getString("city"),
                result.getDouble("coordinates_latitude"),
                result.getDouble("coordinates_longitude"),
                result.getString("timezone"));
        return airportFromDB;
    }

    private HashMap<String, String> createCityWithSeveralAirports(ResultSet result) throws SQLException {
        HashMap<String, String> parsedMap = new HashMap<>();
        while (result.next()) {
            String cityName = result.getString("city");
            String airportsList = result.getString("airports_list");
            parsedMap.put(cityName, airportsList);
        }
        return parsedMap;
    }


    public void saveAirports(Collection<Airport> airports) throws SQLException {
        source.preparedStatement("insert into airports (airport_code, airport_name, city, " +
                        "coordinates_latitude, coordinates_longitude, timezone) values (?, ?, ?, ?, ?, ?)",
                newAirport -> {
            for(Airport currentAirport : airports) {
                newAirport.setString(1, currentAirport.getCode());
                newAirport.setString(2, currentAirport.getName());
                newAirport.setString(3, currentAirport.getCity());
                newAirport.setDouble(4, currentAirport.getLatitude());
                newAirport.setDouble(5, currentAirport.getLongitude());
                newAirport.setString(6, currentAirport.getTimezone());
                newAirport.execute();
            }
        });
    }

    public Set<Airport> getAirports() throws SQLException {
        return source.statement(sqlQvery -> {
            ResultSet resultSet = sqlQvery.executeQuery("select * from airports");
            Set<Airport> resultAirportSet = new HashSet<>();
            while (resultSet.next()) {
                resultAirportSet.add(createAirport(resultSet));
            }
            return resultAirportSet;
        });
    }

    public HashMap<String, String> getCitiesWithSeveralAirports() throws SQLException {
        return source.statement(sqlQvery -> {
            ResultSet resultSet = sqlQvery.executeQuery("with number_in_city as (\n" +
                    "select city, count(*) from airports group by city having count(*) > 1\n" +
                    ")\n" +
                    "select city, array_agg(airport_code::TEXT) as airports_list from airports where city in " +
                    "(select city from number_in_city) group by city\n");
            return createCityWithSeveralAirports(resultSet);
        });
    }
}
