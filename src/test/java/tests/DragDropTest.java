//package tests;
//
//import courseplayw.BaseTest;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import pages.DragDropPage;
//
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class DragDropTest extends BaseTest {
//
//    private DragDropPage dragDropPage;
//
//    @BeforeEach
//    public void setUp() {
//        dragDropPage = new DragDropPage(page);
//    }
//
//    @Test
//    public void testDragAndDrop() {
//        dragDropPage.navigateTo("https://the-internet.herokuapp.com/drag_and_drop");
//        dragDropPage.dragDropArea().dragAToB();
//        assertEquals("A", dragDropPage.dragDropArea().getTextB());
//    }
//}