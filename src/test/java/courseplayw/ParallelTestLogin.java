package courseplayw;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.CONCURRENT)
public class ParallelTestLogin {
    static Playwright playwright;

    @BeforeAll
    static void setup() {
        playwright = Playwright.create();
    }

    @AfterAll
    static void tearDown() {
        if (playwright != null) {
            playwright.close();
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"chromium", "firefox", "webkit"})
    void testLoginPage(String browserType) {
        Browser browser = null;
        BrowserContext context = null;
        Page page = null;

        try {
            switch (browserType.toLowerCase()) {
                case "firefox":
                    browser = playwright.firefox().launch();
                    break;
                case "webkit":
                    browser = playwright.webkit().launch();
                    break;
                case "chromium":
                default:
                    browser = playwright.chromium().launch();
                    break;
            }

            context = browser.newContext();
            page = context.newPage();

            page.navigate("https://the-internet.herokuapp.com/login");
            assertEquals("The Internet", page.title());
        } finally {
            if (page != null) page.close();
            if (context != null) context.close();
            if (browser != null) browser.close();
        }
    }
}