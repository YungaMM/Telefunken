package gui;

import components.BlueButton;
import components.ImagePanel;
import components.MaxLengthDocumentFilter;
import resources.Images;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionListener;

public class CodeForm extends ImagePanel{
    private JPanel rootPanel;
    private JLabel phoneLabel;
    private JButton okButton;
    private JPanel logoPanel;
    private JPanel codePanel;
    private JTextPane hintTextPane;
    private JPanel codeIcon;
    private JPasswordField codePasswordField;

    public CodeForm(){
        super(Images.getBackgroundImage(), true, false, 0);

        Helper.adjustTextPane(hintTextPane);
        Helper.clearBoth(codePasswordField);

        phoneLabel.setFont(resources.Fonts.getLightFont().deriveFont(Font.PLAIN,36));
        hintTextPane.setFont(resources.Fonts.getRegularFont().deriveFont(Font.PLAIN,24));
        codePasswordField.setFont(resources.Fonts.getRegularFont().deriveFont(Font.PLAIN,26));

        if(codePasswordField.getDocument() instanceof AbstractDocument)
            ((AbstractDocument) codePasswordField.getDocument()).setDocumentFilter(new MaxLengthDocumentFilter(5));

        this.logoPanel.setBorder(BorderFactory.createEmptyBorder());

        okButton.setFont(resources.Fonts.getRegularFont().deriveFont(Font.PLAIN, 24));
    }

    public void addListenerForChangeForm(ActionListener listener) {
        okButton.addActionListener(listener);
        codePasswordField.addActionListener(listener);
    }

    public void setPhoneLabel(String phoneLabel) {
        this.phoneLabel.setText(phoneLabel);
    }

    public String getCode() {
        return String.valueOf(codePasswordField.getPassword());
    }

    public void transferFocusToCode() {
        codePasswordField.setText("");
        codePasswordField.requestFocusInWindow();
    }

    public void clear() {
        codePasswordField.setText("");
        phoneLabel.setText("");
    }

    private void createUIComponents() {
        rootPanel = this;
        logoPanel = new ImagePanel(Images.getLogoImageMini(), false, true, 0);
        codeIcon = new ImagePanel(Images.getLockIcon(),false,true,0);
        okButton = new BlueButton();
    }
}
