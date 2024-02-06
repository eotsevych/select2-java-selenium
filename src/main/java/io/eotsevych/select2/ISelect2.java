package io.eotsevych.select2;

import java.util.List;

/**
 * Interface representing a component that interacts with Select2 UI elements.
 * Implementations of this interface provide methods for working with Select2 dropdowns.
 */
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
     * @return {@code true} if the option is disabled, {@code false} otherwise.
     */
    boolean isOptionDisabledByText(String text, boolean... isOpened);

    /**
     * Verify is option from Select2 UI element present by text
     *
     * @param isOpened - Parameter indicating whether the dropdown is already open
     * @param text     - value text
     * @return {@code true} if the option is disabled, {@code false} otherwise.
     */
    boolean isOptionPresentByText(String text, boolean... isOpened);

    /**
     * Remove selected option from single Select UI Element
     */
    void removeSelectedOption();

    /**
     * Remove selected option from Select2 UI element
     *
     * @param text - elected text option
     */
    void removeSelectedOption(String text);

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
     * @param <T> The type parameter for the selected option text.
     * @return selected option text
     */
    <T> T getSelectedOptionText();

    /**
     * Get all options text from single Select
     *
     * @param <T> The type parameter for the options text.
     * @return A list of options text.
     */
    <T> List<T> getOptions();

    /**
     * Get List of selected values text from multi Select
     *
     * @param <T> The type parameter for the options text.
     * @return A list selected options text.
     */
    <T> List<T> getMultiSelectedOptionsText();
}
