package view.dialogs;

import javax.swing.*;
import java.awt.event.ActionListener;

public class ConfirmDialogPanel extends JPanel{
    private JPanel rootPanel;
    private JButton yesButton;
    private JButton noButton;
    private JLabel message;

    public ConfirmDialogPanel(String message) {
        this.message.setText(message);
    }

    private void createUIComponents() {
        rootPanel = this;
    }

    public void addActionListenerForYes(ActionListener action) {
        yesButton.addActionListener(action);
    }

    public void addActionListenerForNo(ActionListener action) {
        noButton.addActionListener(action);
    }

}
