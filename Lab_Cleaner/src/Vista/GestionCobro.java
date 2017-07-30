package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import Controlador.GenerarTicket;
import TablasBD.Clientes;
import TablasBD.Factura;
import TablasBD.HibernateUtil;
import TablasBD.Perchero;


//Clase que se encarga del cobro de la factura
@SuppressWarnings({"serial", "rawtypes", "unchecked"})
public class GestionCobro extends JDialog {
	//Declaramos las variables y objetos a utilizar
	private String dni;
	private int prendas, perchas, id_factura;
	private float total;
	private ArrayList<String> perchas_asignadas;
	private JLabel t_cobrar, cobrar, efectivo, devuelta, t_perchas, espacio;
	private JTextField efectivo_pagado, t_devuelta;
	private JCheckBox pagar_ahora;
	private JComboBox carrusel;
	private JButton aceptar, aceptar_2;
	private boolean resultado;
	private Factura factura;
	//Constructor de la clase, recibe por parametro el dni del cliente, la cantidad total de prendas, cantidad total de perchas y los percheros seleccinados
	public GestionCobro(String dni, int cantidad_prendas, int cantidad_perchas, float total_factura, ArrayList<String> perchas_asignadas){
		//Llamamos al constructor padre 
		super(VentanaPrincipal.getFrame(), "Cobrar", true);
		//Establecemos los valores de las diferentes variables
		this.dni=dni;
		prendas=cantidad_prendas;
		perchas=cantidad_perchas;
		total=total_factura;
		this.perchas_asignadas=perchas_asignadas;
		this.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
		this.setSize(450, 400);
		this.setLocationRelativeTo(VentanaPrincipal.getFrame());
		this.setResizable(false);
		resultado=false;
		//LLamamos a la funcion init, que se encarga de toda la gestion
		init();
		//Hacemos la ventana visible
		this.setVisible(true);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				
	}
	
