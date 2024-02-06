package io.eotsevych.select2.exceptions;


/**
 * Exception thrown when an option is expected to be selected but is not.
 * This exception can be used to indicate unexpected states related to option selection.
 */
public class OptionIsNotSelectedException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Option '%s' is not selected";

    /**
     * Constructs an OptionIsNotSelectedException.
     *
     * @param query The query for the option that is not selected.
     */
    public OptionIsNotSelectedException(String query) {
        super(String.format(DEFAULT_MESSAGE, query));
    }
}
