package calin.bodnar.weatherapp.util;


public class Utils {
    private static final String[] DIRECTIONS = {"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"};

    public static String degreeToCompass(double degree) {
        int value = (int) ((degree / 22.5) + 0.5);
        int index = value % 16;
        return DIRECTIONS[index];
    }
}
