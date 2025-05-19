package pages;

import com.microsoft.playwright.*;

public class TestContext {
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    public TestContext() {
        // Initialize Playwright and browser
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
        context = browser.newContext();
        page = context.newPage();
    }

    public Page getPage() {
        return page;
    }

    public void close() {
        if (playwright != null) {
            playwright.close();
        }
    }
}