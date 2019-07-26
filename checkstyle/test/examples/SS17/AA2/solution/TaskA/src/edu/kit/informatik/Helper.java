package edu.kit.informatik;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Helper {
    public static final String TYPE_NAME = "[a-z]+";
    public static final String AGGREGATIONS = "sum|avg|last|minmax";
    public static final String REAL_NUMBER = "-?[0-9]+\\.?[0-9]*";
    public static final String GOAL_TYPE = "(less-than|greater-than|between)";
    public static final String GOAL_DESCRIPTION = GOAL_TYPE + " (.*)";
    public static final String DATA = "(" + REAL_NUMBER + ";)*" + REAL_NUMBER;
    public static final String NATURAL_NUMBER = "-?\\d+";
    
    private static final List<String> GOAL_VALUES;

    static {
        GOAL_VALUES = new ArrayList<>();
        GOAL_VALUES.add("(" + REAL_NUMBER + ")");
        GOAL_VALUES.add("(" + REAL_NUMBER + ") and (" + REAL_NUMBER + ")");
    }
    
    private Helper() {
    } 
    
    public static String formatDouble(double value) {
        String valueString = String.format(Locale.ENGLISH, "%.16f", value);
        int dotIndex = valueString.indexOf(".");

        double rounded = Double.parseDouble(String.format(Locale.ENGLISH, "%.2f", value));
        if (compare(rounded, value) == 0) {
            return Double.toString(rounded) + "0";
        }
        
        return valueString.substring(0, dotIndex) + valueString.substring(dotIndex, dotIndex + 4);
    }

    public static boolean lessThan(double a, double b) {
        return compare(a, b) == -1;     
    }
    
    public static boolean greaterThan(double a, double b) {
        return compare(a, b) == 1;
    }
    
    public static int compare(double a, double b) {
        double difference = a - b;
        if (Math.abs(difference) < 1E-6) {
            return 0;
        }

        return (difference < 0) ? -1 : 1;
    }

    public static List<Double> valueToList(double value) {
        List<Double> result = new ArrayList<>(1);
        result.add(value);
        return result;
    }
    
    public static double[] goalValueParser(String input) throws InteractionException {
        double[] values = null;
        for (String current : GOAL_VALUES) {
            Matcher matcher = Pattern.compile(current).matcher(input);
            if (matcher.matches()) {
                values = new double[matcher.groupCount()];
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    values[i - 1] = Double.parseDouble(matcher.group(i));
                }
                break;
            }
        }
        
        if (values == null) {
            throw new InteractionException("not a valid value input.");
        }
        
        return values;
    }
}
