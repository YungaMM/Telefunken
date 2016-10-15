package undecorated;

import components.GuiHelper;
import resources.Images;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

public class DecorationForFrame extends JPanel {
    public static final int DIALOG_DEFAULT_RESIZE_POLICY = -1;

    private JPanel rootPanel;
    private JPanel topPanel;
    private JPanel contentPanel;
    private JButton closeButton;
    private JButton minimizeButton;

    private String title;

    private ComponentMover componentMover;
    private ComponentResizerAbstract componentResizerExtended;

    private DecorationForFrame(Window window, int resizePolicy) {

        GuiHelper.decorateAsImageButton(closeButton,Images.getCloseImage(),null,null);
        GuiHelper.decorateAsImageButton(minimizeButton,Images.getHideImage(),null,null);

        componentMover = new ComponentMover(window, topPanel);
        if (resizePolicy < 0)
            return;
        componentResizerExtended = new ComponentResizerAbstract(resizePolicy, window) {
            @Override
            protected int getExtraHeight() {
                return rootPanel.getHeight() - contentPanel.getHeight();
            }

            @Override
            protected int getExtraWidth() {
                return rootPanel.getWidth() - contentPanel.getWidth();
            }
        };
    }

    public DecorationForFrame(JFrame window, int resizePolicy) {
        this((Window) window, resizePolicy);
        title = window.getTitle();
        window.setUndecorated(true);
        window.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        window.setContentPane(this);
    }

    public DecorationForFrame(JDialog window, int resizePolicy) {
        this((Window) window, resizePolicy);
        setContentPanel(window.getContentPane());

        title = window.getTitle();
        window.setContentPane(this);
        window.setUndecorated(true);
        window.getRootPane().setWindowDecorationStyle(JRootPane.NONE);

        topPanel.remove(minimizeButton);
        addActionListenerForClose(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
            }
        });
    }

    public DecorationForFrame(JDialog window) {
        this(window, DIALOG_DEFAULT_RESIZE_POLICY);
    }

    public void setContentPanel(Component component) {
        contentPanel.removeAll();
        contentPanel.add(component);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void addActionListenerForClose(ActionListener listener) {
        closeButton.addActionListener(listener);
    }

    public void addActionListenerForMinimize(ActionListener listener) {
        minimizeButton.addActionListener(listener);
    }

    public void removeActionListenerForClose(ActionListener listener) {
        closeButton.removeActionListener(listener);
    }

    public void removeActionListenerForMinimize(ActionListener listener) {
        minimizeButton.removeActionListener(listener);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        repaint();
    }
    private void createUIComponents() {
        rootPanel = this;
        topPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);

                Font font = getFont();
                graphics.setFont(font);

                FontMetrics fontMetrics = graphics.getFontMetrics();
                int height = fontMetrics.getAscent();// + fontMetrics.getDescent();
                int pos = (this.getHeight() + height) / 2;

                int start = 4;
                int end = this.getWidth();
                for(Component component : getComponents()) {
                    if(component.getX() < end)
                        end = component.getX();
                }
                end -= 4;
                String text = title;

                while(fontMetrics.stringWidth(text) > end - start) {
                    int len = text.length() - 4;
                    if(len < 0)
                        return;
                    else
                        text = text.substring(0, len) + "...";
                }

                graphics.setColor(Color.black);
                graphics.setFont(graphics.getFont().deriveFont(Font.ITALIC));
                graphics.drawString(text, start, pos);
            }
        };
    }

    //--------------------------------------------------------------------------//

    public static int showDialog(Frame frame, Object message, String title, int messageType, int optionType, Icon icon, Object[] options, Object initialValue) {
        return showDialog(new JDialog(frame, title, true), message, messageType, optionType, icon, options, initialValue);
    }

    public static int showDialog(Dialog dialog, Object message, String title, int messageType, int optionType, Icon icon, Object[] options, Object initialValue) {
        return showDialog(new JDialog(dialog, title, true), message, messageType, optionType, icon, options, initialValue);
    }

    public static int showDialog(Frame frame, Object message, String title, int messageType, int optionType, Icon icon) {
        return showDialog(new JDialog(frame, title, true), message, messageType, optionType, icon, null, null);
    }

    public static int showDialog(Dialog dialog, Object message, String title, int messageType, int optionType, Icon icon) {
        return showDialog(new JDialog(dialog, title, true), message, messageType, optionType, icon, null, null);
    }

    public static int showDialog(Frame frame, Object message, String title, int messageType, int optionType) {
        return showDialog(new JDialog(frame, title, true), message, messageType, optionType, null, null, null);
    }

    public static int showDialog(Dialog dialog, Object message, String title, int messageType, int optionType) {
        return showDialog(new JDialog(dialog, title, true), message, messageType, optionType, null, null, null);
    }

    private static int showDialog(JDialog dialog, Object message, int messageType, int optionType, Icon icon, Object[] options, Object initialValue) {
        JOptionPane optionPane = new JOptionPane(message, messageType, optionType, icon, options, initialValue);
        dialog.setModal(true);
        dialog.setContentPane(optionPane);
        dialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        new DecorationForFrame(dialog);
        dialog.pack();
        dialog.setLocationRelativeTo(dialog.getParent());
        Map<ActionListener, AbstractButton> listeners = new HashMap<>();
        if(options != null) {
            for (Object option : options) {
                if(option instanceof AbstractButton) {
                    AbstractButton abstractButton = (AbstractButton)option;
                    ActionListener actionListener = actionEvent -> optionPane.setValue(option);
                    abstractButton.addActionListener(actionListener);
                    listeners.put(actionListener, abstractButton);
                }
            }
        }
        PropertyChangeListener propertyChangeListener = propertyChangeEvent -> dialog.setVisible(false);
        optionPane.addPropertyChangeListener("value", propertyChangeListener);
        dialog.setVisible(true);
        optionPane.removePropertyChangeListener("value", propertyChangeListener);
        for(Map.Entry<ActionListener, AbstractButton> entry : listeners.entrySet())
            entry.getValue().removeActionListener(entry.getKey());
        Object selectedValue = optionPane.getValue();
        if(selectedValue == null)
            return JOptionPane.CLOSED_OPTION;

        //If there is not an array of option buttons:
        if(options == null) {
            if(selectedValue instanceof Integer)
                return ((Integer)selectedValue);
            else
                return JOptionPane.CLOSED_OPTION;
        }
        //If there is an array of option buttons:
        for(int counter = 0, maxCounter = options.length; counter < maxCounter; counter++) {
            if(options[counter].equals(selectedValue))
                return counter;
        }
        return JOptionPane.CLOSED_OPTION;
    }

}
