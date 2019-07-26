/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.material.ui;

import edu.kit.informatik.util.StringUtil;

/**
 * Contains strings or characters used for input or output.
 *
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public enum InteractionStrings {
    /**
     * String for a successfully executed command.
     */
    OK("OK"),

    SUB("-"),

    ADD("+"),
    
    ADD_REGEX("\\+"),
    
    SEPARATOR_EQL("="),

    SEPARATOR_INNER(":"),

    SEPARATOR_OUTER(";"),
    
    AMOUNT(StringUtil.parenthesize("([1-9]([0-9]){0,2})|(1000)")),
    
    NAME(StringUtil.parenthesize("[a-zA-Z]+")),
    
    PAIR(StringUtil.parenthesize("" + AMOUNT + SEPARATOR_INNER + NAME)),
    
    LIST_OF_PAIRS(StringUtil.parenthesize("" + PAIR + SEPARATOR_OUTER) + "*" + PAIR),
    
    ASSEMBLY_DESC("" + NAME + SEPARATOR_EQL + LIST_OF_PAIRS),
    
    COMP("COMPONENT"),
    
    EMPTY("EMPTY");

    /**
     * String representation of the output.
     */
    private String content;

    /**
     * @param content the string representation for output
     */
    InteractionStrings(final String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return content;
    }
}
