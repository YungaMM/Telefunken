package gui;

import components.BlueButton;
import contacts.ContactsListPanel;
import messsages.MessagesForm;
import org.javagram.TelegramApiBridge;
import org.javagram.dao.*;
import org.javagram.dao.proxy.TelegramProxy;
import org.javagram.dao.proxy.changes.UpdateChanges;
import overlays.*;
import resources.Images;
import undecorated.ComponentResizerAbstract;
import undecorated.DecorationForFrame;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.List;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;

public class BasicFrame extends JFrame {
    private static final String ERROR_TITLE = "Ошибка!";
    private static final String WARNING_TITLE = "Внимание!";

    private static final String PHONE_NUMBER_INVALID = "Неверно введен номер телефона!";
    private static final String PHONE_NUMBER_INCORRECT = "Введите корректный номер телефона!";
    private static final String EMPTY_FIELD = "Заполните все поля!";

    private static final int MAIN_WINDOW = -1, PROFILE_FORM = 0, ADD_CONTACT_FORM = 1, EDIT_CONTACT_FORM = 2;

    private TelegramDAO telegramDAO;
    private TelegramProxy telegramProxy;
    private DecorationForFrame undecoratedFrame;

    private PhoneForm phoneForm = new PhoneForm();
    private CodeForm codeForm = new CodeForm();
    private RegistrationForm registrationForm = new RegistrationForm();
    private MainForm mainForm = new MainForm();
    private ContactsListPanel contactsList = new ContactsListPanel();

    private MyLayeredPane contactsLayeredPane = new MyLayeredPane();
    private PlusOverlay plusOverlay = new PlusOverlay();

    private ProfileForm profileForm = new ProfileForm();
    private AddContactForm addContactForm = new AddContactForm();
    private EditContactForm editContactForm = new EditContactForm();
    private MyBufferedOverlayDialog mainWindowManager = new MyBufferedOverlayDialog(
            mainForm, profileForm, addContactForm, editContactForm);

    private int messagesFrozen;
    private Timer timer;

    {
        setTitle("Javagram");
        setIconImage(Images.getLogoImage());

        setSize(925 + 4, 390 + 39);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);

        undecoratedFrame = new DecorationForFrame(
                this, ComponentResizerAbstract.KEEP_RATIO_CENTER);

        initFrameListener();
        initPhonePanel();
        initCodePanel();
        initRegistrationPanel();
        initMainForm();
        initPlusOverlay();
        initAddContactForm();
        initProfileForm();
        initEditContactForm();

        timer = new Timer(2000, actionEvent -> checkForUpdates(false));
        timer.start();

