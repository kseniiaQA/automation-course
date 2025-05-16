package courseplayw;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import java.nio.file.Paths;

public class DynamicLoadingTraceTest {

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    @BeforeEach
    void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        context = browser.newContext();
        page = context.newPage();
    }

    @Test
    void testDynamicLoadingWithTrace() {
        // Настройка трассировки
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true));

        // Навигация и действия
        page.navigate("https://the-internet.herokuapp.com/dynamic_loading/1");
        page.click("button"); // Клик на "Start"

        // Ожидание появления текста
        page.locator("#finish").waitFor();
        String text = page.locator("#finish").textContent();

        // Проверка результата
        Assertions.assertEquals("Hello World!", text.trim());

        // Сохранение трассировки
        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("trace-dynamic-loading.zip")));
    }

    @AfterEach
    void tearDown() {
        if (context != null) {
            context.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}