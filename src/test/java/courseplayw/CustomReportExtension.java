package courseplayw;

import com.microsoft.playwright.Page;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
public class CustomReportExtension implements TestWatcher, BeforeEachCallback, AfterEachCallback {
    private static final List<TestResult> results = new ArrayList<>();
    private long startTime;
    private Page page;

    @Override
    public void beforeEach(ExtensionContext context) {
        startTime = System.currentTimeMillis();
        page = ((BaseTest) context.getRequiredTestInstance()).getPage();
    }

    @Override
    public void afterEach(ExtensionContext context) {
        // Очистка контекста (если требуется)
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        results.add(new TestResult(
                context.getDisplayName(),
                "Passed",
                System.currentTimeMillis() - startTime,
                null,
                null
        ));
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        BaseTest testInstance = (BaseTest) context.getRequiredTestInstance();
        Page page = testInstance.getPage();

        if (page != null) {
            String screenshotPath = "screenshots/" + context.getDisplayName() + ".png";
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(screenshotPath)));
            results.add(new TestResult(
                    context.getDisplayName(),
                    "Failed",
                    System.currentTimeMillis() - startTime,
                    cause.getMessage(),
                    screenshotPath
            ));
        } else {
            System.err.println("Page is null, cannot take a screenshot");
            results.add(new TestResult(
                    context.getDisplayName(),
                    "Failed",
                    System.currentTimeMillis() - startTime,
                    cause.getMessage(),
                    null
            ));
        }
    }
    public static List<TestResult> getResults() {
        return results;
    }
}