package courseplayw;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.Cookie;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.microsoft.playwright.BrowserContext;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OptimizedLoginTest {

    static Playwright playwright;
    static Browser browser;
    static String backtraceGuid;
    static String backtraceLastActive;

    // Создаём список cookies


    @BeforeAll
    static void globalSetup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));

        // Аутентификация один раз перед всеми тестами
        BrowserContext context = browser.newContext();
        Page page = context.newPage();

        page.navigate("https://www.saucedemo.com/");
        page.locator("#user-name").fill("standard_user");
        page.locator("#password").fill("secret_sauce");
        page.locator("#login-button").click();

        // Ждём появления localStorage item 'backtrace-guid' и 'backtrace-last-active'
        page.waitForFunction("() => localStorage.getItem('backtrace-guid') !== null");
        backtraceGuid = (String) page.evaluate("() => localStorage.getItem('backtrace-guid')");
        System.out.println("backtraceGuid: " + backtraceGuid);
        page.waitForFunction("() => localStorage.getItem('backtrace-last-active') !== null");
        backtraceLastActive = (String) page.evaluate("() => localStorage.getItem('backtrace-last-active')");
        context.close();
    }

    @Test
    void testDashboard() {
        // Создаём новый контекст
        BrowserContext context = browser.newContext();
        Page page = context.newPage();

        context.addInitScript("localStorage.setItem('backtrace-guid', '" + backtraceGuid + "');");
        context.addInitScript("localStorage.setItem('backtrace-last-active', '" + backtraceLastActive + "');");

        //добавляем куки перед переходом на страницу
        List<Cookie> ls = new ArrayList<Cookie>();
        Cookie cookie = new Cookie("session-username", "standard_user");
        cookie.setPath("/");
        cookie.setDomain("www.saucedemo.com");
        ls.add(cookie);
        context.addCookies(ls);

        page.navigate("https://www.saucedemo.com/inventory.html");

        Locator cartLink = page.locator("a.shopping_cart_link");
        cartLink.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        assertTrue(cartLink.isVisible());

        context.close();
    }


    @Test
    void testLoginPerformance() {
        long start = System.currentTimeMillis();
        BrowserContext context = browser.newContext();
        Page page = context.newPage();

        boolean shouldTrace = Math.random() < 0.1;

        if (shouldTrace) {
            context.tracing().start(new Tracing.StartOptions()
                    .setSnapshots(true)
                    .setScreenshots(true));
        }

        // добавляем куки перед переходом
        List<Cookie> ls = new ArrayList<>();
        Cookie cookie = new Cookie("session-username", "standard_user");
        cookie.setPath("/");
        cookie.setDomain("www.saucedemo.com");
        ls.add(cookie);
        context.addCookies(ls);

        page.navigate("https://www.saucedemo.com/inventory.html");

        long duration = System.currentTimeMillis() - start;
        assertTrue(duration < 3000,
                "Login took " + duration + "ms (exceeds 3000ms limit)");

        if (duration > 3000) {
            if (shouldTrace) {
                context.tracing().stop(new Tracing.StopOptions()
                        .setPath(Paths.get("slow-trace-" + System.currentTimeMillis() + ".zip")));
            }
            System.out.println("Тест превысил лимит времени: " + duration + "ms");
        } else {
            if (shouldTrace) {
                context.tracing().stop(new Tracing.StopOptions()
                        .setPath(Paths.get("normal-trace-" + System.currentTimeMillis() + ".zip")));
            }
        }

        context.close();
    }

    @AfterAll
    static void tearDown() {
        browser.close();
        playwright.close();
    }
}