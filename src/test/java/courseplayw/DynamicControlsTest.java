package courseplayw;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DynamicControlsTest {
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;


    @BeforeEach
    @Step("Инициализация браузера и контекста")
    void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext();
        page = context.newPage();
    }

    @Test
    @Story("Проверка теста с динамическими локаторами")
    @DisplayName("Тестирование динамически генерируемых элементов на странице")
    @Severity(SeverityLevel.CRITICAL)
    void hoverTest() {
        page.navigate("https://the-internet.herokuapp.com/dynamic_controls");
        page.locator("#checkbox").click();
        page.locator("button:text('Remove')").click();
        page.waitForSelector("#checkbox", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.DETACHED));
        page.waitForSelector("text=It's gone!", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
        page.locator("button:text('Add')").click();
        page.waitForSelector("#checkbox", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
    }

    @AfterEach
    @Step("Закрытие ресурсов")
    void tearDown() {
        context.close();
        browser.close();
        playwright.close();
    }

}
