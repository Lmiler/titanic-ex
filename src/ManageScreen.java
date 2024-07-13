import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ManageScreen extends JPanel {
    private JComboBox<String> survivedComboBox;
    private List<Passenger> passengers;
    private JLabel jLabel;

    public ManageScreen(int x, int y, int width, int height) {
        File file = new File(Constants.PATH_TO_DATA_FILE); //this is the path to the data file
        if (file.exists()) {
            this.setLayout(null);
            this.setBounds(x, y + Constants.MARGIN_FROM_TOP, width, height);
            JLabel survivedLabel = new JLabel("Passenger Class: ");
            survivedLabel.setBounds(x + Constants.MARGIN_FROM_LEFT, y, Constants.LABEL_WIDTH,
                    Constants.LABEL_HEIGHT);
            this.add(survivedLabel);
            this.survivedComboBox = new JComboBox<>(Constants.PASSENGER_CLASS_OPTIONS);
            this.survivedComboBox.setBounds(survivedLabel.getX() + survivedLabel.getWidth() + 1,
                    survivedLabel.getY(), Constants.COMBO_BOX_WIDTH, Constants.COMBO_BOX_HEIGHT);
            this.add(this.survivedComboBox);
            passengers = new ArrayList<>();
            try {
                Scanner scanner = new Scanner(file);
                scanner.nextLine();
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    Passenger passenger = new Passenger(line);
                    passengers.add(passenger);
                }
            } catch (FileNotFoundException e) {
            }
            this.survivedComboBox.addActionListener((e) -> {
                String selected = String.valueOf(survivedComboBox.getSelectedItem());
                switch (selected) {
                    case "All" ->
                            System.out.println("All passengers");
                    case "1st" ->
                            System.out.println("First class passenger");
                    case "2nd" ->
                            System.out.println("Second class passengers");
                    case "3rd" ->
                            System.out.println("Third class passengers");
                }
            });
        }
    }

}
