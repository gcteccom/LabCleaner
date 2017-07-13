package Vista;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;



import Controlador.ConexionBBDD;

//Clase que se encarga de toda la gestion del modulo de Clientes
public class PanelCentralCliente extends JPanel{

	//Declaracion de las variables
	private static final long serialVersionUID = 1L;
	private JTextField t_dni, t_nombre, t_apellido, t_fecha, t_telefono, t_direccion, t_poblacion, t_provincia, t_email, t_movil;
	private JButton aceptarNuevo, modificar, eliminar, buscar, volver;
	private ResultSet rs;
	private Font font;
	private SimpleDateFormat formato;
	private JTable tabla = null;
    DefaultTableModel modelo = null;
    JScrollPane desplazamiento = null;
    ConexionBBDD bd;
    Connection conect;
	
    //Constructor de la clase del panel de Cliente
	public PanelCentralCliente(JButton boton){
		//Establecemos los valores del panel, fuentes y formato de fecha
		this.setLayout(null);
		this.setBackground(Color.WHITE);
		font=new Font("Serif", Font.BOLD, 20);
		formato=new SimpleDateFormat("dd/MM/yyyy");
				
		//Comprobamos que boton fue el pulsado
		if(boton.equals(PanelControles.nuevo)){
			
			//Estableces el borde y activamos y desactivamos los botones correspondiente
			this.setBorder(new TitledBorder("Agregar nuevo Cliente"));
			PanelControles.nuevo.setEnabled(false);
			PanelControles.modificar.setEnabled(true);
			PanelControles.eliminar.setEnabled(true);
			PanelControles.consultar.setEnabled(true);
			//Agregamos los label al panel con la funcion agregarLabel
			agregarLabel("NIF: ", 20,40,100,30);
			agregarLabel("Nombre: ", 20,90,100,30);
			agregarLabel("Apellidos: ", 20,140,100,30);
			agregarLabel("Fecha de Nac.: ", 20,190,130,30);
			agregarLabel("Direccion: ",20,240,100,30);
			agregarLabel("Poblacion: ",20,290,100,30);
			agregarLabel("Provincia: ",20,340,100,30);
			agregarLabel("Tel. fijo: ",20,390,100,30);
			agregarLabel("Movil: ",20,440,100,30);
			agregarLabel("Email: ",20,490,100,30);
			
			//Agregamos los campos de texto con la funcion agregarTexto
			t_dni=agregarTextoCliente(150,42,200,30);
			t_nombre=agregarTextoCliente(150,92,200,30);
			t_apellido=agregarTextoCliente(150,142,200,30);
			t_fecha=agregarTextoCliente(190,192,160,30);
			t_direccion=agregarTextoCliente(150,242,200,30);
			t_poblacion=agregarTextoCliente(150,292,200,30);
			t_provincia=agregarTextoCliente(150,342,200,30);
			t_telefono=agregarTextoCliente(150,392,200,30);
			t_movil=agregarTextoCliente(150,442,200,30);
			t_email=agregarTextoCliente(150,492,200,30);
			//Agregamos el boton del panel nuevo cliente y le damos funcionalidad
			aceptarNuevo=new JButton("Agregar");
			aceptarNuevo.setBounds(100, 550, 120, 60);
			aceptarNuevo.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent arg0) {
					
					agregarCliente();
				}
				
				
			});
			add(aceptarNuevo);

		//Aqui comprobamos que sea el boton modificar el pulsado
		} else if(boton.equals(PanelControles.modificar)){
			//Establecemos los datos y activamos y desactivamos los botones correspondientes
			this.setBorder(new TitledBorder("Modificar datos del Cliente"));
			PanelControles.nuevo.setEnabled(true);
			PanelControles.modificar.setEnabled(false);
			PanelControles.eliminar.setEnabled(true);
			PanelControles.consultar.setEnabled(true);
			//Agregamos los label
			agregarLabel("NIF: ", 20,40,100,30);
			agregarLabel("Nombre: ", 20,90,100,30);
			agregarLabel("Apellidos: ", 20,140,100,30);
			agregarLabel("Fecha de Nac.: ", 20,190,130,30);
			agregarLabel("Direccion: ",20,240,100,30);
			agregarLabel("Poblacion: ",20,290,100,30);
			agregarLabel("Provincia: ",20,340,100,30);
			agregarLabel("Tel. fijo: ",20,390,100,30);
			agregarLabel("Movil: ",20,440,100,30);
			agregarLabel("Email: ",20,490,100,30);
			
			//Agregamos los campos de textos
			t_dni=agregarTextoModificar(150,42,200,30);
			t_nombre=agregarTextoModificar(150,92,200,30);
			t_apellido=agregarTextoModificar(150,142,200,30);
			t_fecha=agregarTextoModificar(190,192,160,30);
			t_direccion=agregarTextoModificar(150,242,200,30);
			t_poblacion=agregarTextoModificar(150,292,200,30);
			t_provincia=agregarTextoModificar(150,342,200,30);
			t_telefono=agregarTextoModificar(150,392,200,30);
			t_movil=agregarTextoModificar(150,442,200,30);
			t_email=agregarTextoModificar(150,492,200,30);
			//Llamamos a la funcion setFalse que establece los campos de texto no sean editables
			setFalse();
			//Agregamos evento para cuando presionemos la tecla enter nos haga la misma accion que el boton
			t_dni.addKeyListener(new KeyAdapter(){
				
				public void keyPressed(KeyEvent e){
					
					if(e.getKeyCode()==KeyEvent.VK_ENTER){
					
						buscarCliente(modificar);
					
					}
					
				}
				
			});
			
			
			//Agregar el boton de buscar y le damos la funcionalidad			
			buscar=new JButton("Buscar");
			buscar.setBounds(400, 42, 100, 30);
			buscar.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					
						buscarCliente(modificar);
									
				}
								
			});
			//Agregamos el boton buscar al panel
			add(buscar);
				
			//Agregamos el boton modificar, establecemos los valores, le damos funcionalidad y lo añadimos al panel			
			modificar=new JButton("Modificar");
			modificar.setBounds(100, 550, 120, 60);
			modificar.setEnabled(false);
			modificar.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent arg0) {
					
					modificarCliente();
				
				}
				
				
			});
			add(modificar);
		//Comprobamos que pulsemos el boton eliminar				
		} else if(boton.equals(PanelControles.eliminar)){
			//Establecemos bordes y activamos y desactivamos los botones correspondientes
			this.setBorder(new TitledBorder("Eliminar Cliente"));
			PanelControles.nuevo.setEnabled(true);
			PanelControles.modificar.setEnabled(true);
			PanelControles.eliminar.setEnabled(false);
			PanelControles.consultar.setEnabled(true);
			//Agregamos los label
			agregarLabel("NIF: ", 20,40,100,30);
			agregarLabel("Nombre: ", 20,90,100,30);
			agregarLabel("Apellidos: ", 20,140,100,30);
			agregarLabel("Fecha de Nac.: ", 20,190,130,30);
			agregarLabel("Direccion: ",20,240,100,30);
			agregarLabel("Poblacion: ",20,290,100,30);
			agregarLabel("Provincia: ",20,340,100,30);
			agregarLabel("Tel. fijo: ",20,390,100,30);
			agregarLabel("Movil: ",20,440,100,30);
			agregarLabel("Email: ",20,490,100,30);
			
			//Agregamos los campos de textos
			t_dni=agregarTextoEliminar(150,42,200,30);
			t_nombre=agregarTextoEliminar(150,92,200,30);
			t_apellido=agregarTextoEliminar(150,142,200,30);
			t_fecha=agregarTextoEliminar(190,192,160,30);
			t_direccion=agregarTextoEliminar(150,242,200,30);
			t_poblacion=agregarTextoEliminar(150,292,200,30);
			t_provincia=agregarTextoEliminar(150,342,200,30);
			t_telefono=agregarTextoEliminar(150,392,200,30);
			t_movil=agregarTextoEliminar(150,442,200,30);
			t_email=agregarTextoEliminar(150,492,200,30);
			//Llamamos a la funcion setFalse para desactivar los campos de textos
			setFalse();
			//Agregamos evento para cuando presionemos la tecla enter nos haga la misma accion que el boton
			t_dni.addKeyListener(new KeyAdapter(){
				
				public void keyPressed(KeyEvent e){
					
					if(e.getKeyCode()==KeyEvent.VK_ENTER){
					
						buscarCliente(eliminar);
					
					}
					
				}
				
			});
			
			//Agregamos el boton buscar y le damos funcionalidad
			buscar=new JButton("Buscar");
			buscar.setBounds(400, 42, 100, 30);
			buscar.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
									
						buscarCliente(eliminar);
											
				}
								
			});
			//Agregamos el boton al panel
			add(buscar);
			//Agregamos el boton eliminar, establecemos los valores y le damos funcionalidad al presionarlo y con el teclado
			eliminar=new JButton("Eliminar");
			eliminar.setBounds(50, 550, 140, 60);
			eliminar.setEnabled(false);
			eliminar.addKeyListener(new KeyAdapter(){
				
				public void keyPressed(KeyEvent e){
					
					int op=JOptionPane.showConfirmDialog(null, "¿Seguro que quiere eliminar este cliente?","Eliminar", JOptionPane.OK_OPTION);
					
					if(op==JOptionPane.OK_OPTION){
					
						eliminarCliente();
				
					}
					
				}
				
				
			});
			eliminar.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent arg0) {
					
					int op=JOptionPane.showConfirmDialog(null, "¿Seguro que quiere eliminar este cliente?","Eliminar", JOptionPane.OK_OPTION);
					
					if(op==JOptionPane.OK_OPTION){
					
						eliminarCliente();
				
					}
				}
				
				
			});
			//Agregamos el boton al panel
			add(eliminar);
			//Agregamos el boton volver, le damos los valores y le damos funcionalidad
			volver=new JButton("Nueva busqueda");
			volver.setBounds(200, 550, 140, 60);
			volver.setEnabled(false);
			volver.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent arg0) {
					
					limpiarTexto();
					setFalse();
					eliminar.setEnabled(false);
					volver.setEnabled(false);
					t_dni.setEnabled(true);
					buscar.setEnabled(true);
					
				
				}
				
				
			});
			//Agregamos el boton al panel
			add(volver);
		
		//Comprobamos que se haya pulsado el boton consultar
		} else if(boton.equals(PanelControles.consultar)){
			//Establecemos valores y axtivamos y desactivamos los botones correspondientes
			this.setBorder(new TitledBorder("Consultar base de datos de los Clientes"));
			this.setLayout(new BorderLayout());
			PanelControles.nuevo.setEnabled(true);
			PanelControles.modificar.setEnabled(true);
			PanelControles.eliminar.setEnabled(true);
			PanelControles.consultar.setEnabled(false);
			//Declaramos un array de String que corresponden a las columnas de la tabla
			String[] columnas={"ID","Nombres", "Apellidos", "Fecha de Nacimiento", "Telefono", "Movil", "Direccion", "Poblacion", "Provincia", "Email"};			
			String id,nombre,apellido,fecha,telefono,movil,direccion,poblacion,provincia,email;
			//Iniciamos la tabla, establecemos el modelo y la barra de desplazamiento
			tabla = new JTable();
		    modelo = new DefaultTableModel();
		    desplazamiento = new JScrollPane(tabla);
		    //Agregamos el array a las columnas		    
		    modelo.setColumnIdentifiers(columnas);
		    //Estableces que si es necesario se desplaze horizontal y verticalmene
		    desplazamiento.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	        desplazamiento.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	        //Adaptamos las columnas con los valores
	        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
	        tabla.setFillsViewportHeight(true);        
	        //Agregamos el modelo a la tabla
	        tabla.setModel(modelo);
	        //Agregamos la tabla al panel
	        add(desplazamiento, BorderLayout.CENTER);
	        //Realizamos la consulta	        
	        rs=consultarClientes();
	        
	        try{
			   	//Recorremos el resltSet y lo vamos guardando en las variables   
		       	while(rs.next()){
			        	
		       	id=rs.getString("dni");
		       	nombre=rs.getString("nombre");
		       	apellido=rs.getString("apellido");
		       	fecha="" + rs.getDate("fecha");
		       	telefono=rs.getString("telefono");
		       	movil=rs.getString("movil");
		       	direccion=rs.getString("direccion");
		       	poblacion=rs.getString("poblacion");
		       	provincia=rs.getString("provincia");
		       	email=rs.getString("email");
		        //Agregamos los valores a la tabla		
		       	modelo.addRow(new Object[]{id,nombre,apellido,fecha,telefono,movil,direccion,poblacion,provincia,email});
			        
		       	}
		       	//Cerramos conexion	
		       	bd.cerrarConexion();
		       	conect.close();
		        	
		       	
		        
		    } catch(Exception e){
		        	
		       	e.printStackTrace();
		        	
		    }
	        						
		}
		
	}
	
	//Funcion para agregar los label al panel
	public void agregarLabel(String nombre, int x, int y, int ancho, int alto){
		
		JLabel label=new JLabel(nombre);
		label.setBounds(x, y, ancho, alto);
		label.setFont(font);
		this.add(label);
								
	}
	//Funcion para agregar los campos de textos de agregar nuevos clientes
	public JTextField agregarTextoCliente(int x, int y, int ancho, int alto){
		
		JTextField txt=new JTextField();
		txt.setBounds(x, y, ancho, alto);
		txt.addKeyListener(new KeyAdapter(){
			
			public void keyPressed(KeyEvent e){
				
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					
					agregarCliente();
					
				}
				
			}
			
		});
		this.add(txt);
		return txt;
		
		
	}
	//Funcion para agregar los campos de textos al panel de modificar
	public JTextField agregarTextoModificar(int x, int y, int ancho, int alto){
		
		JTextField txt=new JTextField();
		txt.setBounds(x, y, ancho, alto);
		txt.addKeyListener(new KeyAdapter(){
			
			public void keyPressed(KeyEvent e){
				//Comprobamos si esta activada la tecla para que haga la accion
				if(modificar.isEnabled()){
					
					if(e.getKeyCode()==KeyEvent.VK_ENTER){
					
						modificarCliente();
					
					}
				}
								
			}
					
		});
		this.add(txt);
		return txt;
		
		
	} 
	
	//Funcion para agregar los campos de textos al panel de modificar
	public JTextField agregarTextoEliminar(int x, int y, int ancho, int alto){
		
		JTextField txt=new JTextField();
		txt.setBounds(x, y, ancho, alto);
		this.add(txt);
		return txt;
		
		
	} 
	//Funcion para agregar nuevos clientes, es llamada desde el boton agregar
	private void agregarCliente(){
		
		//Comprobamos que los campos dni, nombre, apellido y fecha no esten vacio
		if(t_dni.getText().length()==0 || t_nombre.getText().length()==0 || t_apellido.getText().length()==0 || t_fecha.getText().length()==0){
			
			JOptionPane.showMessageDialog(null, "Los campos Nif, Nombres, Apellidos y Fecha de Nac. son obligatorios");
						
		} else {
			//Declaramos la consulta	
			String consulta="insert into clientes(dni, nombre,apellido,fecha, telefono,movil,direccion,poblacion,provincia,email) values(?,?,?,?,?,?,?,?,?,?)";
			
			
			try{
				//Convertimos la fecha para la tabla de mysql
				java.util.Date utilDate = formato.parse(t_fecha.getText());
			    Date date = new Date(utilDate.getTime());
				//Establecemos conexion con la base de datos			
				bd=new ConexionBBDD();
				conect=bd.getConexion();
				//Preparamos la consulta
				PreparedStatement st=conect.prepareStatement(consulta);
				st.setString(1, t_dni.getText());
				st.setString(2, t_nombre.getText());
				st.setString(3, t_apellido.getText());
				st.setDate(4, date);
				st.setString(5, t_telefono.getText());
				st.setString(6, t_movil.getText());
				st.setString(7, t_direccion.getText());
				st.setString(8, t_poblacion.getText());
				st.setString(9, t_provincia.getText());
				st.setString(10, t_email.getText());
				//Ejecutamos y mostramos por pantalla
				int row=st.executeUpdate();
				
				JOptionPane.showMessageDialog(null, "Se ha insertado correctamente " + row + " cliente");
				//Cerramos conexion
				bd.cerrarConexion();
				conect.close();
				//Limpiamos los campos de textos
				limpiarTexto();
				
				
			//Capturamos posibles errores y mostramos por pantalla
			}catch(ParseException e){
				
				JOptionPane.showMessageDialog(null, "Formato de fecha incorrecto, formato valido dd/mm/aaaa");
				
			}catch(Exception e){
				
				JOptionPane.showMessageDialog(null, "Error al crear cliente");
				
			}
		
		}
		
	}
	
	//Funcion para buscar el cliente
	private void buscarCliente(JButton boton){
		
		//Comprobamos que tenga texto el campo dni
		if(t_dni.getText().length()==0){
			
			JOptionPane.showMessageDialog(null, "El NIF esta vacio");
									
									
		}else {
		
			//Creamos la consulta
			String consulta="select * from clientes where dni='" + t_dni.getText() + "'";
				
			try {
				//Establecemos conexion con la base de datos	
				bd=new ConexionBBDD();
				conect=bd.getConexion();
				Statement st=conect.createStatement();
				//Ejecutamos la consulta
				rs=st.executeQuery(consulta);
				//Verificamos que tenga valor sino el cliente no existe
				rs.next();
				if(rs.getRow()==0){
			
					JOptionPane.showMessageDialog(null, "Ese cliente no existe");
					t_dni.setText("");
											
			
				} else {
				
					if(boton.equals(modificar)){
						
						//Activamos y desactivamos los correspondientes campos y botones
						t_dni.setEnabled(false);
						t_nombre.setEnabled(true);
						t_apellido.setEnabled(true);
						t_fecha.setEnabled(true);
						t_direccion.setEnabled(true);
						t_poblacion.setEnabled(true);
						t_provincia.setEnabled(true);
						t_telefono.setEnabled(true);
						t_movil.setEnabled(true);
						t_email.setEnabled(true);
						modificar.setEnabled(true);
						buscar.setEnabled(false);
						
						//agregamos los valores del resultSet a los campos de textos
						t_nombre.setText(rs.getString("nombre"));
						t_apellido.setText(rs.getString("apellido"));
						t_telefono.setText(rs.getString("telefono"));
						t_fecha.setText("" + rs.getDate("fecha"));
						t_movil.setText(rs.getString("movil"));
						t_direccion.setText(rs.getString("direccion"));
						t_poblacion.setText(rs.getString("poblacion"));
						t_provincia.setText(rs.getString("provincia"));
						t_email.setText(rs.getString("email"));
																	
						
					}else if(boton.equals(eliminar)){
														
						//Establecemos los datos a los campos de textos y activamos y desactivamos los botones correspondientes
						eliminar.setEnabled(true);
						volver.setEnabled(true);
						t_dni.setEnabled(false);
						buscar.setEnabled(false);
						t_nombre.setText(rs.getString("nombre"));
						t_apellido.setText(rs.getString("apellido"));
						t_fecha.setText("" + rs.getDate("fecha"));
						t_telefono.setText(rs.getString("telefono"));
						t_movil.setText(rs.getString("movil"));
						t_direccion.setText(rs.getString("direccion"));
						t_poblacion.setText(rs.getString("poblacion"));
						t_provincia.setText(rs.getString("provincia"));
						t_email.setText(rs.getString("email"));
						
					}
					
				}
											
			} catch(Exception e){
										
					e.printStackTrace();
			
			} finally {
				
				bd.cerrarConexion();
				
				try {
				
					conect.close();
				
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
				
			}
			
			
			
		}
					
	}
	//Funcion para modificar datos del cliente
	private void modificarCliente(){
		
		try {
			//Creamos la consulta
			String consulta="update clientes set nombre=?,apellido=?,fecha=?, telefono=?,movil=?,direccion=?,poblacion=?,provincia=?,email=? where dni=?";
			java.util.Date utilDate = formato.parse(t_fecha.getText());
		    Date date = new Date(utilDate.getTime());
			//Establecemos conexion con la base de datos
			bd=new ConexionBBDD();
			conect=bd.getConexion();
			//Preparamos la consulta
			PreparedStatement st=conect.prepareStatement(consulta);
			st.setString(1, t_nombre.getText());
			st.setString(2, t_apellido.getText());
			st.setDate(3, date);
			st.setString(4, t_telefono.getText());
			st.setString(5, t_movil.getText());
			st.setString(6, t_direccion.getText());
			st.setString(7, t_poblacion.getText());
			st.setString(8, t_provincia.getText());
			st.setString(9, t_email.getText());
			st.setString(10, t_dni.getText());
			//Ejecutamos y mostramos por pantalla
			int row=st.executeUpdate();
			
			JOptionPane.showMessageDialog(null, "Se ha modificado correctamente " + row + " cliente");
			//Cerramos conexion
			bd.cerrarConexion();
			conect.close();
			//Limpiamos los campos de textos
			limpiarTexto();
			//Activamos nuevamente los botones correspondientes
			modificar.setEnabled(false);
			buscar.setEnabled(true);
			t_dni.setEnabled(true);
			//Desactivamos los campos de textos
			setFalse();
							
				
		//Capturamos posibles errores y mostramos por pantalla	
		} catch (SQLException e) {
				
			JOptionPane.showMessageDialog(null, "Error al modificar al cliente");
			
		} catch (ParseException e) {
			
			JOptionPane.showMessageDialog(null, "Formato de fecha incorrecto, formato valido dd/mm/aaaa");
		} 
		
	}
	//Funcion para eliminar un cliente
	private void eliminarCliente(){
		
		try {
			//Creamos la consulta
			String consulta="delete from clientes where dni=?";
			//Establecemos conexion con la base de datos
			bd=new ConexionBBDD();
			conect=bd.getConexion();
			//Preparamos la consulta
			PreparedStatement st=conect.prepareStatement(consulta);
			st.setString(1, t_dni.getText());
			//Ejecutamos y mostramos por pantalla			
			int row=st.executeUpdate();
			
			JOptionPane.showMessageDialog(null, "Se ha eliminado correctamente " + row + " cliente");
			//Cerramos conexion
			bd.cerrarConexion();
			conect.close();
			//Limpiamos los campos de textos
			limpiarTexto();
			//Activamos y desactivamos los botones y campos correspondientes		
			eliminar.setEnabled(false);
			volver.setEnabled(false);
			buscar.setEnabled(true);
			t_dni.setEnabled(true);
					
		} catch (SQLException e) {
				
			e.printStackTrace();
			
		} 
		
	}
	//Funcion para limpiar los campos de textos
	private void limpiarTexto(){
		
		t_dni.setText("");
		t_nombre.setText("");
		t_apellido.setText("");
		t_fecha.setText("");
		t_direccion.setText("");
		t_poblacion.setText("");
		t_provincia.setText("");
		t_telefono.setText("");
		t_movil.setText("");
		t_email.setText("");
			
	}
	//Funcion que desactiva los campos de textos
	private void setFalse(){
		
		t_nombre.setEnabled(false);
		t_apellido.setEnabled(false);
		t_fecha.setEnabled(false);
		t_direccion.setEnabled(false);
		t_poblacion.setEnabled(false);
		t_provincia.setEnabled(false);
		t_telefono.setEnabled(false);
		t_movil.setEnabled(false);
		t_email.setEnabled(false);
			
	}
	//Funcion que devuelve un resultSet con todos los clientes de la base de datos
	private ResultSet consultarClientes(){
		
		String consulta="select * from clientes order by nombre";
			
		try {
				
			bd=new ConexionBBDD();
			conect=bd.getConexion();
			Statement st=conect.createStatement();
			rs=st.executeQuery(consulta);
					
			return rs;				
										
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		}
		
		return null;
			
	}
		
		
}
	

