package hitbeat.view.library;

import hitbeat.controller.library.LibraryController;
import hitbeat.util.CustomMP3File;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.collections.ObservableList;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class LibraryPage extends VBox {

    private LibraryController controller;

    private MP3FileList mp3FileList;

    public LibraryPage() {
        super();
        
        controller = new LibraryController(this);

        initializeStyling();
        configureChildren();

        getStylesheets().add("hitbeat/css/library/library.css");
    }

    private void initializeStyling() {
        getStyleClass().add("library-page");
    }

    private void configureChildren() {
        addDragAndDrop();
        addFilesBoxToScrollPane();
        addSaveButton();
    }

    private void addDragAndDrop() {
        DragAndDrop dragAndDrop = new DragAndDrop(controller);
        getChildren().add(dragAndDrop);
    }

    private void addFilesBoxToScrollPane() {
        createConfiguredScrollPane();

        
        // Set a preferred height to your MP3FileList
        mp3FileList.setPrefHeight(400); // You can adjust this value as needed
        
        // Set VBox constraints to make MP3FileList take available vertical space
        VBox.setVgrow(mp3FileList, Priority.ALWAYS);
        

        getChildren().add(mp3FileList);
    }

    private void createConfiguredScrollPane() {
        mp3FileList = new MP3FileList(controller);
    }

    private void addSaveButton() {
        MFXButton saveButton = createSaveButton();
        getChildren().add(saveButton);
    }

    private MFXButton createSaveButton() {
        MFXButton saveButton = new MFXButton("Save");
        saveButton.getStyleClass().addAll("save-button", "button-raised");
        saveButton.setOnAction(e -> {
            controller.saveToDatabase();
            setFilesFromFolder(controller.getFiles());
        });
        return saveButton;
    }

    public void setFilesFromFolder(ObservableList<CustomMP3File> files) {
        mp3FileList.setFiles(files);
    }

    // class SongEditRowCell extends ListCell<CustomMP3File> {
    //     private final SongEditRow songEditRow;

    //     public SongEditRowCell() {
    //         songEditRow = new SongEditRow(null);
    //     }

    //     @Override
    //     protected void updateItem(CustomMP3File file, boolean empty) {
    //         super.updateItem(file, empty);

    //         if (file == null || empty) {
    //             resetCell();
    //         } else {
    //             updateCellWithFile(file);
    //         }

    //         if (deferredLayoutPasses == 0) {
    //             System.out.println("request layout");
    //             Platform.runLater(() -> {
    //                 requestLayout();
    //                 deferredLayoutPasses++;
    //             });
    //         }
    //     }

    //     private void resetCell() {
    //         songEditRow.prefWidthProperty().unbind();
    //         setText(null);
    //         setGraphic(null);
    //         setId("hidden-list-cell");
    //     }

    //     private void updateCellWithFile(CustomMP3File file) {
    //         Margin margin = new Margin(songEditRow, 0, 0, 30, 0);
    //         songEditRow.updateFile(file);
    //         songEditRow.prefWidthProperty().bind(Layout.getInstance().getContentWidth().subtract(60));
    //         setGraphic(margin);
    //         songEditRow.setId("list-cell");
    //     }
    // }
}
