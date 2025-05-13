package courseplayw;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.junit.jupiter.api.*;

import java.util.Collections;

public class StatusCodeInterceptionTest {
    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;


    @BeforeEach
    void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        context = browser.newContext();
        page = context.newPage();

        // Перехват запроса к /status_codes/404
        context.route("**/status_codes/404", route -> {
            route.fulfill(new Route.FulfillOptions()
                    .setStatus(200) // Устанавливаем статус 200
                    .setHeaders(Collections.singletonMap("Content-Type", "text/html"))
                    .setBody("<h3>Mocked Success Response</h3>")
            );
        });
    }

    @Test
    void testMockedStatusCode() {
        page.navigate("https://the-internet.herokuapp.com/status_codes");

        // Ожидаем ответа перед кликом
        Response response = page.waitForResponse("**/status_codes/404", () -> {
            // Клик по ссылке "404"
            Locator button404 = page.locator("a:has-text('404')");
            button404.click();
        });

        // Проверка мок-текста
        String mockedTitle = page.textContent("h3");
        Assertions.assertEquals("Mocked Success Response", mockedTitle);

        // Проверка статуса ответа
        Assertions.assertEquals(200, response.status());
    }




    @AfterEach
    void tearDown() {
        context.close();
        browser.close();
        playwright.close();
    }
}