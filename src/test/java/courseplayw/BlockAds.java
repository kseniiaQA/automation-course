//package courseplayw;
//
//import com.microsoft.playwright.*;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//public class BlockAds {
//    Playwright playwright;
//    Browser browser;
//    BrowserContext context;
//    Page page;
//
//
//    @BeforeEach
//    void setUp() {
//        playwright = Playwright.create();
//        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
//        context = browser.newContext();
//        page = context.newPage();
//
//    }
//
//
//    @Test
//    void testContentWithoutAds() {
//        page.route("**/*", route -> {
//            String url = route.request().url();
//            if (url.contains("yandex")) {
//                route.abort(); // Блокируем запросы к рекламе
//            } else {
//                route.resume(); // Продолжаем остальные запросы
//            }
//        });
//        long startTimeWithBlock = System.currentTimeMillis();
//        // Навигация на сайт
//        page.navigate("https://dzen.ru/");
//        // Проверяем отсутствие рекламных элементов
//        Locator adLocator =   page.locator("span.yrw-content");
//        Assertions.assertEquals(0, adLocator.count(), "На странице должны отсутствовать рекламные элементы.");
//
//        // Замер времени загрузки с блокировкой рекламы
//        long loadTimeWithBlock = System.currentTimeMillis() - startTimeWithBlock;
//
//        // Выводим время загрузки
//        System.out.println("Время загрузки с блокировкой рекламы: " + loadTimeWithBlock + " мс");
//        // Проверка времени загрузки с блокировкой рекламы
//        Assertions.assertTrue(loadTimeWithBlock < 3000, "Время загрузки страницы с блокировкой рекламы должно быть менее 3 секунд.");
//
//    }
//
//    @Test
//    void testContentWithAds() {
//        // Замер времени загрузки без блокировки рекламы
//        long startTimeWithoutBlock = System.currentTimeMillis();
//        page.navigate("https://dzen.ru/");
//        page.waitForTimeout(20000);
//        long loadTimeWithoutBlock = System.currentTimeMillis() - startTimeWithoutBlock;
//        System.out.println("Время загрузки без блокировки рекламы: " + loadTimeWithoutBlock + " мс");
//        Assertions.assertTrue(loadTimeWithoutBlock > 3000, "Время загрузки страницы с блокировкой рекламы более 3 секунд.");
//
//    }
//
//    @AfterEach
//    void tearDown() {
//        context.close();
//        browser.close();
//        playwright.close();
//    }
//}
//
//
