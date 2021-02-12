import airdb.JDBC.JDBCTemplate;
import airdb.dao.*;
import airdb.entities.*;
import airdb.notentitytypes.TicketConnectedWithFlight;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

public class DBInit {

    final JDBCTemplate source;
    private final SimpleDateFormat dateParser;
    public DBInit(JDBCTemplate source)
    {
        this.source = source;
        dateParser = new SimpleDateFormat("yyyy-M-d HH:mm:ssX");
    }

    private String getSQL(String future_query) throws IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        Main.class.getResourceAsStream(future_query),
                        StandardCharsets.UTF_8))) {
            return br.lines().collect(Collectors.joining("\n"));
        }
    }

    public void create() throws IOException, SQLException {
        String sql_creation_query = getSQL("dbcreate.sql");
        source.statement(stmt -> {
           stmt.execute(sql_creation_query);
        });
    }

    public void addAircraftsFromFile(String fileName) throws IOException, SQLException {
        Set<Aircraft> incomingAircrafts = new HashSet<>();
        File aircraftData = new File(fileName);
        try(Scanner aircraftReader = new Scanner(aircraftData))
        {
            while(aircraftReader.hasNextLine()) {
                String oneAircraftData = aircraftReader.nextLine();
                String[] temp = oneAircraftData.split(",\"\\{|}\",");
                List<String> oneAircraftDataList;
                oneAircraftDataList = Arrays.asList(temp);
                Aircraft incomingAircraft = new Aircraft(oneAircraftDataList.get(0), oneAircraftDataList.get(1),
                        parseInt(oneAircraftDataList.get(2)));
                incomingAircrafts.add(incomingAircraft);
            }
        }
        AircraftDao dao = new AircraftDao(source);
        dao.saveAircrafts(incomingAircrafts);
    }

    public void addAirportsFromFile(String fileName) throws IOException, SQLException {
        Set<Airport> incomingAirports = new HashSet<>();
        File airportData = new File(fileName);
        try(Scanner airportReader = new Scanner(airportData)){
            while(airportReader.hasNextLine()) {
                String oneAirportData = airportReader.nextLine();
                String[] temp = oneAirportData.split("}\",\"\\{|}\",\"\\(|\\)\",|,\"\\{|}\",");
                List<String> oneAirportDataList;
                oneAirportDataList = Arrays.asList(temp);
                Airport incomingAirport = new Airport(oneAirportDataList.get(0), oneAirportDataList.get(1),
                        oneAirportDataList.get(2), parseDouble(oneAirportDataList.get(3).split(",")[0]),
                        parseDouble(oneAirportDataList.get(3).split(",")[1]), oneAirportDataList.get(4));
                incomingAirports.add(incomingAirport);
            }
        }
        AirportDao dao = new AirportDao(source);
        dao.saveAirports(incomingAirports);
    }

    public void addSeatsFromFile(String fileName) throws IOException, SQLException {
        Set<Seat> incomingSeats = new HashSet<>();
        File seatData = new File(fileName);
        try(Scanner seatReader = new Scanner(seatData)){
            while (seatReader.hasNextLine()) {
                String oneSeatData = seatReader.nextLine();
                String[] temp = oneSeatData.split(",");
                List<String> oneSeatDataList;
                oneSeatDataList = Arrays.asList(temp);
                Seat incomingSeat = new Seat(oneSeatDataList.get(0), oneSeatDataList.get(1), oneSeatDataList.get(2));
                incomingSeats.add(incomingSeat);
            }
        }
        SeatDao dao = new SeatDao(source);
        dao.saveSeats(incomingSeats);
    }

    public void addFlightsFromFile(String fileName) throws IOException, SQLException, ParseException {
        Set<Flight> incomingFlights = new HashSet<>();
        File flightData = new File(fileName);
        try(Scanner flightReader = new Scanner(flightData)){
            while (flightReader.hasNextLine()) {
                String oneFlightData = flightReader.nextLine();
                String[] temp = oneFlightData.split(",");
                List<String> oneFlightDataList;
                oneFlightDataList = Arrays.asList(temp);
                long id = parseLong(oneFlightDataList.get(0));
                String flightNumber = oneFlightDataList.get(1);
                Date scheduledDeparture = dateParser.parse(oneFlightDataList.get(2));
                Date scheduledArrive = dateParser.parse(oneFlightDataList.get(3));
                String airportDeparture = oneFlightDataList.get(4);
                String airportArrive = oneFlightDataList.get(5);
                String status = oneFlightDataList.get(6);
                String aircraftCode = oneFlightDataList.get(7);
                Date actualDeparture = null;
                Date actualArrive = null;
                if(oneFlightDataList.size() > 8)
                    actualDeparture = dateParser.parse(oneFlightDataList.get(8));
                if(oneFlightDataList.size() > 9)
                    actualArrive = dateParser.parse(oneFlightDataList.get(9));
                Flight incomingFlight = new Flight(id, flightNumber, scheduledDeparture, scheduledArrive, airportDeparture,
                        airportArrive, status, aircraftCode, actualDeparture, actualArrive);
                incomingFlights.add(incomingFlight);
            }
        }
        FlightDao dao = new FlightDao(source);
        dao.saveFlights(incomingFlights);
    }

    public void addBookingsFromFile(String fileName) throws IOException, SQLException, ParseException {
        Set<Booking> incomingBookings = new HashSet<>();
        File bookingData = new File(fileName);
        try(Scanner bookingReader = new Scanner(bookingData)){
            while (bookingReader.hasNextLine()) {
                String oneBookingData = bookingReader.nextLine();
                String[] temp = oneBookingData.split(",");
                List<String> oneBookingDataList;
                oneBookingDataList = Arrays.asList(temp);
                Date dateOfBooking = dateParser.parse(oneBookingDataList.get(1));
                BigDecimal sumOfBooking = new BigDecimal(oneBookingDataList.get(2));
                Booking incomingBooking = new Booking(oneBookingDataList.get(0), dateOfBooking, sumOfBooking);
                incomingBookings.add(incomingBooking);
            }
        }
        BookingDao dao = new BookingDao(source);
        dao.saveBookings(incomingBookings);
    }

    public void  addTicketsFromFile(String fileName) throws IOException, SQLException {
        Set<Ticket> incomingTickets = new HashSet<>();
        File ticketData = new File(fileName);
        try(Scanner ticketReader = new Scanner(ticketData)){
            while (ticketReader.hasNextLine()) {
                String oneTicketData = ticketReader.nextLine();
                String[] temp = oneTicketData.split(",\"\\{|}\"");
                String tempPassengerInfo = temp[1].replace("\"", "");
                String[] tempMainData = temp[0].split(",");
                List<String> oneTicketMainDataList;
                oneTicketMainDataList = Arrays.asList(tempMainData);
                Ticket incomingTicket = new Ticket(oneTicketMainDataList.get(0), oneTicketMainDataList.get(1),
                        oneTicketMainDataList.get(2), oneTicketMainDataList.get(3), tempPassengerInfo);
                incomingTickets.add(incomingTicket);
            }
        }
        TicketDao dao = new TicketDao(source);
        dao.saveTickets(incomingTickets);
    }

    public void addTicketConnectedWithFlightFromFile(String fileName) throws IOException, SQLException {
        Set<TicketConnectedWithFlight> incomingConnections = new HashSet<>();
        File connectionsData = new File(fileName);
        try(Scanner connectionReader = new Scanner(connectionsData)) {
            while(connectionReader.hasNextLine()) {
                String oneConnectionData = connectionReader.nextLine();
                String[] temp = oneConnectionData.split(",");
                List<String> oneConnectionDataList;
                oneConnectionDataList = Arrays.asList(temp);
                TicketConnectedWithFlight incomingConnection = new TicketConnectedWithFlight(
                        oneConnectionDataList.get(0), parseInt(oneConnectionDataList.get(1)),
                        oneConnectionDataList.get(2), new BigDecimal(oneConnectionDataList.get(3)));
                incomingConnections.add(incomingConnection);
            }
        }
        TicketConnectedWithFlightDao dao = new TicketConnectedWithFlightDao(source);
        dao.saveConnections(incomingConnections);
    }

    public void addPassesFromFile(String fileName) throws SQLException, FileNotFoundException {
        Set<BoardingPass> incomingPasses = new HashSet<>();
        File boardingData = new File(fileName);
        try(Scanner boardingReader = new Scanner(boardingData)) {
            while(boardingReader.hasNextLine()) {
                String onePassData = boardingReader.nextLine();
                String[] temp = onePassData.split(",");
                List<String> onePassDataList;
                onePassDataList = Arrays.asList(temp);
                BoardingPass incomingPass = new BoardingPass(onePassDataList.get(0), parseInt(onePassDataList.get(1)),
                        parseInt(onePassDataList.get(2)), onePassDataList.get(3));
                incomingPasses.add(incomingPass);
            }

        }
        BoardingPassDao dao = new BoardingPassDao(source);
        dao.saveBoardingPasses(incomingPasses);
    }
}
