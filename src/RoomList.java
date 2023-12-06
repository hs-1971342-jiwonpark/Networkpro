import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RoomList {

    int roomNum =0;
    RoomList() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Game Lobby");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);

            // Main panel
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());




            // Top panel with buttons
            JPanel topPanel = new JPanel();
            JButton ok = new JButton("확인");
            JButton add = new JButton("추가");
            JButton del =  new JButton("삭제");
            topPanel.add(ok);
            topPanel.add(add);
            topPanel.add(del);


            panel.add(topPanel, BorderLayout.NORTH);

            // List model and list component
            DefaultListModel<String> listModel = new DefaultListModel<>();
            add.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // JPanel to hold the label and text field
                    JPanel dialogPanel = new JPanel();
                    dialogPanel.add(new JLabel("Enter room name:"));

                    // Create the text field for room name input
                    JTextField roomNameField = new JTextField(20);
                    dialogPanel.add(roomNameField);

                    // Show the dialog on top of the frame
                    int result = JOptionPane.showConfirmDialog(
                            frame, // use the frame as the parent
                            dialogPanel,
                            "Add Room",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE);

                    // Handle the dialog result
                    if (result == JOptionPane.OK_OPTION) {
                        String roomName = roomNameField.getText().trim();
                        if (!roomName.isEmpty()) {
                            listModel.addElement(roomName);
                            roomNum++;
                        }
                    }
                }
            });
            ok.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Room room = new Room();
                }
            });

            del.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Show an input dialog to get the room name to delete
                    String roomNameToDelete = JOptionPane.showInputDialog(
                            frame,
                            "Enter the room name to delete:",
                            "Delete Room",
                            JOptionPane.PLAIN_MESSAGE);

                    if (roomNameToDelete != null && !roomNameToDelete.trim().isEmpty()) {
                        // Check if the room name exists in the list model
                        boolean exists = false;
                        for (int i = 0; i < listModel.size(); i++) {
                            if (listModel.get(i).equals(roomNameToDelete.trim())) {
                                // If it exists, remove it
                                listModel.remove(i);
                                JOptionPane.showMessageDialog(frame, "Room '" + roomNameToDelete + "' deleted.");
                                exists = true;
                                break;
                            }
                        }
                        // If the room name does not exist, show a message
                        if (!exists) {
                            JOptionPane.showMessageDialog(frame, "Room '" + roomNameToDelete + "' not found.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else if (roomNameToDelete != null) {
                        // If the user entered a blank name, show a warning message
                        JOptionPane.showMessageDialog(frame, "Room name cannot be blank.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });



            JList<String> roomList = new JList<>(listModel);

            // Scroll pane for the list
            JScrollPane scrollPane = new JScrollPane(roomList);
            panel.add(scrollPane, BorderLayout.CENTER);

            // Status bar at the bottom
            JLabel statusBar = new JLabel("Status: Connected");
            panel.add(statusBar, BorderLayout.SOUTH);

            frame.setContentPane(panel);
            frame.setVisible(true);
        });
    }

    public static void main(String[] args) {
        new RoomList();
    }
}
