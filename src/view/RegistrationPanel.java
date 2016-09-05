package view;

import javax.swing.*;
import java.awt.event.ActionListener;

public class RegistrationPanel extends JPanel{
    private JPanel rootPanel;
    private JButton okBtn;
    private JTextField firstNameField;
    private JTextField lastNameField;

    public void addListenerForChangeForm(ActionListener listener) {
        okBtn.addActionListener(listener);
    }

    public String getFirstName() {
        return firstNameField.getText();
    }

    public void setFirstNameField(String text) {
        firstNameField.setText(text);
    }

    public String getLastName() {
        return lastNameField.getText();
    }

    public void setLastNameField(String text) {
        lastNameField.setText(text);
    }

    public void transferFocusToFirstName() {
        firstNameField.requestFocusInWindow();
    }

    public void transferFocusToLastName() {
        lastNameField.requestFocusInWindow();
    }
    private void createUIComponents() {
        rootPanel = this;
    }
}
