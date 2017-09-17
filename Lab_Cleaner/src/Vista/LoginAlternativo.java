package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import TablasBD.HibernateUtil;
import TablasBD.UsuariosLogin;
import Utils.Utils;
import java.awt.Toolkit;

public class LoginAlternativo {

	private JFrame frame;
	private JTextField usuario;
	private JPasswordField contrasena;
	
	/**
	 * Create the application.
	 */
	public LoginAlternativo() {
		initialize();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setUndecorated(true);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(LoginAlternativo.class.getResource("/Recursos/Icono Lab-Cleaner.png")));
		frame.setResizable(false);
		frame.setBackground(new Color(255, 255, 255));
		frame.getContentPane().setBackground(new Color(255, 255, 255));
		frame.setSize(467, 458);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setHgap(40);
		flowLayout.setVgap(40);
		panel.setBackground(new Color(255, 255, 255));
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(LoginAlternativo.class.getResource("/Recursos/VentanaPrincipal.jpg")));
		panel.add(lblNewLabel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(255, 255, 255));
		frame.getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Usuario:");
		lblNewLabel_1.setBounds(124, 22, 78, 22);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel_1.add(lblNewLabel_1);
		
		usuario = new JTextField();
		usuario.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				usuario.setBackground(new Color(255,255,255));
			}
		});
		usuario.setBounds(212, 21, 154, 31);
		usuario.setPreferredSize(new Dimension(50, 20));
		panel_1.add(usuario);
		usuario.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Contrase\u00F1a:");
		lblNewLabel_2.setBounds(97, 71, 105, 22);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panel_1.add(lblNewLabel_2);
		
		JButton btnNewButton = new JButton("");
		btnNewButton.setIcon(new ImageIcon(LoginAlternativo.class.getResource("/Recursos/cancelar.png")));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		btnNewButton.setBounds(290, 136, 105, 41);
		panel_1.add(btnNewButton);
		
		contrasena = new JPasswordField();
		contrasena.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
				if (e.getKeyCode()==KeyEvent.VK_ENTER){
					
					comprobarLogin();
					
				}
				
			}
		});
		contrasena.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				contrasena.setBackground(new Color(255,255,255));
			}
		});
		contrasena.setBounds(212, 70, 154, 31);
		panel_1.add(contrasena);
		
		JButton btnNewButton_1 = new JButton("");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				comprobarLogin();
				
			}
		});
		btnNewButton_1.setIcon(new ImageIcon(LoginAlternativo.class.getResource("/Recursos/aceptar_16.png")));
		btnNewButton_1.setBounds(84, 136, 105, 41);
		panel_1.add(btnNewButton_1);
		
		JLabel lblNewLabel_3 = new JLabel("");
		lblNewLabel_3.setIcon(new ImageIcon(LoginAlternativo.class.getResource("/Recursos/candado.png")));
		lblNewLabel_3.setBounds(376, 71, 32, 29);
		panel_1.add(lblNewLabel_3);
	}
	
	public void comprobarLogin() {
		
		SessionFactory sesion=HibernateUtil.getSessionFactory();
		Session session=sesion.openSession();
		Transaction tx=session.beginTransaction();
		
		String user = usuario.getText();
		String pass = Utils.encriptacionMD5(String.valueOf(contrasena.getPassword())); 
		
		try {
												
			UsuariosLogin usuarioLogin = (UsuariosLogin)session.get(UsuariosLogin.class, user);
			
			if(usuarioLogin.getPass().equals(pass)) {
				
				new VentanaPrincipal(usuarioLogin.getAcceso(), usuarioLogin);
				frame.dispose();
				
			} else {
				
				contrasena.setBackground(new Color(235, 117,117));
		
			}
									
			tx.commit();
		
		} catch(NullPointerException e) {
																		
			usuario.setBackground(new Color(235, 117,117));
			tx.commit();
													
		}
		
	}
}
