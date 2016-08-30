package view;

import javax.swing.*;
import java.awt.event.ActionListener;


public class Registration extends JPanel{
    private JPanel rootPanel;
    private JButton completeBtn;
    private JTextField firstNameField;
    private JTextField lastNameField;

    public void addListenerForChangeForm(ActionListener listener) {
        completeBtn.addActionListener(listener);
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

    private void createUIComponents() {
        rootPanel = this;
    }
}
