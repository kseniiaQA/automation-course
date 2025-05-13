//package courseplayw;
//
//import com.microsoft.playwright.*;
//import com.microsoft.playwright.options.WaitForSelectorState;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//public class CartTest {
//
//    Playwright playwright;
//    Browser browser;
//    BrowserContext context;
//    Page page;
//
//    @BeforeEach
//    void setup() {
//        playwright = Playwright.create();
//        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
//        context = browser.newContext(new Browser.NewContextOptions()
//                .setRecordVideoDir(Paths.get("videos/")));
//        page = context.newPage();
//    }
//
//    @Test
//    void testCartActions() {
//        page.navigate("https://www.komus.ru/katalog/khozyajstvennye-tovary/mylo/c/4118/?from=menu-g2-proizvodstvo_i_sklady");
//        Locator addToButton = page.locator("div.product-price__add-to-cart").nth(0);
//
//
//        addToButton.waitFor(new Locator.WaitForOptions().setTimeout(120000).setState(WaitForSelectorState.VISIBLE));
//        // Добавление товара
//        page.locator("div.product-price__add-to-cart").nth(0).click();
//        page.locator("span.b-icn.b-icn--mini-cart").screenshot(new Locator.ScreenshotOptions()
//                .setPath(getTimestampPath("cart_after_add.png")));
//
//        // Удаление товара
//        page.locator("span.b-icn.b-icn--mini-cart").click();
//        page.locator("div.cart-product-item__remove.gtm-remove-item-fromCart").click();
//        page.locator("a.cart__link.button.button--primary.button--size-m.gtm-empty-cart-to-catalog").click();
//
//        page.locator("span.b-icn.b-icn--mini-cart").screenshot(new Locator.ScreenshotOptions()
//                .setPath(getTimestampPath("cart_after_remove.png")));
//    }
//
//    private Path getTimestampPath(String filename) {
//        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
//        return Paths.get("screenshots/", timestamp, filename);
//    }
//
//    @AfterEach
//    void teardown() {
//        if (context != null) {
//            context.close();
//        }
//        if (playwright != null) {
//            playwright.close();
//        }
//    }
//}