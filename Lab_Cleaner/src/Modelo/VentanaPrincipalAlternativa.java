package Modelo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.SoftBevelBorder;

import TablasBD.UsuariosLogin;
import Vista.PanelVentas;

@SuppressWarnings("serial")
public class VentanaPrincipalAlternativa extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaPrincipalAlternativa frame = new VentanaPrincipalAlternativa();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VentanaPrincipalAlternativa() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1458, 954);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(null);
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.RAISED, new Color(135, 206, 250), new Color(135, 206, 250), new Color(135, 206, 250), new Color(135, 206, 250)));
		panel.setBackground(new Color(255, 250, 250));
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setVgap(20);
		flowLayout.setHgap(30);
		flowLayout.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel, BorderLayout.NORTH);
		
		Component horizontalGlue_1 = Box.createHorizontalGlue();
		horizontalGlue_1.setPreferredSize(new Dimension(30, 0));
		panel.add(horizontalGlue_1);
		
		JButton btnNewButton = new JButton("Ventas");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				UsuariosLogin usuariosLogin = new UsuariosLogin(); 
				
				PanelVentas panelVentas = new PanelVentas(0, usuariosLogin); 
				contentPane.add(panelVentas, BorderLayout.CENTER);
				
				
			}
		});
		btnNewButton.setIcon(null);
		btnNewButton.setBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(135, 206, 250), new Color(173, 216, 230)));
		btnNewButton.setPreferredSize(new Dimension(120, 60));
		panel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Salidas");
		btnNewButton_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(135, 206, 250), new Color(173, 216, 230)));
		btnNewButton_1.setPreferredSize(new Dimension(120, 60));
		panel.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Carrusel");
		btnNewButton_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(135, 206, 250), new Color(173, 216, 230)));
		btnNewButton_2.setPreferredSize(new Dimension(120, 60));
		panel.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("Clientes");
		btnNewButton_3.setBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(135, 206, 250), new Color(173, 216, 230)));
		btnNewButton_3.setPreferredSize(new Dimension(120, 60));
		panel.add(btnNewButton_3);
		
		JButton btnNewButton_4 = new JButton("Servicios");
		btnNewButton_4.setBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(135, 206, 250), new Color(173, 216, 230)));
		btnNewButton_4.setPreferredSize(new Dimension(120, 60));
		panel.add(btnNewButton_4);
		
		JButton btnNewButton_5 = new JButton("Configuracion");
		btnNewButton_5.setBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(135, 206, 250), new Color(173, 216, 230)));
		btnNewButton_5.setPreferredSize(new Dimension(120, 60));
		panel.add(btnNewButton_5);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		horizontalGlue.setPreferredSize(new Dimension(30, 0));
		panel.add(horizontalGlue);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(VentanaPrincipalAlternativa.class.getResource("/Recursos/VentanaPrincipal.jpg")));
		panel.add(lblNewLabel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, new Color(135, 206, 250), new Color(135, 206, 250), new Color(135, 206, 250), new Color(135, 206, 250)));
		panel_1.setBackground(new Color(255, 250, 250));
		FlowLayout flowLayout_1 = (FlowLayout) panel_1.getLayout();
		flowLayout_1.setVgap(20);
		flowLayout_1.setHgap(35);
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		contentPane.add(panel_1, BorderLayout.SOUTH);
		
		Component horizontalGlue_2 = Box.createHorizontalGlue();
		horizontalGlue_2.setPreferredSize(new Dimension(20, 0));
		panel_1.add(horizontalGlue_2);
		
		JLabel lblNewLabel_1 = new JLabel("New label");
		panel_1.add(lblNewLabel_1);
		
		Component horizontalGlue_3 = Box.createHorizontalGlue();
		horizontalGlue_3.setPreferredSize(new Dimension(425, 0));
		panel_1.add(horizontalGlue_3);
		
		JLabel lblNewLabel_2 = new JLabel("Lab Cleaner - Sistema de Lavanderia");
		panel_1.add(lblNewLabel_2);
		
		Component horizontalGlue_4 = Box.createHorizontalGlue();
		horizontalGlue_4.setPreferredSize(new Dimension(275, 0));
		panel_1.add(horizontalGlue_4);
		
		JButton btnNewButton_6 = new JButton("Cambiar Usuario");
		panel_1.add(btnNewButton_6);
		
		JButton btnNewButton_7 = new JButton("Salir");
		panel_1.add(btnNewButton_7);
		
		
	}

}
