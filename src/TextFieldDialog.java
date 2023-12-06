import javax.swing.*;

public class TextFieldDialog {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create the main frame
            JFrame frame = new JFrame("Main Frame");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setLocationRelativeTo(null);

            // Create a button that opens the dialog
            JButton button = new JButton("Open Dialog");
            button.addActionListener(e -> {
                // Create a JPanel to hold the components
                JPanel panel = new JPanel();
                panel.add(new JLabel("Enter text:"));

                // Create the text field
                JTextField textField = new JTextField(20);
                panel.add(textField);

                // Show the dialog with the panel
                int result = JOptionPane.showConfirmDialog(frame, panel, "Enter Text", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                // Handle the dialog result
                if (result == JOptionPane.OK_OPTION) {
                    System.out.println("Text entered: " + textField.getText());
                }
            });

            // Add the button to the main frame
            frame.add(button);
            frame.setVisible(true);
        });
    }
}
