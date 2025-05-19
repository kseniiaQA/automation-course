package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;


public class DynamicControlsPage {
    private final Page page;

    public DynamicControlsPage(Page page) {
        this.page = page;
    }

    public void clickRemoveButton() {
        page.locator("button:has-text('Remove')").click();
        page.waitForTimeout(3000);
    }

    public boolean isCheckboxVisible() {
        return page.locator("#checkbox").isVisible();

    }

    public void clickAddButton() {
        page.locator("button:text('Add')").click();
        page.waitForTimeout(3000);
    }

    public boolean isAddButtonVisible() {
        return page.locator("text=It's gone!").isVisible();
    }

    public boolean isGoneVisible() {
        return page.locator("button:text('Add')").isVisible();
    }
}