package courseplayw;
import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MobileDragAndDropTest {
    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;

    @BeforeEach
    void setup() {
        playwright = Playwright.create();

        // Ручная настройка параметров Samsung Galaxy S22 Ultra
        Browser.NewContextOptions deviceOptions = new Browser.NewContextOptions()
                .setUserAgent("Mozilla/5.0 (Linux; Android 12; SM-S908B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.0.0 Mobile Safari/537.36")
                .setViewportSize(384, 873)  // Разрешение экрана
                .setDeviceScaleFactor(3.5)
                .setIsMobile(true)
                .setHasTouch(true);

        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext(deviceOptions);
        page = context.newPage();
    }

    @Test
    void testDragAndDropMobile() {
        page.navigate("https://the-internet.herokuapp.com/drag_and_drop");

        Locator columnA = page.locator("#column-a");
        Locator columnB = page.locator("#column-b");

        // 1. Проверка начального состояния
        String draggableAttr = columnA.getAttribute("draggable");
        Assertions.assertEquals("true", draggableAttr, "Элемент должен иметь атрибут draggable='true'");


        columnB.getAttribute("draggable");
        Assertions.assertEquals("true", draggableAttr, "Элемент должен иметь атрибут draggable='true'");

        Locator firstColumn = page.locator("div.column").nth(0);
        Assertions.assertEquals("A", firstColumn.innerText().trim(), "Первый столбец должен содержать 'A'");


        Locator secondColumn = page.locator("div.column").nth(1);
        Assertions.assertEquals("B", secondColumn.innerText().trim(), "Второй столбец должен содержать 'B'");

        // 2. Перетаскивание через JS (т.к. HTML5 DnD не работает в мобильном режиме)
        page.evaluate("() => {\n" +
                "  const dataTransfer = new DataTransfer();\n" +
                "  const event = new DragEvent('drop', { dataTransfer });\n" +
                "  document.querySelector('#column-a').dispatchEvent(new DragEvent('dragstart', { dataTransfer }));\n" +
                "  document.querySelector('#column-b').dispatchEvent(event);\n" +
                "}");
        // 3. Ожидание и проверка

        Assertions.assertEquals("B", firstColumn.innerText().trim(), "Первый столбец должен содержать 'A'");
        Assertions.assertEquals("A", secondColumn.innerText().trim(), "Второй столбец должен содержать 'B'");
    }

    @AfterEach
    void tearDown() {
        context.close();
        browser.close();
        playwright.close();
    }
}