package courseplayw;

import com.microsoft.playwright.*;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@ExtendWith(AllureScreenshotOnFailureExtension.class)

public class BlockAds {

    Playwright playwright;
    Browser browser;
    BrowserContext context;
   private Page page;


    @BeforeEach
    void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
        context = browser.newContext(new Browser.NewContextOptions()
                .setRecordVideoDir(Paths.get("videos/")));
        page = context.newPage();

        AllureScreenshotOnFailureExtension.setPage(page);
    }
    @Test
    void testCartActions() {
            page.navigate("https://www.komus.ru/katalog/khozyajstvennye-tovary/mylo/c/4118/?from=menu-g2-proizvodstvo_i_sklady");
            // Добавление товара
            page.locator("div.product-price__add-to-cart").nth(0).click();
            page.locator("span.b-icn.b-icn--mini-cart").screenshot(new Locator.ScreenshotOptions()
                    .setPath(getTimestampPath("cart_after_add.png")));

        // Удаление товара
            page.locator("span.b-icn.b-icn--mini-cart").click();
            page.locator("div.cart-product-item__remove.gtm-remove-item-fromCart").click();
            page.locator("a.cart__link.button.button--primary.button--size-m.gtm-empty-cart-to-catalog").click();

            page.locator("span.b-icn.b-icn--mini-cart").screenshot(new Locator.ScreenshotOptions()
                    .setPath(getTimestampPath("cart_after_remove.png")));

        }

    private Path getTimestampPath(String filename) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        return Paths.get("screenshots/", timestamp, filename);
    }

    @Test
    void testContentWithoutAds() {
        page.route("**/*", route -> {
            String url = route.request().url();
            if (url.contains("yandex")) {
                route.abort(); // Блокируем запросы к рекламе
            } else {
                route.resume(); // Продолжаем остальные запросы
            }
        });
        long startTimeWithBlock = System.currentTimeMillis();
        // Навигация на сайт
        page.navigate("https://dzen.ru/");
        // Проверяем отсутствие рекламных элементов
        Locator adLocator =   page.locator("span.yrw-content");
        Assertions.assertEquals(0, adLocator.count(), "На странице должны отсутствовать рекламные элементы.");

        // Замер времени загрузки с блокировкой рекламы
        long loadTimeWithBlock = System.currentTimeMillis() - startTimeWithBlock;

        // Выводим время загрузки
        System.out.println("Время загрузки с блокировкой рекламы: " + loadTimeWithBlock + " мс");
        // Проверка времени загрузки с блокировкой рекламы
        Assertions.assertTrue(loadTimeWithBlock < 3000, "Время загрузки страницы с блокировкой рекламы должно быть менее 3 секунд.");

    }

    @Test
    void testContentWithAds() {
        // Замер времени загрузки без блокировки рекламы
        long startTimeWithoutBlock = System.currentTimeMillis();
        page.navigate("https://dzen.ru/");
        page.waitForTimeout(20000);
        long loadTimeWithoutBlock = System.currentTimeMillis() - startTimeWithoutBlock;
        System.out.println("Время загрузки без блокировки рекламы: " + loadTimeWithoutBlock + " мс");
        Assertions.assertTrue(loadTimeWithoutBlock > 3000, "Время загрузки страницы с блокировкой рекламы более 3 секунд.");

    }




    @AfterEach
    void tearDown() {
        context.close();
        browser.close();
        playwright.close();
    }
}


