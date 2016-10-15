package components;

import javax.swing.*;
import java.awt.*;

public class ImageButton extends JButton{
    private Image image;
    private boolean uniform;
    private boolean fill;

    public ImageButton(Image image, boolean uniform, Image assureImages, boolean fill) {
        this.image = image;
        this.uniform = uniform;
        this.fill = fill;
    }

    @Override
    protected void paintComponent(Graphics g) {
       // super.paintComponent(g);
       if(isOpaque()){
           g.setColor(getBackground());
           g.fillRect(0,0,getWidth(),getHeight());
       }
        Rectangle rect = GuiHelper.getAreaToFill(
                getSize(),
                new Dimension(image.getWidth(null),image.getHeight(null)),
                uniform,
                fill);
       if(isEnabled()) {
           g.drawImage(image, rect.x, rect.y, rect.width, rect.height, null);
       } else {
           //прорисовываем неактивную кнопку

       }
        String text = getText();
        FontMetrics fontMetrics = g.getFontMetrics();

    }

    @Override
    protected void paintBorder(Graphics g) {
        //super.paintBorder(g);
    }
}
