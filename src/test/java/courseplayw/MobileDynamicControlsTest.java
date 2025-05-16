package courseplayw;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MobileDynamicControlsTest {
    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;

    @BeforeEach
    void setUp() {
        playwright = Playwright.create();

        // Настройка параметров iPad Pro 11
        Browser.NewContextOptions deviceOptions = new Browser.NewContextOptions()
                .setUserAgent("Mozilla/5.0 (iPad; CPU OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko)")
                .setViewportSize(834, 1194)
                .setDeviceScaleFactor(2)
                .setIsMobile(true)
                .setHasTouch(true);

        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false)); // Установите headless на false для визуализации
        context = browser.newContext(deviceOptions);
        page = context.newPage();
    }

    @Test
    void testInputEnabling() {
        page.navigate("https://the-internet.herokuapp.com/dynamic_controls");

        Locator inputField = page.locator("input[type='text']");
        Assertions.assertTrue(inputField.isDisabled(), "Input должно быть отключено");

        Locator enable = page.locator("button:has-text('Enable')");
        enable.click();

        Locator disableButton = page.locator("button:has-text('Disable')");
        disableButton.waitFor();

        Assertions.assertTrue(disableButton.isVisible());
        Assertions.assertTrue(inputField.isEnabled(), "Input должно быть отключено");
    }

    @AfterEach
    void tearDown() {
        context.close();
        browser.close();
        playwright.close();
    }
}