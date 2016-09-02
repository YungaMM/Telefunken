package view;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.text.ParseException;


public class EnterPhone extends JPanel {
    private JPanel rootPanel;
    private JButton okBtn;
    private JFormattedTextField phone;

    public String getPhone() {
        if(phone.getValue() != null) {
            try {
                phone.commitEdit();
                return (String) phone.getValue();
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        } else return null;
    }

    public void addListenerForChangeForm(ActionListener listener) {
        okBtn.addActionListener(listener);
    }

    public void addListenerForPhoneField(KeyAdapter listener) {
        phone.addKeyListener(listener);
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
