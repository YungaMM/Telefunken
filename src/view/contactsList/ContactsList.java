package view.contactsList;

import org.javagram.response.object.UserContact;

import javax.swing.*;
import java.util.ArrayList;

public class ContactsList extends JPanel{
    private JPanel rootPanel;
    private JScrollPane scrollPane;
    private JList contactList;
    private JPanel buttonPanel;
    private JButton buttonOk;
    private JButton buttonCancel;

    private DefaultListModel listModel;

    public ContactsList(ArrayList<UserContact> userContacts) {
//        setListeners();

        listModel = new DefaultListModel();
        for (UserContact userContact : userContacts) {
            listModel.addElement(userContact);
        }

        contactList.setModel(listModel);
        contactList.setCellRenderer(new ContactForList());
    }

//    private void onDelete() {
//        listModel.remove(contactList.getSelectedIndex());
//    }
//
//    private void onOK() {
//        dispose();
//    }
//
//    private void onCancel() {
//        dispose();
//    }
//
//    private void setListeners(){
//        buttonOk.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                onOK();
//            }
//        });
//        buttonCancel.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                onCancel();
//            }
//        });
//        buttonDelete.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                onDelete();
//            }
//        });
//
//// call onCancel() on ESCAPE
//        rootPanel.registerKeyboardAction(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                onCancel();
//            }
//        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
//    }
//
    private void createUIComponents() {
        rootPanel=this;
    }
}
