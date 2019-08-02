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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.InsetsUIResource;

import com.chatroom.client.ClientModel;
import com.chatroom.configuration.Config;
import com.chatroom.models.Request;
import com.chatroom.models.Response;
import com.chatroom.others.Hash;
import com.chatroom.others.LogFileWriter;
import com.chatroom.others.Message;

public class SignUpActivity {
	private JLabel jLabel;
	private JLabel jLabelsignin;
	private JLabel jLabelsignUptitle;
	private JFrame jFrame;
	private JButton jBtnSignUp;
	private JTextField jTvUsername;
	private JPasswordField jTvpassword;
	private CompoundBorder compoundBorder;
	private CompoundBorder compoundBorderAfterClick;
	private BufferedImage iconLogo;
	private ClientModel clientModel;
	
	@SuppressWarnings("serial")
	public SignUpActivity(ClientModel cm) throws IOException {
		clientModel = cm;
		jFrame = new JFrame("CHATROOM Sign Up");
		
		//scaling logo
		iconLogo = ImageIO.read(this.getClass().getResource("/logo.png"));
		
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
		
		Border lineBorder1 = BorderFactory.createLineBorder(Config.colorPrimary, 3);
		Border emptyBorder1 = new EmptyBorder(0,10,0,0); //left margin for text
		compoundBorderAfterClick = new CompoundBorder(lineBorder1,emptyBorder1);
		
		jBtnSignUp = new JButton("Sign Up");
		jTvUsername = new JTextField("Enter Username");
		
		jTvpassword = new JPasswordField();
		jTvpassword.setText("Enter Password");
		//making text visible in password field
		jTvpassword.setEchoChar((char)0);
		
		jLabelsignUptitle = new JLabel("SIGN UP");
		jLabelsignin = new JLabel("Already a user? Sign In here");
		
		initializeAllWithProperties();

	}
	
	private void ListeningEvents() {
		jTvUsername.addFocusListener(new FocusListener() {	
			@Override
			public void focusLost(FocusEvent e) {
				if(jTvUsername.getText().length() == 0) {
					jTvUsername.setForeground(Color.gray);
					jTvUsername.setText("Enter Username");
					jTvUsername.setBorder(compoundBorder);
				}
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				if(jTvUsername.getText().equals("Enter Username"))
					jTvUsername.setText("");
				jTvUsername.setForeground(Color.black);	
				jTvUsername.setBorder(compoundBorderAfterClick);
			}
		});
		
		jTvpassword.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				if(jTvpassword.getText().length() == 0) {
					jTvpassword.setForeground(Color.gray);
					jTvpassword.setEchoChar((char)0);
					jTvpassword.setText("Enter Password");
					jTvpassword.setBorder(compoundBorder);
				}
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				if(jTvpassword.getText().equals("Enter Password"))
					jTvpassword.setText("");
				jTvpassword.setEchoChar('\u2022');
				jTvpassword.setForeground(Color.gray);
				jTvpassword.setBorder(compoundBorderAfterClick);
			}
		});
		
		jBtnSignUp.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = jTvUsername.getText();
				String password = jTvpassword.getText();		
				signUp(username,password);
			}
		});		
		
		
		jLabelsignin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				try {
					jFrame.dispose();
					new SignInActivity(clientModel);
				} catch (IOException e1) {
					e1.printStackTrace(new PrintWriter(Config.errors));
					LogFileWriter.Log(Config.errors.toString());
				}
			}
		});
	}
	
	private void signUp(String username, String password) {
		if(username.equals("Enter Username") || username.length() <= 0) {
			JOptionPane.showMessageDialog(null, "Enter username properly", null, JOptionPane.ERROR_MESSAGE);
		}
		else if(password.equals("Enter Password") || password.length() <= 0) {
			JOptionPane.showMessageDialog(null, "Enter password properly", null, JOptionPane.ERROR_MESSAGE);
		}
		else {
			String cont = username;
			cont += "#";
			cont += Hash.getHash(new String(password));
			Request request = new Request(Request.Type.SIGN_UP.ordinal(),clientModel.getClientID(),clientModel.getRoomId(),cont);
			try {
				ClientModel.objectOutputStream.writeObject(request);
				ClientModel.objectOutputStream.flush();
				Response response = (Response) ClientModel.objectInputStream.readObject();
				
				if( response.getId() == Response.Type.SIGN_UP.ordinal())
				{
					if(response.getSuccess()) {
						clientModel.setClientID(Integer.parseInt(response.getContents()));
						new MainMenuOptions(clientModel);
						jFrame.dispose();
					}
					else {
						JOptionPane.showMessageDialog(null, response.getContents(), null, JOptionPane.ERROR_MESSAGE);
					}
				}
				
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace(new PrintWriter(Config.errors));
				LogFileWriter.Log(Config.errors.toString());
			}
		}
	}
	
	private void initializeAllWithProperties() {
		//User name text field
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
		jBtnSignUp.setPreferredSize(new Dimension(150,35));
		jBtnSignUp.setBackground(Color.WHITE);
		jBtnSignUp.setBorder(new LineBorder(Color.blue, 3));
		jBtnSignUp.setFocusPainted(false);
		jBtnSignUp.requestFocus();
		
		jFrame.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		Insets buttonInsets = new Insets(4, 200, 20, 4);
		Insets textFieldInsets = new Insets(4, 150, 10, 4);
		Insets temp = new Insets(4, 185, 4, 4);
		Insets textTitle = new Insets(4, 250, 20, 4);
		Insets logoInsets = new InsetsUIResource(0, 200, 50, 0);

		c.anchor = GridBagConstraints.NORTHWEST;
		c.weightx = 1.0;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = logoInsets;
		c.gridy = 1;
		
		//setting logo
		jLabel = new JLabel(new ImageIcon(iconLogo));
		jLabel.setBounds(0, 0, 100, 100);
		jLabel.setPreferredSize(new Dimension(150,150));
		jFrame.add(jLabel,c);
		
		//setting sign up title
		c.gridx = 0;
		c.gridy = 2;
		c.insets = textTitle;
		jFrame.add(jLabelsignUptitle,c);
		
		//setting user name text field
		c.gridx = 0;
		c.gridy = 3;
		c.insets = textFieldInsets;
		jFrame.add(jTvUsername,c);
		
		//setting password text field
		c.gridy = 4;
		jFrame.add(jTvpassword,c);
		
		//setting sign up button
		c.gridx = 0;
		c.gridy = 5;
		c.insets = buttonInsets;
		jFrame.add(jBtnSignUp,c);
		
		//setting sign in link
		c.gridy = 6;
		c.insets = temp;
		jFrame.add(jLabelsignin,c);
		

		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setSize(864,614);
		jFrame.setResizable(false);
		jFrame.setLocationRelativeTo(null);
		jFrame.setVisible(true);
		
		//removing focus from edit text and set it to the button
		jFrame.getRootPane().setDefaultButton(jBtnSignUp);
		jBtnSignUp.requestFocus();
		
		ListeningEvents();
	}	
}
