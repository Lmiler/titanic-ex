import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ManageScreen extends JPanel {
    private JComboBox<String> survivedComboBox;
    private List<Passenger> passengers;
    private List<Passenger> filteredPassengers;
    private JLabel response;
    private JButton filterButton;
    private int filterButtonPressCount = 0; // Track how many times filter button is pressed

    // Combo boxes for embarked and sex fields
    private JComboBox<String> embarkedComboBox;
    private JComboBox<String> sexComboBox;

    // Text fields for other passenger attributes
    private JTextField nameField;
    private JTextField minIdField;
    private JTextField maxIdField;
    private JTextField sibSpField;
    private JTextField parchField;
    private JTextField ticketField;
    private JTextField minFareField;
    private JTextField maxFareField;
    private JTextField cabinField;

    public ManageScreen(int x, int y, int width, int height) {
        File file = new File(Constants.PATH_TO_DATA_FILE);
        if (file.exists()) {
            this.setLayout(null);
            this.setBounds(x, y + Constants.MARGIN_FROM_TOP, width, height);

            JLabel survivedLabel = new JLabel("Passenger Class: ");
            survivedLabel.setBounds(x + Constants.MARGIN_FROM_LEFT, y, Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
            this.add(survivedLabel);

            this.survivedComboBox = new JComboBox<>(Constants.PASSENGER_CLASS_OPTIONS);
            this.survivedComboBox.setBounds(survivedLabel.getX() + survivedLabel.getWidth() + 1,
                    survivedLabel.getY(), Constants.COMBO_BOX_WIDTH, Constants.COMBO_BOX_HEIGHT);
            this.add(this.survivedComboBox);

            response = new JLabel();
            response.setBounds(survivedLabel.getX(), height - 170, width, 150);
            response.setVisible(true);
            this.add(response);

            filterButton = new JButton("Filter");
            filterButton.setBounds(width - Constants.LABEL_WIDTH, y, Constants.COMBO_BOX_WIDTH, Constants.COMBO_BOX_HEIGHT);
            filterButton.setVisible(true);
            this.add(filterButton);

            // Combo box for embarked field
            embarkedComboBox = new JComboBox<>(new String[]{"All", "S", "C", "Q"});
            JLabel embarkedLabel = new JLabel("Embarked:");
            embarkedLabel.setBounds(survivedLabel.getX(), survivedLabel.getY() + survivedLabel.getHeight() + 10,
                    Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
            this.add(embarkedLabel);
            embarkedComboBox.setBounds(survivedLabel.getX() + Constants.LABEL_WIDTH, embarkedLabel.getY(),
                    Constants.COMBO_BOX_WIDTH, Constants.COMBO_BOX_HEIGHT);
            this.add(embarkedComboBox);

            // Combo box for sex field
            sexComboBox = new JComboBox<>(new String[]{"All", "male", "female"});
            JLabel sexLabel = new JLabel("Sex:");
            sexLabel.setBounds(survivedLabel.getX(), embarkedLabel.getY() + embarkedLabel.getHeight() + 10,
                    Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
            this.add(sexLabel);
            sexComboBox.setBounds(survivedLabel.getX() + Constants.LABEL_WIDTH, sexLabel.getY(),
                    Constants.COMBO_BOX_WIDTH, Constants.COMBO_BOX_HEIGHT);
            this.add(sexComboBox);

            // Text fields for other passenger attributes
            int textFieldX = survivedLabel.getX();
            int textFieldY = sexComboBox.getY() + sexComboBox.getHeight() + 10;
            int textFieldWidth = Constants.LABEL_WIDTH + Constants.COMBO_BOX_WIDTH;
            int textFieldHeight = Constants.COMBO_BOX_HEIGHT;

            minIdField = createTextField("ID Min:", textFieldX, textFieldY += 40, textFieldWidth, textFieldHeight);
            maxIdField = createTextField("ID Max:", minIdField.getX() + minIdField.getWidth() + 20, textFieldY, textFieldWidth, textFieldHeight);
            nameField = createTextField("Name:", textFieldX, textFieldY += 40, textFieldWidth, textFieldHeight);
            sibSpField = createTextField("SibSp:", textFieldX, textFieldY += 40, textFieldWidth, textFieldHeight);
            parchField = createTextField("Parch:", textFieldX, textFieldY += 40, textFieldWidth, textFieldHeight);
            ticketField = createTextField("Ticket:", textFieldX, textFieldY += 40, textFieldWidth, textFieldHeight);
            minFareField = createTextField("Fare Min:", textFieldX, textFieldY += 40, textFieldWidth, textFieldHeight);
            maxFareField = createTextField("Fare Max:", minFareField.getX() + minFareField.getWidth() + 20, textFieldY, textFieldWidth, textFieldHeight);
            cabinField = createTextField("Cabin:", textFieldX, textFieldY += 40, textFieldWidth, textFieldHeight);

            passengers = new ArrayList<>();
            filteredPassengers = new ArrayList<>();

            try {
                Scanner scanner = new Scanner(file);
                scanner.nextLine(); // Skip header line
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    Passenger passenger = new Passenger(line);
                    passengers.add(passenger);
                }
                scanner.close();
            } catch (FileNotFoundException ignored) {
            }

            filterButton.addActionListener((e) -> {
                filterPassengersAndUpdateUI();
            });

            this.survivedComboBox.addActionListener((e) -> {
//                filterPassengersAndUpdateUI();
            });

        }
    }

    private JTextField createTextField(String label, int x, int y, int width, int height) {
        JLabel jLabel = new JLabel(label);
        jLabel.setBounds(x, y, Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
        this.add(jLabel);

        JTextField jTextField = new JTextField();
        jTextField.setBounds(x + Constants.LABEL_WIDTH, y, Constants.COMBO_BOX_WIDTH, Constants.COMBO_BOX_HEIGHT);
        this.add(jTextField);

        return jTextField;
    }

    private void filterPassengersAndUpdateUI() {
        filteredPassengers.clear();

        String selectedEmbarked = String.valueOf(embarkedComboBox.getSelectedItem());
        String selectedSex = String.valueOf(sexComboBox.getSelectedItem());
        String selectedClass = String.valueOf(survivedComboBox.getSelectedItem());

        int minId = parseTextFieldToInt(minIdField);
        int maxId = parseTextFieldToInt(maxIdField);
        double minFare = parseTextFieldToDouble(minFareField);
        double maxFare = parseTextFieldToDouble(maxFareField);

        filteredPassengers.addAll(passengers.stream()
                .filter(passenger -> matchesTextField(nameField, passenger.getName()))
                .filter(passenger -> matchesIdRange(passenger.getPassengerId(), minId, maxId))
                .filter(passenger -> matchesTextField(sibSpField, passenger.getSibSp()))
                .filter(passenger -> matchesTextField(parchField, passenger.getParch()))
                .filter(passenger -> matchesTextField(ticketField, passenger.getTicket()))
                .filter(passenger -> matchesFareRange(passenger.getFare(), minFare, maxFare))
                .filter(passenger -> matchesTextField(cabinField, passenger.getCabin()))
                .filter(passenger -> matchesEmbarked(passenger, selectedEmbarked))
                .filter(passenger -> matchesSex(passenger, selectedSex))
                .filter(passenger -> matchesClass(passenger, selectedClass))
                .collect(Collectors.toList()));

        int totalCount = filteredPassengers.size();
        int survivedCount = (int) filteredPassengers.stream().filter(passenger -> passenger.getSurvived() == 1).count();
        int notSurvivedCount = totalCount - survivedCount;

        String responseText = "Total rows: " + totalCount +
                " (" + survivedCount + " survived, " + notSurvivedCount + " didn't survive)";
        response.setText(responseText);

        // Save filtered passengers to CSV
        String fileName = generateFileName();
        saveFilteredPassengersToCSV(fileName);
    }

    private String generateFileName() {
        // Generate file name based on the number of times filter button was pressed
        int pressCount = ++filterButtonPressCount; // Increment and use
        return pressCount + ".csv";
    }

    private void saveFilteredPassengersToCSV(String fileName) {
        File outputFile = new File(fileName);
        try (PrintWriter writer = new PrintWriter(outputFile)) {
            filteredPassengers.sort(Comparator.comparing(Passenger::getFormattedName));
            // Write header
            writer.println("PassengerId,Survived,PClass,Name,Sex,Age,SibSp,Parch,Ticket,Fare,Cabin,Embarked");

            // Write filtered passengers
            for (Passenger passenger : filteredPassengers) {
                writer.println(passenger.getPassengerId() + "," +
                        passenger.getSurvived() + "," +
                        passenger.getPClass() + "," +
                        "\"" + passenger.getFormattedName() + "\"," + // Quote name to handle commas
                        passenger.getSex() + "," +
                        passenger.getAge() + "," +
                        passenger.getSibSp() + "," +
                        passenger.getParch() + "," +
                        passenger.getTicket() + "," +
                        passenger.getFare() + "," +
                        passenger.getCabin() + "," +
                        passenger.getEmbarked());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace(); // Handle this according to your application's needs
        }
    }

    private <T> boolean matchesTextField(JTextField textField, T value) {
        String text = textField.getText().trim();
        if (text.isEmpty()) {
            return true; // No filter applied if text field is empty
        }
        // Check if the value string representation contains the text
        return String.valueOf(value).toLowerCase().contains(text.toLowerCase());
    }

    private boolean matchesIdRange(int id, int minId, int maxId) {
        if (minId == Integer.MIN_VALUE && maxId == Integer.MIN_VALUE) {
            return true; // No filter applied if both fields are empty
        }
        if (minId == Integer.MIN_VALUE) {
            return id <= maxId;
        }
        if (maxId == Integer.MAX_VALUE) {
            return id >= minId;
        }
        return id >= minId && id <= maxId;
    }

    private boolean matchesFareRange(double fare, double minFare, double maxFare) {
        if (Double.isNaN(minFare) && Double.isNaN(maxFare)) {
            return true; // No filter applied if both fields are empty
        }
        if (Double.isNaN(minFare)) {
            return fare <= maxFare;
        }
        if (Double.isNaN(maxFare)) {
            return fare >= minFare;
        }
        return fare >= minFare && fare <= maxFare;
    }

    private boolean matchesEmbarked(Passenger passenger, String selectedEmbarked) {
        if (selectedEmbarked.equals("All")) {
            return true; // No filter applied if embarked is not selected
        }
        if (passenger.getEmbarked() != null) {
            return passenger.getEmbarked().equalsIgnoreCase(selectedEmbarked);
        }
        return false;
    }

    private boolean matchesSex(Passenger passenger, String selectedSex) {
        if (selectedSex.equals("All")) {
            return true; // No filter applied if sex is not selected
        }
        return passenger.getSex().equalsIgnoreCase(selectedSex);
    }

    private boolean matchesClass(Passenger passenger, String selectedClass) {
        if (selectedClass.equals("All")) {
            return true; // No filter applied if "All" is selected
        }
        int pClass = passenger.getPClass();
        switch (selectedClass) {
            case "1st":
                return pClass == 1;
            case "2nd":
                return pClass == 2;
            case "3rd":
                return pClass == 3;
            default:
                return false;
        }
    }

    private int parseTextFieldToInt(JTextField textField) {
        String text = textField.getText().trim();
        if (text.isEmpty()) {
            return Integer.MIN_VALUE; // Indicates no filter applied
        }
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return Integer.MIN_VALUE; // Invalid input, treat as no filter applied
        }
    }

    private double parseTextFieldToDouble(JTextField textField) {
        String text = textField.getText().trim();
        if (text.isEmpty()) {
            return Double.NaN; // Indicates no filter applied
        }
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return Double.NaN; // Invalid input, treat as no filter applied
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
    }
}
