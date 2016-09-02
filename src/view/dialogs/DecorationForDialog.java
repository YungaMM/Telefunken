package view.dialogs;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DecorationForDialog extends JPanel {
    private JPanel rootPanel;
    private JPanel buttonPanel;
    private JPanel textPanel;
    private JButton okButton;
    private JButton cancelButton;
    private JLabel messageText;
    private JButton noButton;
    private JButton yesButton;

    public DecorationForDialog(String messageText) {
        this.messageText.setText(messageText);
    }

    public JButton getOkButton() {
        return okButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public JLabel getMessageText() {
        return messageText;
    }

    public JButton getNoButton() {
        return noButton;
    }

    public JButton getYesButton() {
        return yesButton;
    }

    public void addActionListenerForOk(ActionListener action) {
        okButton.addActionListener(action);
    }

    public void addActionListenerForYes(ActionListener action) {
        yesButton.addActionListener(action);
    }

    public void addActionListenerForNo(ActionListener action) {
        noButton.addActionListener(action);
    }

    public void addActionListenerForCancel(ActionListener action) {
        cancelButton.addActionListener(action);
    }

    private void createUIComponents() {
        rootPanel = this;
    }
}