	public void init(){
		//Declaramos la fuente y definimos como mostrar los float
		Font fuente_cobrar=new Font("Serif", Font.PLAIN, 20);
		
		DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
	    simbolo.setDecimalSeparator('.');
	    simbolo.setGroupingSeparator(',');
		DecimalFormat decimal=new DecimalFormat("###,###.##", simbolo);
		//Etiqueta que muestra el titulo del total a cobrar		
		t_cobrar=new JLabel("Total a cobrar:");
		t_cobrar.setPreferredSize(new Dimension(150,40));
		t_cobrar.setHorizontalTextPosition(SwingConstants.CENTER);
		t_cobrar.setFont(fuente_cobrar);
		//Etiqueta que muestra el total a cobrar
		fuente_cobrar=new Font("Serif", Font.BOLD, 50);
		cobrar=new JLabel("$ " + decimal.format(total));
		cobrar.setForeground(Color.BLUE);
		cobrar.setPreferredSize(new Dimension(250,60));
		cobrar.setHorizontalTextPosition(SwingConstants.CENTER);
		cobrar.setFont(fuente_cobrar);
		//Iniciamos el check box que indica si la factura se paga ahora o despues
		fuente_cobrar=new Font("Serif", Font.PLAIN, 20);
		pagar_ahora=new JCheckBox("Cobrar ahora");
		pagar_ahora.setFont(fuente_cobrar);
		pagar_ahora.setPreferredSize(new Dimension(400,40));
		pagar_ahora.addActionListener(new ActionListener(){
			//Si se activa, se activan el campo de efectivo pagado
			public void actionPerformed(ActionEvent e) {
				
				JCheckBox check=(JCheckBox)e.getSource();
				
				if(check.isSelected()){
					
					efectivo_pagado.setEnabled(true);
					
				} else {
					
					efectivo_pagado.setEnabled(false);
					efectivo_pagado.setText("");
					t_devuelta.setText("");
										
				}
				
			}
			
		});
		//Etiqueta que muestra el titulo de efectivo pagado
		fuente_cobrar=new Font("Serif", Font.PLAIN, 20);
		efectivo=new JLabel("Efectivo pagado:");
		efectivo.setPreferredSize(new Dimension(150,40));
		efectivo.setFont(fuente_cobrar);
		//Campo de texto para introducir la cantidad de efectivo con que esta pagando el cliente
		fuente_cobrar=new Font("Serif", Font.BOLD, 35);
		efectivo_pagado=new JTextField();
		efectivo_pagado.setEnabled(false);
		efectivo_pagado.setPreferredSize(new Dimension(180,40));
		efectivo_pagado.setHorizontalAlignment(SwingConstants.RIGHT);
		efectivo_pagado.setForeground(Color.RED);
		efectivo_pagado.setFont(fuente_cobrar);
		//añadimos un key listener para que solo acepte numeros y el punto
		efectivo_pagado.addKeyListener(new KeyAdapter(){
			
			public void keyTyped(KeyEvent e){
				
				char caracter=e.getKeyChar();
				
				if((caracter<'0' || caracter>'9') && caracter!=KeyEvent.VK_PERIOD){
					
					e.consume();
				
				}
							
			}
			//Al ir pulsando la tecla se va comprobando si la cantidad es mayor que la cantidad total a cobrar
			public void keyReleased(KeyEvent e){
				
				try{
					//Si es mayor lo ponemos el verde y mostramos la diferencia a devolver
					if(total<=Double.parseDouble(efectivo_pagado.getText())){
						
						efectivo_pagado.setForeground(new Color(9,120,16));
						t_devuelta.setText("$ " + decimal.format((Double.parseDouble(efectivo_pagado.getText())-total)));
						t_devuelta.setForeground(new Color(9,120,16));
						
					//Sino sigue en rojo y no muestra la devuelta	
					} else {
						
						efectivo_pagado.setForeground(Color.red);
						t_devuelta.setText("") ;
					
					}
				
				}catch(NumberFormatException ex){
					
					//JOptionPane.showMessageDialog(null, "Introduzca la cantidad");
				
				}
			}
						
		});
		//Etiqueta que muetra el titulo de a devolver
		fuente_cobrar=new Font("Serif", Font.PLAIN, 20);
		devuelta=new JLabel("A devolver:");
		devuelta.setPreferredSize(new Dimension(150,40));
		devuelta.setFont(fuente_cobrar);
		//Campo de texto que muestra la cantidad a devolver
		fuente_cobrar=new Font("Arial", Font.ITALIC, 35);
		t_devuelta=new JTextField();
		t_devuelta.setPreferredSize(new Dimension(180,40));
		t_devuelta.setFont(fuente_cobrar);
		t_devuelta.setHorizontalAlignment(SwingConstants.RIGHT);
		t_devuelta.setEditable(false);
		//Etiqueta qye muestra el titulo de las perchas asignadas
		fuente_cobrar=new Font("Serif", Font.PLAIN, 20);
		t_perchas=new JLabel("Perchas asignadas:");
		t_perchas.setPreferredSize(new Dimension(150,40));
		t_perchas.setFont(fuente_cobrar);
		//Declaramos un array e iniciamos el combobox, pasamos por parametro las perchas asignadas
		String[] array=new String[perchas_asignadas.size()];
		carrusel=new JComboBox(perchas_asignadas.toArray(array));
		carrusel.setPreferredSize(new Dimension(180,30));
		//Para mostrar espacio y queden ordenados los componentes
		espacio=new JLabel("     ");
		espacio.setPreferredSize(new Dimension(85, 40));
		espacio.setEnabled(false);
		//Boton de aceptar e imprimir el ticket
		aceptar=new JButton("<html><p>Registrar</p><p>Imprimir Ticket</p></html>");
		aceptar.setPreferredSize(new Dimension(120,60));
		aceptar.addActionListener(new GestionAceptar());
		//Boton aceptar que solo muestra los datos y no el ticket
		aceptar_2=new JButton("<html><p>Registrar</p><p>Mostrar datos</p></html>");
		aceptar_2.setPreferredSize(new Dimension(120,60));
		aceptar_2.addActionListener(new GestionAceptar());
		//Agregamos todos los compronentes al panel		
		add(t_cobrar);
		add(cobrar);
		add(pagar_ahora);
		add(efectivo);
		add(efectivo_pagado);
		add(devuelta);
		add(t_devuelta);
		add(t_perchas);
		add(carrusel);
		add(espacio);
		add(aceptar);
		add(aceptar_2);
				
	}
	//Clase privada que lleva la gestion de los botones aceptar
	private class GestionAceptar implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			//Si es el boton aceptar e imprimir ticket generamos factura, asignamos las perchas e imprimimos el ticket
			if(e.getSource().equals(aceptar)){
				
				generarFactura();
				GenerarTicket ticket=new GenerarTicket(id_factura, pagar_ahora.isSelected(), perchas_asignadas, efectivo_pagado.getText(),t_devuelta.getText());
				ticket.print();
				new VisualizarPerchas();
			//Sino hacemos todo menos generamos la factura					
			} else if(e.getSource().equals(aceptar_2)){
				
				generarFactura();
				GenerarTicket ticket=new GenerarTicket(id_factura, pagar_ahora.isSelected(), perchas_asignadas, efectivo_pagado.getText(),t_devuelta.getText());
				ticket.show();
				new VisualizarPerchas();
								
			}
			
		}
				
	}
	//Funcion para guardar los datos en la tabla factura
	private void generarFactura(){
		//Conectamos con la base de datos
		SessionFactory sesion=HibernateUtil.getSessionFactory();
		Session session=sesion.openSession();
		Transaction tx=session.beginTransaction();
		//Cargamos el cliente y la fecha actual
		Clientes cliente=(Clientes)session.get(Clientes.class, dni);
		java.util.Date fecha=new java.util.Date();
		
		//Comprobamos si el cliente ha hecho el pago ahora o no
		if(pagar_ahora.isSelected()){
		
			factura=new Factura(cliente, fecha, fecha, prendas, perchas, total, pagar_ahora.isSelected());
		
		} else {
		
			factura=new Factura(cliente, fecha, null,prendas, perchas, total, pagar_ahora.isSelected());
			
		}
		//Guardamos cambios		
		session.save(factura);
		//Guardamos el numero de factura en la variable id_factura
		id_factura=factura.getId();
				
		//Si todo va bien imprimimos mensaje en pantalla con el numero de factura
		JOptionPane.showMessageDialog(this, "Factura generada correctamente con id: " + id_factura);
		
		//Recorremos el arraylist
		for(String str:perchas_asignadas){
					
				//Capturamos la posicion en el arraylist
				int pos=Integer.parseInt(str);
				//Cargamos el perchero
				Perchero percha=(Perchero)session.get(Perchero.class,pos);
				//Establecemos los valores y guardamos
				percha.setEstado(true);
				percha.setClientes(cliente);
				percha.setFactura(factura);
				session.save(percha);
												
		}
		
		tx.commit();
		//Si todo va bien lo mostramos por pantalla
		JOptionPane.showMessageDialog(this, "Perchas asignadas correctamente");
		//Asigamos true a resultado para saber que todo va bien
		resultado=true;
				
	}
	
	//Funcion para cerrar la ventana actual
	private void cerrar(){
		
		this.dispose();
				
	}
	//Funcion que devuelve el resultado
	public boolean isResultado(){
		
		return resultado;
		
	}
	//Clase privada que visualiza en pantalla la posicion en el carrusel
	private class VisualizarPerchas extends JDialog{
		//Declaramos las variables
		private JLabel a_perchas, a_cliente;
		private JPanel panel_norte, panel_central, panel_este;
		private JList listaNombres;
		private DefaultListModel modelo;
		private JScrollPane scrollLista;
		private JButton aceptar_3;
		
		//Constructor de la clase
		public VisualizarPerchas(){
			//Llamamos al constructor padre para establecer los valores del JDialog
			super(VentanaPrincipal.getFrame(), "Carrusel", true);
			//Configuramos la ventana
			this.setSize(500, 400);
			this.setLayout(new BorderLayout());
			this.setLocationRelativeTo(VentanaPrincipal.getFrame());
			this.setResizable(false);
			//Funcion que establece el panel norte
			setPanelNorte();
			//Funcion que establece el panel central
			setPanelCentral();
			//Funcion que establece el panel este
			setPanelEste();
						
			//Hacemos visible
			this.setVisible(true);
			this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			//Cerramos la ventana anterior			
			cerrar();
			
		}
		
		private void setPanelNorte(){
			//Declaramos la fuente
			Font fuente=new Font("Serif", Font.BOLD, 20);
			//Declaramos el panel norte con un flowlayout
			panel_norte=new JPanel(new FlowLayout(FlowLayout.CENTER));
			//Etiqueta que muestra el cliente con su identificacion
			a_cliente=new JLabel("Cliente: " + dni);
			a_cliente.setPreferredSize(new Dimension(200,40));
			a_cliente.setFont(fuente);
			//Etiqueta que muestra las perchas asignadas con la cantidad
			a_perchas=new JLabel("Perchas asignadas: " + perchas);
			a_perchas.setPreferredSize(new Dimension(200,40));
			a_perchas.setFont(fuente);
			//Agregamos la etiqueta al panel y agregamos el panel al JDialog
			panel_norte.add(a_cliente);
			panel_norte.add(a_perchas);
			
			add(panel_norte, BorderLayout.NORTH);
			
		}
		//Funcion que agrega el panel central
		private void setPanelCentral(){
			//Iniciamos el panel
			panel_central=new JPanel(null);
			//Establecemos la fuente
			Font fuente=new Font("Serif", Font.ITALIC, 20);
			//Iniciamos la List que va a mostrar las pocisiones en el carrusel y le pasamos el modelo
			modelo = new DefaultListModel();
			listaNombres = new JList(modelo);
			listaNombres.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
			listaNombres.setFont(fuente);
			listaNombres.setForeground(Color.BLUE);
			//Establecemos la barra de desplazamiento					
			scrollLista = new JScrollPane();
			scrollLista.setBounds(15, 15,300, 270);
			scrollLista.setViewportView(listaNombres);
			//Recorremos el arraylist y vamos añadiendolo
			for(String str:perchas_asignadas){
				
				modelo.addElement("Posicion en el carrusel: " + str);
				
			}
			//Agregamos la lista y agregamos el panel al JDialog
			panel_central.add(scrollLista);
			
			add(panel_central,BorderLayout.CENTER);
			
			
		}
		//Funcion que establece el panel este
		private void setPanelEste(){
			//Iniciamos el panel
			panel_este=new JPanel(new FlowLayout(FlowLayout.CENTER, 30,100));
			//Agregamos el boton de aceptar y le damos funcionalidad
			aceptar_3=new JButton("Aceptar");
			aceptar_3.setPreferredSize(new Dimension(120,60));
			aceptar_3.addActionListener(new ActionListener(){
				//Si se presiona cierra las ventanas
				public void actionPerformed(ActionEvent arg0) {
					
					cerrar2();
										
				}
				
			});
			//Agregamos el boton y el panel al JDialog
			panel_este.add(aceptar_3);
			
			add(panel_este, BorderLayout.EAST);
					
		}
		//Funcion para cerrar la ventana actual
		private void cerrar2(){
			
			this.dispose();
		}
				
	}

}
