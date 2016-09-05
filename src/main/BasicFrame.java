package main;

import org.javagram.TelegramApiBridge;
import org.javagram.response.AuthCheckedPhone;
import org.javagram.response.object.User;
import org.javagram.response.object.UserContact;
import view.contactsList.ContactsList;
import view.EnterConfirmCodePanel;
import view.EnterPhonePanel;
import view.RegistrationPanel;
import view.dialogs.ConfirmDialogPanel;
import view.dialogs.ErrorMessagePanel;
import view.undecorated.ComponentResizerAbstract;
import view.undecorated.DecorationForFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

public class BasicFrame extends JFrame {
    private static final String FRAME_TITLE = "Telefunken";
    private static final String PHONE_NUMBER_INVALID = "Неверно введен номер телефона!";
    private static final String EMPTY_FIELD = "Заполните все поля!";

    private TelegramApiBridge apiBridge;
    private User user;
    private boolean userRegistered;

    private DecorationForFrame undecoratedFrame;
    private EnterPhonePanel enterPhonePanelPanel = new EnterPhonePanel();
    private EnterConfirmCodePanel confirmCodePanel = new EnterConfirmCodePanel();
    private RegistrationPanel registrationPanelPanel = new RegistrationPanel();

    public BasicFrame(TelegramApiBridge apiBridge) throws HeadlessException {
        this.apiBridge = apiBridge;
        setSize(800, 600);
        undecoratedFrame = new DecorationForFrame(
                this, ComponentResizerAbstract.KEEP_RATIO_CENTER);
        undecoratedFrame.setTitle(FRAME_TITLE);
        undecoratedFrame.addActionListenerForMinimize(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BasicFrame.this.setState(ICONIFIED);
            }
        });

        initCloseWindowListener();
    }

    private void close() {
        System.out.println("Closed!");
        try {
            apiBridge.authLogOut();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private void initCloseWindowListener() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        undecoratedFrame.addActionListenerForClose(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BasicFrame.this.dispose();
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent windowEvent) {
                super.windowClosed(windowEvent);
                close();
            }
        });
    }

    public void showStartPanel() {
        enterPhonePanelPanel.addListenerForChangeForm(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchPhoneToCode();
            }
        });
        enterPhonePanelPanel.addListenerForPhoneField(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) switchPhoneToCode();
            }
        });
        undecoratedFrame.setContentPanel(enterPhonePanelPanel);
        enterPhonePanelPanel.transferFocusToPhone();
    }

    private void showRegistration() {
        registrationPanelPanel.addListenerForChangeForm(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchRegistrationToCode();
            }
        });
        undecoratedFrame.setContentPanel(registrationPanelPanel);
        registrationPanelPanel.transferFocusToFirstName();
    }

    private void showConfirmCodePanel(String phone) {
        confirmCodePanel.addListenerForChangeForm(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchCodeToContactList();
            }
        });
        confirmCodePanel.addListenerForCodeField(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) switchCodeToContactList();
            }
        });
        confirmCodePanel.setPhoneLabel(phone);
        undecoratedFrame.setContentPanel(confirmCodePanel);
        confirmCodePanel.transferFocusToCode();
        sendCode(phone);
    }

    private void showContactList() throws IOException {
        ArrayList<UserContact> userContacts = apiBridge.contactsGetContacts();
        ContactsList contactsList = new ContactsList(userContacts);
        undecoratedFrame.setContentPanel(contactsList);
    }

    private void switchPhoneToCode() {
        String phone = enterPhonePanelPanel.getPhone();

        if (phone == null) {
            showErrorMessage(PHONE_NUMBER_INVALID);
            enterPhonePanelPanel.transferFocusToPhone();
        } else {
            try {
                AuthCheckedPhone checkedPhone = apiBridge.authCheckPhone(phone);
                if (checkedPhone.isRegistered()) {
                    userRegistered = true;
                    showConfirmCodePanel(phone);
                } else {
                    if (showDialogSignUp(phone)) {
                        userRegistered = false;
                        showRegistration();
                    } else {
                        enterPhonePanelPanel.transferFocusToPhone();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                showErrorMessage(textError(e));
                enterPhonePanelPanel.transferFocusToPhone();
            }
        }
    }

    private void switchRegistrationToCode() {
        String firstName = registrationPanelPanel.getFirstName();
        String lastName = registrationPanelPanel.getLastName();
        String phone = enterPhonePanelPanel.getPhone();

        if (!firstName.equals("") && !lastName.equals("")) {
            showConfirmCodePanel(phone);
        } else {
            showErrorMessage(EMPTY_FIELD);
            if (firstName.equals("")) registrationPanelPanel.transferFocusToFirstName();
            else registrationPanelPanel.transferFocusToLastName();
        }
    }

    private void switchCodeToContactList() {
        try {
            String smsCode = confirmCodePanel.getCode();
            if (userRegistered) {
                user = apiBridge.authSignIn(smsCode).getUser();
            } else {
                String firstName = registrationPanelPanel.getFirstName();
                String lastName = registrationPanelPanel.getLastName();
                user = apiBridge.authSignUp(smsCode, firstName, lastName).getUser();
            }
            showContactList();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage(textError(e));
            confirmCodePanel.transferFocusToCode();
        }
    }

    private void sendCode(String phone) {
        try {
            apiBridge.authSendCode(phone);
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage(e.getMessage());
        }
    }

    private String textError(IOException exception) {
        String dialogText;
        switch (exception.getMessage()) {
            case "PHONE_CODE_INVALID":
                dialogText = "Неверно введен СМС код!";
                break;
            case "PHONE_CODE_EXPIRED":
                dialogText = "СМС код просрочен!";
                break;
            case "FIRSTNAME_INVALID":
                dialogText = "Неверный ввод имени!";
                break;
            case "LASTNAME_INVALID":
                dialogText = "Неверный ввод фамилии!";
                break;
            case "PHONE_NUMBER_INVALID":
                dialogText = PHONE_NUMBER_INVALID;
                break;
            default:
                dialogText = "Unknown error";
        }
        return dialogText;
    }

    private void showErrorMessage(String message) {
        JDialog dialog = new JDialog(this, true);

        DecorationForFrame dialogFrame = new DecorationForFrame(
                dialog, ComponentResizerAbstract.KEEP_RATIO_CENTER);
        dialogFrame.setTitle("Внимание!");

        ErrorMessagePanel dialogPanel = new ErrorMessagePanel(message);
        dialogPanel.addActionListenerForOk(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        showDialog(dialog, dialogFrame, dialogPanel);
    }

    private boolean showDialogSignUp(String phone) {
        final boolean[] result = {false};

        String message = "<HTML>" + phone + " не зарегестрирован! " +
                "Для продолжения работы необходимо пройти регистрацию!</HTML>";

        JDialog dialog = new JDialog(this, true);

        DecorationForFrame dialogFrame = new DecorationForFrame(
                dialog, ComponentResizerAbstract.KEEP_RATIO_CENTER);
        dialogFrame.setTitle("Телефонный номер не зарегестрирован!");

        ConfirmDialogPanel dialogPanel = new ConfirmDialogPanel(message);
        dialogPanel.addActionListenerForYes(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                result[0] = true;
                dialog.dispose();
            }
        });
        dialogPanel.addActionListenerForNo(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                result[0] = false;
                dialog.dispose();
            }
        });
        showDialog(dialog, dialogFrame, dialogPanel);

        return result[0];
    }

    private void showDialog(JDialog dialog, DecorationForFrame dialogFrame, JPanel dialogPanel) {
        dialogFrame.setContentPanel(dialogPanel);
        dialogFrame.getMinimizeButton().setVisible(false);
        dialogFrame.addActionListenerForClose(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setSize(500, 250);
        dialog.setLocationRelativeTo(undecoratedFrame);
        dialog.setVisible(true);
    }
}
