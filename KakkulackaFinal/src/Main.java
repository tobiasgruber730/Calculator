import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        ComplexFunctions.setMaps();
        SwingUtilities.invokeLater(CalculatorGUI::new);
    }
}
