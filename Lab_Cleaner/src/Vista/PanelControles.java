package Vista;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.*;

//Clase encargada de los controles del panel oeste
public class PanelControles extends JPanel {
	
	//Declaramos la variable
	private static final long serialVersionUID = 1L;
	public static JButton nuevo, modificar, eliminar, consultar;
	public static PanelCentralCliente panel_central_cliente;
	public static PanelCentralServicio panel_central_servicio;
	private int acceso;
	ImageIcon icono_nuevo, icono_modificar, icono_eliminar, icono_consultar;
	
		
	//Contructor de la clase, recibe el nombre de los botones, el acceso y el boton de origen que se pulso
	public PanelControles(String n_boton1, String n_boton2, String n_boton3, String n_boton4, int acceso, JButton origen){
		//establecemos los valores del panel, el layout y el borde
		this.acceso=acceso;
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		this.setBackground(Color.WHITE);
		this.setBorder(new MatteBorder(30,30,30,30,Color.WHITE));
		
		icono_nuevo=new ImageIcon(getClass().getResource("/Recursos/add.png"));
		icono_modificar=new ImageIcon(getClass().getResource("/Recursos/modificar.png"));
		icono_eliminar=new ImageIcon(getClass().getResource("/Recursos/eliminar.png"));
		icono_consultar=new ImageIcon(getClass().getResource("/Recursos/buscar.png"));
		
		//Comprobamos que se llama pulsado en el boton de clientes
		if(origen.equals(VentanaPrincipal.Clientes)){
			
			if(panel_central_servicio!=null){
				
				removePanelCentralServicio();
				
			}
							
			//Desabilitamos el boton de origen, osea, cliente en este caso, y habilitamos los demas botones
			origen.setEnabled(false);
			VentanaPrincipal.Servicios.setEnabled(true);
			VentanaPrincipal.Ventas.setEnabled(true);
			VentanaPrincipal.salida_factura.setEnabled(true);
			VentanaPrincipal.gestion_perchero.setEnabled(true);
				
			//Comprobamos el tipo de acceso para saber si tenemos que habilitar los botones de reportes y configuracion				
			if(acceso==1){
				
				VentanaPrincipal.Reportes.setEnabled(true);
				VentanaPrincipal.Configuracion.setEnabled(true);
				
			}
			
			
			
			//Iniciamos los botones con la funcion agregarBotones	
			nuevo=agregarBotones(n_boton1,120,50, icono_nuevo);
			modificar=agregarBotones(n_boton2,120,50, icono_modificar);
			eliminar=agregarBotones(n_boton3,120,50, icono_eliminar);
			consultar=agregarBotones(n_boton4,120,50, icono_consultar);
			//Iniciamos el panel central de clientes y pasamos el boton nuevo para que por defecto inicie con ese boton activado y lo agregamos a la ventana principal
			panel_central_cliente=new PanelCentralCliente(nuevo);
			VentanaPrincipal.getFrame().add(panel_central_cliente, BorderLayout.CENTER);
			//Agregamos la clase AccionBotonesClientes a los botones 	
			nuevo.addActionListener(new AccionBotonesCliente());
			modificar.addActionListener(new AccionBotonesCliente());
			eliminar.addActionListener(new AccionBotonesCliente());
			consultar.addActionListener(new AccionBotonesCliente());
								
					
		} else if (origen.equals(VentanaPrincipal.Servicios)){
									
			if(panel_central_cliente!=null){
				
				removePanelCentralCliente();
				
			}
			
			origen.setEnabled(false);
			VentanaPrincipal.Clientes.setEnabled(true);
			VentanaPrincipal.Ventas.setEnabled(true);
				
			nuevo=agregarBotones(n_boton1,120,50, icono_nuevo);
			
				
			if(acceso==1){
					
				VentanaPrincipal.Reportes.setEnabled(true);
				VentanaPrincipal.Configuracion.setEnabled(true);
				modificar=agregarBotones(n_boton2,120,50, icono_modificar);
				eliminar=agregarBotones(n_boton3,120,50, icono_eliminar);
					
			}
			
			consultar=agregarBotones(n_boton4,120,50, icono_consultar);
				
			panel_central_servicio=new PanelCentralServicio(nuevo, acceso);
			VentanaPrincipal.getFrame().add(panel_central_servicio, BorderLayout.CENTER);
			
			nuevo.addActionListener(new AccionBotonesServicio());
			consultar.addActionListener(new AccionBotonesServicio());
			
			if(acceso==1){
				
				modificar.addActionListener(new AccionBotonesServicio());
				eliminar.addActionListener(new AccionBotonesServicio());
			
			}
						
		} 
				
	}
	
