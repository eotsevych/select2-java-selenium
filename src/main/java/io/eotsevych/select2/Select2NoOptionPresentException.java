package io.eotsevych.select2;

import org.openqa.selenium.WebElement;

import java.util.List;

public class Select2NoOptionPresentException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Option '%s' is not present inside '%s'";

    public Select2NoOptionPresentException(String query, List<String> optionList) {
        super(String.format(DEFAULT_MESSAGE, query, optionList));
    }
}
