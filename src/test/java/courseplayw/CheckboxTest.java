package courseplayw;

import com.microsoft.playwright.*;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CheckboxTest {
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    @BeforeEach
    @Step("Инициализация браузера и контекста")
    void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext();
        page = context.newPage();
    }

    @Test
    @Story("Проверка работы чекбоксов")
    @DisplayName("Тестирование выбора/снятия чекбоксов")
    @Severity(SeverityLevel.CRITICAL)
    void testCheckboxes() {
        navigateToCheckboxesPage();
        verifyInitialState();

        // Инициализация локаторов после загрузки страницы
        Locator firstCheckbox = page.locator("input[type=checkbox]").nth(0);
        Locator secondCheckbox = page.locator("input[type=checkbox]").nth(1);

        toggleCheckboxes(firstCheckbox, secondCheckbox);
    }

    @Step("Переход на страницу /checkboxes")
    private void navigateToCheckboxesPage() {
        page.navigate("https://the-internet.herokuapp.com/checkboxes");
    }

    @Step("Проверка начального состояния чекбоксов")
    private void verifyInitialState() {
        Locator firstCheckbox = page.locator("input[type=checkbox]").nth(0);
        Locator secondCheckbox = page.locator("input[type=checkbox]").nth(1);

        assertThat(firstCheckbox.isChecked()).isFalse();
        assertThat(secondCheckbox.isChecked()).isTrue();
    }

    @Step("Изменение состояния чекбоксов")
    private void toggleCheckboxes(Locator firstCheckbox, Locator secondCheckbox) {
        firstCheckbox.click();
        assertThat(firstCheckbox.isChecked()).isTrue();

        secondCheckbox.click();
        assertThat(secondCheckbox.isChecked()).isFalse();
    }

    @AfterEach
    @Step("Закрытие ресурсов")
    void tearDown() {
        context.close();
        browser.close();
        playwright.close();
    }
}