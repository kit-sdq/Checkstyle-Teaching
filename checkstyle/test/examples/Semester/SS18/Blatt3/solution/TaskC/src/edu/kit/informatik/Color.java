package edu.kit.informatik;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Color {
    // This is in german for conversion convenience
    ROT,
    GELB,
    BLAU,
    ORANGE,
    LILA,
    BRAUN,
    GRAU,
    CYAN,
    MAGENTA,
    MAUVE;
    
    public static List<Color> convertStringsToColors(String... colorsArray) throws ColorConversionException {
        List<Color> colors = new ArrayList<>();
        for (String color : colorsArray) {
            try {
                colors.add(Color.valueOf(color.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new ColorConversionException("color '" + color + "' does not exist.");
            }
        }
        
        return colors;
    }

    public static String getRegex() {
        return "(?:"
                + Arrays.stream(values())
                .map(Color::toString)
                .collect(Collectors.joining("|"))
                + ")";
    }
}
