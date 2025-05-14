package courseplayw;


import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.io.ByteArrayInputStream;


public class AllureScreenshotOnFailureExtension implements TestWatcher {

    private static Page page;

    public static void setPage(Page pageInstance) {
        page = pageInstance;
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        if (page != null) {
            try {
                byte[] screenshotBytes = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
                // Прикрепляем скриншот в Allure
                Allure.addAttachment("Скриншот при падении теста",
                        "image/png",
                        new ByteArrayInputStream(screenshotBytes),
                        ".png");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
