package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import Controlador.ConexionBBDD;


public class PanelCentralServicio extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private Font font;
	private JTextField descripcion, precio, codigo;
	private JButton aceptarNuevo, buscar, modificar, eliminar,buscar_nuevo;
	ConexionBBDD bd;
    Connection conect;
    private ResultSet rs;
    private JTable tabla = null;
    DefaultTableModel modelo = null;
    JScrollPane desplazamiento = null;
	

	public PanelCentralServicio(JButton boton, int acceso){
		
		this.setLayout(null);
		this.setBackground(Color.WHITE);
		font=new Font("Serif", Font.BOLD, 20);
		
		if(boton.equals(PanelControles.nuevo)){
			
			this.setBorder(new TitledBorder("Agregar nuevo Servicio"));
			PanelControles.nuevo.setEnabled(false);
			
			if(acceso==1){
			
				PanelControles.modificar.setEnabled(true);
				PanelControles.eliminar.setEnabled(true);
			
			}
			
			PanelControles.consultar.setEnabled(true);
			
			agregarLabel("Codigo: ", 20,40,100,30);
			agregarLabel("Descripcion: ", 20,90,120,30);
			agregarLabel("Precio: ", 20,140,100,30);
			
			codigo=agregarTextoServicioNuevo(150,42,200,30);
			descripcion=agregarTextoServicioNuevo(150,92,400,30);
			precio=agregarTextoServicioNuevo(150,142,200,30);
			
			aceptarNuevo=new JButton("Agregar");
			aceptarNuevo.setBounds(120, 190, 120, 60);
			aceptarNuevo.addActionListener(new ActionListener(){
				
				public void actionPerformed(ActionEvent arg0) {
					
					agregarServicio();
					
				}
				
			});
			add(aceptarNuevo);
			
			
				
		} else if(boton.equals(PanelControles.modificar)){
			
			this.setBorder(new TitledBorder("Modificar un Servicio"));
			PanelControles.nuevo.setEnabled(true);
			
			if(acceso==1){
				
				PanelControles.modificar.setEnabled(false);
				PanelControles.eliminar.setEnabled(true);
			
			}
			
			PanelControles.consultar.setEnabled(true);
			
			agregarLabel("Codigo: ", 20,40,100,30);
			agregarLabel("Descripcion: ", 20,90,120,30);
			agregarLabel("Precio: ", 20,140,100,30);
			
			codigo=agregarTextoModificar(150,42,200,30);
			descripcion=agregarTextoModificar(150,92,400,30);
			precio=agregarTextoModificar(150,142,200,30);
			
			descripcion.setEnabled(false);
			precio.setEnabled(false);
			
			codigo.addKeyListener(new KeyAdapter(){
				
				public void keyPressed(KeyEvent e){
					
					if(e.getKeyCode()==KeyEvent.VK_ENTER){
					
						buscarServicio(modificar);
					
					}
				}
								
			});
			
			buscar=new JButton("Buscar");
			buscar.setBounds(400, 42, 100, 30);
			buscar.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
									
						buscarServicio(modificar);
											
				}
								
			});
			
			add(buscar);
			
			modificar=new JButton("Modificar");
			modificar.setBounds(120, 190, 120, 60);
			modificar.setEnabled(false);
			modificar.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent arg0) {
				
					modificarServicio();
				}
								
			});
			
			add(modificar);
			
			
			
						
		} else if(boton.equals(PanelControles.eliminar)){
			
			this.setBorder(new TitledBorder("Eliminar un Servicio"));
			PanelControles.nuevo.setEnabled(true);
			
			if(acceso==1){
				
				PanelControles.modificar.setEnabled(true);
				PanelControles.eliminar.setEnabled(false);
			
			}
			PanelControles.consultar.setEnabled(true);
			
			agregarLabel("Codigo: ", 20,40,100,30);
			agregarLabel("Descripcion: ", 20,90,120,30);
			agregarLabel("Precio: ", 20,140,100,30);
			
			codigo=agregarTextoEliminar(150,42,200,30);
			descripcion=agregarTextoEliminar(150,92,400,30);
			precio=agregarTextoEliminar(150,142,200,30);
			
			descripcion.setEnabled(false);
			precio.setEnabled(false);
			
			codigo.addKeyListener(new KeyAdapter(){
				
				public void keyPressed(KeyEvent e){
					
					if(e.getKeyCode()==KeyEvent.VK_ENTER){
					
						buscarServicio(eliminar);
					
					}
				}
								
			});
			
			buscar=new JButton("Buscar");
			buscar.setBounds(400, 42, 100, 30);
			buscar.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
									
						buscarServicio(eliminar);
											
				}
								
			});
			
			add(buscar);
			
			eliminar=new JButton("Eliminar");
			eliminar.setBounds(120, 190, 120, 60);
			eliminar.setEnabled(false);
			eliminar.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent arg0) {
				
					int op=JOptionPane.showConfirmDialog(null, "¿Seguro que quiere eliminar este servicio?","Eliminar", JOptionPane.OK_OPTION);
					
					if(op==JOptionPane.OK_OPTION){
					
						eliminarServicio();
				
					}
				}
								
			});
			
			add(eliminar);
			
			buscar_nuevo=new JButton("Nueva busqueda");
			buscar_nuevo.setBounds(260, 190, 140, 60);
			buscar_nuevo.setEnabled(false);
			buscar_nuevo.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent arg0) {
				
					codigo.setText("");
					descripcion.setText("");
					precio.setText("");
					codigo.setEnabled(true);
					buscar.setEnabled(true);
					descripcion.setEnabled(false);
					precio.setEnabled(false);
					eliminar.setEnabled(false);
					buscar_nuevo.setEnabled(false);
					
				}
								
			});
			add(buscar_nuevo);
			
			
						
		} else if(boton.equals(PanelControles.consultar)){
			
			this.setBorder(new TitledBorder("Consultar base de datos de los Servicios"));
			this.setLayout(new BorderLayout());
			PanelControles.nuevo.setEnabled(true);
			
			if(acceso==1){
				
				PanelControles.modificar.setEnabled(true);
				PanelControles.eliminar.setEnabled(true);
			
			}
			
			PanelControles.consultar.setEnabled(false);
			
			String[] columnas={"Codigo","Descripcion", "Precio"};			
			int t_codigo; float t_precio; String t_descripcion;
			
			tabla = new JTable();
		    modelo = new DefaultTableModel();
		    desplazamiento = new JScrollPane(tabla);
		    
		    modelo.setColumnIdentifiers(columnas);
		    
		    desplazamiento.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	        desplazamiento.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	        
	        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
	        tabla.setRowHeight(35);
	        tabla.setFont(font);
	        tabla.setFillsViewportHeight(true); 
	        
	        tabla.setModel(modelo);
	        
	        add(desplazamiento, BorderLayout.CENTER);
	        
	        rs=consultarServicio();
	        
	        try{
			   	//Recorremos el resltSet y lo vamos guardando en las variables   
		       	while(rs.next()){
			        	
		       	t_codigo=rs.getInt("id");
		       	t_descripcion=rs.getString("descripcion");
		       	t_precio=rs.getFloat("precio");
		       	
		        //Agregamos los valores a la tabla		
		       	modelo.addRow(new Object[]{t_codigo,t_descripcion,t_precio});
			        
		       	}
		       	//Cerramos conexion	
		       	bd.cerrarConexion();
		       	conect.close();
		        	
		       	
		        
		    } catch(Exception e){
		        	
		       	e.printStackTrace();
		        	
		    }
	
		}
	

	}
	
	private void agregarLabel(String nombre, int x, int y, int ancho, int alto){
		
		JLabel label=new JLabel(nombre);
		label.setBounds(x, y, ancho, alto);
		label.setFont(font);
		this.add(label);
								
	}
	
	private JTextField agregarTextoServicioNuevo(int x, int y, int ancho, int alto){
		
		JTextField txt=new JTextField();
		txt.setBounds(x, y, ancho, alto);
		txt.addKeyListener(new KeyAdapter(){
			
			public void keyPressed(KeyEvent e){
				
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					
					agregarServicio();
					
				}
				
			}
			
		});
		
		this.add(txt);
		return txt;

	}
	
	private JTextField agregarTextoModificar(int x, int y, int ancho, int alto){
		
		JTextField txt=new JTextField();
		txt.setBounds(x, y, ancho, alto);
		txt.addKeyListener(new KeyAdapter(){
			
			public void keyPressed(KeyEvent e){
				
				if(modificar.isEnabled()){
					
					if(e.getKeyCode()==KeyEvent.VK_ENTER){
						
						modificarServicio();
						
					}
					
					
				}
				
				
			}
			
			
		});
		this.add(txt);
		return txt;

	}
	
	private JTextField agregarTextoEliminar(int x, int y, int ancho, int alto){
		
		JTextField txt=new JTextField();
		txt.setBounds(x, y, ancho, alto);
		txt.addKeyListener(new KeyAdapter(){
			
			public void keyPressed(KeyEvent e){
				
				if(eliminar.isEnabled()){
					
					if(e.getKeyCode()==KeyEvent.VK_ENTER){
						
						eliminarServicio();
						
					}
					
					
				}
				
				
			}
			
			
		});
		this.add(txt);
		return txt;

	}
	
	private void agregarServicio(){
		
		if(codigo.getText().length()==0 || descripcion.getText().length()==0 || precio.getText().length()==0){
			
			JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
						
		} else {
			
			try{
				
				String consulta="insert into servicio(id,descripcion,precio) values(?,?,?)";
				
				bd=new ConexionBBDD();
				conect=bd.getConexion();
				
				PreparedStatement st=conect.prepareStatement(consulta);
				st.setInt(1, Integer.parseInt(codigo.getText()));
				st.setString(2, descripcion.getText());
				st.setFloat(3, Float.parseFloat(precio.getText()));
								
				int row=st.executeUpdate();
				
				JOptionPane.showMessageDialog(null, "Se ha insertado correctamente " + row + " servicio");
				
				bd.cerrarConexion();
				conect.close();
				
				codigo.setText("");
				descripcion.setText("");
				precio.setText("");
				
			} catch(NumberFormatException e){
				
				JOptionPane.showMessageDialog(null, "El codigo y el precio tienen que ser numericos");
								
			} catch(MySQLIntegrityConstraintViolationException e){
				
				JOptionPane.showMessageDialog(null, "Ese codigo de servicio ya existe");
				
			} catch(Exception e){
				
				e.printStackTrace();
			
			} finally {
				
				bd.cerrarConexion();
				
			}
			
			
		}
		
		
		
		
	}
	
	private void buscarServicio(JButton origen){
		
		if(codigo.getText().length()==0){
			
			JOptionPane.showMessageDialog(null, "Introduzca un codigo valido");
			
		} else {
			
			try{
				
				String consulta="select * from servicio where id=" + Integer.parseInt(codigo.getText());
				
				bd=new ConexionBBDD();
				conect=bd.getConexion();
				
				Statement st=conect.createStatement();
				rs=st.executeQuery(consulta);
				
				rs.next();
				
				if(rs.getRow()==0){
			
					JOptionPane.showMessageDialog(null, "Ese codigo de servicio no existe");
					codigo.setText("");
											
			
				} else {
					
					if(origen.equals(modificar)){
						
						codigo.setEnabled(false);
						buscar.setEnabled(false);
						descripcion.setEnabled(true);
						precio.setEnabled(true);
						modificar.setEnabled(true);
						
						descripcion.setText(rs.getString("descripcion"));
						precio.setText(""+rs.getInt("precio"));
												
					} else {
						
						codigo.setEnabled(false);
						buscar.setEnabled(false);
						eliminar.setEnabled(true);
						buscar_nuevo.setEnabled(true);
						
						descripcion.setText(rs.getString("descripcion"));
						precio.setText(""+rs.getInt("precio"));
												
					}
															
				}
								
				
			} catch(NumberFormatException e){
				
				JOptionPane.showMessageDialog(null, "El codigo tiene que ser un numero entero");
				codigo.setText("");
				
			} catch(Exception e){
				
				e.printStackTrace();
				
			}
			
						
		}
				
	}
	
	private void modificarServicio(){
		
		try{
			
			String consulta="update servicio set descripcion=?, precio=? where id=?";
			
			bd=new ConexionBBDD();
			conect=bd.getConexion();
			
			PreparedStatement st=conect.prepareStatement(consulta);
			st.setString(1, descripcion.getText());
			st.setFloat(2, Float.parseFloat(precio.getText()));
			st.setInt(3, Integer.parseInt(codigo.getText()));
			
			int row=st.executeUpdate();
			
			JOptionPane.showMessageDialog(null, "Se ha modificado correctamente " + row + " Servicio");
			
			codigo.setText("");
			descripcion.setText("");
			precio.setText("");
			
			buscar.setEnabled(true);
			modificar.setEnabled(false);
			
			codigo.setEnabled(true);
			descripcion.setEnabled(false);
			precio.setEnabled(false);
						
			
		}catch(NumberFormatException e){
			
			JOptionPane.showMessageDialog(null, "El precio tiene que ser numerico");
			precio.setText("");
			
		} catch(Exception e){
			
			e.printStackTrace();
			
		} finally {
			
			bd.cerrarConexion();
		
		}
					
	}
	
	private void eliminarServicio(){
		
		try {
			//Creamos la consulta
			String consulta="delete from servicio where id=?";
			//Establecemos conexion con la base de datos
			bd=new ConexionBBDD();
			conect=bd.getConexion();
			//Preparamos la consulta
			PreparedStatement st=conect.prepareStatement(consulta);
			st.setInt(1, Integer.parseInt(codigo.getText()));
			//Ejecutamos y mostramos por pantalla			
			int row=st.executeUpdate();
			JOptionPane.showMessageDialog(null, "Se ha eliminado correctamente " + row + " servicio");
			//Limpiamos los campos de textos
			codigo.setText("");
			descripcion.setText("");
			precio.setText("");
			//Activamos y desactivamos los botones y campos correspondientes		
			eliminar.setEnabled(false);
			buscar_nuevo.setEnabled(false);
			buscar.setEnabled(true);
			codigo.setEnabled(true);
					
		} catch (SQLException e) {
				
			e.printStackTrace();
			
		} finally {
			
			bd.cerrarConexion();
		}
		
		
	}
	
	private ResultSet consultarServicio(){
		
		String consulta="select * from servicio order by descripcion";
		
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