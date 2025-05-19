package courseplayw;

import com.github.javafaker.Faker;
import com.microsoft.playwright.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class DynamicContentFakerTest {


    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;

    Faker faker = new Faker();
    String mockName = faker.name().fullName();

    @BeforeEach
    void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        context = browser.newContext();
        page = context.newPage();

        page.route("**/dynamic_content", route -> {
            route.fulfill(new Route.FulfillOptions()
                    .setBody("<div class='large-10 columns'>" + mockName + "</div>"));
        });
    }

    @Test
    void testDynamicContentFaker() {
        // Запуск теста
        page.navigate("https://the-internet.herokuapp.com/dynamic_content");
        Locator content = page.locator(".large-10.columns");
        assert content.textContent().contains(mockName);
    }


    @AfterEach
    void tearDown() {
        context.close();
        browser.close();
        playwright.close();
    }
}
