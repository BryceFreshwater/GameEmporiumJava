package ff;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

class Store {
    private List<Game> games;
    private List<List<Game>> transactions;

    public Store(String inventoryFilePath) {
        games = new ArrayList<>();
        transactions = new ArrayList<>();
        loadInventory(inventoryFilePath);
    }

    private void loadInventory(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int copies = Integer.parseInt(parts[0]);
                char format = parts[1].charAt(0);
                String title = parts[2];
                games.add(new Game(copies, format, title));
            }
        } catch (IOException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            // Handle exceptions as needed
        }
    }

    public ObservableList<Game> getGames(char formatFilter) {
        ObservableList<Game> filteredGames = FXCollections.observableArrayList();
        for (Game game : games) {
            if (formatFilter == 'A' || game.getFormat() == formatFilter) {
                filteredGames.add(game);
            }
        }
        Collections.sort(filteredGames, Comparator.comparing(Game::getTitle, String.CASE_INSENSITIVE_ORDER));
        return filteredGames;
    }

    public boolean isGameAvailable(Game selectedGame) {
        return selectedGame.getCopies() > 0;
    }

    public void createTransaction(List<Game> selectedGames) {
        transactions.add(selectedGames);
        for (Game game : selectedGames) {
            game.copies--; // Reduce available copies
        }
    }

    public List<List<Game>> getTransactions() {
        return transactions;
    }

    public double calculateTotalSales() {
        double totalSales = 0.0;
        for (List<Game> transaction : transactions) {
            totalSales += transaction.size() * 2.00;
        }
        return totalSales;
    }

    public void displayDailyPurchaseSummary() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Daily Purchase Summary");
        alert.setHeaderText(null);

        StringBuilder summary = new StringBuilder("Total Sales: $" + calculateTotalSales() + "\n\n");

        for (char format : Arrays.asList('N', 'P', 'X')) {
            int formatSales = 0;
            for (List<Game> transaction : transactions) {
                for (Game game : transaction) {
                    if (game.getFormat() == format) {
                        formatSales++;
                    }
                }
            }
            summary.append(formatSales > 0 ? format + ": " + formatSales + " games\n" : "");
        }

        alert.setContentText(summary.toString());
        alert.showAndWait();
    }
}

