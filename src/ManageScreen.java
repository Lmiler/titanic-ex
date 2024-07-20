import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.List;

public class ManageScreen extends JPanel {
    private JComboBox<String> survivedComboBox;
    private List<Passenger> passengers;
    private List<Passenger> filteredPassengers;
    private JLabel response;
    private int filterButtonPressCount;

    private JComboBox<String> embarkedComboBox;
    private JComboBox<String> sexComboBox;
    private JComboBox<String> groupingComboBox;

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
        filterButtonPressCount = 0;
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

            JButton filterButton = new JButton("Filter");
            filterButton.setBounds(width - Constants.LABEL_WIDTH, y, Constants.COMBO_BOX_WIDTH, Constants.COMBO_BOX_HEIGHT);
            filterButton.setVisible(true);
            this.add(filterButton);

            JButton statisticsButton = new JButton("Create statistics");
            statisticsButton.setBounds(filterButton.getX() - 35, filterButton.getY() + 40,
                    filterButton.getWidth() + 55, filterButton.getHeight());
            statisticsButton.setVisible(true);
            this.add(statisticsButton);

            embarkedComboBox = new JComboBox<>(Constants.EMBARKED_OPTIONS);
            JLabel embarkedLabel = new JLabel("Embarked:");
            embarkedLabel.setBounds(survivedLabel.getX(), survivedLabel.getY() + survivedLabel.getHeight() + 10,
                    Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
            this.add(embarkedLabel);
            embarkedComboBox.setBounds(survivedLabel.getX() + Constants.LABEL_WIDTH, embarkedLabel.getY(),
                    Constants.COMBO_BOX_WIDTH, Constants.COMBO_BOX_HEIGHT);
            this.add(embarkedComboBox);

            // Combo box for sex field
            sexComboBox = new JComboBox<>(Constants.SEX_OPTIONS);
            JLabel sexLabel = new JLabel("Sex:");
            sexLabel.setBounds(survivedLabel.getX(), embarkedLabel.getY() + embarkedLabel.getHeight() + 10,
                    Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
            this.add(sexLabel);
            sexComboBox.setBounds(survivedLabel.getX() + Constants.LABEL_WIDTH, sexLabel.getY(),
                    Constants.COMBO_BOX_WIDTH, Constants.COMBO_BOX_HEIGHT);
            this.add(sexComboBox);

            groupingComboBox = new JComboBox<>(Constants.GROUPING_OPTIONS);
            JLabel groupingLabel = new JLabel("Data grouping:");
            groupingLabel.setBounds(survivedLabel.getX() + 235, survivedLabel.getY(), survivedLabel.getWidth()
                    , survivedLabel.getHeight());
            this.add(groupingLabel);
            groupingComboBox.setBounds(survivedComboBox.getX() + 220, survivedComboBox.getY(),
                    survivedComboBox.getWidth() + 30, survivedComboBox.getHeight());
            this.add(groupingComboBox);

            int textFieldX = survivedLabel.getX();
            int textFieldY = sexComboBox.getY() + sexComboBox.getHeight() + 10;
            int textFieldWidth = Constants.LABEL_WIDTH + Constants.COMBO_BOX_WIDTH;
            int textFieldHeight = Constants.COMBO_BOX_HEIGHT;

            minIdField = createTextField("ID Min:", textFieldX, textFieldY += Constants.LABEL_DISTANCE,
                    textFieldWidth, textFieldHeight);
            maxIdField = createTextField("ID Max:",
                    minIdField.getX() + minIdField.getWidth() + Constants.X_DIFFERENCE,
                    textFieldY, textFieldWidth, textFieldHeight);
            nameField = createTextField("Name:", textFieldX, textFieldY += Constants.LABEL_DISTANCE,
                    textFieldWidth, textFieldHeight);
            sibSpField = createTextField("SibSp:", textFieldX, textFieldY += Constants.LABEL_DISTANCE,
                    textFieldWidth, textFieldHeight);
            parchField = createTextField("Parch:", textFieldX, textFieldY += Constants.LABEL_DISTANCE,
                    textFieldWidth, textFieldHeight);
            ticketField = createTextField("Ticket:", textFieldX, textFieldY += Constants.LABEL_DISTANCE,
                    textFieldWidth, textFieldHeight);
            minFareField = createTextField("Fare Min:", textFieldX, textFieldY += Constants.LABEL_DISTANCE,
                    textFieldWidth, textFieldHeight);
            maxFareField = createTextField("Fare Max:",
                    minFareField.getX() + minFareField.getWidth() + Constants.X_DIFFERENCE,
                    textFieldY, textFieldWidth, textFieldHeight);
            cabinField = createTextField("Cabin:", textFieldX, textFieldY += Constants.LABEL_DISTANCE,
                    textFieldWidth, textFieldHeight);

            passengers = new ArrayList<>();
            filteredPassengers = new ArrayList<>();

            try {
                Scanner scanner = new Scanner(file);
                scanner.nextLine();
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

            groupingComboBox.addActionListener((e) -> {
                String selectedItem = String.valueOf(groupingComboBox.getSelectedItem());
                switch (selectedItem) {
                    case "Passenger Class" -> printMap(calculatePercentageByClass());
                    case "Survived" -> printMap(calculatePercentageBySurvived());
                    case "Sex" -> printMap(calculatePercentageBySex());
                    case "Age Groups" -> printMap(calculatePercentageByAgeGroups());
                    case "Parch & SibSp" -> printMap(calculatePercentageByParchAndSib());
                    case "Fare Groups" -> printMap(calculatePercentageByFareGroups());
                    case "Embarked" -> printMap(calculatePercentageByEmbarked());
                }
            });

            statisticsButton.addActionListener(e -> {
                generateSurvivalStatistics();
            });

        }
    }

    private void printMap(Map<String, Double> data) {
        List<Map.Entry<String, Double>> entryList = new ArrayList<>(data.entrySet());
        entryList.sort(new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return Double.compare(o2.getValue(), o1.getValue());
            }
        });
        StringBuilder message = new StringBuilder();
        message.append("Percentage of passengers according to: \n\n");
        for (Map.Entry<String, Double> entry : entryList) {
            message.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        JOptionPane.showMessageDialog(null, message.toString(), "Grouping Data",
                JOptionPane.INFORMATION_MESSAGE);
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
                .toList());

