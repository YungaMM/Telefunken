package view;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;

public class EnterConfirmationCode extends JPanel{
    private JPanel rootPanel;
    private JLabel phoneLabel;
    private JButton continueBtn;
    private JPasswordField codeField;

    public void addListenerForChangeForm(ActionListener listener) {
        continueBtn.addActionListener(listener);
    }

    public void addListenerForCodeField(KeyAdapter listener) {
        codeField.addKeyListener(listener);
    }

    private void createUIComponents() {
        rootPanel = this;
    }

    public void setPhoneLabel(String phoneLabel) {
        this.phoneLabel.setText(phoneLabel);
    }

    public String getCode() {
        return String.valueOf(codeField.getPassword());
    }

    public void transferFocusToCode() {
        codeField.requestFocusInWindow();
    }
}
