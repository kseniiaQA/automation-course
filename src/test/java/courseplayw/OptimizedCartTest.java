package courseplayw;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OptimizedCartTest {
    static Playwright playwright;
    static Browser browser;
    static ProductApi productApi;

    @BeforeAll
    static void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        BrowserContext context = browser.newContext();

        productApi = mock(ProductApi.class);
        when(productApi.getProduct(1, "Product 1", 2)).thenReturn(String.valueOf(new Product("Product 1")));
        when(productApi.getProduct(2, "Product 2", 1)).thenReturn(String.valueOf(new Product("Product 2")));
        when(productApi.getProduct(3, "Product 3", 1)).thenReturn(String.valueOf(new Product("Product 3")));
        when(productApi.getProduct(4, "Product 4", 1)).thenReturn(String.valueOf(new Product("Product 4")));

        context.close();
    }

    @Test
    void testAddMultipleProductsToCart() {
        BrowserContext context = browser.newContext();
        Page page = context.newPage();

        page.navigate("https://ginandjuice.shop");
        page.locator("a.account-icon").click();
        page.locator("input[name='username']").fill("carlos");
        page.locator("button:text('Log in')").click();
        page.locator("input[name='password']").fill("hunter2");
        page.locator("button:text('Log in')").click();
        page.locator("div.scanme-logo").click();

        page.locator("h3:text('Pineapple Edition Cocktail')").click();
        addToCart(page, "Product 1");

        page.locator("h3:text('Pineapple Edition Cocktail')").click();
        addToCart(page, "Product 2");

        page.locator("h3:text('Pineapple Edition Cocktail')").click();
        addToCart(page, "Product 3");

        page.locator("a.cart-icon").click();

        Locator textLocator = page.locator("text='$91.50'");
        assertTrue(textLocator.isVisible(), "Текст '$91.50'  виден на странице");

        context.close();
    }
    private void addToCart(Page page, String productName) {
        Locator addToCartButton = page.locator("button:text('Add to cart')");
        addToCartButton.waitFor(new Locator.WaitForOptions().setTimeout(3000));
        addToCartButton.click();
        System.out.println(productName + " добавлен в корзину");
    }


    @AfterAll
    static void tearDown() {
        browser.close();
        playwright.close();
    }
}
