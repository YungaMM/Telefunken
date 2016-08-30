package view.contactsList;

import org.javagram.response.object.UserContact;
import javax.swing.*;
import java.awt.*;

public class ContactForList extends JPanel implements ListCellRenderer {
    private JPanel rootPanel;
    private JLabel contactIcon;
    private JLabel lastTime;
    private JLabel contactName;
    private JLabel lastMessage;
    private JPanel iconPanel;
    private JPanel dataPanel;

    private void createUIComponents() {
        rootPanel = this;
    }

    @Override
    public Component getListCellRendererComponent(JList list,
                                                  Object value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {

        if (value instanceof UserContact) {
            contactIcon.setPreferredSize(new Dimension(5, 5));
            contactName.setPreferredSize(new Dimension(0, 50));
            lastMessage.setPreferredSize(new Dimension(0, 50));
            lastTime.setPreferredSize(new Dimension(0, 10));

            UserContact contact = (UserContact) value;
//            contactIcon.setIcon(contact.getIcon());
//            contactIcon.setText("");
            contactName.setText(contact.toString());
//            lastMessage.setText(contact.getLastMassage());
//            lastTime.setText(contact.getLastTimeSession());

            setContactBackground(isSelected);
            return this;
        }
        return null;
    }

    private void setContactBackground(boolean isSelected) {
        //https://docs.oracle.com/javase/7/docs/api/javax/swing/ListCellRenderer.html
        Color background;
        Color foreground;
        if (isSelected) {
            background = Color.RED;
            foreground = Color.WHITE;
        } else {
            background = Color.WHITE;
            foreground = Color.BLACK;
        }

        rootPanel.setBackground(background);
        iconPanel.setBackground(background);
        dataPanel.setBackground(background);

        contactName.setForeground(foreground);
        lastMessage.setForeground(foreground);
        lastTime.setForeground(foreground);
    }
}
