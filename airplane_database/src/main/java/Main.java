import airdb.JDBC.JDBCTemplate;
import airdb.dao.*;
import airdb.entities.Flight;
import airdb.notentitytypes.AverageTimeQueryResult;
import airdb.notentitytypes.IntPair;
import org.h2.jdbcx.JdbcConnectionPool;

import javax.sql.DataSource;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.text.ParseException;
import java.util.*;
import java.util.Date;

public class Main {

    public static void initializationFromFiles(DataSource ConnectionPool) throws IOException, SQLException,
            ParseException {
        JDBCTemplate source = new JDBCTemplate(ConnectionPool);
        DBInit IntializationForBD = new DBInit(source);
        IntializationForBD.create();
        //Здесь важен порядок!!
        IntializationForBD.addAirportsFromFile("airports_data.csv");
        IntializationForBD.addAircraftsFromFile("aircrafts_data.csv");
        IntializationForBD.addSeatsFromFile("seats.csv");
        IntializationForBD.addFlightsFromFile("flights.csv");
        IntializationForBD.addBookingsFromFile("bookings.csv");
        IntializationForBD.addTicketsFromFile("tickets.csv");
        IntializationForBD.addTicketConnectedWithFlightFromFile("ticket_flights.csv");
        IntializationForBD.addPassesFromFile("boarding_passes.csv");
    }

    public static void runTackQueries(DataSource ConnectionPool) throws SQLException, IOException {
        JDBCTemplate source = new JDBCTemplate(ConnectionPool);

        try(FileWriter output = new FileWriter("output_query1.txt")) {
            AirportDao getCities = new AirportDao(source);
            HashMap<String, String> getFromQuery = getCities.getCitiesWithSeveralAirports();
            getFromQuery.forEach((key, val) -> {
                try {
                    output.write(("city:" + key + " airports:" + val + "\n"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        try(FileWriter output = new FileWriter("output_query2.txt")) {
            FlightDao getCancelledFlights = new FlightDao(source);
            LinkedHashMap<String, Integer> getFromQuery2 = getCancelledFlights.getCancelledFlights();
            getFromQuery2.forEach((key, val) -> {
                try {
                    output.write("city:" + key +
                            " number of cancelled flights:" + val + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        try(FileWriter output = new FileWriter("output_query3.txt")) {
            FlightDao getAverageTime = new FlightDao(source);
            LinkedHashSet<AverageTimeQueryResult> getFromQuery3 = getAverageTime.getShortestRoutes();
            getFromQuery3.forEach(val -> {
                try {
                    output.write("city of departure:" + val.getCityFrom() +
                            ", city of arrival:" + val.getCityTo() + ", airport of arrival: " + val.getAirportTo() +
                            ", average time:" + val.getAverageDuration());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }


        try(FileWriter output = new FileWriter("output_query4.txt")) {
            FlightDao getCancelledByMonth = new FlightDao(source);
            HashMap<Integer, Integer> getFromQuery4 = getCancelledByMonth.getFlightsCancelledByMonth();
            getFromQuery4.forEach((key, val) -> {
                try {
                    output.write("month:" + key +
                            ", number of flights cancelled:" + val + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        try(FileWriter output = new FileWriter("output_query5.txt")) {
            FlightDao getMoscowFlights = new FlightDao(source);
            LinkedHashMap<IntPair, Integer> getFromQuery5 = getMoscowFlights.getFlightsFromToMoscow();
            getFromQuery5.forEach((key, val) -> {
                try {
                    output.write("day of week:" + key.getArg1() +
                            ", is from Moscow:" + key.getArg2() + ", number of flights: " + val + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        //Тут особого вывода нет, вывела верхние 100 полётов, обычно там попадается т. к. самолётов в базе мало
        try(FileWriter output = new FileWriter("output_query6.txt")) {
            FlightDao deleteAircraft = new FlightDao(source);
            Set<Flight> before = deleteAircraft.getFlightsLimited();
            output.write("flights before delete\n");
            before.forEach(flight -> {
                try {
                    output.write(flight.toString() + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            deleteAircraft.deleteBadAircraft(ConnectionPool, "321" );
            Set<Flight> after = deleteAircraft.getFlightsLimited();
            output.write("flights after delete\n");
            after.forEach(flight -> {
                try {
                    output.write(flight.toString() + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        try(FileWriter output = new FileWriter("output_query7.txt")){
            FlightDao cancellFlightsLoss = new FlightDao(source);
            Date startDate, endDate;
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, 2017);
            cal.set(Calendar.MONTH, Calendar.JULY);
            cal.set(Calendar.DAY_OF_MONTH, 13);
            startDate = cal.getTime();
            cal.set(Calendar.MONTH, Calendar.AUGUST, 1);
            endDate = cal.getTime();
            LinkedHashMap<IntPair, BigDecimal> getFromQuery7 = cancellFlightsLoss.covidCancell(ConnectionPool,
                    startDate, endDate);
            getFromQuery7.forEach((key, val) -> {
                try {
                    output.write("day of month:" + key.getArg1() + "." + key.getArg2()
                            + ", loss amount:" + val + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static void main(String[] args) throws SQLException, IOException, ParseException {
        DataSource ConnectionPool = JdbcConnectionPool.create("jdbc:h2:mem:database;DB_CLOSE_DELAY=-1", "",
                "" );
        initializationFromFiles(ConnectionPool);
        JDBCTemplate source = new JDBCTemplate(ConnectionPool);
        runTackQueries(ConnectionPool);
    }
}