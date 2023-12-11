package io.eotsevych.select2;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class Select2 implements ISelect2 {
    public static final String SELECT_2_RESULTS_LI = ".select2-results li";
    public static final String SELECT_2_DROPDOWN_LOCATOR = "//span[contains(@class, 'select2-dropdown')]";
    public static final By SELECT_2_SEARCH_FIELD_LOCATOR = By.cssSelector("input.select2-search__field");
    private final WebElement selectElement;
    private final WebElement containerElement;
    private WebElement select2DropDownElement = null;
    private final WebDriver driver;
    private final WebDriverWait webDriverWait;

    public Select2(WebElement selectElement, WebDriverWait webDriverWait) {
        this.selectElement = selectElement;
        this.containerElement = selectElement.findElement(By.xpath("./../span")); //./../span
        driver = ((RemoteWebElement) selectElement).getWrappedDriver();
        this.webDriverWait = webDriverWait;
    }

    public Select2(WebElement selectElement) {
        this.selectElement = selectElement;
        this.containerElement = selectElement.findElement(By.xpath("./../span"));
        driver = ((RemoteWebElement) selectElement).getWrappedDriver();
        this.webDriverWait = new WebDriverWait(driver, Duration.of(10, ChronoUnit.SECONDS));
    }

    @Override
    public void selectByIndex(int index, boolean... isOpened) {
        if (!(isOpened.length > 0 && isOpened[0])) {
            expandContainerElement();
        }
        getOptions().get(index).click();
    }

    @Override
    public void selectByIndex(int[] index, boolean... isOpened) {
        if (!(isOpened.length > 0 && isOpened[0])) {
            expandContainerElement();
        }
        List<WebElement> optionList = getOptions();
        for (int i : index) {
            optionList.get(i - 1).click();
        }
    }

    @Override
    public void selectByText(List<String> textList, boolean... isOpened) {
        selectByText(textList, isOpened.length > 0 && isOpened[0], false);
    }

    @Override
    public void selectByText(List<String> queryList, boolean isOpened, boolean closeOnSelect) {
        boolean isDynamicData = false;
        if (!isOpened) {
            expandContainerElement();
        }

        WebElement select2Multiple = containerElement.findElement(By.cssSelector(".select2-selection--multiple"));
        if (select2Multiple.getAttribute("aria-activedescendant") == null) {
            isDynamicData = true;
        }
        for (String value : queryList) {
            multiSearch(value, isDynamicData);
            selectSingleOption(value);
            if (closeOnSelect) expandContainerElement();
        }
        if (!isDynamicData) collapseContainerElement();
    }

    @Override
    public void selectByText(String query, boolean isOpened, boolean closeOnSelect) {
        boolean isDynamicData = false;
        if (!isOpened) {
            expandContainerElement();
        }

        WebElement selectSelection = containerElement.findElement(By.cssSelector(".select2-selection"));

        if (selectSelection.getAttribute("class").contains("--single")) {
            optionalSearch(query);
        } else {
            if (selectSelection.getAttribute("aria-activedescendant") == null) {
                isDynamicData = true;
            }
            multiSearch(query, isDynamicData);
        }
        selectSingleOption(query);
        if (closeOnSelect) expandContainerElement();
    }

    @Override
    public void selectByText(String query, boolean... isOpened) {
        selectByText(query, isOpened.length > 0 && isOpened[0], false);
    }

    @Override
    public boolean isOptionDisabledByText(String text, boolean... isOpened) {
        if (!(isOpened.length > 0 && isOpened[0])) {
            expandContainerElement();
        }

        List<WebElement> optionList = getOptions();

        return Boolean.parseBoolean(optionList.stream()
                .filter(option -> option.getText().equalsIgnoreCase(text)).findFirst()
                .orElseThrow(() -> new Select2NoOptionPresentException(text, optionList.stream()
                        .map(WebElement::getText)
                        .toList())).getAttribute("aria-disabled"));
    }

    @Override
    public boolean isOptionPresentByText(String query, boolean... isOpened) {
        boolean isDynamicData = false;
        if (!(isOpened.length > 0 && isOpened[0])) {
            expandContainerElement();
        }

        WebElement selectSelection = containerElement.findElement(By.cssSelector(".select2-selection"));

        if (selectSelection.getAttribute("class").contains("--single")) {
            optionalSearch(query);
        } else {
            if (selectSelection.getAttribute("aria-activedescendant") == null) {
                isDynamicData = true;
            }
            multiSearch(query, isDynamicData);
        }

        return getOptions().stream()
                .anyMatch(option -> option.getText().equalsIgnoreCase(query));
    }

    @Override
    public void removeSelectedOption() {
        containerElement.findElement(By.cssSelector(".select2-selection__clear")).click();
    }

    @Override
    public void removeAllSelectedOptions() {
        List<WebElement> list = containerElement.findElements(By.xpath(".//li[@class='select2-selection__choice']"));
        while (!list.isEmpty()) {
            list.get(0).findElement(By.cssSelector("span")).click();
            list = containerElement.findElements(By.xpath(".//li[@class='select2-selection__choice']"));
        }
    }

    @Override
    public void removeSelectedOptions(List<String> textList) {
        for (String text : textList) {
            containerElement.findElements(By.xpath(".//li[@class='select2-selection__choice']"))
                    .stream().filter(el -> el.getAttribute("title").equalsIgnoreCase(text))
                    .findFirst().get()
                    .findElement(By.cssSelector("span")).click();
        }
    }

    @Override
    public <T> T getSelectedOptionText() {
        return (T) containerElement.findElement(By.cssSelector("[role='textbox']")).getText();
    }

    @Override
    public <T> List<T> getMultiSelectedOptionsText() {
        return containerElement.findElements(By.cssSelector(".select2-selection__choice__display")).stream().map(element -> (T) element.getText()).collect(Collectors.toList());
    }

    private List<WebElement> getOptions() {
        select2DropDownElement = selectElement.findElement(By.xpath(SELECT_2_DROPDOWN_LOCATOR));
        return select2DropDownElement.findElements(By.cssSelector(SELECT_2_RESULTS_LI));
    }

    private void expandContainerElement() {
        new Actions(driver).moveToElement(containerElement, containerElement.getRect().getWidth() / 2 - 1, 0).click().build().perform();
        try {
            webDriverWait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(selectElement, By.xpath(SELECT_2_DROPDOWN_LOCATOR)));
        } catch (TimeoutException ex) {
            throw new Select2DropdownNotOpenedException(selectElement);
        }
    }

    private void collapseContainerElement() {
        if (select2DropDownElement.isDisplayed()) {
            new Actions(driver).moveToElement(containerElement, containerElement.getRect().getWidth() / 2 - 1, 0).click().build().perform();
        }
    }

    private void selectSingleOption(String query) {
        List<WebElement> optionsList = getOptions();
        optionsList
                .stream()
                .filter(optionText -> optionText.getText().equalsIgnoreCase(query))
                .findFirst()
                .orElseThrow(() -> new Select2NoOptionPresentException(query, optionsList.stream()
                        .map(WebElement::getText)
                        .toList())).click();
    }

    private void optionalSearch(String query) {
        select2DropDownElement = selectElement.findElement(By.xpath(SELECT_2_DROPDOWN_LOCATOR));
        List<WebElement> searchField = select2DropDownElement.findElements(SELECT_2_SEARCH_FIELD_LOCATOR);
        if (!searchField.isEmpty()) {
            searchField.get(0).clear();
            searchField.get(0).sendKeys(query);
        }
    }

    private void multiSearch(String query, boolean isDynamicData) {
        WebElement searchField = containerElement.findElement(By.cssSelector("textarea"));
        searchField.clear();
        searchField.sendKeys(query);
        if (isDynamicData) {
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".loading-results")));
            webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".loading-results")));
        }
        select2DropDownElement = selectElement.findElement(By.xpath(SELECT_2_DROPDOWN_LOCATOR));
    }
}
