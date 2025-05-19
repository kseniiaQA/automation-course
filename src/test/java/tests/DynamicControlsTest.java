package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.DynamicControlsPage;
import pages.TestContext;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class DynamicControlsTest {
    private TestContext context;
    private DynamicControlsPage controlsPage;

    @BeforeEach
    public void setup() {
        context = new TestContext();
        controlsPage = new DynamicControlsPage(context.getPage());
        context.getPage().navigate("https://the-internet.herokuapp.com/dynamic_controls");
    }

    @Test
    public void testCheckboxRemoval() {
        controlsPage.clickRemoveButton();
        assertFalse(controlsPage.isCheckboxVisible());
        controlsPage.clickAddButton();
        assertFalse(controlsPage.isGoneVisible());
        assertFalse(controlsPage.isAddButtonVisible());
    }

    @AfterEach
    public void teardown() {
        context.getPage().close();
    }
}