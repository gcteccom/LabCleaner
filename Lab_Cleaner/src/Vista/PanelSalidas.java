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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.List;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.TypeMismatchException;

import TablasBD.Clientes;
import TablasBD.Factura;
import TablasBD.HibernateUtil;
import TablasBD.Perchero;
import TablasBD.Servicio;
//Clase encargada de dar salida a los tickets
public class PanelSalidas extends JPanel {
	
	//Declaramos todas las variables a utilizar
	private int acceso;
	private float total_pvp;
	private Font font_label, font_botones;
	private JTextField ticket, dni, nombre, apellido, prendas, perchas, fecha, pvp, t_estado, fecha_pago, fecha_entrega;
	private JButton borrar, buscar, salida, cobrar;
	private JPanel panel_oeste, panel_central,panel_este;
	private JList<String> listaNombres;
	private DefaultListModel<String> modelo;
	private JScrollPane scrollLista;
	private boolean estado;
	private List<Perchero> perchas_asignadas;
	
	//Constructor de la clase, recibe por parametro el tipo de acceso	
	public PanelSalidas(int acceso){
		
		this.acceso=acceso;
		font_label=new Font("Arial", Font.BOLD, 20);
		font_botones=new Font("Arial", Font.BOLD, 16);
		//Activamos y desactivamos los botones correspondientes
		VentanaPrincipal.Ventas.setEnabled(true);
		VentanaPrincipal.salida_factura.setEnabled(false);
		VentanaPrincipal.gestion_perchero.setEnabled(true);
		VentanaPrincipal.Clientes.setEnabled(true);
		VentanaPrincipal.Servicios.setEnabled(true);
		VentanaPrincipal.Reportes.setEnabled(true);
		VentanaPrincipal.Configuracion.setEnabled(true);
		//En caso que el acceso sea limito desactivamos estos botones
		if(this.acceso==0){
			
			VentanaPrincipal.Reportes.setEnabled(false);
			VentanaPrincipal.Configuracion.setEnabled(false);
			
		}
		
		this.setBackground(Color.WHITE);
		this.setLayout(new BorderLayout());
		//Funcion que establece el panel oeste		
		initPanelOeste();
		//Funcion que establece el panel central
		initPanelCentral();
		//Funcion que establece el panel este
		initPanelEste();
		
		this.add(panel_central, BorderLayout.CENTER);
		this.add(panel_oeste, BorderLayout.WEST);
		this.add(panel_este, BorderLayout.EAST);
		
		
		
	}
	//Funcion para configurar el panel oeste
	private void initPanelOeste(){
		//Agregamos un panel y lo configuramos
		panel_oeste=new JPanel(new FlowLayout(FlowLayout.LEFT, 10,30));
		panel_oeste.setBorder(new TitledBorder("Datos del ticket"));
		panel_oeste.setBackground(Color.WHITE);
		panel_oeste.setPreferredSize(new Dimension(540,400));
		
		ImageIcon icono_buscar=new ImageIcon("src/Recursos/buscar.png");
		//Con la funcion agregarLabel añadimos las etiquetas y con agregarTexto añadimos los campos de textos		
		agregarLabel("Ticket Nº: ", 120,35);
		ticket=agregarTexto(150,35);
		buscar=agregarBoton("Buscar",120,40, icono_buscar);
		
		//agregarLabel("",100,35);
		agregarLabel("NIF: ",120,35);
		dni=agregarTexto(280,35);
		agregarLabel("Nombre: ",120,35);
		nombre=agregarTexto(280,35);
		agregarLabel("Apellido: ",120,35);
		apellido=agregarTexto(280,35);
		agregarLabel("Prendas: ",120,35);
		prendas=agregarTexto(280,35);
		agregarLabel("Perchas: ",120,35);
		perchas=agregarTexto(280,35);
		agregarLabel("Fecha: ",120,35);
		fecha=agregarTexto(280,35);
		agregarLabel("Total PVP: ",120,35);
		pvp=agregarTexto(280,35);
		agregarLabel("Estado: ",120,35);
		t_estado=agregarTexto(280,35);
		agregarLabel("Fecha de Pago: ",180,35);
		fecha_pago=agregarTexto(220,35);
		agregarLabel("Fecha de entrega: ",180,35);
		fecha_entrega=agregarTexto(220,35);
		//Le damos funcionalidad al boton
		buscar.addActionListener(new GestionBotones());
		//Establecemos que los campos de textos no sean editables
		dni.setEditable(false);
		nombre.setEditable(false);
		apellido.setEditable(false);
		prendas.setEditable(false);
		perchas.setEditable(false);
		fecha.setEditable(false);
		pvp.setEditable(false);
		t_estado.setEditable(false);
		fecha_pago.setEditable(false);
		fecha_entrega.setEditable(false);
		//Le añadimos un evento de teclado al campo ticket
		ticket.addKeyListener(new KeyAdapter(){
			
			public void keyPressed(KeyEvent e){
				//Si presionamos enter llama a la funcion buscarFactura()
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					
					buscarFactura();
					
				}
				
			}
			
			
		});
				
	}
	//Funcion que establece el panel central
	private void initPanelCentral(){
		//Agregamos un panel
		panel_central=new JPanel(new FlowLayout(FlowLayout.CENTER, 30,30));
		panel_central.setBorder(new TitledBorder("Carrusel"));
		panel_central.setBackground(Color.WHITE);
		
		Font fuente_lista=new Font("Courier New", Font.BOLD, 26);
		//Iniciamos la JList y la añadimos al panel
		modelo = new DefaultListModel<String>();
		listaNombres = new JList<String>(modelo);
		listaNombres.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
		listaNombres.setFont(fuente_lista);
		listaNombres.setForeground(Color.BLUE);
		scrollLista = new JScrollPane();
		scrollLista.setPreferredSize(new Dimension(800,600));
		scrollLista.setViewportView(listaNombres);
				
		panel_central.add(scrollLista);
				
	}
	//Funcion para establecer el panel este
	private void initPanelEste(){
		//Establecemos el panel con un FlowLayout y lo configuramos
		panel_este=new JPanel(new FlowLayout(FlowLayout.CENTER, 20,40));
		panel_este.setBorder(new TitledBorder("Operaciones"));
		panel_este.setBackground(Color.WHITE);
		panel_este.setPreferredSize(new Dimension(200,400));
		
		ImageIcon icono_borrar=new ImageIcon("src/Recursos/delete.png");
		ImageIcon icono_cobrar=new ImageIcon("src/Recursos/cobrar.png");
		ImageIcon icono_entregar=new ImageIcon("src/Recursos/entregar.png");
		
		
		//Agregamos los tres botones que contiene este panel
		borrar=new JButton("Borrar", icono_borrar);
		borrar.setPreferredSize(new Dimension(120,80));
		borrar.setFont(font_botones);
		borrar.setVerticalTextPosition(SwingConstants.BOTTOM);
		borrar.setHorizontalTextPosition(SwingConstants.CENTER);
		
		cobrar=new JButton("Cobrar", icono_cobrar);
		cobrar.setPreferredSize(new Dimension(120,80));
		cobrar.setFont(font_botones);
		cobrar.setVerticalTextPosition(SwingConstants.BOTTOM);
		cobrar.setHorizontalTextPosition(SwingConstants.CENTER);
		
		salida=new JButton("Entregar", icono_entregar);
		salida.setPreferredSize(new Dimension(120,80));
		salida.setFont(font_botones);
		salida.setVerticalTextPosition(SwingConstants.BOTTOM);
		salida.setHorizontalTextPosition(SwingConstants.CENTER);
		//Al iniciar lo desactivamos
		borrar.setEnabled(false);
		cobrar.setEnabled(false);
		salida.setEnabled(false);
		//Le pasamos el evento
		borrar.addActionListener(new GestionBotones());
		cobrar.addActionListener(new GestionBotones());
		salida.addActionListener(new GestionBotones());
		
		panel_este.add(borrar);
		panel_este.add(cobrar);
		panel_este.add(salida);
		
	}
	//Funcion para agregar las etiquetas al panel oeste
	private void agregarLabel(String name, int ancho, int alto){
		
		JLabel label=new JLabel(name);
		label.setPreferredSize(new Dimension(ancho, alto));
		label.setFont(font_label);
		panel_oeste.add(label);
		
	}
	//Funcion para agregar los campos de textos al panel oeste
	private JTextField agregarTexto(int ancho, int alto){
		
		JTextField campo_texto=new JTextField();
		campo_texto.setPreferredSize(new Dimension(ancho, alto));
		campo_texto.setFont(font_label);
		panel_oeste.add(campo_texto);
		return campo_texto;
		
		
	}
	//Funcion para agregar botones al panel oeste
	private JButton agregarBoton(String name, int ancho, int alto, ImageIcon icono){
		
		JButton boton=new JButton(name, icono);
		boton.setPreferredSize(new Dimension(ancho, alto));
		boton.setFont(font_botones);
		panel_oeste.add(boton);
		return boton;
				
	}
	
	//Funcion encargada de buscar el numero de ticket correspondiente
	private void buscarFactura(){
		//Verificamos que tenga texto el campo
		if(ticket.getText().length()==0){
			
			JOptionPane.showMessageDialog(this, "Escriba el numero del ticket");
			
		} else {
			
				try{
					//Abrimos session con hibernate
					SessionFactory sesion=HibernateUtil.getSessionFactory();
					Session session=sesion.openSession();
					Transaction tx=session.beginTransaction();
					//Cargamos la factura
					Factura factura=(Factura)session.get(Factura.class, Integer.parseInt(ticket.getText()));
					//Si no encuentra nada mostramos el error					
					if(factura==null){
						
						JOptionPane.showMessageDialog(this, "Ese numero de ticket no existe");
						ticket.setText("");
					//Sino  realizamos las siguientes operaciones					
					} else {
						//Cargamos el cliente relacionado con la factura
						Clientes cliente=factura.getClientes();
						//Guardamos el total de la factura en esta variable ya que la utilizaremos en otras funciones
						total_pvp=factura.getTotal();
						//Verificamos el estado, si esta paga o no
						estado=factura.isEstado();
						//Desactivamos el campo de ticket y activamos el boton borrar
						ticket.setEditable(false);
						borrar.setEnabled(true);
						//Volcamos los datos a sus respectivos campos de textos
						dni.setText(cliente.getDni());
						nombre.setText(cliente.getNombre());
						apellido.setText(cliente.getApellido());
						prendas.setText("" + factura.getPrendas());
						perchas.setText("" + factura.getPerchas());
						fecha.setText("" + factura.getFecha());
						pvp.setText("$" + total_pvp);
						//Aqui verificamos si esta paga o no.
						if(estado){
							//Si esta paga, activamos el boton de salida
							t_estado.setText("Ticket pagado");
							t_estado.setForeground(new Color(9,120,16));
							salida.setEnabled(true);
							fecha_pago.setText("" + factura.getFechaPago());
							fecha_pago.setForeground(new Color(9,120,16));
							
							
						} else {
							//Sino activamos el boton de cobrar
							t_estado.setText("Ticket sin pagar");
							t_estado.setForeground(Color.RED);
							cobrar.setEnabled(true);
							fecha_pago.setText("Pendiente de pago");
							fecha_pago.setForeground(Color.RED);
							
						}
						//Verificamos si esta entregada						
						if(factura.getFechaEntrega()==null){
							
							fecha_entrega.setText("Pendiente de entregar");
							fecha_entrega.setForeground(Color.RED);
						
						} else {
							//Si esta entregada desactivamos el boton de salida
							fecha_entrega.setText("" + factura.getFechaEntrega());
							fecha_entrega.setForeground(new Color(9,120,16));
							salida.setEnabled(false);
						
						}
						//Limpiamos el JList por si tiene datos de operaciones anteriores
						modelo.clear();
						//Establecemos el color de letras
						listaNombres.setForeground(Color.BLUE);
						//Creamos la consulta						
						Query query=session.createQuery("Select p from " + Perchero.class.getName() + " p where clientes.dni='" + dni.getText() + "'");
						//Guardamos datos en el ArrayList
						perchas_asignadas=query.list();
						//Recorresmos el array list
						for(Perchero str:perchas_asignadas){
							//Vamos añadiendo las filas
							modelo.addElement("Posicion en el carrusel: " + str.getId());
														
						}
						//Aqui comprobamos en caso de que el cliente tenga dos ticket sin dar salida, mostramos este mensaje de advertencia
						if(Integer.parseInt(perchas.getText())!=perchas_asignadas.size() && factura.getFechaEntrega()==null){
							
							modelo.addElement("----------------------------------------------");
							listaNombres.setForeground(Color.RED);
							modelo.addElement("VERIFIQUE QUE EL CLIENTE NO TENGA MAS TICKET");
							modelo.addElement("PENDIENTES, LA CANTIDAD DE PRENDAS NO COINCIDEN");
							modelo.addElement("CON LAS POSICIONES EN EL CARRUSEL");
														
						}
						
						tx.commit();							
					
					}
				//Capturamos los diferentes errores y mostramos por pantalla		
				}catch(NullPointerException e){
									
					e.printStackTrace();
					
				}catch(TypeMismatchException e){
					
					e.printStackTrace();
										
				}catch(NumberFormatException e){
					
					JOptionPane.showMessageDialog(this, "El valor tiene que ser numerico");
					ticket.setText("");
					
				}
					
		}
			
	}
	//Clase privada para gestionar la accion de los botones
	private class GestionBotones implements ActionListener{

		
		public void actionPerformed(ActionEvent e) {
			//Verificamos que se haya pulsado el boton de borrar
			if(e.getSource().equals(borrar)){
				//borramos todos los campos, activamos nuevamente el campo de ticket, desactivamos los botones y borramos el JList
				ticket.setText("");
				ticket.setEditable(true);
				dni.setText("");
				nombre.setText("");
				apellido.setText("");
				prendas.setText("");
				perchas.setText("");
				fecha.setText("");
				pvp.setText("");
				t_estado.setText("");
				fecha_pago.setText("");
				fecha_entrega.setText("");
				
				borrar.setEnabled(false);
				cobrar.setEnabled(false);
				salida.setEnabled(false);
				
				modelo.clear();
			//Comprobamos que se pulse el boton de buscar, si es asi, abrimos la ventana de busqueda
			} else if(e.getSource().equals(buscar)){
				
				new VentanaBusqueda();
			//Comprobamos que pulsamos el boton cobrar, si es asi, abrimosla ventana de cobrar				
			} else if(e.getSource().equals(cobrar)){
				
				new Cobrar();
			//Comprobamos que se pulse el boton salida, si es asi hacemos lo siguiente	
			} else if(e.getSource().equals(salida)){
				//Confirmamos que se entre las prensas
				int op=JOptionPane.showConfirmDialog(VentanaPrincipal.getFrame(), "Confirme la entrega", "Despachar prendas", JOptionPane.OK_CANCEL_OPTION);
				//Si se selecciona si
				if(op==JOptionPane.YES_OPTION){
					//Abrimos session
					SessionFactory sesion=HibernateUtil.getSessionFactory();
					Session session=sesion.openSession();
					Transaction tx=session.beginTransaction();
					//Cargamos la factura
					Factura factura=(Factura)session.get(Factura.class, Integer.parseInt(ticket.getText()));
					//Declaramos la fecha actual
					Date fecha=new Date();
					//Configuramos la fecha
					factura.setFechaEntrega(fecha);
					//Guardamos session
					session.save(factura);
					//Recorremos las perchas asignadas								
					for(Perchero pch:perchas_asignadas){
						//Cargamos la percha y liberamos el espacio poniendo false a estado y null a cliente, guardamos cambios
						Perchero percha=(Perchero)session.get(Perchero.class, pch.getId());
						percha.setEstado(false);
						percha.setClientes(null);
						session.save(percha);
						
											
					}
																	
					tx.commit();
					//Llamamos a la funcion reiniciar para que borre todos los datos
					reiniciar();
				
				}
			}
			
		}
		
		
	}
	//Clase privada encargada de la ventana de busqueda de ticket
	private class VentanaBusqueda extends JDialog{
		//Declaramos las variables a utilizar
		private JTextField busqueda;
		private JTable tabla_factura=null;
		private DefaultTableModel modelo = null;
	    private JScrollPane desplazamiento = null;
	    private JButton aceptar;
	    Font font;
	    List<Factura> listado;
	    //Cnstructor de la clase				
		public VentanaBusqueda(){
			//Configuramos las diferentes opciones de la ventana
			super(VentanaPrincipal.getFrame(), "Busqueda de ticket por NIF", true);
			
			this.setSize(800, 600);
			this.setLocationRelativeTo(VentanaPrincipal.getFrame());
			this.setBackground(Color.WHITE);
			this.setResizable(false);
			this.setLayout(new BorderLayout(10,10));
			font=new Font("Arial", Font.PLAIN, 18);
			//Llamamos a la funcion init encargada de iniciar todos los componentes
			init();
			//Llamamos a la funcion getBills para capturar los datos del ticket
			getBills();
						
			this.setVisible(true);
			this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				
		}
		
		private void init(){
			//Array de las columnas de la tabla
			String[] columnas={"ID", "NIF", "Fecha", "Fecha de Pago", "Fecha de entrega", "Prendas"};
						
			//Campo de texto para buscar por nif
			busqueda=new JTextField();
			busqueda.setPreferredSize(new Dimension(150,35));
			busqueda.setFont(font);
			this.add(busqueda, BorderLayout.NORTH);
			//Le agregamos evento del teclado para que vaya actualizando la tabla mientras se va escribiendo
			busqueda.addKeyListener(new KeyAdapter(){
				
				public void keyReleased(KeyEvent e){
					
					getBills();					
					
				}
				
			});
			//Iniciamos la tabla y la hacemos que no se pueda editar
			tabla_factura=new JTable(){
				
				public boolean isCellEditable(int rowIndex, int colIndex) {
					 
					return false;
				}
				
			};
			//Iniciamos el modelo y la barra de desplazamiento
			modelo = new DefaultTableModel();
		    desplazamiento = new JScrollPane(tabla_factura);
		    modelo.setColumnIdentifiers(columnas);
		    
		    desplazamiento.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	        desplazamiento.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	        
	        tabla_factura.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
	        tabla_factura.setFillsViewportHeight(true);
	        //Configuramos que los datos esten centrados	        
	        DefaultTableCellRenderer render=new DefaultTableCellRenderer();
	        render.setHorizontalAlignment(SwingConstants.CENTER);
	        //Agregamos el modelo a la tabla              
	        tabla_factura.setModel(modelo);
	        
	        tabla_factura.getColumnModel().getColumn(0).setCellRenderer(render);
	        tabla_factura.getColumnModel().getColumn(2).setCellRenderer(render);
	        tabla_factura.getColumnModel().getColumn(3).setCellRenderer(render);
	        tabla_factura.getColumnModel().getColumn(4).setCellRenderer(render);
	        //Agregamos la tabla al panel
	        this.add(desplazamiento, BorderLayout.CENTER);
	        //Evento del mouse para la tabla para que al hacer 1 click o doble click haga su funcion
	        tabla_factura.addMouseListener(new MouseAdapter(){
	        	
	        	public void mouseClicked(MouseEvent e){
	        		//desactivamos los botones de operaciones	        		
	        		borrar.setEnabled(false);
	        		cobrar.setEnabled(false);
	        		salida.setEnabled(false);
	        		//Si hacemos un solo click nos vuelca los datos pero sigue la ventana activa	        		
	        		if(e.getClickCount()==1){
	        			
	        			int row=tabla_factura.getSelectedRow();
	        			
	        			ticket.setText(tabla_factura.getValueAt(row, 0).toString());
	        			buscarFactura();
	        			        			
	        		}
	        		//Si hacemos doble click vuelca los datos y cierra la ventana	        		
	        		if(e.getClickCount()==2){
	        			
	        			int row=tabla_factura.getSelectedRow();
	        			
	        			ticket.setText(tabla_factura.getValueAt(row, 0).toString());
	        			buscarFactura();
	        			cerrar();
	        			        			
	        		}
	        		
	        	}
	        	  		        	
	        });
	         			
		}
		//Funcion para ir actualizando los datos de la tabla de busqueda
		private void getBills(){
			//Borramos todas las filas
			for (int i = 0; i < tabla_factura.getRowCount(); i++) {
			    
				   modelo.removeRow(i);
		           i-=1;
		   
			}			
			//Abrimos session			
			SessionFactory sesion=HibernateUtil.getSessionFactory();
			Session session=sesion.openSession();
			Transaction tx=session.beginTransaction();
	        //Hacemos la busqueda con lo que vaya escribiendo y que la fecha de entrega sea nula
	        Query query=session.createQuery("SELECT p FROM " + Factura.class.getName() + " p where clientes.dni like '%" + busqueda.getText() + "%' and p.fechaEntrega=null");
			//Obtenemos el listado del resultado			       
	        listado=query.list();
	        //Recorremos la lista y vamos agregando la fila
	        for(Factura list:listado){
	        		        	
			    int codigo=list.getId();
			    String dni=list.getClientes().getDni();
			    Date fecha=list.getFecha();
			    Date fecha_pago=list.getFechaPago();
			    Date fecha_entrega=list.getFechaEntrega();
			    int prendas=list.getPrendas();
			        	
			    modelo.addRow(new Object[]{codigo, dni, fecha, fecha_pago, fecha_entrega, prendas});
		        	        	
		    }
	        
	        tx.commit();
		}
		//Funcion para cerrar la ventana actual
		private void cerrar(){
			
			this.dispose();
		}
	
	}
	//Clase privada encargada de cobrar en caso que el ticket no este pagado
	private class Cobrar extends JDialog{
		//Declaramos las variables a utilizar
		private JLabel l_total, l_pagado, l_devuelto;
		private JTextField t_total, t_pagado, t_devuelto;
		private JButton c_aceptar;
		//Constructor de la clase
		public Cobrar(){
			
			super(VentanaPrincipal.getFrame(), "Cobrar", true);
			
			this.setSize(480, 350);
			this.setResizable(false);
			this.setLocationRelativeTo(VentanaPrincipal.getFrame());
			this.setLayout(new FlowLayout(FlowLayout.CENTER,10,30));
			//Llamamos la funcion init que inicia todos los componentes
			init();
						
			this.setVisible(true);
			this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			
			
			
		}
		//Funcion que inicia y coloca todos los componentes
		private void init(){
			//Declaramos la fuente a utilizar
			Font fuente_cobrar=new Font("Arial", Font.PLAIN, 20);
			//Agregamos las etiquetas y los campos de texto
			l_total=new JLabel("Total a cobrar: ");
			l_total.setPreferredSize(new Dimension(180,35));
			l_total.setFont(fuente_cobrar);
			
			l_pagado=new JLabel("Efectivo pagado: ");
			l_pagado.setPreferredSize(new Dimension(180,35));
			l_pagado.setFont(fuente_cobrar);
			
			l_devuelto=new JLabel("Devuelta: ");
			l_devuelto.setPreferredSize(new Dimension(180,35));
			l_devuelto.setFont(fuente_cobrar);
			
			t_total=new JTextField();
			t_total.setPreferredSize(new Dimension(150,35));
			t_total.setFont(fuente_cobrar);
			t_total.setEditable(false);
			t_total.setHorizontalAlignment(JTextField.RIGHT);
			t_total.setText(pvp.getText());
			t_total.setForeground(Color.BLUE);
						
			t_pagado=new JTextField();
			t_pagado.setPreferredSize(new Dimension(150,35));
			t_pagado.setFont(fuente_cobrar);
			t_pagado.setHorizontalAlignment(JTextField.RIGHT);
			
			t_devuelto=new JTextField();
			t_devuelto.setPreferredSize(new Dimension(150,35));
			t_devuelto.setFont(fuente_cobrar);
			t_devuelto.setEditable(false);
			t_devuelto.setHorizontalAlignment(JTextField.RIGHT);
			//Por ultimo agregamos el boton y le agregamos el evento
			c_aceptar=new JButton("Aceptar");
			c_aceptar.setPreferredSize(new Dimension(120,60));
			c_aceptar.addActionListener(new ActionListener(){
				
				public void actionPerformed(ActionEvent arg0) {
					//llamamos a la funcion setCobrar que se encarga de realizar el cobro y registrarlo
					setCobrar();
					
				}
				
			});
			//Agregamos evento de teclado al campo de texto que al presionar enter llama tambien a la funcion setCobrar
			t_pagado.addKeyListener(new KeyAdapter(){
				
				public void keyPressed(KeyEvent e){
					
					if(e.getKeyCode()==KeyEvent.VK_ENTER){
					
						setCobrar();
					
					}
					
				}
				//Comprobamos que la cantidad introducida sea mayor que el pvp
				public void keyReleased(KeyEvent e){
					
					if(total_pvp>Float.parseFloat(t_pagado.getText())){
						
						t_pagado.setForeground(Color.RED);
						
					} else {
						
						t_pagado.setForeground(new Color(6,120,16));
						t_devuelto.setText("$" + (Integer.parseInt(t_pagado.getText())-total_pvp));
						
					}
					
				}
				
				
			});
			//Agregamos los componentes		
			add(l_total);
			add(t_total);
			add(l_pagado);
			add(t_pagado);
			add(l_devuelto);
			add(t_devuelto);
			add(c_aceptar);
			
						
			
		}
		//Funcion cobrar que se encarga de gestionar el cobro y registrarlo en la base de datos
		private void setCobrar(){
			//Abrimos sesion
			SessionFactory sesion=HibernateUtil.getSessionFactory();
			Session session=sesion.openSession();
			Transaction tx=session.beginTransaction();
			//Capturamos la fecha actual
			Date fecha=new Date();
			//Cargamos la factura
			Factura factura=(Factura)session.get(Factura.class, Integer.parseInt(ticket.getText()));
			//Establecemos la fecha de pago y cambiamos el estado de pagado
			factura.setFechaPago(fecha);
			factura.setEstado(true);
			//Guardamos cambios
			session.save(factura);
					
			tx.commit();
			//Cambiamos los campos de ticket y fecha para que establecer que se ha pagado					
			t_estado.setText("Ticket pagado");
			fecha_pago.setText("" + factura.getFechaPago());
			//Cambiamos al color verde						
			t_estado.setForeground(new Color(5,120,16));
			fecha_pago.setForeground(new Color(5,120,16));
			//Desactivamos el boton cobrar y activamos el boton de salida
			cobrar.setEnabled(false);
			salida.setEnabled(true);
			//Cerramos la ventana actuar
			this.dispose();
			
		}
				
	}
	//Funcion para reiniciar todos los componentes del panel, se llama desde el evento de salida.
	private void reiniciar(){
		
		ticket.setText("");
		ticket.setEditable(true);
		dni.setText("");
		nombre.setText("");
		apellido.setText("");
		prendas.setText("");
		perchas.setText("");
		fecha.setText("");
		pvp.setText("");
		t_estado.setText("");
		fecha_pago.setText("");
		fecha_entrega.setText("");
		
		borrar.setEnabled(false);
		cobrar.setEnabled(false);
		salida.setEnabled(false);
		
		modelo.clear();
				
	}
	
}


