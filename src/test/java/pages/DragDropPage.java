package pages;

import com.microsoft.playwright.Page;
import components.DragDropArea;

public class DragDropPage {

    private Page page;
    private DragDropArea dragDropArea;

    public DragDropPage(Page page) {
        this.page = page;
        this.dragDropArea = new DragDropArea(page);
    }

    public void navigateTo(String url) {
        if (page == null) throw new IllegalStateException("Page is not initialized");
        page.navigate(url);
    }

    public DragDropArea dragDropArea() {
        return dragDropArea;
    }
}