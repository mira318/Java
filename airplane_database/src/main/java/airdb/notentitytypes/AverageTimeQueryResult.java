package airdb.notentitytypes;

public class AverageTimeQueryResult {
    private final String cityFrom;
    private final String cityTo;
    private final String airportTo;
    private final double averageDuration;

    public AverageTimeQueryResult(String cityFrom, String cityTo, String airportTo, double averageDuration) {
        this.cityFrom = cityFrom;
        this.cityTo = cityTo;
        this.airportTo = airportTo;
        this.averageDuration = averageDuration;
    }

    public String getCityFrom() {
        return cityFrom;
    }

    public String getCityTo() {
        return cityTo;
    }

    public String getAirportTo() {
        return airportTo;
    }

    public double getAverageDuration() {
        return averageDuration;
    }

    @Override
    public String toString() {
        return "cityFrom='" + cityFrom + '\'' +
                ", cityTo='" + cityTo + '\'' +
                ", airportTo='" + airportTo + '\'' +
                ", averageDuration=" + averageDuration;
    }
}
