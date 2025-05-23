package courseplayw;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.nio.file.Paths;

@ExtendWith(CustomReportExtension.class)
public class BaseTest {
    protected static Playwright playwright;
    protected static Browser browser;
    protected Page page;



    @BeforeAll
    static void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
    }

    @BeforeEach
    void createPage() {
        page = browser.newPage();
    }

    @AfterAll
    static void teardown() {
        browser.close();
        playwright.close();
        // Генерация отчета после всех тестов
        HtmlReportGenerator.generateReport(
                CustomReportExtension.getResults(),
                "test-report.html"
        );
    }


    public Page getPage() {
        return page;
    }
}