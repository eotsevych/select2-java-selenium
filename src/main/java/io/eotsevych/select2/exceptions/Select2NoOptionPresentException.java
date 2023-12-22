package io.eotsevych.select2.exceptions;

import java.util.List;

/**
 * Exception thrown when no matching options are found in a Select2 dropdown for a given query.
 * This exception is typically used to indicate that the expected options are not present.
 *
 */
public class Select2NoOptionPresentException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Option '%s' is not present inside '%s'";

    /**
     * Constructs a Select2NoOptionPresentException.
     *
     * @param query      The query for which no matching options were found.
     * @param optionList The list of options that were expected but not found.
     */
    public Select2NoOptionPresentException(String query, List<String> optionList) {
        super(String.format(DEFAULT_MESSAGE, query, optionList));
    }
}
