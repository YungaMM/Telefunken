package view;

import javax.swing.*;
import java.awt.event.ActionListener;

public class EnterConfirmationCode extends JPanel{
    private JPanel rootPanel;
    private JLabel phoneLabel;
    private JButton continueBtn;
    private JPasswordField codeField;

    public void addListenerForChangeForm(ActionListener listener) {
        continueBtn.addActionListener(listener);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        rootPanel = this;
    }

    public void setPhoneLabel(String phoneLabel) {
        this.phoneLabel.setText(phoneLabel);
    }

    public char[] getCodeField() {
        return codeField.getPassword();
    }

    public void transferFocusToCode() {
        codeField.requestFocusInWindow();
    }
}
