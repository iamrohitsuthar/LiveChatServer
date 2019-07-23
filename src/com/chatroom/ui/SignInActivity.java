package com.chatroom.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
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

public class SignInActivity {
	private JLabel jLabel;
	private JFrame jFrame;
	private JButton jBtnSignIn;
	private JTextField jTvUsername;
	private JPasswordField jTvpassword;
	private Image iconLogo;
	private CompoundBorder compoundBorder ;
	private JLabel jLabelSignup;
	private JLabel jLabelSignIntitle;
	
	
	public SignInActivity() throws IOException {
		jFrame = new JFrame("CHATROOM Sign In");
		
		//scaling logo
		BufferedImage myImage1 = ImageIO.read(this.getClass().getResource("/logo.png"));
		iconLogo = myImage1.getScaledInstance(150, 150, BufferedImage.SCALE_DEFAULT);
		
		//setting main background
		jFrame.setContentPane(new JPanel() {
			BufferedImage myImage = ImageIO.read(this.getClass().getResource("/background.jpg"));
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(myImage, 0, 0, this);
			}
		});
		
		//creating compound border for Text Field to specify left mergin to the text
		Border lineBorder = BorderFactory.createLineBorder(Color.blue, 1);
		Border emptyBorder = new EmptyBorder(0,10,0,0); //left marign for text
		compoundBorder = new CompoundBorder(lineBorder,emptyBorder);
		
		jBtnSignIn = new JButton("Sign In");
		jTvUsername = new JTextField("Enter Username");
		
		jTvpassword = new JPasswordField();
		jTvpassword.setText("Enter Password");
		//making text visible in password field
		jTvpassword.setEchoChar((char)0);
		
		jLabelSignIntitle = new JLabel("SIGN IN");
		jLabelSignup = new JLabel("Not a user? Sign Up here");
		
		initializeAllWithProperties();

	}
	
	private void ListeningEvents() {
		jTvUsername.addFocusListener(new FocusListener() {	
			@Override
			public void focusLost(FocusEvent e) {
				if(jTvUsername.getText().length() == 0) {
					jTvUsername.setForeground(Color.gray);
					jTvUsername.setText("Enter Username");
				}
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				jTvUsername.setText("");
				jTvUsername.setForeground(Color.black);	
			}
		});
		
		jTvpassword.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				if(jTvpassword.getText().length() == 0) {
					jTvpassword.setForeground(Color.gray);
					jTvpassword.setEchoChar((char)0);
					jTvpassword.setText("Enter Password");
				}
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				jTvpassword.setText("");
				jTvpassword.setEchoChar('\u2022');
				jTvpassword.setForeground(Color.gray);
			}
		});
		
		jLabelSignup.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					new SignUpActivity();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		
	}
	
	private void initializeAllWithProperties() {
		//Username text field
		jTvUsername.setPreferredSize(new Dimension(250,35));
		jTvUsername.setBackground(Color.WHITE);
		jTvUsername.setBorder(compoundBorder);
		jTvUsername.setForeground(Color.gray);
		
		//password text field
		jTvpassword.setPreferredSize(new Dimension(250,35));
		jTvpassword.setBackground(Color.WHITE);
		jTvpassword.setBorder(compoundBorder);
		jTvpassword.setForeground(Color.gray);
		
		
		//sign Up button
		jBtnSignIn.setPreferredSize(new Dimension(150,35));
		jBtnSignIn.setBackground(Color.WHITE);
		jBtnSignIn.setBorder(new LineBorder(Color.blue, 3));
		jBtnSignIn.setFocusPainted(false);
		jBtnSignIn.requestFocus();
		
		jFrame.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		Insets buttonInsets = new Insets(20, 4, 4, 4);
		Insets textFieldInsets = new Insets(4, 4, 4, 4);
		Insets textTitle = new Insets(4, 4, 20, 4);
		
		Insets logoInsets = new InsetsUIResource(0, 0, 50, 0);
		c.anchor = GridBagConstraints.CENTER;
		c.insets = logoInsets;
		c.gridy = 1;
		
		//setting logo
		jLabel = new JLabel(new ImageIcon(iconLogo));
		jLabel.setBounds(0, 0, 100, 100);
		jLabel.setPreferredSize(new Dimension(150,150));
		jFrame.add(jLabel,c);
		
		//setting sign in title
		c.gridx = 0;
		c.gridy = 2;
		c.insets = textTitle;		
		jFrame.add(jLabelSignIntitle,c);
		
		//setting username text field
		c.gridx = 0;
		c.gridy = 3;
		c.insets = textFieldInsets;
		jFrame.add(jTvUsername,c);
		
		//setting password textfield
		c.gridy = 4;
		jFrame.add(jTvpassword,c);
		
		//setting sign up button
		c.gridx = 0;
		c.gridy = 5;
		c.insets = buttonInsets;
		jFrame.add(jBtnSignIn,c);
		
		//setting sign up link
		c.gridy = 6;
		c.insets = textFieldInsets;
		jFrame.add(jLabelSignup,c);

		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setSize(500,500);
		
		jFrame.setVisible(true);
		
		//removing focus from text field and set it to the button
		jFrame.getRootPane().setDefaultButton(jBtnSignIn);
		jBtnSignIn.requestFocus();
		
		ListeningEvents();
	}
	
	public static void main(String args[]) throws IOException {
		new SignInActivity();
	}	
}

