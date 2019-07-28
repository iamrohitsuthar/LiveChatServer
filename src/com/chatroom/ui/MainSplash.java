package com.chatroom.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	private BufferedImage iconLogo;

	public MainSplash() throws IOException {
		jFrame = new JFrame("CHATROOM");
		
		iconLogo = ImageIO.read(this.getClass().getResource("/logo.png"));

		
		jFrame.setContentPane(new JPanel() {
			BufferedImage myImage = ImageIO.read(this.getClass().getResource("/background.png"));
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(myImage, 0, 0,this);
			}
		});
		
		jBtnSignUp = new JButton("SIGN UP");
		jBtnSignIn = new JButton("SIGN IN");
		
		initializeAllWithProperties();

	}
	
	private void ListeningEvents() {
		jBtnSignUp.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					jFrame.dispose();
					new SignUpActivity();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		
		jBtnSignIn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					jFrame.dispose();
					new SignInActivity();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}
	
	private void initializeAllWithProperties() {
		jBtnSignUp.setPreferredSize(new Dimension(150,35));
		jBtnSignUp.setBackground(new Color(108, 99, 255));
		//jBtnSignUp.setBorder(new LineBorder(Color.blue, 3));
		jBtnSignUp.setForeground(Color.white);
		jBtnSignUp.setFocusPainted(false);
		
		
		jBtnSignIn.setPreferredSize(new Dimension(150,35));
		jBtnSignIn.setBackground(Color.white);
		jBtnSignIn.setBorder(new LineBorder(new Color(108, 99, 255), 3));
		jBtnSignIn.setForeground(new Color(108, 99, 255));
		jBtnSignIn.setFocusPainted(false);
		
		jFrame.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();	
		Insets buttonInsets = new Insets(4, 200, 20, 4);
		Insets logoInsets = new InsetsUIResource(0, 200, 50, 0);
		
		c.anchor = GridBagConstraints.NORTHWEST;
		c.weightx = 1.0;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = logoInsets;
		c.gridy = 1;
		
		jLabel = new JLabel(new ImageIcon(iconLogo));
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
		jFrame.setSize(864,614);
		jFrame.setLocationRelativeTo(null);
		jFrame.setVisible(true);
		jFrame.setResizable(false);
		
		ListeningEvents();
	}
	
	public static void main(String args[]) throws IOException {
		new MainSplash();
	}	
}

