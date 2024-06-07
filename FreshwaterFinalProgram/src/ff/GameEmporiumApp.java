package ff;

import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameEmporiumApp extends Application {
    private Store store;
    private ListView<Game> gameListView;
    private ListView<Game> cartListView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        store = new Store("game_Inventory.txt");

        gameListView = new ListView<>(store.getGames('A'));
        gameListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        cartListView = new ListView<>();

        Button addToCartButton = new Button("Add to Cart");
        addToCartButton.setOnAction(e -> addToCart());

        Button checkoutButton = new Button("Checkout");
        checkoutButton.setOnAction(e -> checkout());

        Button dailySummaryButton = new Button("Daily Summary");
        dailySummaryButton.setOnAction(e -> store.displayDailyPurchaseSummary());

        VBox root = new VBox(10);
        //root.setPadding(new Insets(10));
        root.getChildren().addAll(gameListView, addToCartButton, cartListView, checkoutButton, dailySummaryButton);

        primaryStage.setScene(new Scene(root, 400, 400));
        primaryStage.setTitle("Game Emporium");
        primaryStage.show();
    }

    private void addToCart() {
        ObservableList<Game> selectedGames = gameListView.getSelectionModel().getSelectedItems();
        for (Game game : selectedGames) {
            if (store.isGameAvailable(game)) {
                cartListView.getItems().add(game);
            } else {
                // Handle unavailability
                System.out.println("Sorry, " + game.getTitle() + " is not available.");
            }
        }
    }

    private void checkout() {
        List<Game> selectedGames = new ArrayList<>(cartListView.getItems());
        store.createTransaction(selectedGames);

        // Display receipt (console output for simplicity)
        System.out.println("Receipt:");
        for (Game game : selectedGames) {
            System.out.println(game.getTitle() + " - $" + 2.00);
        }
        System.out.println("Total: $" + (selectedGames.size() * 2.00));

        // Clear cart
        cartListView.getItems().clear();
    }
}