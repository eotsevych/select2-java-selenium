package io.eotsevych.select2;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

public class Select2DropdownNotOpenedException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "An exception occurred: Select2 element not opened \n%s.";

    public Select2DropdownNotOpenedException(WebElement element) {
        super(String.format(DEFAULT_MESSAGE, element.getAttribute("outerHTML")));
    }
}
