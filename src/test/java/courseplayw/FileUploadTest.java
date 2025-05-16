package courseplayw;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.FormData;
import com.microsoft.playwright.options.RequestOptions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

public class FileUploadTest {
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;
    private APIRequest apiRequest;


    @BeforeEach
    void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
        context = browser.newContext();
        page = context.newPage();

        // Инициализация APIRequest
        apiRequest = playwright.request();
    }

    @AfterEach
    void tearDown() {
        if (playwright != null) {
            playwright.close();
        }
    }

    @Test
    void testFileUploadAndDownload() throws IOException {
        Path testFile = Files.createTempFile("test", ".png");
        Files.write(testFile, new byte[]{(byte)0x89, 0x50, 0x4E, 0x47});

        APIResponse uploadResponse = apiRequest.newContext().post(
                "https://httpbin.org/post",
                RequestOptions.create().setMultipart(
                        FormData.create().set("file", testFile)
                )
        );

        String responseBody = uploadResponse.text();
        assertTrue(responseBody.contains("data:image/png;base64"));

        String base64DataUrl = responseBody.split("\"file\": \"")[1].split("\"")[0];
        String[] parts = base64DataUrl.split(",");
        assertTrue(parts.length == 2);
        byte[] receivedBytes = Base64.getDecoder().decode(parts[1]);

        byte[] originalBytes = Files.readAllBytes(testFile);
        assertArrayEquals(originalBytes, receivedBytes);

        APIResponse downloadResponse = apiRequest.newContext().get("https://httpbin.org/image/png");
        assertEquals("image/png", downloadResponse.headers().get("content-type"));

        byte[] content = downloadResponse.body();


        assertEquals(0x89, content[0] & 0xFF);
        assertEquals(0x50, content[1] & 0xFF);
        assertEquals(0x4E, content[2] & 0xFF);
        assertEquals(0x47, content[3] & 0xFF);

        Files.deleteIfExists(testFile);
    }
}