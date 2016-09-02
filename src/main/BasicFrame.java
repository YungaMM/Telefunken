package main;

import org.javagram.TelegramApiBridge;
import org.javagram.response.AuthCheckedPhone;
import org.javagram.response.object.User;
import org.javagram.response.object.UserContact;
import view.contactsList.ContactsList;
import view.EnterConfirmationCode;
import view.EnterPhone;
import view.Registration;
import view.dialogs.BasicDialog;
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
    private EnterPhone enterPhonePanel = new EnterPhone();
    private EnterConfirmationCode confirmCodePanel = new EnterConfirmationCode();
    private Registration registrationPanel = new Registration();

    public BasicFrame(TelegramApiBridge apiBridge) throws HeadlessException {
        this.apiBridge = apiBridge;
        setSize(800, 600);
        undecoratedFrame = new DecorationForFrame(
                this, ComponentResizerAbstract.KEEP_RATIO_CENTER);
        undecoratedFrame.setContentPanel(enterPhonePanel);
        undecoratedFrame.setTitle(FRAME_TITLE);
        setListeners();
        closeWindow();
        enterPhonePanel.transferFocusToPhone();
    }

    private void setListeners() {
        enterPhonePanel.addListenerForChangeForm(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchPhoneToCode();
            }
        });
        enterPhonePanel.addListenerForPhoneField(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) switchPhoneToCode();
            }
        });

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

        registrationPanel.addListenerForChangeForm(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchRegistrationToCode();
            }
        });

        undecoratedFrame.addActionListenerForMinimize(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BasicFrame.this.setState(ICONIFIED);
            }
        });
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

    private void closeWindow() {
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

    //***********************************************************************
    private void openRegistration() {
        undecoratedFrame.setContentPanel(registrationPanel);
        registrationPanel.transferFocusToFirstName();
    }

    private void openConfirmCodePanel(String phone) {
        confirmCodePanel.setPhoneLabel(phone);
        undecoratedFrame.setContentPanel(confirmCodePanel);
        confirmCodePanel.transferFocusToCode();
        sendCode(phone);
    }

    private void openContactList() throws IOException {
        ArrayList<UserContact> userContacts = apiBridge.contactsGetContacts();
        ContactsList contactsList = new ContactsList(userContacts);
        undecoratedFrame.setContentPanel(contactsList);
    }

    private void switchPhoneToCode() {
        String phone = enterPhonePanel.getPhone();

        if (phone == null) {
            showErrorMessage(PHONE_NUMBER_INVALID);
            enterPhonePanel.transferFocusToPhone();
        } else {
            try {
                AuthCheckedPhone checkedPhone = apiBridge.authCheckPhone(phone);
                if (checkedPhone.isRegistered()) {
                    userRegistered = true;
                    openConfirmCodePanel(phone);
                } else {
                    if (dialogSignUp(phone)) {
                        userRegistered = false;
                        openRegistration();
                    } else {
                        enterPhonePanel.transferFocusToPhone();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                showErrorMessage(textError(e));
                enterPhonePanel.transferFocusToPhone();
            }
        }
    }

    private void switchRegistrationToCode() {
        String firstName = registrationPanel.getFirstName();
        String lastName = registrationPanel.getLastName();
        String phone = enterPhonePanel.getPhone();

        if (!firstName.equals("") && !lastName.equals("")) {
            openConfirmCodePanel(phone);
        } else {
            showErrorMessage(EMPTY_FIELD);
            registrationPanel.transferFocusToFirstName();
        }
    }

    private void switchCodeToContactList() {
        try {
            String smsCode = confirmCodePanel.getCode();
            if (userRegistered) {
                user = apiBridge.authSignIn(smsCode).getUser();
            } else {
                String firstName = registrationPanel.getFirstName();
                String lastName = registrationPanel.getLastName();
                user = apiBridge.authSignUp(smsCode, firstName, lastName).getUser();
            }
            openContactList();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage(textError(e));
            confirmCodePanel.transferFocusToCode();
        }
    }

    private void endOfRegistration() {
        String smsCode = String.valueOf(confirmCodePanel.getCode());
        String firstName = registrationPanel.getFirstName();
        String lastName = registrationPanel.getLastName();
        try {
            user = apiBridge.authSignUp(smsCode, firstName, lastName).getUser();
            openContactList();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage(textError(e));
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
        BasicDialog.showMessageDialog(this, "Внимание", message);
    }

    private boolean dialogSignUp(String phone) {
        String message = phone + " не зарегестрирован!\n" +
                "Для продолжения работы необходимо пройти регистрацию!";
        int option = BasicDialog.showConfirmDialog(
                this,
                "Телефонный номер не зарегестрирован!",
                message,
                BasicDialog.YES_NO_OPTION
        );
        return option == BasicDialog.YES_OPTION;
    }
}
