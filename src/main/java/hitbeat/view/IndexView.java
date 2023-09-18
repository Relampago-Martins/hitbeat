package hitbeat.view;

import hitbeat.styles.Styles;
import hitbeat.view.base.mementos.ContentCaretaker;
import hitbeat.view.base.mementos.ContentMemento;
import hitbeat.view.base.widgets.SVGWidget;
import hitbeat.view.base.widgets.Widget;
import hitbeat.view.footer.Footer;
import hitbeat.view.sidebar.Sidebar;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class IndexView extends Application {
    private static final double BACK_BUTTON_SIZE = 30;

    private BorderPane root;
    private Scene scene;
    private Sidebar sidebar;
    private Node content;
    private ContentCaretaker caretaker = new ContentCaretaker();
    private StartPage startPage = new StartPage();

    @Override
    public void start(Stage primaryStage) throws Exception {
        root = new BorderPane();
        content = new StartPage().build();
        root = new BorderPane();

        // Sidebar
        VBox sidebar = new VBox(10);
        MFXButton button1 = new MFXButton("Sidebar Item 1");
        MFXButton button2 = new MFXButton("Sidebar Item 2");
        // button colors
        button1.setMinWidth(200);
        button2.setMinWidth(200);

        button1.setStyle(Styles.SIDEBAR_BUTTONS); 
        button2.setStyle(Styles.SIDEBAR_BUTTONS); 
        
        sidebar.getChildren().addAll(button1, button2);

        // Content area
        CenterOne content = new CenterOne();

        Footer footer = new Footer();

        root.setLeft(sidebar);
        root.setCenter(content);

        Scene scene = new Scene(root, 800, 700);
        scene.getStylesheets().add(
                getClass().getResource("/hitbeat/css/index.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("HitBeat");
        primaryStage.show();
    }

    public void restoreLastState() {
        content = restoreFromMemento(caretaker.getLastMemento());
        root.setCenter(content);
    }

    public void setContent(Widget contentWidget) {
        caretaker.addMemento(saveToMemento(root.getCenter()));

        this.content = contentWidget.build();
        root.setCenter(wrapContentWithBackButton());
    }

    private StackPane wrapContentWithBackButton() {
        StackPane contentWrapper = new StackPane(this.content);
        MFXButton backButton = createBackButton();

        contentWrapper.getChildren().add(backButton);

        return contentWrapper;
    }

    private MFXButton createBackButton() {
        SVGWidget backButtonIcon = new SVGWidget("/hitbeat/svg/back-button.svg", BACK_BUTTON_SIZE, Color.WHITE);

        MFXButton backButton = new MFXButton("");
        backButton.setGraphic(backButtonIcon.build());
        backButton.setStyle("-fx-background-color: transparent;");
        StackPane.setAlignment(backButton, Pos.TOP_LEFT);
        StackPane.setMargin(backButton, new Insets(10));

        backButton.setOnAction(e -> restoreLastState());

        return backButton;
    }

    private ContentMemento saveToMemento(Node state) {
        return new ContentMemento(state);
    }

    private static Node restoreFromMemento(ContentMemento memento) {
        return memento.getContentState();
    }

    private void activateMaterialFX() {
        MFXThemeManager.addOn(scene, Themes.DEFAULT);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
