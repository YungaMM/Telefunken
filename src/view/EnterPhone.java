package view;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionListener;
import java.text.ParseException;

public class EnterPhone extends JPanel{
    private JPanel rootPanel;
    private JButton continueBtn;
    private JFormattedTextField phone;

    public String getPhone(){
        return (String) phone.getValue();
    }

    public void addListenerForChangeForm(ActionListener listener) {
        continueBtn.addActionListener(listener);
    }

    public void transferFocusToPhone() {
        phone.requestFocusInWindow();
    }

    public String getFormattedPhone(){
        return getPhone().replaceAll("[^0-9]+", "");
    }

    private void createUIComponents() throws ParseException {
        // TODO: place custom component creation code here
        rootPanel = this;

        MaskFormatter mask = new MaskFormatter("+7 (###) ###-##-##");
        phone = new JFormattedTextField(mask);
    }
}
