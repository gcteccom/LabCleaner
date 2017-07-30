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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import TablasBD.Clientes;
import TablasBD.Factura;
import TablasBD.HibernateUtil;
import TablasBD.Servicio;


//Clase encargada del panel de ventas
@SuppressWarnings({"serial", "unchecked"})
public class PanelVentas extends JPanel {
	
	//Declaramos todas las variables a utilizar
	private JPanel panel_norte, panel_central, panel_sur;
	private JTextField dni, nombre, apellido;
	private JLabel total_factura, t_cliente, t_items, t_perchas;
	private JButton buscar, buscar_nuevo, cobrar, buscar_servicio, modificar_servicio, eliminar_servicio, entrada, salida, perchero, addServiceAcept;
	private Font font;
	private static JTable tabla_factura=null;
	private DefaultTableModel modelo = null;
    private JScrollPane desplazamiento = null;
    private int acceso,cantidad_total=0,resultado_perchero=0;
    Ventana_Perchero o_perchero;
    private float total;
    
	//Iniciamos con el constructo, recibe por parameto el tipo de acceso		
	public PanelVentas(int acceso){
		//Establecemos los valores
    	this.acceso=acceso;
		this.setLayout(new BorderLayout());
		this.setBackground(Color.WHITE);
		//Fuente principal a utilizar
		font=new Font("Arial", Font.BOLD, 16);
		//Activamos y desactivamos los botones correspondiente
		VentanaPrincipal.Ventas.setEnabled(false);
		VentanaPrincipal.salida_factura.setEnabled(true);
		VentanaPrincipal.gestion_perchero.setEnabled(true);
		VentanaPrincipal.Clientes.setEnabled(true);
		VentanaPrincipal.Servicios.setEnabled(true);
		VentanaPrincipal.Reportes.setEnabled(true);
		VentanaPrincipal.Configuracion.setEnabled(true);
		//Si el tipo de acceso es 0, desactivamos los botones de reportes y configuracion
		if(this.acceso==0){
		
			VentanaPrincipal.Reportes.setEnabled(false);
			VentanaPrincipal.Configuracion.setEnabled(false);
		
		}
				
		//Configuracion del panel norte y lo agregamos 
		panel_norte=setPanelNorte();
		//Agregamos los label y los campos de textos
		agregarLabel("NIF: ");
		dni=agregarTexto(250,35);
		panel_norte.add(buscar);
		panel_norte.add(buscar_nuevo);
		agregarLabel("Nombre: ");
		nombre=agregarTexto(450,35);
		agregarLabel("Apellido: ");
		apellido=agregarTexto(450,35);
		//Los campos de textos nombre y apellido no se puede modificar
		nombre.setEditable(false);
		apellido.setEditable(false);
		add(panel_norte, BorderLayout.NORTH);
		
		//Configuracion del panel sur y lo agregamos
		panel_sur=setPanelSur();
		add(panel_sur, BorderLayout.SOUTH);
				
		//Configuracion del panel central y lo agregamos
		panel_central=setPanelCentral();
		add(panel_central, BorderLayout.CENTER);
		
		
	}
	//Funcion para establecer el panel norte
	private JPanel setPanelNorte(){
		
		JPanel panel=new JPanel();
		
		ImageIcon icono_buscar=new ImageIcon(getClass().getResource("/Recursos/buscar.png"));
		ImageIcon icono_borrar=new ImageIcon(getClass().getResource("/Recursos/delete.png"));
		
		panel.setBackground(Color.WHITE);
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 20,20));
		panel.setBorder(new TitledBorder("Datos Cliente"));
		
		buscar=new JButton("Buscar", icono_buscar);
		buscar.setPreferredSize(new Dimension(120,40));
		buscar.setFont(font);
		buscar.addActionListener(new AccionBuscar());
				
		buscar_nuevo=new JButton("Borrar", icono_borrar);
		buscar_nuevo.setPreferredSize(new Dimension(120,40));
		buscar_nuevo.setFont(font);
		buscar_nuevo.setEnabled(false);
		buscar_nuevo.addActionListener(new AccionBuscar());
					
		return panel;
		
	}
	//Funcion para establecer el panel sur
	private JPanel setPanelSur(){
		
		JPanel panel=new JPanel();
		
		ImageIcon icono_cobrar=new ImageIcon(getClass().getResource("/Recursos/cobrar.png"));
				
		t_cliente=new JLabel("");
		t_items=new JLabel("");
		t_perchas=new JLabel("");
		
		Font font2=new Font("Arial", Font.BOLD, 30);
		Font font3=new Font("Arial", Font.BOLD, 16);
				
		panel.setBackground(Color.WHITE);
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 20,20));
		panel.setBorder(new TitledBorder("Resumen de Venta"));
		
		t_cliente.setPreferredSize(new Dimension(700,75));
		t_cliente.setFont(font3);
		t_cliente.setForeground(new Color(25, 148, 230));
		t_items.setPreferredSize(new Dimension(250,35));
		t_items.setFont(font3);
		t_items.setForeground(new Color(25, 148, 230));
		t_perchas.setPreferredSize(new Dimension(450,35));
		t_perchas.setFont(font3);
		t_perchas.setForeground(new Color(25, 148, 230));
		
		cobrar=new JButton("<html><p>Alt + F12</p><p>COBRAR</p></html>", icono_cobrar);
		cobrar.setPreferredSize(new Dimension(135,60));
		cobrar.setFont(font3);
		cobrar.setMnemonic(KeyEvent.VK_F12);
		cobrar.addActionListener(new GestionAcciones());
				
		total_factura=new JLabel("$ " + "00.00");
		total_factura.setFont(font2);
		total_factura.setForeground(new Color(25, 148, 230));
		
		panel.add(t_cliente);
		panel.add(t_items);
		panel.add(t_perchas);
		panel.add(cobrar);
		panel.add(total_factura);
		
			
		return panel;
	}
	//Funcion para establecer el panel central
	private JPanel setPanelCentral(){
		
		//Declaracion de las columnas de la tabla de factura
		String[] columnas={"Codigo", "Descripcion", "Precio", "Cantidad", "Total"};
		//Declaramos un nuevo panel con un borderlayout
		JPanel panel=new JPanel(new BorderLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(new TitledBorder("Datos Factura"));
		
		//Establecemos otro panel que va a ir en la parte norte, contiene los botones de funciones
		JPanel panel_factura_norte=new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
		panel_factura_norte.setBackground(Color.WHITE);
		//Declaramos la tabla que ira en el centro, y la hacemos que no sea editable
		tabla_factura=new JTable(){
			
			public boolean isCellEditable(int rowIndex, int colIndex) {
				 
				return false;
			}
			
		};
		//Establecemos el modelo de la table y la barra de desplazamiento
		modelo = new DefaultTableModel();
	    desplazamiento = new JScrollPane(tabla_factura);
	    //Agregamos el array de columnas declaradas anteriormente
	    modelo.setColumnIdentifiers(columnas);
	    //Establecemos que la barra se puede desplazar horizontal y verticalmente	    	    
	    desplazamiento.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        desplazamiento.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //En la tabla establecemos que las columnas no se establecezcan automaticamente debido a que queremos que las columnas tengas diferentes tamaños
        tabla_factura.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabla_factura.setFillsViewportHeight(true);
        tabla_factura.setFont(font);
        //Con esta clase establacemos que los textos de las columnas esten centrada
        DefaultTableCellRenderer render=new DefaultTableCellRenderer();
        render.setHorizontalAlignment(SwingConstants.CENTER);
        //agregamos el modelo a la tabla               
        tabla_factura.setModel(modelo);
        //Agregamos el centrado a las columnas que queremos
        tabla_factura.getColumnModel().getColumn(0).setCellRenderer(render);
        tabla_factura.getColumnModel().getColumn(2).setCellRenderer(render);
        tabla_factura.getColumnModel().getColumn(3).setCellRenderer(render);
        tabla_factura.getColumnModel().getColumn(4).setCellRenderer(render);
        //Establecemos la altura de las filas
        tabla_factura.setRowHeight(30);
        //Aqui establecemos el ancho de cada columna
        TableColumnModel columnModel = tabla_factura.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(200);
        columnModel.getColumn(1).setPreferredWidth(986);
        columnModel.getColumn(2).setPreferredWidth(260);
        columnModel.getColumn(3).setPreferredWidth(200);
        columnModel.getColumn(4).setPreferredWidth(260);
        //Agregamos un key listener para que al presionar en la tecla delete podamos borrar filas
        tabla_factura.addKeyListener(new KeyAdapter(){
        	
        	public void keyPressed(KeyEvent e){
        		
        		if(e.getKeyCode()==KeyEvent.VK_DELETE){
        			
        			eliminarServicio();
        			
        		}
        		
        	}
        	
        });
        //Agregamos los botones del panel norte y le damos funcionalidas con la clase GestionAcciones
        
        ImageIcon icono_buscar_servicio=new ImageIcon(getClass().getResource("/Recursos/add.png"));
        ImageIcon icono_modificar=new ImageIcon(getClass().getResource("/Recursos/modificar.png"));
        ImageIcon icono_eliminar=new ImageIcon(getClass().getResource("/Recursos/eliminar.png"));
        ImageIcon icono_entrada=new ImageIcon(getClass().getResource("/Recursos/entrada.png"));
        ImageIcon icono_salida=new ImageIcon(getClass().getResource("/Recursos/salida.png"));
        ImageIcon icono_percha=new ImageIcon(getClass().getResource("/Recursos/agregar_percha.png"));
        
        buscar_servicio=new JButton("Agregar Servicio", icono_buscar_servicio);
		buscar_servicio.setPreferredSize(new Dimension(140,35));
		buscar_servicio.addActionListener(new GestionAcciones());
		buscar_servicio.setHorizontalTextPosition(SwingConstants.LEFT);
		
		modificar_servicio=new JButton("Modificar", icono_modificar);
		modificar_servicio.setPreferredSize(new Dimension(140,35));
		modificar_servicio.addActionListener(new GestionAcciones());
		modificar_servicio.setHorizontalTextPosition(SwingConstants.LEFT);
		
		eliminar_servicio=new JButton("Eliminar", icono_eliminar);
		eliminar_servicio.setPreferredSize(new Dimension(140,35));
		eliminar_servicio.addActionListener(new GestionAcciones());
		eliminar_servicio.setHorizontalTextPosition(SwingConstants.LEFT);
		
		entrada=new JButton("Entrada", icono_entrada);
		entrada.setPreferredSize(new Dimension(140,35));
		entrada.addActionListener(new GestionAcciones());
		entrada.setHorizontalTextPosition(SwingConstants.LEFT);
				
		salida=new JButton("Salida", icono_salida);
		salida.setPreferredSize(new Dimension(140,35));
		salida.addActionListener(new GestionAcciones());
		salida.setHorizontalTextPosition(SwingConstants.LEFT);
		//Si el tipo de acceso fuera 0, desativamos los botones de agregar o sacar efectivo
		if(acceso==0){
			
			entrada.setEnabled(false);
			salida.setEnabled(false);
			
		}
		
		perchero=new JButton("Asignar Percha", icono_percha);
		perchero.setPreferredSize(new Dimension(140,35));
		perchero.addActionListener(new GestionAcciones());
		perchero.setHorizontalTextPosition(SwingConstants.LEFT);
		
		panel_factura_norte.add(buscar_servicio);
		panel_factura_norte.add(modificar_servicio);
		panel_factura_norte.add(eliminar_servicio);
		panel_factura_norte.add(entrada);
		panel_factura_norte.add(salida);
		panel_factura_norte.add(perchero);
		//Por ultimo agregamos los dos paneles al panel principal
		panel.add(panel_factura_norte, BorderLayout.NORTH);
		panel.add(desplazamiento, BorderLayout.CENTER);
		
		return panel;
		
	}
	//Funcion para agregar Label al panel norte
	private void agregarLabel(String nombre){
		
		JLabel label=new JLabel(nombre);
		label.setFont(font);
		panel_norte.add(label);
	//Funciona para agregar los campos de textos al panel norte			
	} private JTextField agregarTexto(int ancho, int alto){
		
		JTextField texto=new JTextField();
		texto.setPreferredSize(new Dimension(ancho,alto));
		texto.setFont(font);
		texto.addKeyListener(new KeyAdapter(){
			
			public void keyPressed(KeyEvent e){
				
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					
					buscarCliente();
					
				}
				
			}
						
		});
		panel_norte.add(texto);
		return texto;
				
	}
	//Clase privada encargada de agregar la funcionalidad al panel norte, especificamente a los botones de busqueda de cliente
	private class AccionBuscar implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			//Si presionamos en buscar llamamos a la funcion buscarCliente sino borra todos los datos
			if(e.getSource().equals(buscar)){
				
				buscarCliente();
								
			} else {
				
				dni.setText("");
				dni.setEditable(true);
				
				buscar.setEnabled(true);
				buscar_nuevo.setEnabled(false);
				
				nombre.setText("");
				apellido.setText("");
				
				t_cliente.setText("");
				
			}
			
		}
				
	}
	//Funcion encargada de buscar el cliente
	private void buscarCliente(){
		//Comprobamos que tenga texto sino mostramos el error
		if(dni.getText().length()==0){
			
			JOptionPane.showMessageDialog(null, "El campo NIF esta en blanco");
			
		} else {
			//Si tiene texto introducismo conectamos con la base de datos	
			SessionFactory sesion=HibernateUtil.getSessionFactory();
			Session session=sesion.openSession();
			Transaction tx=session.beginTransaction();
			//Cargamos el cliente	
			Clientes cliente=(Clientes)session.get(Clientes.class, dni.getText());
			//Si no existe mostramos  el error
			if(cliente==null){
				
				JOptionPane.showMessageDialog(null, "Ese cliente no existe");
				dni.setText("");
			//Si existe volcamos los datos a los campos correspondiente			
			} else {
			
				dni.setEditable(false);
				buscar.setEnabled(false);
				buscar_nuevo.setEnabled(true);
				nombre.setText(cliente.getNombre());
				apellido.setText(cliente.getApellido());
				
				t_cliente.setText("<html><p>Cliente: " + cliente.getNombre() + " " + cliente.getApellido() + "</p><p>NIF: " 
				+ cliente.getDni() + "</p><p>Direccion: " + cliente.getDireccion() + ", " + cliente.getPoblacion() + ", " 
				+ cliente.getProvincia() + "</p><p>Telefono fijo: " + cliente.getTelefono() + ", Movil: " + cliente.getMovil() + "</p>");
						
			}
			
			tx.commit();
				
		}
	
	}
	//Clase privada encargada de darle funcionalidas a los botones del panel norte del panel central
	private class GestionAcciones implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			//Si presionamos en buscar servicio llamamos a la funciona agregarServicio
			if(e.getSource().equals(buscar_servicio)){
					
				agregarServicio();
			//Si presionamos en modificar hacemos la siguiente comprobaciones			
			} else if(e.getSource().equals(modificar_servicio)){
				//Primero que haya una fila seleccionada sino mostramos el error
				int row=tabla_factura.getSelectedRow();
				
				if(row==-1){
					
					JOptionPane.showMessageDialog(null, "No hay ningun servicio seleccionado");
				//Si esta seleccionada	
				} else {
					//Cargamos la columna de cantidad y lo convertimos en entero
					String cantidadstr=tabla_factura.getValueAt(row, 3).toString();
					int cantidad_restar=Integer.parseInt(cantidadstr);
					//Cargamos  la columna de precio para poder hacer el calculo nuevamente y lo convertimos a decimal					
					String preciostr=tabla_factura.getValueAt(row, 2).toString();
					float aux_precio=Float.parseFloat(preciostr);
					
					try{
						//Pedimos el nuevo datos y lo convertimos a entero, si introducimos un valor no valido no da error
						String aux_cantidad=(String)JOptionPane.showInputDialog(null, "Introduzca una nueva cantidad","1");
						int cantidad=Integer.parseInt(aux_cantidad);
						
						//Comprobamos que la cantidad introducida sea mayor que 0
						if(cantidad<1){
							
							JOptionPane.showMessageDialog(null, "Cantidad no es correcta");
						//Si la cantidad es correcta, podemos la nueva cantidad en la columna de cantidad y el precio, recalculamos la nueva cantidad total y llamamos a la funcion setTotal Factura	
						} else {
							
							tabla_factura.setValueAt(cantidad, row, 3);
							tabla_factura.setValueAt(cantidad*aux_precio, row, 4);
							cantidad_total-=cantidad_restar;
							cantidad_total+=cantidad;
							t_items.setText("Cantidad total: " + cantidad_total);
							setTotalFactura();
						}
					//Capturamos los diferentes error que puedan surgir
					} catch (NumberFormatException ex){
						
						JOptionPane.showMessageDialog(null, "Error en la cantidad");
						
					} catch (Exception ex){
						
						JOptionPane.showMessageDialog(null, "Error inesperado");
					
					}
										
				}
			//Si presionamos en el boton eliminar llamamos a la funcion eliminarServicio					
			} else if(e.getSource().equals(eliminar_servicio)){
				
				eliminarServicio();
			//Si presionamos en el boton perchero llamamos a la clase Ventana_Perchero					
			} else if(e.getSource().equals(perchero)){
				
				o_perchero=new Ventana_Perchero(cantidad_total);
				resultado_perchero=o_perchero.getResultado();
				t_perchas.setText("Perchas Asignadas: " + resultado_perchero);;
			//Si presionamos el cobrar hacemos la siguiente comprobaciones								
			} else if(e.getSource().equals(cobrar)){
				//Que se haya introducido el cliente
				if(t_cliente.getText().length()>0){
					//Que la cantidad total y el perchero total sea mayor que 0
					if(cantidad_total>0 && resultado_perchero>0){
						//Que cantidad sea igual a perchero
						if(cantidad_total==resultado_perchero){
							//Si todo es correcto llamamos a la clase GestionCobro que es la encargada de cobrar							
							GestionCobro cobrar=new GestionCobro(dni.getText(), cantidad_total, resultado_perchero, total, o_perchero.getCheck());
							//Si todo ha ido bien, borrar todos los datos del panel de venta
							if(cobrar.isResultado()){
								
								dni.setText("");
								dni.setEditable(true);
								
								buscar.setEnabled(true);
								buscar_nuevo.setEnabled(false);
								
								nombre.setText("");
								apellido.setText("");
								
								t_cliente.setText("");
								
								cantidad_total=0;
								
								try {
						            									       
									int filas=tabla_factura.getRowCount();
						            
									for (int i = 0;filas>i; i++) {
						            
										modelo.removeRow(0);
						            
									}
						        
								} catch (Exception ex) {
						        
									JOptionPane.showMessageDialog(null, "Error al limpiar la tabla.");
						        }
								
								t_cliente.setText("");
								t_items.setText("");
								t_perchas.setText("");
								total_factura.setText("$ 00.00");
																								
							}
							
													
						} else {
							
							JOptionPane.showMessageDialog(null, "La cantidad de prendas no coincide con las perchas seleccionadas");
							
						}
										
						
					} else {
						
						JOptionPane.showMessageDialog(null, "Verifique la cantidad o las perchas");
					}
												
				} else {
				
				JOptionPane.showMessageDialog(null, "Introduzca los datos del  cliente");
								
				}
			//Si presionamos en entrada hacemos lo siguiente			
			} else if(e.getSource().equals(entrada)){
				//Declaramos un float y pedimos al usuario que ingrese la cantidad
				float ingreso;
				String op=JOptionPane.showInputDialog(VentanaPrincipal.getFrame(), "Ingrese la cantidad", "Entrada de efectivo a caja", JOptionPane.PLAIN_MESSAGE);
				
				try{
					
					ingreso=Float.parseFloat(op);
					//Conectamos con la base de datos
					SessionFactory sesion=HibernateUtil.getSessionFactory();
					Session session=sesion.openSession();
					Transaction tx=session.beginTransaction();
					//Cargamos el cliente nulo y la fecha
					Clientes cliente=(Clientes)session.get(Clientes.class, "nulo");
					java.util.Date fecha=new java.util.Date();
					//Ingresamos datos a la tabla factura
					Factura factura=new Factura(cliente, fecha, fecha,fecha, 0, 0, ingreso, true);
					//Guardamos cambios
					session.save(factura);
															
					tx.commit();
					//Si todo ha ido bien mostramos por pantalla
					JOptionPane.showMessageDialog(VentanaPrincipal.getFrame(), "Cantidad ingresada correctamente");
												
				}catch(NumberFormatException ex){
					
					JOptionPane.showMessageDialog(VentanaPrincipal.getFrame(), "Cantida ingresada no es valida");
					
				} catch(Exception ex){
					
					JOptionPane.showMessageDialog(VentanaPrincipal.getFrame(), "Error, no se pudo completar la operacion");
					
				}
				
			//Si presionamos en salida hacemos lo siguiente	
			} else if(e.getSource().equals(salida)){
				//Al igual que entrada declaramos un float pero lo convertimos a negativo para que reste en la tabla factura
				float ingreso;
				String op=JOptionPane.showInputDialog(VentanaPrincipal.getFrame(), "Ingrese la cantidad", "Salida de efectivo de caja", JOptionPane.PLAIN_MESSAGE);
				
				try{
					
					ingreso=Float.parseFloat(op);
					ingreso=-ingreso;
					
					SessionFactory sesion=HibernateUtil.getSessionFactory();
					Session session=sesion.openSession();
					Transaction tx=session.beginTransaction();
					
					Clientes cliente=(Clientes)session.get(Clientes.class, "nulo");
					java.util.Date fecha=new java.util.Date();
					
					Factura factura=new Factura(cliente, fecha,fecha,fecha, 0, 0, ingreso, true);
					
					session.save(factura);
															
					tx.commit();
					
					JOptionPane.showMessageDialog(VentanaPrincipal.getFrame(), "Cantidad retirada correctamente");
												
				}catch(NumberFormatException ex){
					
					JOptionPane.showMessageDialog(VentanaPrincipal.getFrame(), "Cantida ingresada no es valida");
					
				} catch(Exception ex){
					
					JOptionPane.showMessageDialog(VentanaPrincipal.getFrame(), "Error, no se pudo completar la operacion");
					
				}
				
				
			}
				
		}
		
	}
	//Funcion para agregar servicio
	private void agregarServicio(){
		//Declaramos los JDialog y los diferentes componentes
		JDialog dialog=new JDialog(VentanaPrincipal.getFrame(), "Agregar Servicio", true);
		JTextField texto_codigo=new JTextField();
		JButton buscar_codigo=new JButton("Buscar");
		JTextField texto_nombre=new JTextField();
		String[] columnas_servicio={"Codigo", "Descripcion", "Precio"};
		//Declaramos la tabla y la hacemos que no se puede editar
		JTable tabla_servicio=new JTable(){
			
			public boolean isCellEditable(int rowIndex, int colIndex) {
				 
				return false;
			}
			
		};
		//Declaramos el modelo y la barra de desplazamiento
		DefaultTableModel modelo_servicio = new DefaultTableModel();
	    JScrollPane desplazamiento_servicio = new JScrollPane(tabla_servicio);
	    //Agregamos las columnas
	    modelo_servicio.setColumnIdentifiers(columnas_servicio);
	    //Establecemos el desplazamiento horizontal y vertical
	    desplazamiento_servicio.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        desplazamiento_servicio.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //Hacemos que la tabla no se auto configure las columnas
        tabla_servicio.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabla_servicio.setFillsViewportHeight(true);
        //Agregamos el modelo a la tabla               
        tabla_servicio.setModel(modelo_servicio);
        //Agregamos un mouseListener para que al ahcer doble click en una fila la agregue
        tabla_servicio.addMouseListener(new MouseAdapter(){
        	
        	public void mouseClicked(MouseEvent e){
        		
        		if(e.getClickCount()==2){
        			
        			traspasarTabla(tabla_servicio, dialog);
        			
        		}
        		
        	}
        	        	
        });
        //Establecemos el ancho de las columnas
        TableColumnModel columnModel = tabla_servicio.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(120);
        columnModel.getColumn(1).setPreferredWidth(451);
        columnModel.getColumn(2).setPreferredWidth(120);
        //Hacemos la busqueda de todas las filas de la tabla servicio
        java.util.List<Servicio> servicios=getServices("SELECT p FROM " + Servicio.class.getName() + " p order by p.id");
        //Agregamos todas las filas
        for(Servicio list:servicios){
        	
        	int codigo=list.getId();
        	String desc=list.getDescripcion();
        	float precio=list.getPrecio();
        	
        	modelo_servicio.addRow(new Object[]{codigo, desc, precio});
        	        	
        }
        //establecemos las opciones del JDialog        
		dialog.setLayout(new BorderLayout());
		dialog.setBounds(600, 200, 700, 600);
		dialog.setResizable(false);
		dialog.setBackground(Color.WHITE);
		//Agregamos el campo de texto para buscar por codigoy le agregamos un key listener para que vaya buscando mientras vayamos escribiendo		
		texto_codigo.setPreferredSize(new Dimension(500,30));
		texto_codigo.addKeyListener(new KeyAdapter(){
			
			public void keyPressed(KeyEvent e){
				//Si presionamos enter llamamos a la funcion actualizarTabla
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
				
				actualizarTablaCodigo(tabla_servicio, modelo_servicio, texto_codigo);
				texto_codigo.setText("");
				
				}
			}
					
		});
		//Agregamos el boton buscar
		buscar_codigo.setPreferredSize(new Dimension(160,30));
		//Tambien le damos funcionalidad
		buscar_codigo.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent arg0) {
								
				actualizarTablaCodigo(tabla_servicio, modelo_servicio, texto_codigo);
				texto_codigo.setText("");
				
			}	
		
		});
		//Agregamos el texto de busque por nombre y le agregamos un key listener para que vaya actualizando la tabla mientras vayamos escribiendo
		texto_nombre.setPreferredSize(new Dimension(650,30));
		texto_nombre.addKeyListener(new KeyAdapter(){
			
			public void keyReleased(KeyEvent e) {
				//borramos todos los datos de la tabla								
				for (int i = 0; i < tabla_servicio.getRowCount(); i++) {
			    
					   modelo_servicio.removeRow(i);
			           i-=1;
			   
				}
				//Hacemos una busqueda mientras se va escribiendo y agregamos el resultado
				java.util.List<Servicio> servicios=getServices("SELECT p FROM " + Servicio.class.getName() + " p where p.descripcion like '%" + texto_nombre.getText() + "%'");
			        
			    for(Servicio list:servicios){
			        	
			    int codigo=list.getId();
			    String desc=list.getDescripcion();
			    float precio=list.getPrecio();
			        	
			    modelo_servicio.addRow(new Object[]{codigo, desc, precio});
			        	        	
			    }
				
			        
			}
		
		});
		//Declaramos un JTabbedPane para tener las dos pestañas de busqueda, por codigo y por nombre
		JTabbedPane pestañas=new JTabbedPane();
		//Declaramos los tres paneles, el de codigo, el de nombre y el sur que es comun a los dos
		JPanel panel_codigo=new JPanel();
		JPanel panel_nombre=new JPanel();
		JPanel panel_sur=new JPanel();
		//definimos como van a ser el panel de codigo y le agregamos el campo de texto y el boton de buscar
		panel_codigo.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel_codigo.add(texto_codigo);
		panel_codigo.add(buscar_codigo);
		//En el panel de nombre agregamos solo el campo de texto
		panel_nombre.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel_nombre.add(texto_nombre);
				
		panel_codigo.setBackground(Color.WHITE);
		panel_nombre.setBackground(Color.WHITE);
		panel_sur.setBackground(Color.WHITE);
			
		panel_sur.setLayout(new FlowLayout(FlowLayout.RIGHT));
		//Agreamos los dos paneles a la pestañas		
		pestañas.addTab("Busqueda por codigo", panel_codigo);
		pestañas.addTab("Busqueda por nombre", panel_nombre);
		//Agregamos el boton de agregar al panel sur y le damos funcionalidad	
		addServiceAcept=new JButton("Agregar");
		addServiceAcept.setPreferredSize(new Dimension(140,35));
		addServiceAcept.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
			
				traspasarTabla(tabla_servicio, dialog);
										
				}
									
		});
		
		panel_sur.add(addServiceAcept);
		//Agregamos las pestañas, la barra de desplazamiento y el panel sur al JDialog
		dialog.add(pestañas, BorderLayout.NORTH);
		dialog.add(desplazamiento_servicio, BorderLayout.CENTER);
		dialog.add(panel_sur, BorderLayout.SOUTH);
								
		dialog.setVisible(true);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		
				
	}
	//Funcion que devuelve un List para hacer las busquedas
	private java.util.List<Servicio> getServices(String consulta){
		
		SessionFactory sesion=HibernateUtil.getSessionFactory();
		Session session=sesion.openSession();
		Transaction tx=session.beginTransaction();
		
		Query query = session.createQuery(consulta);
		
		java.util.List<Servicio> servicios=query.list();
		
		tx.commit();
				
		return servicios;
				
	}
	//Funcion que actualiza la tabla por la busqueda por codigo
	private void actualizarTablaCodigo(JTable tabla_servicio, DefaultTableModel modelo_servicio, JTextField texto_codigo){
		
		java.util.List<Servicio> servicios;
		//borramos todas las filas de la tabla
		for (int i = 0; i < tabla_servicio.getRowCount(); i++) {
		    
			   modelo_servicio.removeRow(i);
	           i-=1;
	   
		}
		//Si el campo de texto no esta vacio hacemos la busqueda
		if(texto_codigo.getText().length()>0){
			
			servicios=getServices("SELECT p FROM " + Servicio.class.getName() + " p where p.id=" + texto_codigo.getText());
		//Si esta vacio la volvemos a llenar con todos los datos
		} else {
		
			servicios=getServices("SELECT p FROM " + Servicio.class.getName() + " p");
		
		}
		//Agregamos las filas 
		for(Servicio list:servicios){
	        	
		    int codigo=list.getId();
		    String desc=list.getDescripcion();
		    float precio=list.getPrecio();
		        	
		    modelo_servicio.addRow(new Object[]{codigo, desc, precio});
	        	        	
	    }
		
	        
	}
	//Funcion encargada de traspasar las filas a la tabla de factura
	private void traspasarTabla(JTable tabla_servicio, JDialog dialog){
		//Seleccionamos la fila elegido en servicio
		int row=tabla_servicio.getSelectedRow();
		//Si no hay ninguna fila mostramos el error		
		if(row==-1){
			
			JOptionPane.showMessageDialog(dialog, "No hay ningun servicio seleccionado");
		//Sino,capturamos los valores de la tabla de servicio	
		} else {
			
			String aux_id=tabla_servicio.getValueAt(row, 0).toString();
			String aux_desc=tabla_servicio.getValueAt(row, 1).toString();
			String aux_precio=tabla_servicio.getValueAt(row, 2).toString();
						
			try{
				//Pedimos al usuario la cantidad a agregar
				String aux_cantidad=(String)JOptionPane.showInputDialog(null, "Introduzca la cantidad","1");
				int cantidad=Integer.parseInt(aux_cantidad);
				//Si la cantidad introducida es menor que 1 mostramos el error
				if(cantidad<1){
					
					JOptionPane.showMessageDialog(dialog, "Cantidad no es correcta");
				//Si es correcta agregamos la fila y modificamos la cantidad total	
				} else {
															
					boolean isExist = false;
					
					for(int i=0; i<tabla_factura.getRowCount(); i++) {
						
						if(Integer.parseInt(aux_id)==((Integer)modelo.getValueAt(i, 0))) {
							
							int cantidadSecundario=(Integer)modelo.getValueAt(i, 3);
							cantidad_total+=cantidad;
							cantidad+=cantidadSecundario;
							modelo.setValueAt(cantidad, i, 3);
							modelo.setValueAt(cantidad*Float.parseFloat(aux_precio), i, 4);
							isExist=true;
							
						}
											
					}
					
					if(!isExist) {
						
						modelo.addRow(new Object[]{Integer.parseInt(aux_id), aux_desc, Float.parseFloat(aux_precio), cantidad, cantidad*Float.parseFloat(aux_precio)});
						cantidad_total+=cantidad;
						isExist=false;
					
					}
					
					t_items.setText("Cantidad total: " + cantidad_total);
					setTotalFactura();
				}
			//Captura de los diferentes errores
			} catch (NumberFormatException e){
				
				JOptionPane.showMessageDialog(dialog, "Error en la cantidad");
				
			} catch (Exception e){
				
				JOptionPane.showMessageDialog(dialog, "Error inesperado");
				e.printStackTrace();
			
			}
										
		}
				
	}
	//Funcion para modificar el total de la factura
	private void setTotalFactura(){
		//Al iniciar es 0, verificamos cuantas filas tiene la tabla, definimos el modelo de decimal
		total=0;
		int filas=modelo.getRowCount();
		DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
	    simbolo.setDecimalSeparator('.');
	    simbolo.setGroupingSeparator(',');
		DecimalFormat decimal=new DecimalFormat("###,###.##", simbolo);
		//Recorremos las filas y capturamos la columa de precio y vamos sumando fila a fila
		for(int i=0;i<filas;i++){
			
			String aux=tabla_factura.getValueAt(i, 4).toString();
			double aux_monto=Double.parseDouble(aux);
			total+=aux_monto;
			
		}
		//Por ultimo modificamos el total				
		total_factura.setText("$ " + decimal.format(total));
			
	}
	//Funcion para eliminar un servicio
	private void eliminarServicio(){
		//Capturamos la fila seleccionada
		int row=tabla_factura.getSelectedRow();
		//Si no hay filas seleccionadas mostramos el error
		if(row==-1){
			
			JOptionPane.showMessageDialog(null, "No hay ningun servicio seleccionado");
		//Si hay, capturamos la cantidad y la restamos de la cantidad total y llamamos a la funcion setTotalFactura para que actualize el precio	
		} else {
			
			String cantidadstr=tabla_factura.getValueAt(row, 3).toString();
			int cantidad_restar=Integer.parseInt(cantidadstr);
			//Confirmamos que el usuario quiere borrar el servicio
			int op=JOptionPane.showConfirmDialog(null, "¿Seguro que quiere eliminar este servicio?", "Eliminar", JOptionPane.OK_CANCEL_OPTION);
		
			if(op==JOptionPane.YES_OPTION){		
			
				modelo.removeRow(row);
				cantidad_total-=cantidad_restar;
				t_items.setText("Cantidad total: " + cantidad_total);
				setTotalFactura();
			
			}				
		}
				
	}
	//Funcion estatica que devuelve la tabla de factura
	public static JTable getDatosTabla(){
		
		return tabla_factura;
		
		
	}
		
}
	

