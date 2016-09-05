package view.dialogs;

import javax.swing.*;
import java.awt.event.ActionListener;

public class ErrorMessagePanel extends JPanel{
    private JPanel rootPanel;
    private JLabel message;
    private JButton okButton;

    public ErrorMessagePanel(String messageText) {
        this.message.setText(messageText);
    }

    public void addActionListenerForOk(ActionListener action) {
        okButton.addActionListener(action);
    }

    private void createUIComponents() {
        rootPanel = this;
    }
}
