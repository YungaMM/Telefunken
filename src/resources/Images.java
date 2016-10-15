package resources;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Images {
    private static BufferedImage phoneIcon;
    private static BufferedImage buttonImage;
    private static BufferedImage closeIcon;
    private static BufferedImage hideIcon;
    private static BufferedImage logoIcon;
    private static BufferedImage backgroundImage;
    private static BufferedImage logoIconMini;
    private static BufferedImage smallUserImage;
    private static BufferedImage largeUserImage;
    private static Icon questionIcon;
    private static Icon informationIcon;
    private static Icon warningIcon;
    private static Icon errorIcon;
    private static BufferedImage lockIcon;
    private static BufferedImage gearIcon;
    private static BufferedImage pencilIcon;
    private static BufferedImage sendMessageImage;
    private static BufferedImage penIcon;
    private static BufferedImage penLogo;
    private static BufferedImage magnifyingGlassIcon;
    private static BufferedImage plusImage;
    private static BufferedImage closeOverlay;
    private static BufferedImage addContact;
    private static BufferedImage removeContact;
    private static BufferedImage updateContact;
    private static BufferedImage logoutIcon;

    private Images() {
    }

    public static BufferedImage getLogoutIcon() {
        if(logoutIcon == null)
            logoutIcon = loadImage("logout-icon.png");
        return logoutIcon;
    }

    public static BufferedImage getUpdateContact() {
        if(updateContact == null)
            updateContact = loadImage("43781db5c40ecc39fd718685594f0956.png");
        return updateContact;
    }

    public static BufferedImage getRemoveContact() {
        if(removeContact == null)
            removeContact = loadImage("Remove-Male-User.png");
        return removeContact;
    }

    public static BufferedImage getAddContact() {
        if(addContact == null)
            addContact = loadImage("Add-Male-User.png");
        return addContact;
    }

    public static BufferedImage getCloseOverlay() {
        if(closeOverlay == null)
            closeOverlay = loadImage("Close.png");
        return closeOverlay;
    }

    public static BufferedImage getPlus() {
        if(plusImage == null)
            plusImage = loadImage("LTKjko5Ta.png");
        return plusImage;
    }

    public static BufferedImage getMagnifyingGlassIcon() {
        if(magnifyingGlassIcon == null)
            magnifyingGlassIcon = loadImage("Magnifying_glass_icon.svg.png");
        return magnifyingGlassIcon;
    }

    public static BufferedImage getPenLogo() {
        if(penLogo == null)
            penLogo = loadImage("handposition3png.png");
        return penLogo;
    }

    public static BufferedImage getPenIcon() {
        if(penIcon == null)
            penIcon = loadImage("writing-146913_960_720.png");
        return penIcon;
    }

    public synchronized static BufferedImage getSendMessageImage() {
        if (sendMessageImage == null)
            sendMessageImage = loadImage("letter2.png");
        return sendMessageImage;
    }

    public static BufferedImage getPencilIcon() {
        if(pencilIcon == null)
            pencilIcon = loadImage("blue-pencil.jpg");
        return pencilIcon;
    }

    public static BufferedImage getGearIcon() {
        if(gearIcon == null)
            gearIcon = loadImage("ModelcheckingPlugin.ICON.png");
        return gearIcon;
    }

    public static BufferedImage getLockIcon() {
        if(lockIcon == null)
            lockIcon = loadImage("padlock_closed_inv.png");
        return lockIcon;
    }

    public static BufferedImage getButtonImage() {
        if(buttonImage == null) {
            buttonImage = loadImage("button-background.png");
        }
        return buttonImage;
    }

    public static BufferedImage getLogoImage() {
        if(logoIcon == null) {
            logoIcon = loadImage("logo.png");
        }
        return logoIcon;
    }

    public static BufferedImage getLogoImageMini() {
        if(logoIconMini == null) {
            logoIconMini = loadImage("logo-mini.png");
        }
        return logoIconMini;
    }

    public static BufferedImage getPhoneIcon() {
        if(phoneIcon == null)
            phoneIcon = loadImage("phone-icon-hi.png");
        return phoneIcon;
    }

    public static Icon getQuestionIcon() {
        if(questionIcon == null)
            questionIcon = scaleImageToIcon(loadImage("icon-question.png"));
        return questionIcon;
    }

    public static Icon getInformationIcon() {
        if(informationIcon == null)
            informationIcon = scaleImageToIcon(loadImage("icon-information.png"));
        return informationIcon;
    }

    public static Icon getWarningIcon() {
        if(warningIcon == null)
            warningIcon = scaleImageToIcon(loadImage("icon-warning.png"));
        return warningIcon;
    }

    public static Icon getErrorIcon() {
        if(errorIcon == null)
            errorIcon = scaleImageToIcon(loadImage("icon-error.png"));
        return errorIcon;
    }

    public static BufferedImage getBackgroundImage() {
        if(backgroundImage == null) {
            backgroundImage = loadImage("background.png");
        }
        return backgroundImage;
    }

    public static BufferedImage getCloseImage() {
        if(closeIcon == null) {
            closeIcon = loadImage("icon-close.png");
        }
        return closeIcon;
    }

    public static BufferedImage getHideImage() {
        if(hideIcon == null) {
            hideIcon = loadImage("icon-hide.png");
        }
        return hideIcon;
    }

    public synchronized static BufferedImage getSmallUserImage() {
        if (smallUserImage == null)
            smallUserImage = loadImage("small-user.jpg");
        return smallUserImage;
    }

    public synchronized static BufferedImage getLargeUserImage() {
        if (largeUserImage == null)
            largeUserImage = loadImage("large-user.png");
        return largeUserImage;
    }

    public static BufferedImage getUserImage(boolean small) {
        return small ? getSmallUserImage() : getLargeUserImage();
    }

    private static BufferedImage loadImage(String name) {
        try {
            return ImageIO.read(Images.class.getResource("/images/" + name));
        } catch (IOException e) {
            e.printStackTrace();
            return new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
        }
    }

    private static BufferedImage scaleImage(BufferedImage image, int width, int height) {
        BufferedImage result = new BufferedImage(width, height, image.getType());
        Graphics2D g2d = result.createGraphics();
        try {
            g2d.drawImage(image, 0, 0, width, height, null);
        } finally {
            g2d.dispose();
        }
        return result;
    }

    private static Icon scaleImageToIcon(BufferedImage image) {
        return new ImageIcon(scaleImage(image, 50, 50));
    }
}
