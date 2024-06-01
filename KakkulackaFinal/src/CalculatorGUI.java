import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A class representing the GUI for a calculator application.
 */
public class CalculatorGUI extends JFrame {

    private JTextField inputField;
    private JTextArea outputArea;
    private Calculator calculator;
    private ArrayList<String> history;
    private UserPreferences userPreferences;

    /**
     * Constructs the CalculatorGUI, initializing all components and settings.
     */
    public CalculatorGUI() {
        userPreferences = new UserPreferences();
        calculator = new Calculator();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 700);

        inputField = new JTextField();
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Arial", Font.PLAIN, userPreferences.getFontSize()));

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 5, 5, 5));

        String[] buttons = {
                "7", "8", "9", "/", "^",
                "4", "5", "6", "*", "!",
                "1", "2", "3", "-", "sin",
                "0", ".", "=", "+", "cos",
                "(", ")", "tan", "log",
                "exp", "sqrt", "Save History", "Load History", "Clear", "Preferences"
        };

        Color[] buttonColors = {
                Color.CYAN, Color.CYAN, Color.CYAN, Color.ORANGE, Color.ORANGE,
                Color.CYAN, Color.CYAN, Color.CYAN, Color.ORANGE, Color.ORANGE,
                Color.CYAN, Color.CYAN, Color.CYAN, Color.ORANGE, Color.ORANGE,
                Color.CYAN, Color.CYAN, Color.GREEN, Color.ORANGE, Color.ORANGE,
                Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW,
                Color.YELLOW, Color.YELLOW,
                Color.PINK, Color.PINK, Color.RED, Color.MAGENTA
        };

        for (int i = 0; i < buttons.length; i++) {
            JButton button = new JButton(buttons[i]);
            button.addActionListener(new ButtonClickListener());
            button.setBackground(buttonColors[i]);
            button.setForeground(Color.BLACK);
            button.setFont(new Font("Arial", Font.PLAIN, userPreferences.getFontSize()));
            panel.add(button);
        }

        getContentPane().add(BorderLayout.NORTH, inputField);
        getContentPane().add(BorderLayout.CENTER, panel);
        getContentPane().add(BorderLayout.SOUTH, scrollPane);

        history = new ArrayList<>();

        applyTheme(userPreferences.getTheme());

        setVisible(true);
    }

    /**
     * Applies the selected theme to the GUI components.
     *
     * @param theme the theme to be applied (light or dark)
     */
    private void applyTheme(String theme) {
        if (theme.equals("dark")) {
            getContentPane().setBackground(Color.DARK_GRAY);
            inputField.setBackground(Color.BLACK);
            inputField.setForeground(Color.WHITE);
            outputArea.setBackground(Color.BLACK);
            outputArea.setForeground(Color.WHITE);
        } else {
            getContentPane().setBackground(Color.LIGHT_GRAY);
            inputField.setBackground(Color.WHITE);
            inputField.setForeground(Color.BLACK);
            outputArea.setBackground(Color.WHITE);
            outputArea.setForeground(Color.BLACK);
        }
    }

    /**
     * Displays a preferences dialog for the user to change theme and font size.
     */
    private void showPreferencesDialog() {
        String[] themes = {"light", "dark"};
        String theme = (String) JOptionPane.showInputDialog(this, "Select theme:", "Preferences",
                JOptionPane.QUESTION_MESSAGE, null, themes, userPreferences.getTheme());

        if (theme != null) {
            userPreferences.setTheme(theme);
            applyTheme(theme);
        }

        String fontSizeStr = JOptionPane.showInputDialog(this, "Enter font size:", userPreferences.getFontSize());
        if (fontSizeStr != null && !fontSizeStr.isEmpty()) {
            int fontSize = Integer.parseInt(fontSizeStr);
            userPreferences.setFontSize(fontSize);
            updateFontSizes(fontSize);
        }

        userPreferences.savePreferences();
    }

    /**
     * Updates the font sizes of the input field, output area, and buttons.
     *
     * @param fontSize the new font size
     */
    private void updateFontSizes(int fontSize) {
        inputField.setFont(new Font("Arial", Font.PLAIN, fontSize));
        outputArea.setFont(new Font("Arial", Font.PLAIN, fontSize));

        Component[] components = ((JPanel) getContentPane().getComponent(1)).getComponents();
        for (Component component : components) {
            component.setFont(new Font("Arial", Font.PLAIN, fontSize));
        }
    }

    /**
     * Saves the calculation history to a file.
     */
    private void saveHistoryToFile() {
        try (FileWriter writer = new FileWriter("calculator_history.txt")) {
            for (String calculation : history) {
                writer.write(calculation + "\n");
            }
            JOptionPane.showMessageDialog(this, "Calculation history saved to file.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving calculation history to file.");
            e.printStackTrace();
        }
    }

    /**
     * Loads the calculation history from a file.
     */
    private void loadHistoryFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("calculator_history.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                outputArea.append(line + "\n");
            }
            JOptionPane.showMessageDialog(this, "Calculation history loaded from file.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading calculation history from file.");
            e.printStackTrace();
        }
    }

    /**
     * Inner class to handle button click events.
     */
    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            switch (command) {
                case "Preferences":
                    showPreferencesDialog();
                    break;
                case "=":
                    try {
                        String expression = inputField.getText();
                        double result = calculator.calculate(expression);
                        String calculation = expression + " = " + result;
                        outputArea.append(calculation + "\n");
                        history.add(calculation);
                        outputArea.setCaretPosition(outputArea.getDocument().getLength());
                    } catch (Exception ex) {
                        outputArea.append("Error: " + ex.getMessage() + "\n");
                    }
                    inputField.setText("");
                    break;
                case "Clear":
                    outputArea.setText("");
                    inputField.setText("");
                    break;
                case "Save History":
                    saveHistoryToFile();
                    break;
                case "Load History":
                    loadHistoryFromFile();
                    break;
                default:
                    inputField.setText(inputField.getText() + command);
                    break;
            }
        }
    }
}