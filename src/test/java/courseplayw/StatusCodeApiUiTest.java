package courseplayw;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

public class StatusCodeApiUiTest {
    private Playwright playwright;
    private APIRequestContext apiRequest;
    private Browser browser;
    private Page page;

    @BeforeEach
    void setup() {
        playwright = Playwright.create();

        // Настройка API контекста
        apiRequest = playwright.request().newContext(
                new APIRequest.NewContextOptions()
                        .setBaseURL("https://the-internet.herokuapp.com")
        );

        // Настройка браузера
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
                        .setSlowMo(500)
        );

        page = browser.newPage();
        page.setDefaultTimeout(60000);

        // Навигация на страницу статус кодов один раз
        page.navigate("https://the-internet.herokuapp.com/status_codes");
        page.waitForSelector("div.example");
    }

    @Test
    void testStatusCodesCombined() {
        getApiStatusCode(200);
        getUiStatusCode(200);
        getApiStatusCode(404);
        getUiStatusCode(404);
    }

    private int getApiStatusCode(int code) {
        APIResponse response = apiRequest.get("/status_codes/" + code);
        return response.status();
    }

    private int getUiStatusCode(int code) {
        try {
            Locator link = page.locator("text=" + code).first();

            Response response = page.waitForResponse(
                    res -> res.url().endsWith("/status_codes/" + code),
                    () -> link.click(new Locator.ClickOptions().setTimeout(15000))
            );

            page.goBack(); // Возврат на исходную страницу после клика
            return response.status();

        } catch (Exception e) {
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get("screenshots/error-" + code + ".png")));
            throw new RuntimeException("Ошибка при проверке UI для кода " + code, e);
        }
    }

    @AfterEach
    void teardown() {
        if (page != null) page.close();
        if (browser != null) browser.close();
        if (apiRequest != null) apiRequest.dispose();
        if (playwright != null) playwright.close();
    }
}

