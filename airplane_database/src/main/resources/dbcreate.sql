create table aircrafts (
    aircraft_code VARCHAR2(3) not null primary key,
    model JSON not null,
    range INT not null
);

create table airports (
    airport_code VARCHAR2(3) not null primary key,
    airport_name JSON not null,
    city JSON not null,
    coordinates_latitude NUMBER(9, 6) not null,
    coordinates_longitude NUMBER(9, 6) not null,
    timezone text not null
);

create table seats(
    aircraft_code VARCHAR2(3) not null,
    seat_no VARCHAR2(4) not null,
    fare_conditions VARCHAR2(10) not null,
    primary key (aircraft_code, seat_no),
    CONSTRAINT validFareConditions CHECK fare_conditions in ('Economy', 'Comfort', 'Business'),
    foreign key (aircraft_code) references aircrafts(aircraft_code)
);


create table bookings(
    book_ref VARCHAR2(6) not null primary key,
    book_date TIMESTAMP not null,
    total_amount NUMERIC(10, 2) not null
);

create table flights(
    flight_id serial not null primary key,
    flight_no VARCHAR2(6) not null,
    scheduled_departure TIMESTAMP not null,
    scheduled_arrival TIMESTAMP not null,
    departure_airport VARCHAR2(3) not null,
    arrival_airport VARCHAR2(3) not null,
    status VARCHAR2(20) not null,
    aircraft_code VARCHAR2(3) not null,
    actual_departure TIMESTAMP,
    actual_arrival TIMESTAMP,
    CONSTRAINT uniqueNaturalKey unique(flight_no, scheduled_departure),
    CONSTRAINT validStatus CHECK status in ('Scheduled', 'On Time', 'Delayed', 'Departed', 'Arrived', 'Cancelled'),
    foreign key (departure_airport) references airports(airport_code),
    foreign key (arrival_airport) references airports(airport_code),
    foreign key (aircraft_code) references aircrafts(aircraft_code)
);

create table tickets(
    ticket_no VARCHAR2(13) not null primary key,
    book_ref VARCHAR2(6) not null,
    passenger_id VARCHAR2(20) not null,
    passenger_name text not null,
    contact_data JSON,
    foreign key (book_ref) references bookings(book_ref)
);

create table ticket_flights(
    ticket_no VARCHAR2(13) not null,
    flight_id INTEGER not null,
    fare_conditions VARCHAR2(10) not null,
    amount NUMERIC(10, 2) not null,
    primary key (flight_id, ticket_no),
    foreign key (flight_id) references flights(flight_id),
    foreign key (ticket_no) references tickets(ticket_no),
    CONSTRAINT fare_conditions CHECK fare_conditions in ('Economy', 'Comfort', 'Business')
);

create table boarding_passes (
    ticket_no VARCHAR2(13) not null,
    flight_id INTEGER not null,
    boarding_no INTEGER not null,
    seat_no VARCHAR2(4) not null,
    primary key (ticket_no, flight_id),
    CONSTRAINT uniquePlacesOnBoard UNIQUE(boarding_no, flight_id),
    foreign key (ticket_no, flight_id) references ticket_flights(ticket_no, flight_id)
);

