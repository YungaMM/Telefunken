package overlays;

import components.GuiHelper;
import components.OverlayBackground;
import org.javagram.dao.Me;
import org.javagram.dao.proxy.TelegramProxy;
import resources.Fonts;
import resources.Images;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by HerrSergio on 15.06.2016.
 */
public class ProfileForm extends OverlayBackground {
    private JButton closeButton;
    private JButton logoutButton;
    private JPanel photoPanel;
    private JPanel rootPanel;
    private JLabel nameLabel;

    private TelegramProxy telegramProxy;

    {
        nameLabel.setFont(Fonts.getRegularFont().deriveFont(0, 45));
        nameLabel.setForeground(Color.white);

        GuiHelper.decorateAsImageButton(closeButton, Images.getCloseOverlay());
        GuiHelper.decorateAsImageButton(logoutButton, Images.getLogoutIcon());
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        rootPanel = this;

        photoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics graphics) {
                //super.paintComponent(graphics);
                if(telegramProxy == null)
                    return;
                BufferedImage me = null;
                try {
                    me = telegramProxy.getPhoto(telegramProxy.getMe(), false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(me == null)
                    me = Images.getLargeUserImage();
                GuiHelper.drawImage(graphics, me, 0, 0, this.getWidth(), this.getHeight());
            }
        };

        //Альтернтивное решение
        //closeButton = new ImageButton(Images.getCloseOverlay());
        //logoutButton = new ImageButton(Images.getLogoutIcon());
    }

    public TelegramProxy getTelegramProxy() {
        return telegramProxy;
    }

    public void setTelegramProxy(TelegramProxy telegramProxy) {
        this.telegramProxy = telegramProxy;
        if(telegramProxy != null) {
            Me me = telegramProxy.getMe();
            nameLabel.setText(me.getFirstName() + " " + me.getLastName());
        } else {
            nameLabel.setText("");
        }
        repaint();
    }

    public void addActionListenerForLogout(ActionListener actionListener) {
        logoutButton.addActionListener(actionListener);
    }

    public void removeActionListenerForLogout(ActionListener actionListener) {
        logoutButton.removeActionListener(actionListener);
    }

    public void addActionListenerForClose(ActionListener actionListener) {
        closeButton.addActionListener(actionListener);
    }

    public void removeActionListenerForClose(ActionListener actionListener) {
        closeButton.removeActionListener(actionListener);
    }
}
