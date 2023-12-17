package io.eotsevych.select2;

import io.eotsevych.select2.exceptions.Select2DropdownNotOpenedException;
import io.eotsevych.select2.exceptions.Select2NoOptionPresentException;
import io.eotsevych.select2.exceptions.UnexpectedSelect2StructureException;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.net.MalformedURLException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.Random.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Select2Test {
    private final List<String> optionTextList = Arrays.asList("Alaska", "Hawaii", "California", "Nevada", "Oregon", "Washington");
    private WebDriver driver;
    private WebDriverWait webDriverWait;

    @BeforeAll
    void setup() throws MalformedURLException {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-gpu", "--window-size=1920,1200", "--ignore-certificate-errors", "--disable-extensions", "--no-sandbox", "--disable-dev-shm-usage");

        //driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), chromeOptions);
        driver = new ChromeDriver(chromeOptions);
        webDriverWait = new WebDriverWait(driver, Duration.of(10, ChronoUnit.SECONDS), Duration.of(500, ChronoUnit.MILLIS));
    }

    @BeforeEach
    void refresh() {
        driver.get("file://" + new File("src/test/resources/index.html").getAbsolutePath());
    }

    @Test
    void singleSelectByTextTest() {
        final String textToSelect = optionTextList.stream().skip(new Random().nextInt(optionTextList.size())).findFirst().orElse(null);

        Select2 element = new Select2(driver.findElement(By.cssSelector(".single-select")));
        element.selectByText(textToSelect);

        String selectedOptionText = element.getSelectedOptionText();
        assertEquals(textToSelect, selectedOptionText);
    }

    @Test
    void noOptionPresentExceptionTest() {
        Select2 element = new Select2(driver.findElement(By.cssSelector(".single-select")));
        assertThrows(Select2NoOptionPresentException.class, () -> element.selectByText("No option"));
    }

    @Test
    void dropdownNotOpenedExceptionTest() {
        final String textToSelect = optionTextList.stream().skip(new Random().nextInt(optionTextList.size())).findFirst().orElse(null);

        Select2 element = new Select2(driver.findElement(By.cssSelector(".failed-to-open-select")));
        assertThrows(Select2DropdownNotOpenedException.class, () -> element.selectByText(textToSelect));
    }

    @Test
    void singleSelectByIndexTest() {
        final String textToSelect = "Hawaii";

        Select2 element = new Select2(driver.findElement(By.cssSelector(".single-select")));
        element.selectByIndex(1);

        String selectedOptionText = element.getSelectedOptionText();
        assertEquals(textToSelect, selectedOptionText);
    }

    @Test
    void multiplySelectByTextWithCloseOnSelectTest() {
        List<String> fewOptionValues = optionTextList.stream().skip(new Random().nextInt(optionTextList.size())).toList();

        Select2 Select2Element = new Select2(driver.findElement(By.cssSelector(".multiple-select")));

        Select2Element.selectByText(fewOptionValues, false, true);

        List<String> chosenValues = Select2Element.getMultiSelectedOptionsText();
        assertEquals(fewOptionValues, chosenValues);
    }

    @Test
    void multiplySelectByTextTest() {
        List<String> fewOptionValues = optionTextList.stream().skip(new Random().nextInt(optionTextList.size())).toList();

        Select2 Select2Element = new Select2(driver.findElement(By.cssSelector(".multiple-select-remain-open")));

        Select2Element.selectByText(fewOptionValues);

        List<String> chosenValues = Select2Element.getMultiSelectedOptionsText();
        assertEquals(fewOptionValues, chosenValues);
    }

    @Test
    void multiplySelectByTextWithCustomWaitTest() {
        List<String> fewOptionValues = optionTextList.stream().skip(new Random().nextInt(optionTextList.size())).toList();

        Select2 Select2Element = new Select2(driver.findElement(By.cssSelector(".multiple-select-remain-open")), webDriverWait);

        Select2Element.selectByText(fewOptionValues);

        List<String> chosenValues = Select2Element.getMultiSelectedOptionsText();
        assertEquals(fewOptionValues, chosenValues);
    }

    @Test
    void multiplySelectByIndexTest() {
        int[] fewOptionValues = new int[]{1, 5, 2};
        List<String> expectedOptionValues = List.of("Alaska", "Hawaii", "Oregon");

        Select2 Select2Element = new Select2(driver.findElement(By.cssSelector(".multiple-select-remain-open")));

        Select2Element.selectByIndex(fewOptionValues);

        List<String> chosenValues = Select2Element.getMultiSelectedOptionsText();
        assertEquals(expectedOptionValues, chosenValues);
    }

    @Test
    void singleSelectByTextWithSearchTest() {
        final String CALIFORNIA = "California";

        Select2 Select2Element = new Select2(driver.findElement(By.cssSelector(".single-select")));
        Select2Element.selectByText(CALIFORNIA);

        String chosen = Select2Element.getSelectedOptionText();
        assertEquals(CALIFORNIA, chosen);
    }

    @Test
    void multiSelectByTextWithSearchTest() {
        List<String> fewOptionValues = optionTextList.stream().skip(new Random().nextInt(optionTextList.size())).toList();

        Select2 Select2Element = new Select2(driver.findElement(By.cssSelector(".multiple-select-remain-open")));
        Select2Element.selectByText(fewOptionValues);

        List<String> chosen = Select2Element.getMultiSelectedOptionsText();
        assertEquals(fewOptionValues, chosen);
    }

    @Test
    void isOptionDisabledSelectTest() {
        final String DISABLED_OPTION = "Hawaii";

        Select2 Select2Element = new Select2(driver.findElement(By.cssSelector(".single-select-disabled-option")));
        boolean optionDisabled = Select2Element.isOptionDisabledByText(DISABLED_OPTION);

        assertTrue(optionDisabled);
    }

    @Test
    void noDisabledOptionPresentException() {
        final String DISABLED_OPTION = "Hawaiid";

        Select2 Select2Element = new Select2(driver.findElement(By.cssSelector(".single-select-disabled-option")));
        assertThrows(Select2NoOptionPresentException.class, () -> Select2Element.isOptionDisabledByText(DISABLED_OPTION));
    }

    @Test
    void getChosenSelectTest() {
        final String PREDEFINED_OPTION = "Hawaii";

        Select2 Select2Element = new Select2(driver.findElement(By.cssSelector(".single-select-predefined")));
        String chosenValue = Select2Element.getSelectedOptionText();

        assertEquals(PREDEFINED_OPTION, chosenValue);
    }

    @Test
    void isOptionPresentTest() {
        final String CALIFORNIA = "California";

        Select2 Select2Element = new Select2(driver.findElement(By.cssSelector(".single-select")));
        boolean isPresent = Select2Element.isOptionPresentByText(CALIFORNIA);
        assertTrue(isPresent);
    }

    @Test
    @Disabled
    void isOptionPresentDynamicDataTest() {
        final String query = "dsadasashjfbsajhfbas";

        Select2 Select2Element = new Select2(driver.findElement(By.cssSelector(".itemSearch")));
        boolean isPresent = Select2Element.isOptionPresentByText(query);
        assertFalse(isPresent);
    }

    @Test
    void getMultiValueChosenTest() {
        List<String> predefinedOptionList = Arrays.asList("California", "Nevada", "Oregon", "Washington");

        Select2 Select2Element = new Select2(driver.findElement(By.cssSelector(".multiple-select-predefined")));
        List<String> chosenOption = Select2Element.getMultiSelectedOptionsText();

        assertEquals(predefinedOptionList, chosenOption);
    }

    @Test
    void clearChosenValueFromSingleSelectTest() {
        final String PLACEHOLDER = "Select an option";
        Select2 Select2Element = new Select2(driver.findElement(By.cssSelector(".single-select-predefined")));

        Select2Element.removeSelectedOption();
        String chosenValue = Select2Element.getSelectedOptionText();

        assertEquals(PLACEHOLDER, chosenValue);
    }

    @Test
    void removeMultiValueChosen() {
        List<String> predefinedOptionList = Arrays.asList("California", "Washington");
        Select2 Select2Element = new Select2(driver.findElement(By.cssSelector(".multiple-select-predefined")));

        Select2Element.removeSelectedOptions(List.of("Nevada", "Oregon"));
        List<String> chosenValues = Select2Element.getMultiSelectedOptionsText();

        assertEquals(predefinedOptionList, chosenValues);
    }

    @Test
    void removeAllChosen() {
        Select2 Select2Element = new Select2(driver.findElement(By.cssSelector(".multiple-select-predefined")));

        Select2Element.removeAllSelectedOptions();
        List<String> getAllChosenValue = Select2Element.getMultiSelectedOptionsText();

        assertEquals(Collections.emptyList(), getAllChosenValue);
    }

    @Test
    void coveredSelectTest() {
        final String textToSelect = optionTextList.stream().skip(new Random().nextInt(optionTextList.size())).findFirst().orElse(null);

        WebElement clickMeButton = driver.findElement(By.cssSelector("#clickMeButton"));
        clickMeButton.click();

        Select2 Select2Element = new Select2(driver.findElement(By.cssSelector(".single-select-covered")));
        Select2Element.selectByText(textToSelect, true);
        String getChosenValue = clickMeButton.getText();

        assertEquals(textToSelect, getChosenValue);
    }

    @Test
    void singleSelectByTextWithSearchDynamicDataTest() {
        final String searchQuery = "qq";

        Select2 Select2Element = new Select2(driver.findElement(By.cssSelector(".itemSearch")));
        Select2Element.selectByText(searchQuery);

        List<String> chosen = Select2Element.getMultiSelectedOptionsText();
        assertTrue(chosen.contains(searchQuery));
    }

    @Test
    void multiSelectByTextWithSearchDynamicDataTest() {
        final List<String> searchQueryList = List.of("qq", "QA");

        Select2 Select2Element = new Select2(driver.findElement(By.cssSelector(".itemSearch")));
        Select2Element.selectByText(searchQueryList);

        List<String> chosen = Select2Element.getMultiSelectedOptionsText();
        assertEquals(searchQueryList, chosen);
    }

    @Test
    void unexpectedSelect2StructureExceptionText() {
        driver.get("https://select2.github.io/select2/");

        assertThrows(UnexpectedSelect2StructureException.class, () -> new Select2(driver.findElement(By.cssSelector("#e9"))));
    }

    @Test
    void UnexpectedTagNameExceptionTest() {
        assertThrows(UnexpectedTagNameException.class, () -> new Select2(driver.findElement(By.cssSelector("#clickMeButton"))));
    }

    @AfterAll
    void tearDown() {
        driver.quit();
    }
}
