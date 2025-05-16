package courseplayw;

import com.microsoft.playwright.*;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HoverTest {
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
    @Story("Проверка работы ховера")
    @DisplayName("Тестирование наведения на объект мышкой")
    @Severity(SeverityLevel.CRITICAL)
    void hoverTest() {
        page.navigate("https://the-internet.herokuapp.com/hovers");

        // Найти все элементы с классом .figure
        Locator figures = page.locator(".figure");
        int count = figures.count();

        for (int i = 0; i < count; i++) {
            Locator figure = figures.nth(i);
            figure.hover();

            // Проверить текст "View profile"
            Locator profileText = figure.locator("text=View profile");
            assert profileText.isVisible();

            // Кликнуть на ссылку
            profileText.click();
            assert page.url().contains("/users/" + (i + 1));

            page.goBack();
        }
    }
}
