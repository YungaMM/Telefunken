package view;

import javax.swing.*;
import java.awt.event.ActionListener;

public class EnterPhone extends JPanel{
    private JPanel rootPanel;
    private JButton continueBtn;
    private JFormattedTextField enterPhone;

    public void addListenerForChangeForm(ActionListener listener) {
        continueBtn.addActionListener(listener);
    }
}