	//Funcion para agregar los botones
	public JButton agregarBotones(String nombre, int ancho, int alto, ImageIcon icono){
		
		add(Box.createVerticalStrut(20));
		JButton boton=new JButton(nombre, icono);
		boton.setMaximumSize(new Dimension(ancho,alto));
		add(boton);
		add(Box.createVerticalStrut(20));
		return boton;
				
	}
	//Clase para gestiones los botones del modulo de los clientes
	private class AccionBotonesCliente implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			//Comprobamos que boton se ha pulsado y establecemos el panel correspondiente
			if(e.getSource().equals(nuevo)){
				
				removePanelCentralCliente();
				panel_central_cliente=new PanelCentralCliente(nuevo);
				VentanaPrincipal.getFrame().add(panel_central_cliente, BorderLayout.CENTER);
				
			} else if(e.getSource().equals(modificar)){
				
				removePanelCentralCliente();
				panel_central_cliente=new PanelCentralCliente(modificar);
				VentanaPrincipal.getFrame().add(panel_central_cliente, BorderLayout.CENTER);
				
			} else if(e.getSource().equals(eliminar)){
				
				removePanelCentralCliente();
				panel_central_cliente=new PanelCentralCliente(eliminar);
				VentanaPrincipal.getFrame().add(panel_central_cliente, BorderLayout.CENTER);
				
			}  else {
				
				removePanelCentralCliente();
				panel_central_cliente=new PanelCentralCliente(consultar);
				VentanaPrincipal.getFrame().add(panel_central_cliente, BorderLayout.CENTER);
								
			}
						
		}
				
	}
	
	private class AccionBotonesServicio implements ActionListener{

		
		public void actionPerformed(ActionEvent e) {
			
			if(e.getSource().equals(nuevo)){
				
				removePanelCentralServicio();
				panel_central_servicio=new PanelCentralServicio(nuevo, acceso);
				VentanaPrincipal.getFrame().add(panel_central_servicio, BorderLayout.CENTER);
				
			} else if(e.getSource().equals(modificar)){
				
				removePanelCentralServicio();
				panel_central_servicio=new PanelCentralServicio(modificar, acceso);
				VentanaPrincipal.getFrame().add(panel_central_servicio, BorderLayout.CENTER);
				
			} else if(e.getSource().equals(eliminar)){
				
				removePanelCentralServicio();
				panel_central_servicio=new PanelCentralServicio(eliminar, acceso);
				VentanaPrincipal.getFrame().add(panel_central_servicio, BorderLayout.CENTER);
				
			}  else {
				
				removePanelCentralServicio();
				panel_central_servicio=new PanelCentralServicio(consultar, acceso);
				VentanaPrincipal.getFrame().add(panel_central_servicio, BorderLayout.CENTER);
								
			}
						
		}
		
		
	}	
	
	//Funcion para remover el panel central de cliente
	private void removePanelCentralCliente(){
		
		VentanaPrincipal.getFrame().remove(panel_central_cliente);
		
	}
	//Funcion para remover el panel central de servicio
	private void removePanelCentralServicio(){
		
		VentanaPrincipal.getFrame().remove(panel_central_servicio);		
		
	}
	
		

}