        int totalCount = filteredPassengers.size();
        int survivedCount = (int) filteredPassengers.stream().filter(Passenger::isSurvived).count();
        int notSurvivedCount = totalCount - survivedCount;

        String responseText = "Total rows: " + totalCount +
                " (" + survivedCount + " survived, " + notSurvivedCount + " didn't survive)";
        response.setText(responseText);

        String fileName = generateFileName();
        saveFilteredPassengersToCSV(fileName);
    }

    private String generateFileName() {
        int pressCount = ++filterButtonPressCount;
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
                        "\"" + passenger.getFormattedName() + "\"," +
                        passenger.getSex() + "," +
                        passenger.getAge() + "," +
                        passenger.getSibSp() + "," +
                        passenger.getParch() + "," +
                        passenger.getTicket() + "," +
                        passenger.getFare() + "," +
                        passenger.getCabin() + "," +
                        passenger.getEmbarked());
            }
        } catch (FileNotFoundException ignored) {
        }
    }

    private <T> boolean matchesTextField(JTextField textField, T value) {
        String text = textField.getText().trim();
        if (text.isEmpty()) {
            return true;
        }
        return String.valueOf(value).toLowerCase().contains(text.toLowerCase());
    }

    private boolean matchesIdRange(int id, int minId, int maxId) {
        if (minId == Integer.MIN_VALUE && maxId == Integer.MIN_VALUE) {
            return true;
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
            return true;
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
            return true;
        }
        if (passenger.getEmbarked() != null) {
            return passenger.getEmbarked().equalsIgnoreCase(selectedEmbarked);
        }
        return false;
    }

    private boolean matchesSex(Passenger passenger, String selectedSex) {
        if (selectedSex.equals("All")) {
            return true;
        }
        return passenger.getSex().equalsIgnoreCase(selectedSex);
    }

    private boolean matchesClass(Passenger passenger, String selectedClass) {
        if (selectedClass.equals("All")) {
            return true;
        }
        int pClass = passenger.getPClass();
        return switch (selectedClass) {
            case "1st" -> pClass == 1;
            case "2nd" -> pClass == 2;
            case "3rd" -> pClass == 3;
            default -> false;
        };
    }

    private int parseTextFieldToInt(JTextField textField) {
        String text = textField.getText().trim();
        if (text.isEmpty()) {
            return Integer.MIN_VALUE;
        }
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return Integer.MIN_VALUE;
        }
    }

    private double parseTextFieldToDouble(JTextField textField) {
        String text = textField.getText().trim();
        if (text.isEmpty()) {
            return Double.NaN;
        }
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

    public Map<String, Double> calculateSurvivalPercentageByClass() {
        Map<String, Double> percentages = new LinkedHashMap<>();
        for (String option : Constants.PASSENGER_CLASS_OPTIONS) {
            if (!option.equals("All")) {
                List<Passenger> classPassengers = passengers.stream()
                        .filter(passenger -> passenger.getClassAsString().equals(option))
                        .toList();
                long count = passengers.stream()
                        .filter(passenger -> passenger.getClassAsString().equals(option))
                        .filter(Passenger::isSurvived)
                        .count();
                double percentage = (count * Constants.HUNDRED) / classPassengers.size();
                percentages.put(option, percentage);
            }
        }

        return percentages;
    }

    public Map<String, Double> calculateSurvivalPercentageBySex() {
        Map<String, Double> percentages = new LinkedHashMap<>();
        for (String sex : new String[]{"male", "female"}) {
            List<Passenger> sexByPassenger = passengers.stream()
                    .filter(passenger -> passenger.getSex().equals(sex))
                    .toList();
            long count = sexByPassenger.stream()
                    .filter(Passenger::isSurvived)
                    .count();
            double percentage = (count * Constants.HUNDRED) / sexByPassenger.size();
            percentages.put(sex, percentage);
        }

        return percentages;
    }

    public Map<String, Double> calculateSurvivalPercentageByAgeGroups() {
        Map<String, Double> percentages = new LinkedHashMap<>();
        List<Passenger> passengerChildCount = passengers.stream()
                .filter(passenger -> passenger.getAge() <= Constants.CHILD)
                .toList();
        long childCount = passengerChildCount.stream()
                .filter(Passenger::isSurvived)
                .count();
        List<Passenger> passengerAdultCount = passengers.stream()
                .filter(passenger -> passenger.getAge() >= Constants.ADULT_MIN &&
                        passenger.getAge() <= Constants.ADULT_MAX)
                .toList();
        long adultCount = passengerAdultCount.stream()
                .filter(Passenger::isSurvived)
                .count();
        List<Passenger> passengerElderlyCount = passengers.stream()
                .filter(passenger -> passenger.getAge() >= Constants.ELDER)
                .toList();
        long elderlyCount = passengerElderlyCount.stream()
                .filter(Passenger::isSurvived)
                .count();

        percentages.put("Child (0 - 17)", (childCount * Constants.HUNDRED) / passengerChildCount.size());
        percentages.put("Adult (18 - 64)", (adultCount * Constants.HUNDRED) / passengerAdultCount.size());
        percentages.put("Elderly (65+)", (elderlyCount * Constants.HUNDRED) / passengerElderlyCount.size());

        return percentages;
    }

    public Map<String, Double> calculateSurvivalPercentageByParchAndSib() {
        Map<String, Double> percentages = new LinkedHashMap<>();

        List<Passenger> withFamilyPassengers = passengers.stream()
                .filter(passenger -> passenger.getParch() + passenger.getSibSp() > 0)
                .toList();
        List<Passenger> withoutFamilyPassengers = passengers.stream()
                .filter(passenger -> passenger.getParch() + passenger.getSibSp() == 0)
                .toList();
        long survivedWithFamily = withFamilyPassengers.stream()
                .filter(Passenger::isSurvived)
                .count();
        long survivedWithoutFamily = withoutFamilyPassengers.stream()
                .filter(Passenger::isSurvived)
                .count();

        percentages.put("With family", (survivedWithFamily * Constants.HUNDRED) / withFamilyPassengers.size());
        percentages.put("Without family", (survivedWithoutFamily * Constants.HUNDRED) / withoutFamilyPassengers.size());

        return percentages;
    }

    public Map<String, Double> calculateSurvivalPercentageByFareGroups() {
        Map<String, Double> percentages = new LinkedHashMap<>();
        List<Passenger> passengerLowFareCount = passengers.stream()
                .filter(passenger -> passenger.getFare() <= Constants.LOW_FARE)
                .toList();
        long lowFareCount = passengerLowFareCount.stream()
                .filter(Passenger::isSurvived)
                .count();
        List<Passenger> passengerMediumFareCount = passengers.stream()
                .filter(passenger -> passenger.getFare() >= Constants.MID_FARE_MIN &&
                        passenger.getFare() <= Constants.MID_FARE_MAX)
                .toList();
        long mediumFareCount = passengerMediumFareCount.stream()
                .filter(Passenger::isSurvived)
                .count();
        List<Passenger> passengerHighFareCount = passengers.stream()
                .filter(passenger -> passenger.getFare() >= Constants.HIGH_FARE)
                .toList();
        long highFareCount = passengerHighFareCount.stream()
                .filter(Passenger::isSurvived)
                .count();
        percentages.put("Low Fare (0 - 49)", (lowFareCount * Constants.HUNDRED) / passengerLowFareCount.size());
        percentages.put("Medium Fare (50 - 99)", (mediumFareCount * Constants.HUNDRED) / passengerMediumFareCount.size());
        percentages.put("High Fare (100+)", (highFareCount * Constants.HUNDRED) / passengerHighFareCount.size());

        return percentages;
    }

    public Map<String, Double> calculateSurvivalPercentageByEmbarked() {
        Map<String, Double> percentages = new LinkedHashMap<>();
        List<Passenger> passengerSClass = passengers.stream()
                .filter(passenger -> passenger.getEmbarked().equals("S"))
                .toList();
        long embarkedSCount = passengerSClass.stream()
                .filter(Passenger::isSurvived)
                .count();
        List<Passenger> passengerCClass = passengers.stream()
                .filter(passenger -> passenger.getEmbarked().equals("C"))
                .toList();
        long embarkedCCount = passengerCClass.stream()
                .filter(Passenger::isSurvived)
                .count();
        List<Passenger> passengerQClass = passengers.stream()
                .filter(passenger -> passenger.getEmbarked().equals("Q"))
                .toList();
        long embarkedQCount = passengerQClass.stream()
                .filter(Passenger::isSurvived)
                .count();

        percentages.put("Embarked S", (embarkedSCount * Constants.HUNDRED) / passengerSClass.size());
        percentages.put("Embarked C", (embarkedCCount * Constants.HUNDRED) / passengerCClass.size());
        percentages.put("Embarked Q", (embarkedQCount * Constants.HUNDRED) / passengerQClass.size());

        return percentages;
    }

    private void generateSurvivalStatistics() {
        // Calculate survival percentages based on different criteria
        Map<String, Double> survivalByClass = calculateSurvivalPercentageByClass();
        Map<String, Double> survivalBySex = calculateSurvivalPercentageBySex();
        Map<String, Double> survivalByAgeGroups = calculateSurvivalPercentageByAgeGroups();
        Map<String, Double> survivalByParch = calculateSurvivalPercentageByParchAndSib();
        Map<String, Double> survivalByFareGroups = calculateSurvivalPercentageByFareGroups();
        Map<String, Double> survivalByEmbarked = calculateSurvivalPercentageByEmbarked();

        StringBuilder content = new StringBuilder();
        content.append("Survival Statistics\n\n");
        content.append("Survival by Passenger Class:\n");
        survivalByClass.forEach((key, value) -> content.append(key + ": " + String.format("%.2f", value) + "%\n"));
        content.append("\nSurvival by Sex:\n");
        survivalBySex.forEach((key, value) -> content.append(key + ": " + String.format("%.2f", value) + "%\n"));
        content.append("\nSurvival by Age Groups:\n");
        survivalByAgeGroups.forEach((key, value) -> content.append(key + ": " + String.format("%.2f", value) + "%\n"));
        content.append("\nSurvival by Family:\n");
        survivalByParch.forEach((key, value) -> content.append(key + ": " + String.format("%.2f", value) + "%\n"));
        content.append("\nSurvival by Fare Groups:\n");
        survivalByFareGroups.forEach((key, value) -> content.append(key + ": " + String.format("%.2f", value) + "%\n"));
        content.append("\nSurvival by Embarked:\n");
        survivalByEmbarked.forEach((key, value) -> content.append(key + ": " + String.format("%.2f", value) + "%\n"));

        saveStatisticsToFile(content.toString());
    }

    private void saveStatisticsToFile(String content) {
        String fileName = "Statistics.txt";
        File outputFile = new File(fileName);
        try (PrintWriter writer = new PrintWriter(outputFile)) {
            writer.println(content);
        } catch (FileNotFoundException ignored) {
        }
    }

    public Map<String, Double> calculatePercentageByClass() {
        Map<String, Double> percentages = new LinkedHashMap<>();
        int totalPassengers = passengers.size();
        for (String option : Constants.PASSENGER_CLASS_OPTIONS) {
            if (!option.equals("All")) {
                List<Passenger> classPassengers = passengers.stream()
                        .filter(passenger -> passenger.getClassAsString().equals(option))
                        .toList();
                double percentage = (classPassengers.size() * Constants.HUNDRED) / totalPassengers;
                percentages.put(option, percentage);
            }
        }

        return percentages;
    }

    public Map<String, Double> calculatePercentageBySurvived() {
        Map<String, Double> percentages = new LinkedHashMap<>();
        int totalPassengers = passengers.size();
        long survived = passengers.stream()
                .filter(Passenger::isSurvived)
                .count();
        long notSurvived = totalPassengers - survived;
        percentages.put("Survived: ", (survived * Constants.HUNDRED) / totalPassengers);
        percentages.put("Not survived: ", (notSurvived * Constants.HUNDRED) / totalPassengers);
        return percentages;
    }

    public Map<String, Double> calculatePercentageBySex() {
        Map<String, Double> percentages = new LinkedHashMap<>();
        int totalPassengers = passengers.size();
        for (String sex : new String[]{"male", "female"}) {
            List<Passenger> sexByPassenger = passengers.stream()
                    .filter(passenger -> passenger.getSex().equals(sex))
                    .toList();
            double percentage = (sexByPassenger.size() * Constants.HUNDRED) / totalPassengers;
            percentages.put(sex, percentage);
        }

        return percentages;
    }

    public Map<String, Double> calculatePercentageByAgeGroups() {
        Map<String, Double> percentages = new LinkedHashMap<>();
        int totalPassengers = passengers.size();

        long childCount = passengers.stream()
                .filter(passenger -> passenger.getAge() <= Constants.CHILD)
                .count();

        long adultCount = passengers.stream()
                .filter(passenger -> passenger.getAge() >= Constants.ADULT_MIN &&
                        passenger.getAge() <= Constants.ADULT_MAX)
                .count();

        long elderlyCount = passengers.stream()
                .filter(passenger -> passenger.getAge() >= Constants.ELDER)
                .count();

        percentages.put("Child (0 - 17)", (childCount * Constants.HUNDRED) / totalPassengers);
        percentages.put("Adult (18 - 64)", (adultCount * Constants.HUNDRED) / totalPassengers);
        percentages.put("Elderly (65+)", (elderlyCount * Constants.HUNDRED) / totalPassengers);

        return percentages;
    }

    public Map<String, Double> calculatePercentageByParchAndSib() {
        Map<String, Double> percentages = new LinkedHashMap<>();
        int totalPassengers = passengers.size();

        long withFamily = passengers.stream()
                .filter(passenger -> passenger.getSibSp() + passenger.getParch() > 0)
                .count();
        long withoutFamily = passengers.stream()
                .filter(passenger -> passenger.getSibSp() + passenger.getParch() == 0)
                .count();

        percentages.put("With family", (withFamily * Constants.HUNDRED) / totalPassengers);
        percentages.put("Without family", (withoutFamily * Constants.HUNDRED) / totalPassengers);

        return percentages;
    }

    public Map<String, Double> calculatePercentageByFareGroups() {
        Map<String, Double> percentages = new LinkedHashMap<>();
        int totalPassengers = passengers.size();
        long lowFareCount = passengers.stream()
                .filter(passenger -> passenger.getFare() <= Constants.LOW_FARE)
                .count();
        long mediumFareCount = passengers.stream()
                .filter(passenger -> passenger.getFare() >= Constants.MID_FARE_MIN &&
                        passenger.getFare() <= Constants.MID_FARE_MAX)
                .count();
        long highFareCount = passengers.stream()
                .filter(passenger -> passenger.getFare() >= Constants.HIGH_FARE)
                .count();
        percentages.put("Low Fare (0 - 49)", (lowFareCount * Constants.HUNDRED) / totalPassengers);
        percentages.put("Medium Fare (50 - 99)", (mediumFareCount * Constants.HUNDRED) / totalPassengers);
        percentages.put("High Fare (100+)", (highFareCount * Constants.HUNDRED) / totalPassengers);

        return percentages;
    }

    public Map<String, Double> calculatePercentageByEmbarked() {
        Map<String, Double> percentages = new LinkedHashMap<>();
        int totalPassengers = passengers.size();
        long embarkedSCount = passengers.stream()
                .filter(passenger -> passenger.getEmbarked().equals("S"))
                .count();
        long embarkedCCount = passengers.stream()
                .filter(passenger -> passenger.getEmbarked().equals("C"))
                .count();
        long embarkedQCount = passengers.stream()
                .filter(passenger -> passenger.getEmbarked().equals("Q"))
                .count();

        percentages.put("Embarked S", (embarkedSCount * Constants.HUNDRED) / totalPassengers);
        percentages.put("Embarked C", (embarkedCCount * Constants.HUNDRED) / totalPassengers);
        percentages.put("Embarked Q", (embarkedQCount * Constants.HUNDRED) / totalPassengers);

        return percentages;
    }
}
