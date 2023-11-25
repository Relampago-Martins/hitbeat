package hitbeat.view.tracks;

import hitbeat.controller.Icons;
import hitbeat.controller.MioloController;
import hitbeat.controller.player.PlayerController;
import hitbeat.controller.playlist.PlaylistController;
import hitbeat.controller.tracks.TracksController;
import hitbeat.model.Playlist;
import hitbeat.model.Track;
import hitbeat.view.base.utils.MyButton;
import hitbeat.view.base.widgets.ListTile;
import hitbeat.view.base.widgets.RoundedButton;
import hitbeat.view.base.widgets.SVGWidget;
import hitbeat.view.base.widgets.listview.BaseCell;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class TrackCell extends BaseCell<Track> {
    private Track track;
    private Label title;
    private Label subtitle;
    private MyButton favoriteBtn;
    private ContextMenu contextMenu;
    private Icons icons = new Icons();
    private PlaylistController playlistController = new PlaylistController();
    private MioloController mioloController = MioloController.getInstance();
    private TracksController tracksController = new TracksController();
    private BehaviorSubject<Track> trackRemovedSubject = BehaviorSubject.create();

    public TrackCell(Track track) {
        // Initialize UI components
        initUI();

        // Set track data
        updateItem(track);
    }

    private void initUI() {
        // Create Leading
        RoundedButton playBtn = new RoundedButton("");
        SVGWidget svgPlay = new SVGWidget("/hitbeat/svg/play.svg", 25, Color.WHITE);
        playBtn.setGraphic(svgPlay);

        StackPane playbox = new StackPane(playBtn);
        VBox.setVgrow(playbox, Priority.ALWAYS);

        playbox.setAlignment(Pos.CENTER);
        playbox.setPickOnBounds(false);

        playBtn.setOnMouseClicked(event -> {
            PlayerController.getInstance().playSingleTrack(this.track);
        });

        title = new Label();
        subtitle = new Label();

        title.getStyleClass().add("title");
        subtitle.getStyleClass().add("subtitle");

        ListTile listTile = new ListTile(playbox, title, subtitle, getMenuBtns());
        this.getChildren().add(listTile);
        this.getStylesheets().add(getClass().getResource("/hitbeat/css/track.css").toExternalForm());
    }

    public void setOnTrackRemoved(Consumer<Track> consumer) {
        trackRemovedSubject.subscribe(consumer);
    }

    @Override
    public void updateItem(Track track) {
        this.track = track;

        if (track != null) {
            title.setText(this.track.getName());
            if (this.track.getAlbum().getArtist() != null) {
                subtitle.setText(this.track.getAlbum().getArtist().getName()); // Update if Track has more data
            }
            favoriteBtn.setGraphic(icons.getFavorite(this.track.isFavorite()));
        } else {
            title.setText("");
            subtitle.setText("");
        }
    }

    public HBox getMenuBtns() {
        Node trailingIcon = icons.getOptions();

        favoriteBtn = new MyButton("", icons.getFavorite(false));
        MyButton optionsBtn = new MyButton("", trailingIcon);

        contextMenu = new ContextMenu();
        contextMenu.setAnchorLocation(ContextMenu.AnchorLocation.CONTENT_BOTTOM_RIGHT);
        
        favoriteBtn.setOnMouseClicked(event -> {
            tracksController.toggleFavorite(this.track);
            favoriteBtn.setGraphic(icons.getFavorite(this.track.isFavorite()));
        });
        
        contextMenu.getItems().add( this.getMenuAdd() );

        MenuItem removeItem = new MenuItem("Remover da playlist");
        Object data1 = mioloController.getCurrentState().getData();
        if (data1 instanceof Playlist) {
            removeItem.getStyleClass().add("custom-menu-item");
            contextMenu.getItems().add(removeItem);
        }
        removeItem.setOnAction(event -> {
            Object data = mioloController.getCurrentState().getData();
            if (data instanceof Playlist) {
                Playlist playlist = (Playlist) data;
                playlistController.removeTrack(playlist, this.track);
                trackRemovedSubject.onNext(track);
            }
        });

        optionsBtn.setOnMouseClicked(event -> {
            contextMenu.show(optionsBtn, event.getScreenX(), event.getScreenY());
        });
        HBox menuBtns = new HBox(favoriteBtn, optionsBtn);
        return menuBtns;
    }

    public Menu getMenuAdd() {
        Menu addMenu = new Menu("Adicionar à playlist");
        addMenu.getStyleClass().add("custom-menu");

        addMenu.getItems().add(new MenuItem("")); // nodo ancora
        addMenu.setOnShowing(event -> {
            addMenu.getItems().clear();
            playlistController.fetchAll().forEach(playlist -> {
                MenuItem item = new MenuItem(playlist.getName());
                item.setOnAction(event1 -> {
                    playlistController.addTrack(playlist, this.track);
                });
                
                item.getStyleClass().add("custom-menu-item");
                addMenu.getItems().add(item);
            });
        });

        return addMenu;
    }
}
