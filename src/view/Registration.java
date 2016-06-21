package view;

import javax.swing.*;
import java.awt.event.ActionListener;


public class Registration {
    private JPanel rootPanel;
    private JButton completeBtn;
    private JTextField firstNameField;
    private JTextField lastNameField;

    public void addListenerForChangeForm(ActionListener listener) {
        completeBtn.addActionListener(listener);
    }
}
