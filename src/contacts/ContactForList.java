package contacts;

import components.PhotoPanel;
import gui.Helper;
import org.javagram.dao.*;
import org.javagram.dao.proxy.TelegramProxy;
import org.javagram.response.object.UserContact;
import javax.swing.*;
import java.awt.*;

public class ContactForList extends JPanel implements ListCellRenderer <Person>{
    private JPanel rootPanel;
    private JLabel nameLabel;
    private JTextPane lastMessageLabel;
    private JPanel photoPanel;

    private TelegramProxy telegramProxy;
    private boolean hasFocus;

    private final int focusMarkerWidth = 4;

    public ContactForList(TelegramProxy telegramProxy) {
        this.telegramProxy = telegramProxy;

        Helper.clearBoth(lastMessageLabel);

        //Рекомендуемый размер является минимальным
        //Меньше JScrollPane ужимать не будет
        //setPreferredSize(new Dimension(0, 100));
        //А вот setMinimumSize(), вопреки здравому смыслу, вызывать не стоит
    }

    private void createUIComponents() {
        rootPanel = this;

        photoPanel = new PhotoPanel(null, true, false, 0, false);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        graphics.setColor(Color.lightGray);
        graphics.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
        if(hasFocus) {
            graphics.setColor(Color.blue);
            graphics.fillRect(0/*this.getWidth() - focusMarkerWidth*/, 0, focusMarkerWidth, this.getHeight());
        }
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Person> jList,
                                                  Person person,
                                                  int index, boolean selected, boolean hasFocus) {

        org.javagram.dao.Dialog dialog = telegramProxy.getDialog(person);
        this.nameLabel.setText(person.getFirstName() + " " + person.getLastName());

        if(dialog != null){
            this.lastMessageLabel.setText(dialog.getLastMessage().getText());
        } else {
            this.lastMessageLabel.setText("");
        }

        if(selected)
            setBackground(Color.white);
        else {
            setBackground(Color.lightGray);
        }

        this.hasFocus = hasFocus;

        ((PhotoPanel)photoPanel).setImage(Helper.getPhoto(telegramProxy, person, true, true));
        ((PhotoPanel)photoPanel).setOnline(telegramProxy.isOnline(person));

        return this;
    }

}
