package edu.kit.informatik;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Currency {
    /**
     * The Euro used in the European Currency Union.
     */
    EUR,
    /**
     * The US-Dollar used in the US.
     */
    USD,
    /**
     * The Chinese Yuan used in China.
     */
    CNY;

    /**
     * Creates a non-capturing group regex of all currencies.
     * 
     * @return the non-capturing group regex of all currencies.
     */
    public static String getRegex() {
        return "(?:"
                + Arrays.stream(values())
                .map(Currency::toString)
                .collect(Collectors.joining("|"))
                + ")";
    }
}
