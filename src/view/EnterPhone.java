package view;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionListener;
import java.text.ParseException;


public class EnterPhone extends JPanel {
    private JPanel rootPanel;
    private JButton continueBtn;
    private JFormattedTextField phone;

    public String getPhone() {
        try {
            phone.commitEdit();
            return (String) phone.getValue();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addListenerForChangeForm(ActionListener listener) {
        continueBtn.addActionListener(listener);
    }

    public void transferFocusToPhone() {
        phone.setText("");
        phone.requestFocusInWindow();
    }

    private void createUIComponents() throws ParseException {
        rootPanel = this;

        MaskFormatter mask = new MaskFormatter("+7 (###) ###-##-##");
        phone = new JFormattedTextField(mask);
        phone.setFocusLostBehavior(JFormattedTextField.PERSIST);
    }
}
