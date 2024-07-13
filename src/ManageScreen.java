import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ManageScreen extends JPanel {
    private JComboBox<String> survivedComboBox;
    private List<Passenger> passengers;
    private List<Passenger> filteredPassengers;
    private JLabel response;
    private JButton filterButton;

    // Combo boxes for embarked and sex fields
    private JComboBox<String> embarkedComboBox;
    private JComboBox<String> sexComboBox;

    // Text fields for other passenger attributes
    private JTextField nameField;
    private JTextField ageField;
    private JTextField sibSpField;
    private JTextField parchField;
    private JTextField ticketField;
    private JTextField fareField;
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
            embarkedComboBox = new JComboBox<>(new String[]{"", "S", "C", "Q"});
            JLabel embarkedLabel = new JLabel("Embarked:");
            embarkedLabel.setBounds(survivedLabel.getX(), survivedLabel.getY() + survivedLabel.getHeight() + 10,
                    Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
            this.add(embarkedLabel);
            embarkedComboBox.setBounds(survivedLabel.getX() + Constants.LABEL_WIDTH, embarkedLabel.getY(),
                    Constants.COMBO_BOX_WIDTH, Constants.COMBO_BOX_HEIGHT);
            this.add(embarkedComboBox);

            // Combo box for sex field
            sexComboBox = new JComboBox<>(new String[]{"", "male", "female"});
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

            nameField = createTextField("Name:", textFieldX, textFieldY += 40, textFieldWidth, textFieldHeight);
            ageField = createTextField("Age:", textFieldX, textFieldY += 40, textFieldWidth, textFieldHeight);
            sibSpField = createTextField("SibSp:", textFieldX, textFieldY += 40, textFieldWidth, textFieldHeight);
            parchField = createTextField("Parch:", textFieldX, textFieldY += 40, textFieldWidth, textFieldHeight);
            ticketField = createTextField("Ticket:", textFieldX, textFieldY += 40, textFieldWidth, textFieldHeight);
            fareField = createTextField("Fare:", textFieldX, textFieldY += 40, textFieldWidth, textFieldHeight);
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
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            filterButton.addActionListener((e) -> {
                filterPassengersAndUpdateUI();
            });

            this.survivedComboBox.addActionListener((e) -> {

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

        filteredPassengers.addAll(passengers.stream()
                .filter(passenger -> matchesTextField(nameField, passenger.getName()))
                .filter(passenger -> matchesTextField(ageField, passenger.getAge()))
                .filter(passenger -> matchesTextField(sibSpField, passenger.getSibSp()))
                .filter(passenger -> matchesTextField(parchField, passenger.getParch()))
                .filter(passenger -> matchesTextField(ticketField, passenger.getTicket()))
                .filter(passenger -> matchesTextField(fareField, passenger.getFare()))
                .filter(passenger -> matchesTextField(cabinField, passenger.getCabin()))
                .filter(passenger -> matchesEmbarked(passenger, selectedEmbarked))
                .filter(passenger -> matchesSex(passenger, selectedSex))
                .collect(Collectors.toList()));

        int count = filteredPassengers.size();
        response.setText("Number of passengers: " + count);
    }

//    private boolean isSelectedClass(Passenger passenger, String selectedClass) {
//        if (selectedClass.equals("All")) {
//            return true;
//        }
//        int pClass = passenger.getPClass();
//        switch (selectedClass) {
//            case "1st":
//                return pClass == 1;
//            case "2nd":
//                return pClass == 2;
//            case "3rd":
//                return pClass == 3;
//            default:
//                return false;
//        }
//    }

    private boolean matchesTextField(JTextField textField, Object value) {
        String text = textField.getText().trim();
        if (text.isEmpty()) {
            return true; // No filter applied if text field is empty
        }
        // Check if the value string representation contains the text
        return String.valueOf(value).toLowerCase().contains(text.toLowerCase());
    }

    private boolean matchesEmbarked(Passenger passenger, String selectedEmbarked) {
        if (selectedEmbarked.isEmpty()) {
            return true; // No filter applied if embarked is not selected
        }
        if (passenger.getEmbarked() != null) {
            return passenger.getEmbarked().equalsIgnoreCase(selectedEmbarked);
        }
        return false;
    }

    private boolean matchesSex(Passenger passenger, String selectedSex) {
        if (selectedSex.isEmpty()) {
            return true; // No filter applied if sex is not selected
        }
        return passenger.getSex().equalsIgnoreCase(selectedSex);
    }
}
