package net.tophatfox.NeuronalesNetz;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class View extends JFrame{
	
	ImagePanel imgPnl = new ImagePanel();
	
	public JLabel digit = new JLabel("");
	
	public View() {
		this.setTitle("NeuroNetz");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		
		imgPnl.setPreferredSize(new Dimension(50, 50));
		this.add(imgPnl,BorderLayout.WEST);
		
		this.add(new JLabel("==>"),BorderLayout.CENTER);
		
		this.add(digit,BorderLayout.EAST);
		
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public void setImg(BufferedImage bufferedImage) {
		imgPnl.setImage(bufferedImage);
		this.validate();
		this.repaint();
		this.pack();
		this.setLocationRelativeTo(null);
	}

}
