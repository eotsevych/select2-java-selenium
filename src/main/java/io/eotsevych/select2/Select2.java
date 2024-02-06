package io.eotsevych.select2;

import io.eotsevych.select2.exceptions.OptionIsNotSelectedException;
import io.eotsevych.select2.exceptions.Select2DropdownNotOpenedException;
import io.eotsevych.select2.exceptions.Select2NoOptionPresentException;
import io.eotsevych.select2.exceptions.UnexpectedSelect2StructureException;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the ISelect2 interface providing methods to interact with Select2 UI elements.
 * This class encapsulates the behavior of working with Select2 dropdowns.
 */
public class Select2 implements ISelect2 {

    /**
     * CSS selector for Select2 results list items.
     */
    public static final String SELECT_2_RESULTS_LI = ".select2-results li";

    /**
     * XPath locator for Select2 dropdown.
     */
    public static final String SELECT_2_DROPDOWN_LOCATOR = "//span[contains(@class, 'select2-dropdown')]";

    /**
     * By locator for Select2 search field.
     */
    public static final By SELECT_2_SEARCH_FIELD_LOCATOR = By.cssSelector("input.select2-search__field");
    private final WebDriverWait webDriverWait;
    private WebElement selectElement;
    private WebElement containerElement;
    private WebElement select2DropDownElement = null;
    private WebDriver driver;

    /**
     * Constructs a Select2 instance for interacting with a Select2 element.
     *
     * @param selectElement The WebElement representing the Select2 element.
     * @param webDriverWait The WebDriverWait to be used for waiting conditions.
     */
    public Select2(WebElement selectElement, WebDriverWait webDriverWait) {
        init(selectElement);
        this.webDriverWait = webDriverWait;
    }

    /**
     * Constructs a Select2 instance for interacting with a Select2 element.
     *
     * @param selectElement The WebElement representing the Select2 element.
     */
    public Select2(WebElement selectElement) {
        init(selectElement);
        this.webDriverWait = new WebDriverWait(driver, Duration.of(5, ChronoUnit.SECONDS));
    }

    private void init(WebElement selectElement) {
        String tagName = selectElement.getTagName();
        if ("select".equalsIgnoreCase(tagName)) {
            this.selectElement = selectElement;
        } else {
            throw new UnexpectedTagNameException("select", tagName);
        }
        try {
            this.containerElement = selectElement.findElement(By.xpath("./../span")); //./../span
        } catch (NoSuchElementException ex) {
            throw new UnexpectedSelect2StructureException(selectElement);
        }
        driver = ((RemoteWebElement) selectElement).getWrappedDriver();
    }

    @Override
    public void selectByIndex(int index, boolean... isOpened) {
        if (!(isOpened.length > 0 && isOpened[0])) {
            expandContainerElement();
        }
        getWebElementOptions().get(index).click();
    }

