package courseplayw;

import com.microsoft.playwright.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParametrizedTestExample {

    @ParameterizedTest(name = "Проверка заголовка")
    @CsvSource({
            "chromium",
            "firefox",
    })
    void testLoginPage(String browserType) {
        // 1. Создаем экземпляр Playwright
        try (Playwright playwright = Playwright.create()) {

            // 2. Выбираем тип браузера
            BrowserType type = switch (browserType.toLowerCase()) {
                case "chromium" -> playwright.chromium();
                case "firefox" -> playwright.firefox();
                default -> throw new IllegalArgumentException("Неподдерживаемый браузер: " + browserType);
            };

            // 3. Запускаем браузер
            try (Browser browser = type.launch(new BrowserType.LaunchOptions().setHeadless(true))) {

                // 4. Создаем контекст и страницу
                try (BrowserContext context = browser.newContext()) {
                    Page page = context.newPage();

                    page.navigate("https://the-internet.herokuapp.com/login");
                    assertEquals("The Internet", page.title());
                }
            }
        }
    }

    @ParameterizedTest(name = "Проверка добавления и удаления элементов")
    @CsvSource({
            "chromium",
            "firefox",
    })
    void testAddRemoveElements(String browserType) {
        // 1. Создаем экземпляр Playwright
        try (Playwright playwright = Playwright.create()) {

            // 2. Выбираем тип браузера
            BrowserType type = switch (browserType.toLowerCase()) {
                case "chromium" -> playwright.chromium();
                case "firefox" -> playwright.firefox();
                default -> throw new IllegalArgumentException("Неподдерживаемый браузер: " + browserType);
            };

            // 3. Запускаем браузер
            try (Browser browser = type.launch(new BrowserType.LaunchOptions().setHeadless(true))) {

                // 4. Создаем контекст и страницу
                try (BrowserContext context = browser.newContext()) {
                    Page page = context.newPage();

                    page.navigate("https://the-internet.herokuapp.com/add_remove_elements/");
                    page.click("button:text('Add Element')");
                    assertTrue(page.isVisible("button.added-manually"));

                }
            }
        }
    }
}