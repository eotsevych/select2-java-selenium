package io.eotsevych.select2.exceptions;

import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Exception thrown when the structure of a Select2 element is unexpected.
 * This exception is typically used to indicate issues with the HTML structure of the Select2 dropdown.
 */
public class UnexpectedSelect2StructureException extends RuntimeException {

    private final static String EXPECTED_STRUCTURE_HTML = "<%any block tag%>\n" + "  <select class=\"***\" aria-hidden=\"true\" data-select2-id=\"***\">\n" + "      <option>%text%</option>\n" + "      ...\n" + "  </select>\n" + "  <span class=\"select2 select2-container select2-container--default..\" data-select2-id=\"***\">\n" + "    …\n" + "  </span>\n" + "</%any block tag>";


    /**
     * Constructs an UnexpectedSelect2StructureException.
     *
     * @param selectElement The WebElement representing the Select2 element for which the structure is unexpected.
     */
    public UnexpectedSelect2StructureException(WebElement selectElement) {
        super(String.format("Unexpected Select2 structure: <span> element should be present near <select>:\n" + "Expected:\n" + "%s\n" + "Actual:\n" + "\"%s\"", EXPECTED_STRUCTURE_HTML, Jsoup.parseBodyFragment(selectElement.findElement(By.xpath("../*")).getAttribute("innerHTML"))));


    }
}