        changeContentPanel(phoneForm);
    }

    public BasicFrame(TelegramDAO telegramDAO) throws HeadlessException {
        this.telegramDAO = telegramDAO;
    }

    private void close() {
        telegramDAO.close();
        System.exit(0);
    }

    private void abort(Throwable e) {
        if (e != null)
            e.printStackTrace();
        else
            System.err.println("Unknown Error");
        telegramDAO.close();
        System.exit(-1);
    }

    private void initFrameListener() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        undecoratedFrame.addActionListenerForMinimize(e -> BasicFrame.this.setState(ICONIFIED));
        undecoratedFrame.addActionListenerForClose(e -> BasicFrame.this.dispatchEvent(
                new WindowEvent(BasicFrame.this, WindowEvent.WINDOW_CLOSING)));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                if (showQuestionMessage("Уверены, что хотите выйти?", "Вопрос"))
                    close();
            }

            @Override
            public void windowOpened(WindowEvent windowEvent) {
                phoneForm.transferFocusToPhone();
            }
        });
    }

    private void initPhonePanel() {
        phoneForm.addListenerForChangeForm(actionEvent -> {
            String phoneNumber = phoneForm.getPhone();
            if (phoneNumber == null) {
                showErrorMessage(PHONE_NUMBER_INCORRECT, ERROR_TITLE);
                phoneForm.transferFocusToPhone();
            } else {
                switchPhoneToCode(phoneNumber);
            }
        });
    }

    private void initRegistrationPanel() {
        registrationForm.addListenerForChangeForm(e -> {
            String phone = phoneForm.getPhone();
            switchRegistrationToCode(phone);
        });
    }

    private void initCodePanel() {
        codeForm.addListenerForChangeForm(e -> {
            String authCode = codeForm.getCode();
            switchFromCodeToMain(authCode);
        });
    }

    private void initMainForm() {
        mainForm.setContactsPanel(contactsLayeredPane);
        contactsLayeredPane.add(contactsList, new Integer(0));
        contactsLayeredPane.add(plusOverlay, new Integer(1));

        contactsList.addListSelectionListener(listSelectionEvent -> {
            if (listSelectionEvent.getValueIsAdjusting() || messagesFrozen != 0)
                return;

            if (telegramProxy == null) {
                displayDialog(null);
            } else {
                displayDialog(contactsList.getSelectedValue());
            }
        });

        mainForm.addSearchEventListener(actionEvent ->
                searchFor(mainForm.getSearchText()));

        mainForm.addSendMessageListener(actionEvent -> {
            Person buddy =  contactsList.getSelectedValue();
            String text = mainForm.getMessageText().trim();
            if(telegramProxy != null && buddy != null && !text.isEmpty()) {
                try {
                    telegramProxy.sendMessage(buddy, text);
                    mainForm.setMessageText("");
                    checkForUpdates(true);
                } catch (Exception e) {
                    showErrorMessage("Не могу отправить сообщение", "Ошибка!");
                }
            }
        });

        mainForm.addGearEventListener(actionEvent -> {
            profileForm.setTelegramProxy(telegramProxy);
            mainWindowManager.setIndex(PROFILE_FORM);
        });

        mainForm.addBuddyEditEventListener(actionEvent -> {
            Person person = contactsList.getSelectedValue();
            if(person instanceof Contact) {
                editContactForm.setContactInfo(new ContactInfo((Contact) person));
                editContactForm.setPhoto(Helper.getPhoto(telegramProxy, person, false, true));
                mainWindowManager.setIndex(EDIT_CONTACT_FORM);
            }
        });
    }

    private void initPlusOverlay(){
        plusOverlay.addActionListener(actionEvent -> {
            ContactInfo contactInfo = new ContactInfo();
            Person person = contactsList.getSelectedValue();
            if(person instanceof KnownPerson && ! (person instanceof Contact))
                contactInfo.setPhone(((KnownPerson) person).getPhoneNumber());
            addContactForm.setContactInfo(contactInfo);
            mainWindowManager.setIndex(ADD_CONTACT_FORM);
        });
    }

    private void initAddContactForm(){
        addContactForm.addActionListenerForClose(actionEvent ->
                mainWindowManager.setIndex(MAIN_WINDOW));

        addContactForm.addActionListenerForAdd(actionEvent ->
                tryAddContact(addContactForm.getContactInfo()));
    }

    private void initProfileForm(){
        profileForm.addActionListenerForClose(actionEvent ->
                mainWindowManager.setIndex(MAIN_WINDOW));

        profileForm.addActionListenerForLogout(actionEvent ->
                switchToBegin());
    }

    private void initEditContactForm(){
        editContactForm.addActionListenerForClose(actionEvent ->
                mainWindowManager.setIndex(MAIN_WINDOW));

        editContactForm.addActionListenerForSave(actionEvent ->
                tryUpdateContact(editContactForm.getContactInfo()));

        editContactForm.addActionListenerForRemove(actionEvent ->
                tryDeleteContact(editContactForm.getContactInfo()));
    }

    private void switchToBegin() {
        try {
            destroyTelegramProxy();
            this.codeForm.clear();
            this.phoneForm.clear();
            mainWindowManager.setIndex(MAIN_WINDOW);
            changeContentPanel(phoneForm);
            phoneForm.transferFocusToPhone();
            if (!telegramDAO.logOut())
                throw new RuntimeException("Отказ сервера разорвать соединение");
        } catch (Exception e) {
            showErrorMessage("Продолжение работы не возможно", "Критическая ошибка!");
            abort(e);
        }
    }

    private void switchToCode(String phone) {
        try {
            telegramDAO.sendCode();
        } catch (Exception e) {
            showErrorMessage("Потеряно соединение с сервером", ERROR_TITLE);
            changeContentPanel(phoneForm);
            phoneForm.transferFocusToPhone();
            return;
        }
        codeForm.setPhoneLabel(phone);
        changeContentPanel(codeForm);
        codeForm.transferFocusToCode();
    }

    private void switchToRegistration(String phone) {
        String text = phone + " не зарегестрирован!\n" +
                "Для продолжения работы необходимо пройти регистрацию!";
        if (showQuestionMessage(text, WARNING_TITLE)) {
            changeContentPanel(registrationForm);
            registrationForm.transferFocusToFirstName();
        }
    }

    private void switchPhoneToCode(String phone) {
        try {
            telegramDAO.acceptNumber(phone.replaceAll("[\\D]+", ""));
        } catch (IOException | NullPointerException e) {
            showWarningMessage(PHONE_NUMBER_INVALID, ERROR_TITLE);
            phoneForm.transferFocusToPhone();
            return;
        }

        if (telegramDAO.canSignIn()) {
            switchToCode(phone);
        } else if (telegramDAO.canSignUp()) {
            switchToRegistration(phone);
        } else {
            abort(null);
        }
    }

    private void switchRegistrationToCode(String phone) {
        String firstName = registrationForm.getFirstName().trim();
        String lastName = registrationForm.getLastName().trim();

        if (!firstName.equals("") && !lastName.equals("")) {
            switchToCode(phone);
        } else {
            showErrorMessage(EMPTY_FIELD, ERROR_TITLE);
            if (firstName.equals(""))
                registrationForm.transferFocusToFirstName();
            else
                registrationForm.transferFocusToLastName();
        }
    }

    private void switchFromCodeToMain(String code) {
        try {
            telegramDAO.signIn(code);
            changeContentPanel(mainWindowManager);
            createTelegramProxy();
        } catch (Exception e) {
            showWarningMessage("Неверный код", "Внимание!");
            codeForm.transferFocusToCode();
        }
    }

    private void changeContentPanel(Container contentPanel) {
        undecoratedFrame.setContentPanel(contentPanel);
    }

    private boolean tryAddContact(ContactInfo info) {
        String phone = info.getClearedPhone() ;
        if(phone.isEmpty()) {
            showWarningMessage("Пожалуйста, введите номер телефона", "Ошибка");
            return false;
        }
        if(info.getFirstName().isEmpty() && info.getLastName().isEmpty()) {
            showWarningMessage("Пожалуйста, введите имя и/или фамилию", "Ошибка");
            return false;
        }
        for(Person person : telegramProxy.getPersons()) {
            if(person instanceof Contact) {
                if(((Contact) person).getPhoneNumber().replaceAll("\\D+", "").equals(phone)) {
                    showWarningMessage("Контакт с таким номером уже существует", "Ошибка");
                    return false;
                }
            }
        }

        if(!telegramProxy.importContact(info.getPhone(), info.getFirstName(), info.getLastName())) {
            showErrorMessage("Ошибка на сервере при добавлении контакта", "Ошибка");
            return  false;
        }

        mainWindowManager.setIndex(MAIN_WINDOW);
        checkForUpdates(true);
        return true;
    }

    private boolean tryUpdateContact(ContactInfo info) {
        String phone = info.getClearedPhone() ;

        if(info.getFirstName().isEmpty() && info.getLastName().isEmpty()) {
            showWarningMessage("Пожалуйста, введите имя и/или фамилию", "Ошибка");
            return false;
        }

        if(!telegramProxy.importContact(info.getPhone(), info.getFirstName(), info.getLastName())) {
            showErrorMessage("Ошибка на сервере при изменении контакта", "Ошибка");
            return  false;
        }

        mainWindowManager.setIndex(MAIN_WINDOW);
        checkForUpdates(true);
        return true;
    }

    private boolean tryDeleteContact(ContactInfo info) {
        int id = info.getId();

        if(!telegramProxy.deleteContact(id)) {
            showErrorMessage("Ошибка на сервере при удалении контакта", "Ошибка");
            return  false;
        }

        mainWindowManager.setIndex(MAIN_WINDOW);
        checkForUpdates(true);
        return true;
    }

    protected void checkForUpdates(boolean force) {
        if (telegramProxy != null) {
            UpdateChanges updateChanges = telegramProxy.update(force ? TelegramProxy.FORCE_SYNC_UPDATE : TelegramProxy.USE_SYNC_UPDATE);

            int photosChangedCount = updateChanges.getLargePhotosChanged().size() +
                    updateChanges.getSmallPhotosChanged().size() +
                    updateChanges.getStatusesChanged().size();

            if (updateChanges.getListChanged()) {
                updateContacts();
            } else if (photosChangedCount != 0) {
                contactsList.repaint();
            }

            Person currentBuddy = getMessagesForm().getPerson();
            Person targetPerson = contactsList.getSelectedValue();

            org.javagram.dao.Dialog currentDialog = currentBuddy != null ? telegramProxy.getDialog(currentBuddy) : null;

            if (!Objects.equals(targetPerson, currentBuddy) ||
                    updateChanges.getDialogsToReset().contains(currentDialog) ||
                    //updateChanges.getDialogsChanged().getChanged().containsKey(currentDialog) ||
                    updateChanges.getDialogsChanged().getDeleted().contains(currentDialog)) {
                updateMessages();
            } else if(updateChanges.getPersonsChanged().getChanged().containsKey(currentBuddy)
                    || updateChanges.getSmallPhotosChanged().contains(currentBuddy)
                    || updateChanges.getLargePhotosChanged().contains(currentBuddy)) {
                displayBuddy(targetPerson);
            }

            if(updateChanges.getPersonsChanged().getChanged().containsKey(telegramProxy.getMe())
                    || updateChanges.getSmallPhotosChanged().contains(telegramProxy.getMe())
                    || updateChanges.getLargePhotosChanged().contains(telegramProxy.getMe())) {
                displayMe(telegramProxy.getMe());
            }
        }
    }

    private void updateContacts() {
        messagesFrozen++;
        try {
            Person person = contactsList.getSelectedValue();
            contactsList.setTelegramProxy(telegramProxy);
            contactsList.setSelectedValue(person);
        } finally {
            messagesFrozen--;
        }
    }

    private void searchFor(String text) {
        text = text.trim();
        if(text.isEmpty()) {
            return;
        }
        String[] words = text.toLowerCase().split("\\s+");
        java.util.List<Person> persons = telegramProxy.getPersons();
        Person person = contactsList.getSelectedValue();
        person = searchFor(text.toLowerCase(), words, persons, person);
        contactsList.setSelectedValue(person);
        if(person == null)
            showInformationMessage("Ничего не найдено", "Поиск");
    }

    private static Person searchFor(String text, String[] words,
                                    java.util.List<? extends Person> persons, Person current) {
        int currentIndex = persons.indexOf(current);

        for(int i = 1; i <= persons.size(); i++) {
            int index = (currentIndex + i) % persons.size();
            Person person = persons.get(index);
            if(contains(person.getFirstName().toLowerCase(), words)
                    || contains(person.getLastName().toLowerCase(), words)) {
                return person;
            }
        }
        return null;
    }

    private static boolean contains(String text, String... words) {
        for(String word : words) {
            if(text.contains(word))
                return true;
        }
        return false;
    }


    private void displayDialog(Person person) {
        try {
            MessagesForm messagesForm = getMessagesForm();
            messagesForm.display(person);
            displayBuddy(person);
            revalidate();
            repaint();
        } catch (Exception e) {
            showErrorMessage("Проблема соединения с сервером", "проблемы в сети");
        }
    }

    private void displayMe(Me me) {
        if(me == null) {
            mainForm.setMeText(null);
            mainForm.setMePhoto(null);
        } else {
            mainForm.setMeText(me.getFirstName() + " " + me.getLastName());
            mainForm.setMePhoto(Helper.getPhoto(telegramProxy, me, true, true));
        }
    }

    private void displayBuddy(Person person) {
        if(person == null) {
            mainForm.setBuddyText(null);
            mainForm.setBuddyPhoto(null);
            mainForm.setBuddyEditEnabled(false);
        } else {
            mainForm.setBuddyText(person.getFirstName() + " " + person.getLastName());
            mainForm.setBuddyPhoto(Helper.getPhoto(telegramProxy, person, true, true));
            mainForm.setBuddyEditEnabled(person instanceof Contact);
        }
    }

    private void updateMessages() {
        displayDialog(contactsList.getSelectedValue());
        mainForm.revalidate();
        mainForm.repaint();
    }

    private MessagesForm createMessagesForm() {
        MessagesForm messagesForm = new MessagesForm(telegramProxy);
        mainForm.setMessagesPanel(messagesForm);
        mainForm.revalidate();
        mainForm.repaint();
        return messagesForm;
    }

    private MessagesForm getMessagesForm() {
        if(mainForm.getMessagesPanel() instanceof MessagesForm) {
            return (MessagesForm) mainForm.getMessagesPanel();
        } else {
            return createMessagesForm();
        }
    }


    private void createTelegramProxy() {
        telegramProxy = new TelegramProxy(telegramDAO);
        updateTelegramProxy();
    }

    private void destroyTelegramProxy() {
        telegramProxy = null;
        updateTelegramProxy();
    }

    private void updateTelegramProxy() {
        messagesFrozen++;
        try {
            contactsList.setTelegramProxy(telegramProxy);
            contactsList.setSelectedValue(null);
            createMessagesForm();
            displayDialog(null);
            displayMe(telegramProxy != null ? telegramProxy.getMe() : null);
        } finally {
            messagesFrozen--;
        }

        mainForm.revalidate();
        mainForm.repaint();
    }


    private JButton[] okButton = BlueButton.createDecoratedButtons(JOptionPane.DEFAULT_OPTION);
    private JButton[] yesNoButtons = BlueButton.createDecoratedButtons(JOptionPane.YES_NO_OPTION);

    private void showErrorMessage(String text, String title) {
        DecorationForFrame.showDialog(
                this, text, title, JOptionPane.ERROR_MESSAGE,
                JOptionPane.DEFAULT_OPTION, Images.getErrorIcon(),
                okButton, okButton[0]);
    }

    private void showWarningMessage(String text, String title) {
        DecorationForFrame.showDialog(
                this, text, title, JOptionPane.WARNING_MESSAGE,
                JOptionPane.DEFAULT_OPTION, Images.getWarningIcon(),
                okButton, okButton[0]);
    }

    private void showInformationMessage(String text, String title) {
        DecorationForFrame.showDialog(
                this, text, title, JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.DEFAULT_OPTION, Images.getInformationIcon(),
                okButton, okButton[0]);
    }

    private boolean showQuestionMessage(String text, String title) {
        return DecorationForFrame.showDialog(
                this, text, title, JOptionPane.QUESTION_MESSAGE,
                JOptionPane.DEFAULT_OPTION, Images.getQuestionIcon(),
                yesNoButtons, yesNoButtons[0]) == JOptionPane.YES_OPTION;
    }


}
