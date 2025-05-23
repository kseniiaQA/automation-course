package courseplayw;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class CompareScreenshots {

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
    }

    @Test
    void testHomePageVisual() throws IOException {
        page.navigate("https://example.com");
        Path actual = Paths.get("actual.png");
        page.screenshot(new Page.ScreenshotOptions().setPath(actual));

        long mismatch = Files.mismatch(actual, Paths.get("fullpage.png"));
        assertThat(mismatch).isEqualTo(-1); // -1 = файлы идентичны
    }

    @AfterEach
    void tearDown() {

        context.close();
        browser.close();
        playwright.close();
    }

}
