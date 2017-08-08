package Controlador;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Date;

import javax.swing.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import TablasBD.Clientes;
import TablasBD.Factura;
import TablasBD.HibernateUtil;
import Vista.VentanaPrincipal;

//Clase para introducir la cantidad inicial de la caja, cuando la aplicacion de abre por primera vez en el dia
@SuppressWarnings("serial")
public class CajaInicial extends JDialog {
	//Declaramos las variables a utilizar
	private JLabel titulo;
	private JTextField campo_numero;
	private JButton aceptar;
	
	//Constructor de la clase
	public CajaInicial(){
		//Configuramos la pantalla
		super(VentanaPrincipal.getFrame(), "CAJA", true);
		this.setSize(400, 200);
		this.setLocationRelativeTo(VentanaPrincipal.getFrame());
		this.setResizable(false);
		this.setLayout(null);
		//Llamamos a la funcion init que establece los componentes
		init();
		
		this.setVisible(true);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		
		
	}
	
	private void init(){
		//Etiqueta para el titulo del campo de texto
		Font font=new Font("Arial", Font.BOLD, 20);
		titulo=new JLabel("EFECTIVO EN CAJA:");
		titulo.setBounds(15, 27, 200, 35);
		titulo.setFont(font);
		//Campo de texto para introducir la cantidad
		font=new Font("Arial", Font.BOLD, 26);
		campo_numero=new JTextField();
		campo_numero.setBounds(225, 20, 150, 50);
		campo_numero.setFont(font);
		campo_numero.setHorizontalAlignment(SwingConstants.RIGHT);
		campo_numero.setForeground(new Color(6,120,16));
		
		ImageIcon icono_aceptar=new ImageIcon("src/Recursos/aceptar.png");
		
		//Boton para aceptar la operacion
		font=new Font("Arial", Font.PLAIN, 18);
		aceptar=new JButton("Aceptar", icono_aceptar);
		aceptar.setBounds(145, 95, 140, 60);
		aceptar.setFont(font);
		//Le agregamos varios eventos al campo de texto
		campo_numero.addKeyListener(new KeyAdapter(){
			//Aqui es solo para que acepte numeros y el punto
			public void keyTyped(KeyEvent e){
				
				char caracter=e.getKeyChar();
				
				if((caracter<'0' || caracter>'9') && caracter!=KeyEvent.VK_PERIOD){
					
					e.consume();
				
				}
							
			}
			//Aqui para al presionar enter ejecute la accion
			public void keyPressed(KeyEvent e){
				
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					
					setCajaInicial();
										
				}
								
			}
		
		});
		//Agregamos el evento al boton
		aceptar.addActionListener(new ActionListener(){

			
			public void actionPerformed(ActionEvent arg0) {
				
				setCajaInicial();
				
			}
						
		});
				
		this.add(titulo);
		this.add(campo_numero);
		this.add(aceptar);
				
	}
	//Funcion para introducir los datos a la base de datos
	private void setCajaInicial(){
		//Comprobamos tenga caracteres el campo de texto
		if(campo_numero.getText().length()==0){
			
			JOptionPane.showMessageDialog(this, "Introduzca la cantidad");
			
			
		} else {
			
			try{
				//Convertimos el campo de texto a float
				float total=Float.parseFloat(campo_numero.getText());
				//Abrimos sesion	
				SessionFactory sesion=HibernateUtil.getSessionFactory();
				Session session=sesion.openSession();
				Transaction tx=session.beginTransaction();
				//Declaramos la fecha actual
				Date fecha=new Date();
				//Cargamos el cliente
				Clientes cliente=(Clientes)session.get(Clientes.class, "Caja");
				//Introducimos los datos de la operacion
				Factura factura=new Factura(cliente,fecha,fecha,fecha,0,0,total,true);
				//Guardamos cambios
				session.save(factura);
				
				tx.commit();
				//Cerramos ventana actual en caso que todo vaya bien
				this.dispose();
		
			//Aqui capturamos si los datos introducidos son correctos
			}catch(NumberFormatException e){
				
				JOptionPane.showMessageDialog(this, "Introduzca una cantidad correcta");
				campo_numero.setText("");
				
			}catch(Exception e){
				
				e.printStackTrace();
			
			}
		
		}
				
	}
	
}
