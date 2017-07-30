package Vista;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import Controlador.CajaInicial;
import TablasBD.Factura;
import TablasBD.HibernateUtil;

//Clase encargada de llevar la gestion de la ventana principal, hereda de JFrame
@SuppressWarnings("rawtypes")
public class VentanaPrincipal extends JFrame{
	
	//Declaramos todas las variables a utilizar para tener amplitud de campo
	private static final long serialVersionUID = 1L;
	private JPanel panel_norte, panel_sur, panel_oeste, panel_ventas, panel_salidas, panel_perchero, panel_reportes, panel_configuracion;
	public static JButton Ventas, salida_factura, gestion_perchero, Clientes, Servicios, Reportes, Configuracion, salir, cambiar;
	private ImageIcon img;
	private JLabel logo, fecha, autor;
	int acceso;
	private static JFrame miframe;
	
	//Constructor de la clase, recibe un entero por parametro que indica el tipo de acceso
	
	public VentanaPrincipal(int acceso){
		//establecemos datos de la ventana
		this.acceso=acceso;
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setUndecorated(true);
		this.setLayout(new BorderLayout());
		miframe=this;
		
		ImageIcon icono_ventas=new ImageIcon(getClass().getResource("/Recursos/icono_ventas.png"));
		ImageIcon icono_salidas=new ImageIcon(getClass().getResource("/Recursos/salidas.png"));
		ImageIcon icono_perchas=new ImageIcon(getClass().getResource("/Recursos/percha.png"));
		ImageIcon icono_clientes=new ImageIcon(getClass().getResource("/Recursos/clientes.png"));
		ImageIcon icono_servicios=new ImageIcon(getClass().getResource("/Recursos/servicios.png"));
		ImageIcon icono_reportes=new ImageIcon(getClass().getResource("/Recursos/reportes.png"));
		ImageIcon icono_configuracion=new ImageIcon(getClass().getResource("/Recursos/configuracion.png"));
		ImageIcon icono_cambiar_usuario=new ImageIcon(getClass().getResource("/Recursos/cambiar_usuario.png"));
		ImageIcon icono_salir=new ImageIcon(getClass().getResource("/Recursos/exit.png"));
						
		
		//Establecemos el Layout del panel norte y tambien los diferentes bordes que vamos a utilizar
		FlowLayout layoutNorte=new FlowLayout(FlowLayout.LEFT);
		layoutNorte.setHgap(40);
		BevelBorder borderRaised=new BevelBorder(BevelBorder.RAISED);
		BevelBorder borderLowered=new BevelBorder(BevelBorder.LOWERED);
		//Creamos el panel norte, establecemos el layout, color y borde
		panel_norte=new JPanel(layoutNorte);
		panel_norte.setBackground(Color.WHITE);
		panel_norte.setBorder(borderRaised);
		//Creamos el panel sur, reutilizamos el layour del norte y establecemos el borde
		panel_sur=new JPanel(layoutNorte);
		panel_sur.setBackground(Color.WHITE);
		panel_sur.setBorder(borderLowered);
		//Creamos el panel oeste
		panel_oeste=new JPanel();
		//Agregamos los botones con la funcion agregarBotones
		Ventas=agregarBotones("VENTAS", 140, 100, icono_ventas);
		salida_factura=agregarBotones("SALIDAS", 140,100, icono_salidas);
		gestion_perchero=agregarBotones("CARRUSEL", 140,100, icono_perchas);
		Clientes=agregarBotones("CLIENTES", 140, 100, icono_clientes);
		Servicios=agregarBotones("SERVICIOS", 140, 100, icono_servicios);
		Reportes=agregarBotones("REPORTES", 140, 100, icono_reportes);
		Configuracion=agregarBotones("CONFIGURACION", 140, 100, icono_configuracion);
		//Aqui si el acceso es 0, desactivamos los botones de reportes y configuracion
		if(this.acceso==0){
			
			Reportes.setEnabled(false);
			Configuracion.setEnabled(false);
			
		}
				
		//Establecemos la imagen de la parte superior derecha y el espacio entre los botones y la imagen
		img=new ImageIcon(getClass().getResource("/Recursos/VentanaPrincipal.jpg"));
		logo=new JLabel(img);
		//Para crear separacion horizontal entre los botones y el logo
		Component horizontalGlue = Box.createHorizontalGlue();
		horizontalGlue.setPreferredSize(new Dimension(150, 0));
		//Lo agregamos al panel norte
		panel_norte.add(horizontalGlue);
		panel_norte.add(logo);
		//Iniciamos el Objeto Relog, le damo tamaño y lo agregamos al panel sur
		fecha=new Reloj();
		fecha.setPreferredSize(new Dimension(650,40));
		panel_sur.add(fecha);
		//Label del nombre de la compañia, establecemos el tamaño y lo agregamos al panel sur
		autor=new JLabel("Lab-Cleaner, Sistema de Lavanderia - Version 1.0.1 - Creador: Jaime Gómez");
		autor.setPreferredSize(new Dimension(700, 40));
		panel_sur.add(autor);
		//Creamos un objeto de la clase Accion que implementa la interfaz ActionListener
		Accion botonact=new Accion();
		//Iniciamos el boton para cambiar el usuario, le pasamos el objeto de Accion, establecemos el tamaño y lo agregamos al panel sur
		cambiar=new JButton("Cambiar de Usuario", icono_cambiar_usuario);
		cambiar.addActionListener(botonact);
		cambiar.setPreferredSize(new Dimension(180,40));
		panel_sur.add(cambiar);
		
		//Igual que con cambiar usuario, iniciamos el boton salir y pasamos los datos al panel sur
		salir=new JButton("Salir", icono_salir);
		salir.addActionListener(botonact);
		salir.setPreferredSize(new Dimension(100,40));
		panel_sur.add(salir);
		
		//Agregamos a la ventana el panel norte y el sur, lo hacemos visible y establecemos que cuando cerremos la ventana cierre el programa
		this.add(panel_norte, BorderLayout.NORTH);
		this.add(panel_sur, BorderLayout.SOUTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		//Pasamos a los botones de gestion el objeto de Accion
		Ventas.addActionListener(botonact);
		Clientes.addActionListener(botonact);		
		Servicios.addActionListener(botonact);
		Reportes.addActionListener(botonact);
		Configuracion.addActionListener(botonact);
		salida_factura.addActionListener(botonact);
		gestion_perchero.addActionListener(botonact);
		
		//Aqui establecemos la primera pantalla inicial el panel de ventas
		panel_ventas=new PanelVentas(this.acceso);
		setPanelCentral(panel_ventas);
		//Abrimos session, vamos a comprobar si es la primera vez que se loguea para pedir los datos iniciales de caja
		SessionFactory sesion=HibernateUtil.getSessionFactory();
		Session session=sesion.openSession();
		Transaction tx=session.beginTransaction();
		//Obetener la fecha actual y la convertimos a sql
		Date fecha_util=new Date();
		java.sql.Date fecha_sql=new java.sql.Date(fecha_util.getTime());
		//Creamos la consulta
		Query query=session.createQuery("select p from " + Factura.class.getName() + " p where p.fecha='" + fecha_sql  + "'");
		
		java.util.List entrada_caja=query.list();
		//Si el tamaño de la lista es 0, significa que es la primera vez, entonces llamamos a la clase CajaInicial
		if(entrada_caja.size()==0){
			
			new CajaInicial();
						
		}
				
		tx.commit();
				
	}
	
	//Clase Accion que identifica el boton pulsado.
	private class Accion implements ActionListener{

		
		public void actionPerformed(ActionEvent e) {
			//Aqui si pulsamos el salir, cierra el programa
			if(e.getSource().equals(salir)){
				//Confirmamos que el usuaroio quiera salir del programa		
				int op=JOptionPane.showConfirmDialog(null, "Esta seguro que quiere salir", "Salir", JOptionPane.OK_OPTION);
				
				if(op==JOptionPane.OK_OPTION){
					
					System.exit(0);
				
				}
			
			//Si pulsamos el boton cambiar llamamos a la funcion reiniciarAplicacion que se encarga de reiniciar todo el programa
			} else if (e.getSource().equals(cambiar)){
				
				reiniciarAplicacion();	
				
			//Si pulsamos en clientes, removemos el panel anterior, establecemos uno nuevo en el panel oeste	
			} else if (e.getSource().equals(Clientes)){
				
				removePanel();
				panel_oeste=new PanelControles("Nuevo", "Modificar", "Eliminar", "Consultar", acceso, Clientes);
				setPanelControles(panel_oeste);
				
			//Si pulsamos en servicio, removemos el panel anterior, establecemos uno nuevo en el panel oeste	
			} else if (e.getSource().equals(Servicios)){
				
				removePanel();
				panel_oeste=new PanelControles("Nuevo", "Modificar", "Eliminar", "Consultar", acceso, Servicios);
				setPanelControles(panel_oeste);
			
			//Si pulsamos en Ventas, removemos el panel anterior y establecemos el panel de ventas	
			} else if (e.getSource().equals(Ventas)){
				
				removePanel();
				panel_ventas=new PanelVentas(acceso);
				setPanelCentral(panel_ventas);
				
			//Si pulsamos en salida_factura, removemos el panel anterior y establecemos el panel de salidas	
			} else if(e.getSource().equals(salida_factura)){
				
				removePanel();
				panel_salidas=new PanelSalidas(acceso);
				setPanelCentral(panel_salidas);
				
			//Si pulsamos en gestion_perchero, removemos el panel anterior y establecemos el panel del carrusel	
			} else if(e.getSource().equals(gestion_perchero)){
				
				removePanel();
				panel_perchero=new Carrusel(acceso);
				setPanelCentral(panel_perchero);
			//Si pulsamos en Reportes, removemos el panel anterior y establecemos el panel de reportes						
			} else if(e.getSource().equals(Reportes)){
				
				removePanel();
				panel_reportes=new Reportes();
				setPanelCentral(panel_reportes);
				
			//Si pulsamos en Configuracion, removemos el panel anterior y establecemos el panel de configuracion		
			} else if(e.getSource().equals(Configuracion)){
				
				removePanel();
				panel_configuracion=new PanelConfiguracion();
				setPanelCentral(panel_configuracion);
				
				
				
			}
		}
				
	}
	
	//Funcion que establece el panel de salidas de tickets
	private void setPanelCentral(JPanel panel){
		
		this.add(panel, BorderLayout.CENTER);
		
	}
	
	//Funcion que establece el panel oeste
	private void setPanelControles(JPanel panel){
		
		this.add(panel, BorderLayout.WEST);
		
	}
	
	//Funcion para reiniciar la aplicacion, llama nuevamente al login y cierra la ventana principal
	private void reiniciarAplicacion(){
		
		new Login();
		this.dispose();
		
		
	}
	//Funcion para remover el panel que esta activo
	private void removePanel(){
		
		if(panel_oeste!=null){
			
			this.remove(panel_oeste);
			
		}
		
		if(PanelControles.panel_central_cliente!=null){
			
			this.remove(PanelControles.panel_central_cliente);
			
		}
		
		if(PanelControles.panel_central_servicio!=null){
			
			this.remove(PanelControles.panel_central_servicio);
			
		}
		
		if(panel_ventas!=null){
			
			this.remove(panel_ventas);
			
		}
		
		if(panel_salidas!=null){
			
			this.remove(panel_salidas);
		}
		
		if(panel_perchero!=null){
			
			this.remove(panel_perchero);
			
		}
		
		if(panel_reportes!=null){
			
			this.remove(panel_reportes);
			
		}
		
		if(panel_configuracion!=null){
			
			this.remove(panel_configuracion);
			
		}
			
	}
	//Funcion que devuelve la Ventana Principal
	public static JFrame getFrame(){
		
		return miframe;
	
	}
	//Funcion para agregar los botones de gestion de la parte norte
	private JButton agregarBotones(String texto, int ancho, int alto, ImageIcon icono){
		
		JButton boton=new JButton(texto, icono);
		boton.setPreferredSize(new Dimension(ancho, alto));
		boton.setVerticalTextPosition(SwingConstants.BOTTOM);
		boton.setHorizontalTextPosition(SwingConstants.CENTER);
		panel_norte.add(boton);
		return boton;
		
		
	}
	//Clase Reloj para mostra la hora actual en tiempo real a traves de un label, implementa la interfaz runnable por que se actualiza segundo a segundo
	private class Reloj extends JLabel implements Runnable { 

		
		private static final long serialVersionUID = 1L;
		private String dia, mes, año, hora, minutos, segundos; 
		private Calendar calendario = new GregorianCalendar(); 
		Thread hilo; 
		Font font = new Font("Arial", Font.BOLD, 22);


		public Reloj() { 
		hilo = new Thread(this); 
		hilo.start(); 
		setFont(font);
		

		} //fin constructor 

		@ Override 
		public void run() { 
		Thread ct = Thread.currentThread(); 
		
			while (ct == hilo) { 
			
				try { 
			
					actualiza(); 
			
					int mesE; 
			
					mesE = Integer.valueOf(mes) + 1; 
	
					setText(hora + ":" + minutos + ":" + segundos + "    " + dia + "/" + mesE + "/" + año); 
	
					Thread.sleep(1000); 
			
				} catch (InterruptedException ex) { 
			
					JOptionPane.showMessageDialog(null, "Error al mostrar la hora"); 
				
				} 
	
			} 

		} 


		public void actualiza() { 

			Date fechaHoraActual = new Date(); 
			calendario.setTime(fechaHoraActual); 
	
			hora = String.valueOf(calendario.get(Calendar.HOUR_OF_DAY)); 
			minutos = calendario.get(Calendar.MINUTE) > 9 ? "" + calendario.get(Calendar.MINUTE) : "0" + calendario.get(Calendar.MINUTE); 
			segundos = calendario.get(Calendar.SECOND) > 9 ? "" + calendario.get(Calendar.SECOND) : "0" + calendario.get(Calendar.SECOND); 
			dia = calendario.get(Calendar.DATE) > 9 ? "" + calendario.get(Calendar.DATE) : "0" + calendario.get(Calendar.DATE); 
			mes = calendario.get(Calendar.MONTH) > 9 ? "" + calendario.get(Calendar.MONTH) : "0" + calendario.get(Calendar.MONTH); 
			año = calendario.get(Calendar.YEAR) > 9 ? "" + calendario.get(Calendar.YEAR) : "0" + calendario.get(Calendar.YEAR); 
		} 
	
	} 
				

}
