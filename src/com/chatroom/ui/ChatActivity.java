package com.chatroom.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.InsetsUIResource;

public class ChatActivity {
	private JLabel jLabel;
	private JFrame jFrame;
	private JButton jBtnSend;
	private JTextField jTfMessageHere;
	private CompoundBorder compoundBorder;
	private CompoundBorder compoundBorderMessage;
	private JPanel jPanel;
	private JPanel jPanelChatWindow;
	private int i = 0;
	
	@SuppressWarnings("serial")
	public ChatActivity() throws IOException {
		jFrame = new JFrame("CHATROOM Chats");
		jPanel = new JPanel();
		jPanelChatWindow = new JPanel();
		
		
		//setting main background
		jFrame.setContentPane(new JPanel() {
			BufferedImage myImage = ImageIO.read(this.getClass().getResource("/background.png"));
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(myImage, 0, 0, this);
			}
		});
		
		//creating compound border for Text Field to specify left margin to the text
		Border lineBorder = BorderFactory.createLineBorder(Color.blue, 1);
		Border emptyBorder = new EmptyBorder(0,10,0,0); //left margin for text
		compoundBorder = new CompoundBorder(lineBorder,emptyBorder);
		
		Border lineBorder1 = BorderFactory.createLineBorder(Color.WHITE, 3);
		Border emptyBorder1 = new EmptyBorder(0,10,0,10);
		compoundBorderMessage = new CompoundBorder(lineBorder1,emptyBorder1);
		
		jTfMessageHere = new JTextField("Type your message here");
		jBtnSend = new JButton("Send");
		
		initializeAllWithProperties();

	}
	
	private void ListeningEvents() {
		jTfMessageHere.addFocusListener(new FocusListener() {	
			@Override
			public void focusLost(FocusEvent e) {
				if(jTfMessageHere.getText().length() == 0) {
					jTfMessageHere.setForeground(Color.gray);
					jTfMessageHere.setText("Enter Username");
				}
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				jTfMessageHere.setText("");
				jTfMessageHere.setForeground(Color.black);	
			}
		});
		
		jBtnSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JLabel label = new JLabel("Hi");
				label.setFont(new Font("Serif",Font.BOLD,20));
				label.setBorder(compoundBorderMessage);
				
				GridBagConstraints gbc = new GridBagConstraints(0, i, 1, 1, 1.0, 0,
			            GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(
			                    0, 0, 5, 0), 0, 0);
				jPanelChatWindow.add(label,gbc);
				jPanelChatWindow.revalidate();
				jPanelChatWindow.repaint();
				i++;
			}
		});
		
	}
	
	private void initializeAllWithProperties() {
		//Username text field
		jTfMessageHere.setPreferredSize(new Dimension(550,35));
		jTfMessageHere.setBackground(Color.WHITE);
		jTfMessageHere.setBorder(compoundBorder);
		jTfMessageHere.setForeground(Color.gray);
		
		//sign Up button
		jBtnSend.setPreferredSize(new Dimension(50,35));
		jBtnSend.setBackground(Color.WHITE);
		jBtnSend.setBorder(new LineBorder(Color.blue, 3));
		jBtnSend.setFocusPainted(false);
		jBtnSend.requestFocus();
		
		//adding text field and send button at the bottom
		jPanel.setLayout(new GridLayout(1,2));
		jPanel.add(jTfMessageHere);
		jPanel.add(jBtnSend);
		jPanel.setOpaque(false);
		
		jPanelChatWindow.setOpaque(false);
		jPanelChatWindow.setLayout(new GridBagLayout());
		
		jFrame.setLayout(new BorderLayout());
		jFrame.add(BorderLayout.PAGE_END,jPanel);
		jFrame.add(BorderLayout.CENTER,jPanelChatWindow);
		//jFrame.add(BorderLayout.LINE_END,jBtnSend);
		
		
//		jFrame.setLayout(new GridBagLayout());
//		
//		GridBagConstraints c = new GridBagConstraints();
//		Insets buttonInsets = new Insets(20, 4, 4, 4);
//		Insets textFieldInsets = new Insets(4, 4, 4, 4);
//		Insets textTitle = new Insets(4, 4, 20, 4);
//		
//		Insets logoInsets = new InsetsUIResource(0, 0, 50, 0);
//		c.anchor = GridBagConstraints.CENTER;
//
//		//setting username text field
//		c.gridx = 0;
//		c.gridy = 1;
//		c.insets = textFieldInsets;
//		jFrame.add(jTfMessageHere,c);
//		
//		//setting sign up button
//		c.gridx = 0;
//		c.gridy = 2;
//		c.insets = buttonInsets;
//		jFrame.add(jBtnSend,c);
//		
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setSize(800,550);
		
		jFrame.setVisible(true);
		
		//removing focus from editext and set it to the button
		jFrame.getRootPane().setDefaultButton(jBtnSend);
		jBtnSend.requestFocus();
		
		ListeningEvents();
	}
	
	public static void main(String args[]) throws IOException {
		new ChatActivity();
	}	
}

