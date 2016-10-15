package gui;

import components.BlueButton;
import components.ImagePanel;
import components.GuiHelper;
import resources.Images;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.text.ParseException;


public class PhoneForm extends ImagePanel {
    private JPanel rootPanel;
    private JButton okButton;
    private JFormattedTextField phone;
    private JPanel logoPanel;
    private JTextPane hintTextPane;
    private JPanel phonePanel;
    private JPanel phoneIcon;

    public PhoneForm() {
        super(Images.getBackgroundImage(), true, false, 0);

        this.logoPanel.setBorder(BorderFactory.createEmptyBorder());

        hintTextPane.setFont(resources.Fonts.getRegularFont().deriveFont(Font.PLAIN, 26));
        Helper.adjustTextPane(hintTextPane);

        createPhoneField();

        okButton.setFont(resources.Fonts.getRegularFont().deriveFont(Font.PLAIN, 24));
        //GuiHelper.decorateAsImageButton(okButton, Images.getButtonImage(), null, Color.WHITE);

        //Альтернативное решение
        //BlueButton.decorateButton(okButton);
    }

    public String getPhone() {
        try {
            phone.commitEdit();
            return phone.getValue().toString();
        } catch (ParseException e) {
            //e.printStackTrace();
            return null;
        }
    }

    public void addListenerForChangeForm(ActionListener listener) {
        okButton.addActionListener(listener);
        phone.addActionListener(listener);
    }

    public void transferFocusToPhone() {
        phone.setText("");
        phone.requestFocusInWindow();
    }

    public void clear() {
        phone.setText("");
        phone.setValue("");
    }

    private void createUIComponents() throws ParseException {
        rootPanel = this;
        logoPanel = new ImagePanel(Images.getLogoImage(), false, true, 0);
        phoneIcon = new ImagePanel(Images.getPhoneIcon(), false, true, 0);
        okButton = new BlueButton();
    }

    private void createPhoneField() {
        Helper.clearBoth(phone);
        try {
            MaskFormatter maskFormatter = new MaskFormatter("+7 (###) ###-##-##");
            maskFormatter.setPlaceholder(null);
            maskFormatter.setPlaceholderCharacter('.');
            phone.setFormatterFactory(new DefaultFormatterFactory(maskFormatter));
            phone.setFocusLostBehavior(JFormattedTextField.PERSIST);
            phone.setFont(resources.Fonts.getSemiBoldFont().deriveFont(Font.PLAIN, 32));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
