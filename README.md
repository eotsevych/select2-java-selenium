# select2-java-selenium

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/4c416ebdae38435f976e1fdbd18dfcc3)](https://app.codacy.com/gh/eotsevych/select2-java-selenium?utm_source=github.com&utm_medium=referral&utm_content=eotsevych/select2-java-selenium&utm_campaign=Badge_Grade)

## Overview

The `Select2` class in the `select2-java-selenium` library provides a powerful implementation of the `ISelect2` interface for seamless integration of Select2 functionality with Selenium. This class enables efficient UI automation by offering methods to select options, verify option states, remove selections, and retrieve selected option text.

## Constructor

### `Select2(WebElement selectElement, WebDriverWait webDriverWait)`

- **Description:** Initializes the `Select2` instance with the given `selectElement` and `webDriverWait`.

### `Select2(WebElement selectElement)`

- **Description:** Initializes the `Select2` instance with the given `selectElement` and a default `WebDriverWait` duration of 10 seconds.

## Implemented Methods

### `selectByIndex`

- **Description:** Selects an option from the Select2 UI element by index.

### `selectByText(List<String> textList, boolean isOpened, boolean closeOnSelect)`

- **Description:** Selects multiple options from the Select2 UI element by text, with control over the dropdown's state.

### `selectByText(String query, boolean isOpened, boolean closeOnSelect)`

- **Description:** Selects an option from the Select2 UI element by text, with control over the dropdown's state.

### `isOptionDisabledByText(String text, boolean isOpened)`

- **Description:** Verifies if an option from the Select2 UI element is disabled by text, with optional control over the dropdown's state.

### `isOptionPresentByText(String query, boolean isOpened)`

- **Description:** Verifies if an option from the Select2 UI element is present by text, with optional control over the dropdown's state.

### `removeSelectedOption`

- **Description:** Removes the selected option from a single Select UI Element.

### `removeAllSelectedOptions`

- **Description:** Removes all selected options from a multi-Select UI Element.

### `removeSelectedOptions(List<String> textList)`

- **Description:** Removes selected options from the Select2 UI element based on a list of text options.

### `getSelectedOptionText`

- **Description:** Retrieves the text of the selected option from a single Select UI Element.

### `getMultiSelectedOptionsText`

- **Description:** Retrieves a list of text values of the selected options from a multi-Select UI Element.

## Example

```java
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.eotsevych.select2.Select2;

public class Select2Example {
    public static void main(String[] args) {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-gpu", "--window-size=1920,1200", "--ignore-certificate-errors", "--disable-extensions", "--no-sandbox", "--disable-dev-shm-usage");

        WebDriver driver = new ChromeDriver(chromeOptions);
        WebDriverWait wait = new WebDriverWait(driver, 5);

        // Locate your Select2 element
        WebElement selectElement = driver.findElement(By.id("yourSelect2ElementId"));

        // Initialize Select2 instance
        Select2 select2 = new Select2(selectElement, wait);

        // Example: Select options by text
        select2.selectByText("Option1", true, true);

        // Example: Verify if an option is disabled
        boolean isDisabled = select2.isOptionDisabledByText("Option2", false);

        // Example: Get list of selected options
        List<String> selectedOptions = select2.getMultiSelectedOptionsText();

        // Perform additional actions as needed...

        // Close the browser
        driver.quit();
    }
}
```