    @Override
    public void selectByIndex(int[] index, boolean... isOpened) {
        if (!(isOpened.length > 0 && isOpened[0])) {
            expandContainerElement();
        }
        List<WebElement> optionList = getWebElementOptions();
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
        boolean isDynamicData;
        if (!isOpened) {
            expandContainerElement();
        }

        WebElement selectSelection = containerElement.findElement(By.cssSelector(".select2-selection"));
        isDynamicData = selectSelection.getAttribute("aria-activedescendant") == null;

        if (!containerElement.findElements(By.cssSelector("[type='search']")).isEmpty()) {
            if (selectSelection.getAttribute("class").contains("--single")) {
                optionalSearch(query, isDynamicData);
            } else {
                multiSearch(query, isDynamicData);
            }
        } else {
            List<WebElement> outsideSearch = driver.findElements(By.xpath("//span[contains(@class, 'select2-search') and not(contains(@class, 'select2-search--hide'))]/input[@class='select2-search__field']\n"));
            if (outsideSearch.size() == 1) {
                outsideSearch.get(0).sendKeys(query);
                if (isDynamicData) {
                    waitUntilLoadingEnd();
                }
            }
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

        List<WebElement> optionList = getWebElementOptions();

        return Boolean.parseBoolean(optionList.stream()
                .filter(option -> option.getText().equalsIgnoreCase(text)).findFirst()
                .orElseThrow(() -> new Select2NoOptionPresentException(text, optionList.stream()
                        .map(WebElement::getText)
                        .toList())).getAttribute("aria-disabled"));
    }

    @Override
    public boolean isOptionPresentByText(String query, boolean... isOpened) {
        boolean isDynamicData, result;
        if (!(isOpened.length > 0 && isOpened[0])) {
            expandContainerElement();
        }

        WebElement selectSelection = containerElement.findElement(By.cssSelector(".select2-selection"));
        isDynamicData = selectSelection.getAttribute("aria-activedescendant") == null;

        if (selectSelection.getAttribute("class").contains("--single")) {
            optionalSearch(query, isDynamicData);
        } else {
            multiSearch(query, isDynamicData);
        }

        result = getWebElementOptions().stream()
                .anyMatch(option -> option.getText().equalsIgnoreCase(query));
        collapseContainerElement();
        return result;
    }

    @Override
    public void removeSelectedOption() {
        containerElement.findElement(By.cssSelector(".select2-selection__clear")).click();
        forceCollapseContainerElement();
    }

    @Override
    public void removeSelectedOption(String text) {
        try {
            containerElement.findElement(By.xpath("//span[text()='" + text + "']/preceding-sibling::button")).click();
        } catch (NoSuchElementException ex) {
            throw new OptionIsNotSelectedException(text);
        }
        forceCollapseContainerElement();
    }

    @Override
    public void removeAllSelectedOptions() {
        List<WebElement> list = containerElement.findElements(By.xpath(".//li[@class='select2-selection__choice']"));
        while (!list.isEmpty()) {
            list.get(0).findElement(By.cssSelector("span")).click();
            list = containerElement.findElements(By.xpath(".//li[@class='select2-selection__choice']"));
        }
        forceCollapseContainerElement();
    }

    @Override
    public void removeSelectedOptions(List<String> textList) {
        for (String text : textList) {
            containerElement.findElements(By.xpath(".//li[@class='select2-selection__choice']"))
                    .stream().filter(el -> el.getAttribute("title").equalsIgnoreCase(text))
                    .findFirst()
                    .orElseThrow(() -> new Select2NoOptionPresentException(text, getWebElementOptions().stream().map(WebElement::getText).toList()))
                    .findElement(By.cssSelector("span")).click();
        }
        forceCollapseContainerElement();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getSelectedOptionText() {
        return (T) containerElement.findElement(By.cssSelector(".select2-selection__rendered,.select2-selection__choice")).getText();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> getMultiSelectedOptionsText() {
        List<T> resultList = new ArrayList<>();
        List<WebElement> listOfOptions = containerElement.findElements(By.cssSelector(".select2-selection__choice"));
        listOfOptions.forEach(liElement -> {
            String remove = liElement.findElement(By.xpath(".//*[contains(@class,'choice__remove')]")).getText();
            String optionText = liElement.getText().replace(remove, "").trim();
            resultList.add((T) optionText);
        });
        return resultList;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> getOptions() {
        expandContainerElement();
        List<T> optionList = getWebElementOptions().stream().map(element -> (T) element.getText()).collect(Collectors.toList());
        collapseContainerElement();
        return optionList;
    }

    private List<WebElement> getWebElementOptions() {
        select2DropDownElement = selectElement.findElement(By.xpath(SELECT_2_DROPDOWN_LOCATOR));
        return select2DropDownElement.findElements(By.cssSelector(SELECT_2_RESULTS_LI));
    }

    private void expandContainerElement() {
        clickAtSelect2ContainerElement();
        try {
            webDriverWait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(selectElement, By.xpath(SELECT_2_DROPDOWN_LOCATOR)));
        } catch (TimeoutException ex) {
            throw new Select2DropdownNotOpenedException(selectElement);
        }
    }

    private void collapseContainerElement() {
        if (select2DropDownElement.isDisplayed()) {
            clickAtSelect2ContainerElement();
        }
    }

    private void forceCollapseContainerElement() {
        clickAtSelect2ContainerElement();
    }

    private void clickAtSelect2ContainerElement() {
        new Actions(driver).moveToElement(containerElement, containerElement.getRect().getWidth() / 2 - 1, 0).click().build().perform();
    }

    private void selectSingleOption(String query) {
        List<WebElement> optionsList = getWebElementOptions();
        optionsList
                .stream()
                .filter(optionText -> optionText.getText().equalsIgnoreCase(query))
                .findFirst()
                .orElseThrow(() -> new Select2NoOptionPresentException(query, optionsList.stream()
                        .map(WebElement::getText)
                        .toList())).click();
    }

    private void optionalSearch(String query, boolean isDynamicData) {
        select2DropDownElement = selectElement.findElement(By.xpath(SELECT_2_DROPDOWN_LOCATOR));
        List<WebElement> searchField = select2DropDownElement.findElements(SELECT_2_SEARCH_FIELD_LOCATOR);
        if (!searchField.isEmpty()) {
            searchField.get(0).clear();
            searchField.get(0).sendKeys(query);
            if (isDynamicData) {
                waitUntilLoadingEnd();
            }
        }
    }

    private void multiSearch(String query, boolean isDynamicData) {
        WebElement searchField = containerElement.findElement(By.cssSelector("[type='search']"));
        searchField.clear();
        searchField.sendKeys(query);
        if (isDynamicData) {
            waitUntilLoadingEnd();
        }
        select2DropDownElement = selectElement.findElement(By.xpath(SELECT_2_DROPDOWN_LOCATOR));
    }

    private void waitUntilLoadingEnd() {
        try {
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".loading-results")));
            webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".loading-results")));
        } catch (TimeoutException ex) {
            //do nothing
        }
    }
}
