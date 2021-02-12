package airdb.dao;

import airdb.JDBC.JDBCTemplate;
import airdb.entities.Flight;
import airdb.notentitytypes.AverageTimeQueryResult;
import airdb.notentitytypes.IntPair;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class FlightDao {
    private final JDBCTemplate source;

    public FlightDao(JDBCTemplate source) {
        this.source = source;
    }

    private Flight createFlight(ResultSet result) throws SQLException {
        Flight flightFromDB = new Flight(
                result.getLong("flight_id"),
                result.getString("flight_no"),
                result.getTimestamp("scheduled_departure"),
                result.getTimestamp("scheduled_arrival"),
                result.getString("departure_airport"),
                result.getString("arrival_airport"),
                result.getString("status"),
                result.getString("aircraft_code"),
                result.getTimestamp("actual_departure"),
                result.getTimestamp("actual_arrival"));
        return flightFromDB;
    }

    private LinkedHashMap<String, Integer> convertCancelledFlights(ResultSet result) throws SQLException {
        LinkedHashMap<String, Integer> parsedResult = new LinkedHashMap<>();
        while (result.next()) {
            String currentCity = result.getString("city");
            Integer cancelledNumber = result.getInt("cancelled_number");
            parsedResult.put(currentCity, cancelledNumber);
        }
        return parsedResult;
    }

    private AverageTimeQueryResult createAverageTimeQueryResult(ResultSet resultSet) throws SQLException {
        AverageTimeQueryResult queryResult = new AverageTimeQueryResult(
                resultSet.getString("start_city"),
                resultSet.getString("end_city"),
                resultSet.getString("end_airport"),
                resultSet.getDouble("avg_duritation_in_seconds"));
        return queryResult;
    }

    private LinkedHashMap<IntPair, BigDecimal> createCovidQueryResult(ResultSet res) throws SQLException {
        LinkedHashMap<IntPair, BigDecimal> parsed = new LinkedHashMap<>();
        while (res.next()) {
            int day = res.getInt("day");
            int month = res.getInt("month");
            BigDecimal loss = res.getBigDecimal("amount_a_day");
            parsed.put(new IntPair(day, month), loss);
        }
        return parsed;
    }

    public void saveFlights(Collection<Flight> flights) throws SQLException {
        source.preparedStatement("insert into flights (flight_id, flight_no, scheduled_departure, " +
                        "scheduled_arrival, departure_airport, arrival_airport, status, aircraft_code, " +
                        "actual_departure, actual_arrival) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                newFlight -> {
            for (Flight currentFlight : flights) {
                newFlight.setLong(1, currentFlight.getId());
                newFlight.setString(2, currentFlight.getFlightNumber());
                newFlight.setTimestamp(3, new Timestamp(currentFlight.getScheduledDeparture().getTime()));
                newFlight.setTimestamp(4, new Timestamp(currentFlight.getScheduledArrival().getTime()));
                newFlight.setString(5, currentFlight.getDepartureAirport());
                newFlight.setString(6, currentFlight.getArrivalAirport());
                newFlight.setString(7, currentFlight.getStatus());
                newFlight.setString(8, currentFlight.getAircraftCode());
                if (currentFlight.getActualDeparture() == null)
                    newFlight.setNull(9, Types.TIMESTAMP);
                else
                    newFlight.setTimestamp(9, new Timestamp(currentFlight.getActualDeparture().getTime()));
                if (currentFlight.getActualArrival() == null)
                    newFlight.setNull(10, Types.TIMESTAMP);
                else
                    newFlight.setTimestamp(10, new Timestamp(currentFlight.getActualArrival().getTime()));
                newFlight.execute();

            }
        });
    }

    public Set<Flight> getFlights() throws SQLException {
        return source.statement(sqlQvery -> {
            ResultSet resultSet = sqlQvery.executeQuery("select * from flights limit");
            Set<Flight> resultFlightSet = new HashSet<>();
            while (resultSet.next()) {
                resultFlightSet.add(createFlight(resultSet));
            }
            return resultFlightSet;
        });
    }

    public Set<Flight> getFlightsLimited() throws SQLException {
        return source.statement(sqlQvery -> {
            ResultSet resultSet = sqlQvery.executeQuery("select * from flights limit 100");
            Set<Flight> resultFlightSet = new HashSet<>();
            while (resultSet.next()) {
                resultFlightSet.add(createFlight(resultSet));
            }
            return resultFlightSet;
        });
    }

    public LinkedHashMap<String, Integer> getCancelledFlights() throws SQLException {
        return source.statement(sqlQuery -> {
            ResultSet resultSet = sqlQuery.executeQuery("select city, count(*) as cancelled_number \n" +
                    "from flights, airports \n" +
                    "where flights.departure_airport = airports.airport_code and status = 'Cancelled'\n" +
                    "group by city \n" +
                    "having count(*) > 5\n" +
                    "order by cancelled_number desc");
            return convertCancelledFlights(resultSet);
        });
    }

    public LinkedHashSet<AverageTimeQueryResult> getShortestRoutes() throws SQLException {
        return source.statement(sqlQuery -> {
            LinkedHashSet<AverageTimeQueryResult> queryResult = new LinkedHashSet<>();
            ResultSet resultSet = sqlQuery.executeQuery("with finished_flights as ( \n" +
                    "    select city, actual_departure, actual_arrival, arrival_airport as airport_to, \n" +
                    "    extract(epoch from flights.actual_arrival - flights.actual_departure) as duritation\n" +
                    "    from flights, airports\n" +
                    "    where airports.airport_code = flights.departure_airport and actual_departure is not null " +
                    "    and actual_arrival is not null\n" +
                    ")\n" +
                    "\n" +
                    "select finished_flights.city as start_city, airports.city as end_city, " +
                    "airport_to as end_airport, avg(duritation) as avg_duritation_in_seconds \n" +
                    "from finished_flights, airports\n" +
                    "where airports.airport_code = airport_to\n" +
                    "group by finished_flights.city, airport_to \n" +
                    "having avg(duritation) <= 2000\n" +
                    "order by avg_duritation_in_seconds\n");
            while (resultSet.next()) {
                queryResult.add(createAverageTimeQueryResult(resultSet));
            }
            return queryResult;
        });
    }

    public HashMap<Integer, Integer> getFlightsCancelledByMonth() throws SQLException {
        return source.statement(sqlQuery -> {
            HashMap<Integer, Integer> parsedResult = new HashMap<>();
            ResultSet result = sqlQuery.executeQuery("select extract(month from scheduled_departure) as month, " +
                    "count(*) as flights_cancelled from flights where status = 'Cancelled' " +
                    "group by extract(month from scheduled_departure)");
            while (result.next()) {
                Integer month = result.getInt("month");
                Integer numberOfCancelledFlights = result.getInt("flights_cancelled");
                parsedResult.put(month, numberOfCancelledFlights);
            }
            return parsedResult;
        });
    }

    public LinkedHashMap<IntPair, Integer> getFlightsFromToMoscow() throws SQLException {
        return source.statement(sqlQuery -> {
            LinkedHashMap<IntPair, Integer> queryRes = new LinkedHashMap<>();
            ResultSet result = sqlQuery.executeQuery("with moscow_airports as (\n" +
                    "select airport_code from airports\n" +
                    "where city like '%Moscow%'\n" +
                    ")\n" +
                    "select count(*) as number_of_flights, \n" +
                    "extract(dow from scheduled_departure) as day_of_week, 1 as from_moscow\n" +
                    "from flights\n" +
                    "where departure_airport in (select * from moscow_airports)\n" +
                    "group by extract(dow from scheduled_departure)\n" +
                    "union\n" +
                    "select count(*) as number_of_flights,\n" +
                    "extract(dow from scheduled_departure) as day_of_week, 0 as from_moscow\n" +
                    "from flights\n" +
                    "where arrival_airport in (select * from moscow_airports)\n" +
                    "group by extract(dow from scheduled_departure)\n");
            while(result.next()) {
                int numberOfFlights = result.getInt("number_of_flights");
                int dayOfWeek = result.getInt("day_of_week");
                int isItFromMoscow = result.getInt("from_moscow");
                queryRes.put(new IntPair(dayOfWeek, isItFromMoscow), numberOfFlights);
            }
            return queryRes;
        });
    }

    public void deleteBadAircraft(DataSource ConnectionPool, String aircraftCode) throws SQLException {
        Connection conn = ConnectionPool.getConnection();
        conn.setAutoCommit(false);
        Statement stmt = conn.createStatement();
        stmt.execute("drop table if exists bad_tickets;");
        PreparedStatement prepstmt = conn.prepareStatement(
                " create table bad_tickets as " +
                "(select ticket_flights.ticket_no, book_ref " +
                "from flights, ticket_flights, tickets " +
                "where tickets.ticket_no = ticket_flights.ticket_no and flights.flight_id = ticket_flights.flight_id " +
                "and flights.aircraft_code = ?);");
        prepstmt.setString(1, aircraftCode);
        prepstmt.executeUpdate();
        prepstmt = conn.prepareStatement(
                "delete from boarding_passes where flight_id in " +
                        "(select flight_id from flights where aircraft_code = ?) " +
                        "or ticket_no in (select ticket_no from bad_tickets);");
        prepstmt.setString(1, aircraftCode);
        prepstmt.executeUpdate();
        prepstmt = conn.prepareStatement("delete from ticket_flights where flight_id in " +
                "(select flight_id from flights where aircraft_code = ?)" +
                " or ticket_no in (select ticket_no from bad_tickets);");
        prepstmt.setString(1, aircraftCode);
        prepstmt.executeUpdate();
        prepstmt = conn.prepareStatement("delete from tickets where ticket_no in " +
                "(select ticket_no from bad_tickets); " +
                "delete from flights where aircraft_code = ?;");
        prepstmt.setString(1, aircraftCode);
        prepstmt.executeUpdate();
        stmt.execute("drop table bad_tickets");
        conn.commit();
        conn.close();
    }

    public LinkedHashMap<IntPair, BigDecimal> covidCancell(DataSource ConnectionPool, Date startDate, Date endDate)
            throws SQLException {
        Connection conn = ConnectionPool.getConnection();
        conn.setAutoCommit(false);
        Statement stmt = conn.createStatement();
        stmt.execute("drop table if exists moscow_airports;");
        stmt.execute("create table moscow_airports as " +
                "(select airport_code from airports where city like '%Moscow%');");

        PreparedStatement prepstmt = conn.prepareStatement("update flights set status = 'Cancelled' " +
                "where (departure_airport in (select * from moscow_airports) " +
                "or arrival_airport in (select * from moscow_airports)) " +
                "and (scheduled_departure between ? and ?);");
        prepstmt.setTimestamp(1, new Timestamp(startDate.getTime()));
        prepstmt.setTimestamp(2, new Timestamp(endDate.getTime()));
        prepstmt.executeUpdate();
        prepstmt = conn.prepareStatement("select sum(total_amount) as amount_a_day, " +
                        "extract(day from scheduled_departure) as day, " +
                        "extract(month from scheduled_departure) as month " +
                        "from bookings, tickets, ticket_flights, flights " +
                        "where bookings.book_ref =  tickets.book_ref " +
                        "and tickets.ticket_no = ticket_flights.ticket_no " +
                        "and ticket_flights.flight_id = flights.flight_id " +
                        "and (departure_airport in (select * from moscow_airports) " +
                        "or arrival_airport in (select * from moscow_airports)) " +
                        "and (scheduled_departure between ? and ?) " +
                "group by extract(month from scheduled_departure), extract(day from scheduled_departure);");
        prepstmt.setTimestamp(1, new Timestamp(startDate.getTime()));
        prepstmt.setTimestamp(2, new Timestamp(endDate.getTime()));
        ResultSet fromStatement;
        LinkedHashMap<IntPair, BigDecimal> queryResult = new LinkedHashMap<>();
        if(prepstmt.execute()) {
            fromStatement = prepstmt.getResultSet();
            queryResult = createCovidQueryResult(fromStatement);
        }
        conn.commit();
        conn.close();
        return queryResult;
    }
}
