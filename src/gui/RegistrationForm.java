package gui;

import components.*;
import resources.Images;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistrationForm extends ImagePanel {
    private JPanel rootPanel;
    private JButton okButton;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JPanel logoPanel;
    private JTextPane hintTextPane;
    private JPanel photoPanel;
    private JPanel firstNamePanel;
    private JPanel lastNamePanel;
    private JPanel dataPanel;
    private JPanel imagePhoto;

    public RegistrationForm() {
        super(Images.getBackgroundImage(), true, false, 0);

        Helper.adjustTextPane(hintTextPane);
        Helper.clearBoth(firstNameField);
        Helper.clearBoth(lastNameField);

        hintTextPane.setFont(resources.Fonts.getRegularFont().deriveFont(Font.PLAIN, 24));
        firstNameField.setFont(resources.Fonts.getRegularFont().deriveFont(Font.PLAIN, 30));
        lastNameField.setFont(resources.Fonts.getRegularFont().deriveFont(Font.PLAIN, 30));
        okButton.setFont(resources.Fonts.getRegularFont().deriveFont(Font.PLAIN, 24));

        firstNameField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transferFocusToLastName();
            }
        });
    }

    public void addListenerForChangeForm(ActionListener listener) {
        okButton.addActionListener(listener);
        lastNameField.addActionListener(listener);
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
        logoPanel = new ImagePanel(Images.getLogoImageMini(), false, true, 0);
        imagePhoto = new ImagePanel(Images.getLargeUserImage(), false, true, 0);
        okButton = new BlueButton();

        firstNameField = new HintTextField("Имя", false);
        lastNameField = new HintTextField("Фамилия", false);
    }
}
