package io.eotsevych.select2.exceptions;

import org.openqa.selenium.WebElement;

public class Select2DropdownNotOpenedException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "An exception occurred: Select2 element not opened \n%s.";

    public Select2DropdownNotOpenedException(WebElement element) {
        super(String.format(DEFAULT_MESSAGE, element.getAttribute("outerHTML")));
    }
}
