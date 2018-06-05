package net.tophatfox.NeuronalesNetz;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ImagePanel extends JPanel{

    private BufferedImage image = null;
    
    public void setImage(BufferedImage bufferedImage) {
    	image = bufferedImage;
    	this.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(image != null) {
        	g.drawImage(image, 0, 0, this); // see javadoc for more info on the parameters            
        }
    }

}