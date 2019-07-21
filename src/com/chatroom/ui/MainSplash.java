package com.chatroom.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.InsetsUIResource;

public class MainSplash {
	private JLabel jLabel;
	private JFrame jFrame;
	private JButton jBtnSignUp;
	private JButton jBtnSignIn;

	public MainSplash() throws IOException {
		jFrame = new JFrame("CHATROOM");
		
		BufferedImage myImage1 = ImageIO.read(this.getClass().getResource("/logo.png"));
		Image iconLogo = myImage1.getScaledInstance(150, 150, BufferedImage.SCALE_DEFAULT);
		
		jFrame.setContentPane(new JPanel() {
			BufferedImage myImage = ImageIO.read(this.getClass().getResource("/background.jpg"));
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(myImage, 0, 0, this);
			}
		});
		
		jBtnSignUp = new JButton("Sign Up");
		jBtnSignUp.setPreferredSize(new Dimension(150,35));
		jBtnSignUp.setBackground(Color.WHITE);
		jBtnSignUp.setBorder(new LineBorder(Color.blue, 3));
		jBtnSignUp.setFocusPainted(false);
		
		jBtnSignIn = new JButton("Sign In ");
		jBtnSignIn.setPreferredSize(new Dimension(150,35));
		jBtnSignIn.setBorder(new LineBorder(Color.blue, 3));
		jBtnSignIn.setFocusPainted(false);
		
		jFrame.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		Insets buttonInsets = new Insets(4, 4, 4, 4);
		Insets logoInsets = new InsetsUIResource(0, 0, 100, 0);
		c.anchor = GridBagConstraints.CENTER;
		c.insets = logoInsets;
		c.gridy = 1;
		jLabel = new JLabel(new ImageIcon(iconLogo));
		jLabel.setBounds(0, 0, 100, 100);
		jLabel.setPreferredSize(new Dimension(150,150));
		jFrame.add(jLabel,c);
		c.gridx = 0;
		c.gridy = 2;
		c.insets = buttonInsets;
		jFrame.add(jBtnSignUp,c);
		c.gridx = 0;
		c.gridy = 3;
		jFrame.add(jBtnSignIn,c);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setSize(500,500);
		jFrame.setVisible(true);
	}
	
	public static void main(String args[]) throws IOException {
		new MainSplash();
	}	
}

