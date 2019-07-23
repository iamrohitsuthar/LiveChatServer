package com.chatroom.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.OptionPaneUI;

public class MainMenuOptions {
	private JLabel jLabel;
	private JLabel jLabelTitle;
	private JFrame jFrame;
	private JButton jBtnCreateRoom;
	private JButton jBtnJoinRoom;
	private JButton jBtnViewRooms;
	private JButton jBtnLogout;
	private Image iconLogo;

	public MainMenuOptions() throws IOException {
		jFrame = new JFrame("CHATROOM");
		
		BufferedImage myImage1 = ImageIO.read(this.getClass().getResource("/logo.png"));
		iconLogo = myImage1.getScaledInstance(150, 150, BufferedImage.SCALE_DEFAULT);
		
		jFrame.setContentPane(new JPanel() {
			BufferedImage myImage = ImageIO.read(this.getClass().getResource("/background.jpg"));
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(myImage, 0, 0, this);
			}
		});
		
		jBtnCreateRoom = new JButton("Create Room");
		jBtnJoinRoom = new JButton("Join Room");
		jBtnViewRooms = new JButton("View Rooms");
		jBtnLogout = new JButton("Logout");
		
		jLabelTitle = new JLabel("WELCOME TO CHATROOM - A LIVE CHAT SOFTWARE");
		
		initializeAllWithProperties();

	}
	
	private void ListeningEvents() {
		jBtnCreateRoom.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				displayAlertDialog(1);
			}
		});
		
		jBtnJoinRoom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO: add join room code
				displayAlertDialog(2);
			}
		});
		
		jBtnLogout.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					new SignInActivity();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
	}
	
	private void initializeAllWithProperties() {
		//create room button
		jBtnCreateRoom.setPreferredSize(new Dimension(150,35));
		jBtnCreateRoom.setBackground(Color.WHITE);
		jBtnCreateRoom.setBorder(new LineBorder(Color.blue, 3));
		jBtnCreateRoom.setFocusPainted(false);
		
		//join room button
		jBtnJoinRoom.setPreferredSize(new Dimension(150,35));
		jBtnJoinRoom.setBackground(Color.WHITE);
		jBtnJoinRoom.setBorder(new LineBorder(Color.blue, 3));
		jBtnJoinRoom.setFocusPainted(false);
		
		//view all rooms button
		jBtnViewRooms.setPreferredSize(new Dimension(150,35));
		jBtnViewRooms.setBackground(Color.WHITE);
		jBtnViewRooms.setBorder(new LineBorder(Color.blue, 3));
		jBtnViewRooms.setFocusPainted(false);
		
		//logout button
		jBtnLogout.setPreferredSize(new Dimension(150,35));
		jBtnLogout.setBackground(Color.WHITE);
		jBtnLogout.setBorder(new LineBorder(Color.blue, 3));
		jBtnLogout.setFocusPainted(false);
		
		jFrame.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		Insets buttonInsets = new Insets(4, 4, 4, 4);
		Insets logoInsets = new InsetsUIResource(0, 0, 50, 0);
		Insets textTitle = new Insets(4, 4, 20, 4);
		
		c.anchor = GridBagConstraints.CENTER;
		c.insets = logoInsets;
		c.gridy = 1;
		//setting logo
		jLabel = new JLabel(new ImageIcon(iconLogo));
		jLabel.setBounds(0, 0, 100, 100);
		jLabel.setPreferredSize(new Dimension(150,150));
		jFrame.add(jLabel,c);
		
		//setting title
		c.gridx = 0;
		c.gridy = 2;
		c.insets = textTitle;
		jFrame.add(jLabelTitle,c);
		
		//setting create room button
		c.gridx = 0;
		c.gridy = 3;
		c.insets = buttonInsets;
		jFrame.add(jBtnCreateRoom,c);
		
		//setting join room button
		c.gridx = 0;
		c.gridy = 4;
		jFrame.add(jBtnJoinRoom,c);
		
		//setting view rooms button
		c.gridx = 0;
		c.gridy = 5;
		jFrame.add(jBtnViewRooms,c);
		
		//setting logout button
		c.gridx = 0;
		c.gridy = 6;
		jFrame.add(jBtnLogout,c);
		
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setSize(500,500);
		jFrame.setVisible(true);
		
		ListeningEvents();
	}
	
	private void displayAlertDialog(int which) {
		JPanel jPanel = new JPanel();
		jPanel.setSize(new Dimension(200, 64));
		jPanel.setLayout(null);
		
		JTextField jTextField = new JTextField();
		jTextField.setBackground(Color.white);
		jTextField.setBounds(0, 0, 250, 35);
		
		jTextField.setHorizontalAlignment(SwingConstants.CENTER);
		jPanel.add(jTextField);
		
		if(which == 1)
			UIManager.put("OptionPane.okButtonText", "Create Room");
		else
			UIManager.put("OptionPane.okButtonText", "Join Room");
		
		JOptionPane.showMessageDialog(null, jPanel, "Enter Room name",JOptionPane.PLAIN_MESSAGE);
	}
	
	public static void main(String args[]) throws IOException {
		new MainMenuOptions();
	}	
}

