package view;

import javax.swing.*;
import java.awt.event.ActionListener;


public class Registration extends JPanel{
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

    private void createUIComponents() {
        rootPanel = this;
    }
}
