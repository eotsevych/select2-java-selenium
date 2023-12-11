package io.eotsevych.select2;

import java.util.List;

public interface ISelect2 {

    /**
     * Select option from Select2 UI element by option text.
     *
     * @param text          - Option text to be selected
     * @param isOpened      - Parameter indicating whether the dropdown is already open
     * @param closeOnSelect - Parameter indicating whether the dropdown is closed when a result is selected
     */
    void selectByText(String text, boolean isOpened, boolean closeOnSelect);

    /**
     * Select option from Select2 UI element by option text
     *
     * @param text     - option text
     * @param isOpened - Optional parameter indicating whether the dropdown is already open
     */
    void selectByText(String text, boolean... isOpened);

    /**
     * Select multiple options from Select2 UI element by indexes
     *
     * @param index    - index array
     * @param isOpened - Optional parameter indicating whether the dropdown is already open
     */
    void selectByIndex(int[] index, boolean... isOpened);

    /**
     * Select multiple options from Select2 UI element by option text.
     *
     * @param text     - List of option values to be selected
     * @param isOpened - Optional parameter indicating whether the dropdown is already open
     */
    void selectByText(List<String> text, boolean... isOpened);

    /**
     * Select multiple options from Select2 UI element by option text.
     *
     * @param text          - Option text to be selected
     * @param isOpened      - Parameter indicating whether the dropdown is already open
     * @param closeOnSelect - Parameter indicating whether the dropdown is closed when a result is selected
     */
    void selectByText(List<String> text, boolean isOpened, boolean closeOnSelect);

    /**
     * Select option from Select2 UI element by option index
     *
     * @param index    - option index
     * @param isOpened - Parameter indicating whether the dropdown is already open
     */
    void selectByIndex(int index, boolean... isOpened);

    /**
     * Verify is option from Select2 UI element disabled by text
     *
     * @param isOpened - Parameter indicating whether the dropdown is already open
     * @param text     - value text
     */
    boolean isOptionDisabledByText(String text, boolean... isOpened);

    /**
     * Verify is option from Select2 UI element present by text
     *
     * @param isOpened - Parameter indicating whether the dropdown is already open
     * @param text     - value text
     */
    boolean isOptionPresentByText(String text, boolean... isOpened);

    /**
     * Remove selected option from single Select UI Element
     */
    void removeSelectedOption();

    /**
     * Remove all selected options from multi Select UI Element
     */
    void removeAllSelectedOptions();

    /**
     * Remove selected options from Select2 UI element
     *
     * @param textList - List of selected text options
     */
    void removeSelectedOptions(List<String> textList);

    /**
     * Get selected option text from single Select
     *
     * @return selected option text
     */
    <T> T getSelectedOptionText();

    /**
     * Get List of selected values text from multi Select
     *
     * @return List of selected options text
     */
    <T> List<T> getMultiSelectedOptionsText();
}
