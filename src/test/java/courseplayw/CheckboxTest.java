package courseplayw;

import com.microsoft.playwright.*;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CheckboxTest extends BaseTest {

    @BeforeAll
    public void setUp() {
        BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                .setRecordVideoDir(Paths.get("videos/"))
                .setRecordVideoSize(1280, 720));
        Page page = context.newPage();
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

    @AfterAll
    void tearDown() {
        browser.close();
    }
}