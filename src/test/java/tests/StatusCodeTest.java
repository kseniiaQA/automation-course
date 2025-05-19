package tests;

import com.microsoft.playwright.*;
import config.StatusConfig;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Properties;

public class StatusCodeTest {
    Playwright playwright;
    Browser browser;
    Page page;
    private StatusConfig config;

    // mvn -Denv=dev -Dtest=tests.StatusCodeTest#test200 test команды запуска

    @BeforeEach
    public void setup() {
        Properties props = new Properties();
        String env = System.getProperty("env", "dev");
        String configFileName = "config-" + env + ".properties";

        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFileName)) {
            if (input == null) {
                throw new RuntimeException("Не найден файл конфигурации: " + configFileName);
            }
            props.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при загрузке файла конфигурации", e);
        }

        config = ConfigFactory.create(StatusConfig.class, props);
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        page = browser.newPage();
        System.out.println("baseUrl из файла: " + config.baseUrl());
    }

    @Test
    public void test200() {
        String url = config.baseUrl() + "/status_codes/200";
        Response response = page.navigate(url);
        int statusCode = response.status();
        Assertions.assertEquals(200, statusCode);
    }

    @AfterEach
    public void teardown() {
        if (page != null) page.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}