package io.eotsevych.select2.exceptions;

import org.openqa.selenium.WebElement;

/**
 * Exception thrown when attempting to interact with a Select2 dropdown that is not opened.
 * This exception is typically used to indicate that the dropdown should be opened before interacting with options.
 *
 */
public class Select2DropdownNotOpenedException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "An exception occurred: Select2 element not opened \n%s.";

    /**
     * Constructs a Select2DropdownNotOpenedException.
     *
     * @param element The WebElement representing the Select2 element for which the dropdown is not opened.
     */
    public Select2DropdownNotOpenedException(WebElement element) {
        super(String.format(DEFAULT_MESSAGE, element.getAttribute("outerHTML")));
    }
}
