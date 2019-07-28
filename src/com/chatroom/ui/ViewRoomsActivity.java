
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

public class ViewRoomsActivity {
	private JLabel jLabel;
	private JFrame jFrame;
	private JButton jBtnJoinRoom;
	private JComboBox<String> jComboBox;
	private Image iconLogo;

	public ViewRoomsActivity() throws IOException {
		jFrame = new JFrame("CHATROOM VIEW ROOMS");
		
		BufferedImage myImage1 = ImageIO.read(this.getClass().getResource("/logo.png"));
		iconLogo = myImage1.getScaledInstance(150, 150, BufferedImage.SCALE_DEFAULT);
		
		jFrame.setContentPane(new JPanel() {
			BufferedImage myImage = ImageIO.read(this.getClass().getResource("/background.jpg"));
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(myImage, 0, 0, this);
			}
		});
		
		String rooms[] = {"N3","My Room","First Room","Test","Free Room","Default Room"};
		jBtnJoinRoom = new JButton("Join Room");
		jComboBox = new JComboBox<>(rooms);
		
		initializeAllWithProperties();

	}
	
	private void ListeningEvents() {
		jBtnJoinRoom.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO: add join room code
			}
		});
	}
	
	@SuppressWarnings("serial")
	private void initializeAllWithProperties() {
		
		//join room button properties
		jBtnJoinRoom.setPreferredSize(new Dimension(150,35));
		jBtnJoinRoom.setBackground(Color.WHITE);
		jBtnJoinRoom.setBorder(new LineBorder(Color.blue, 3));
		jBtnJoinRoom.setFocusPainted(false);
		
		//combo box properties
		jComboBox.setPreferredSize(new Dimension(200,35));
		jComboBox.setBackground(Color.white);
		jComboBox.setRenderer(new DefaultListCellRenderer() {
		    @Override
		    public void paint(Graphics g) {
		        setBackground(Color.WHITE);
		        setForeground(Color.BLUE);
		        super.paint(g);
		    }
		});
		
		jFrame.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		Insets buttonInsets = new Insets(20, 4, 4, 4);
		Insets comboBoxInsets = new Insets(4, 4, 4, 4);
		Insets logoInsets = new InsetsUIResource(0, 0, 40, 0);
		c.anchor = GridBagConstraints.CENTER;
		c.insets = logoInsets;
		c.gridy = 1;
		
		jLabel = new JLabel(new ImageIcon(iconLogo));
		jLabel.setBounds(0, 0, 100, 100);
		jLabel.setPreferredSize(new Dimension(150,150));
		jFrame.add(jLabel,c);
		
		c.gridx = 0;
		c.gridy = 2;
		c.insets = comboBoxInsets;
		jFrame.add(jComboBox,c);
		
		c.gridx = 0;
		c.gridy = 3;
		c.insets = buttonInsets;
		jFrame.add(jBtnJoinRoom,c);
		
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setSize(800,550);
		jFrame.setVisible(true);
		jFrame.getRootPane().setDefaultButton(jBtnJoinRoom);
		jBtnJoinRoom.requestFocus();
		
		ListeningEvents();
	}
	
	public static void main(String args[]) throws IOException {
		new ViewRoomsActivity();
	}	
}