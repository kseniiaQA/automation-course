package courseplayw;

import com.microsoft.playwright.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ParallelParametrization {

    @ParameterizedTest
    @CsvSource({
            "chromium, /dropdown",
            "firefox, /dropdown",
            "firefox, /checkboxes",
            "chromium, /checkboxes",
            "chromium, /exit_intent",
            "firefox, /exit_intent"
    })
    void testLoginPage(String browserType, String path) {
        try (Playwright playwright = Playwright.create()) {
            BrowserType type = switch (browserType.toLowerCase()) {
                case "chromium" -> playwright.chromium();
                case "firefox" -> playwright.firefox();
                default -> throw new IllegalArgumentException("Неподдерживаемый браузер: " + browserType);
            };

            try (Browser browser = type.launch(new BrowserType.LaunchOptions().setHeadless(true))) {
                try (BrowserContext context = browser.newContext()) {
                    Page page = context.newPage();
                    page.navigate("https://the-internet.herokuapp.com" + path);
                    assertThat(page.title()).isNotEmpty();
                }
            }
        }
    }
}