package view;

import javax.swing.*;
import java.awt.event.ActionListener;

public class EnterConfirmationCode {
    private JPanel rootPanel;
    private JLabel phoneLabel;
    private JButton continueBtn;
    private JPasswordField codeField;

    public void addListenerForChangeForm(ActionListener listener) {
        continueBtn.addActionListener(listener);
    }
}
