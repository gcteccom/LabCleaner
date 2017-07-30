package Vista;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import TablasBD.HibernateUtil;
import TablasBD.UsuariosLogin;


@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
//Clase para configurar el panel de configuracion
public class PanelConfiguracion extends JPanel {
	//Declaramos los distintos paneles a utilizar y los botones
	private JPanel panel_usuario, panel_impresora, panel_grafico, panel_bd;
	private JButton nuevo, modificar, eliminar, imprimir, backup, grafico, rutas;
	//Constructor de la clase
	public PanelConfiguracion(){
		//Desactivamos y activamos los botones correspondientes
		VentanaPrincipal.Ventas.setEnabled(true);
		VentanaPrincipal.salida_factura.setEnabled(true);
		VentanaPrincipal.gestion_perchero.setEnabled(true);
		VentanaPrincipal.Clientes.setEnabled(true);
		VentanaPrincipal.Servicios.setEnabled(true);
		VentanaPrincipal.Reportes.setEnabled(true);
		VentanaPrincipal.Configuracion.setEnabled(false);
		
		this.setBackground(Color.WHITE);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		//Llamamos a cada funcion que configurar las diferentes opciones
		setPanelUsuario();
		setPanelImpresora();
		setPanelBD();
		setPanelGrafico();
	
	}
	//Funcion para configurar la gestion de usuarios
	private void setPanelUsuario(){
		//Iniciamos el panel y lo configuramos
		panel_usuario=new JPanel();
		panel_usuario.setLayout(new BoxLayout(panel_usuario, BoxLayout.X_AXIS));
		panel_usuario.setPreferredSize(new Dimension(1900,180));
		panel_usuario.setMaximumSize(new Dimension(1900,180));
		//Declaramos 3 iconos que vamos a utlizar en los botones de usuarios
		ImageIcon nuevo_usuario=new ImageIcon(getClass().getResource("/Recursos/nuevo_usuario.png"));
		ImageIcon modificar_usuario=new ImageIcon(getClass().getResource("/Recursos/modificar_usuario.png"));
		ImageIcon eliminar_usuario=new ImageIcon(getClass().getResource("/Recursos/eliminar_usuario.png"));
		//Iniciamos los botones y le pasamos el icono
		nuevo=new JButton("Nuevo", nuevo_usuario);
		modificar=new JButton("Modificar", modificar_usuario);
		eliminar=new JButton("Eliminar", eliminar_usuario);
		//Configuramos el panel				
		panel_usuario.setBorder(new TitledBorder("Gestion de usuarios"));
		panel_usuario.setBackground(Color.WHITE);
		//Configuramos los botones y le pasamos el evento
		nuevo.setMaximumSize(new Dimension(100,80));
		nuevo.setPreferredSize(new Dimension(100,80));
		nuevo.setVerticalTextPosition(SwingConstants.BOTTOM);
		nuevo.setHorizontalTextPosition(SwingConstants.CENTER);
		nuevo.addActionListener(new GestionBotones());
						
		modificar.setMaximumSize(new Dimension(100,80));
		modificar.setPreferredSize(new Dimension(100,80));
		modificar.setVerticalTextPosition(SwingConstants.BOTTOM);
		modificar.setHorizontalTextPosition(SwingConstants.CENTER);
		modificar.addActionListener(new GestionBotones());
		
		eliminar.setMaximumSize(new Dimension(100,80));
		eliminar.setPreferredSize(new Dimension(100,80));
		eliminar.setVerticalTextPosition(SwingConstants.BOTTOM);
		eliminar.setHorizontalTextPosition(SwingConstants.CENTER);
		eliminar.addActionListener(new GestionBotones());
		
		panel_usuario.add(Box.createHorizontalStrut(35));//Para dejar espacio entre los botones
		panel_usuario.add(nuevo);
		panel_usuario.add(Box.createHorizontalStrut(35));
		panel_usuario.add(modificar);
		panel_usuario.add(Box.createHorizontalStrut(35));
		panel_usuario.add(eliminar);
		//Agregamos el panel de usuarios al panel principal
		this.add(panel_usuario);
				
	}
	//Funcion que establece el panel de la configuracion de la impresora
	private void setPanelImpresora(){
		//Iniciamos el panel y lo configuramos
		panel_impresora=new JPanel();
		panel_impresora.setLayout(new BoxLayout(panel_impresora, BoxLayout.X_AXIS));
		panel_impresora.setPreferredSize(new Dimension(1900,180));
		panel_impresora.setMaximumSize(new Dimension(1900,180));
		panel_impresora.setBorder(new TitledBorder("Configuracion de la impresora"));
		panel_impresora.setBackground(Color.WHITE);
		//Declaramos el icono a utilizar en el boton
		ImageIcon impresora=new ImageIcon(getClass().getResource("/Recursos/impresora.png"));
		//Iniciamos el boton y le pasamos el icono
		imprimir=new JButton("Configurar", impresora);
		//Configuramos el boton y le pasamos el evento
		imprimir.setMaximumSize(new Dimension(100,80));
		imprimir.setPreferredSize(new Dimension(100,80));
		imprimir.setVerticalTextPosition(SwingConstants.BOTTOM);
		imprimir.setHorizontalTextPosition(SwingConstants.CENTER);
		imprimir.addActionListener(new GestionBotones());
		
		panel_impresora.add(Box.createHorizontalStrut(35));
		panel_impresora.add(imprimir);
		//Agregamos el panel al panel principal
		this.add(panel_impresora);
				
	}
	//Funcion para configurar el panel de la base de datos
	private void setPanelBD(){
		//Iniciamos el panel y lo configuramos
		panel_bd=new JPanel();
		panel_bd.setLayout(new BoxLayout(panel_bd, BoxLayout.X_AXIS));
		panel_bd.setPreferredSize(new Dimension(1900,180));
		panel_bd.setMaximumSize(new Dimension(1900,180));
		panel_bd.setBorder(new TitledBorder("Configuracion de rutas y Base de datos"));
		panel_bd.setBackground(Color.WHITE);
		//Declaramos los iconos a utilizar en el boton
		ImageIcon backupBD=new ImageIcon(getClass().getResource("/Recursos/backup.png"));
		ImageIcon rutasBD=new ImageIcon(getClass().getResource("/Recursos/carpeta.png"));
		//Iniciamos los botones y le pasamos el icono
		backup=new JButton("Backup", backupBD);
		//Configuramos los botones y le pasamos el evento
		backup.setMaximumSize(new Dimension(100,80));
		backup.setPreferredSize(new Dimension(100,80));
		backup.setVerticalTextPosition(SwingConstants.BOTTOM);
		backup.setHorizontalTextPosition(SwingConstants.CENTER);
		backup.addActionListener(new GestionBotones());
		
		rutas=new JButton("Carpetas", rutasBD);
		//Configuramos los botones y le pasamos el evento
		rutas.setMaximumSize(new Dimension(100,80));
		rutas.setPreferredSize(new Dimension(100,80));
		rutas.setVerticalTextPosition(SwingConstants.BOTTOM);
		rutas.setHorizontalTextPosition(SwingConstants.CENTER);
		rutas.addActionListener(new GestionBotones());
		
				
		//Agregamos los botones al panel y creamos espacio entre ellos
		panel_bd.add(Box.createHorizontalStrut(35));
		panel_bd.add(backup);
		panel_bd.add(Box.createHorizontalStrut(35));
		panel_bd.add(rutas);
		//Agregamos el panel al panel principal
		this.add(panel_bd);
				
	}
	//Funcion para establecel el panel de grafico
	private void setPanelGrafico(){
		//Iniciamos el panel y lo configuramos
		panel_grafico=new JPanel();
		panel_grafico.setLayout(new BoxLayout(panel_grafico, BoxLayout.X_AXIS));
		panel_grafico.setPreferredSize(new Dimension(1900,180));
		panel_grafico.setMaximumSize(new Dimension(1900,180));
		panel_grafico.setBorder(new TitledBorder("Configurar aspecto grafico"));
		panel_grafico.setBackground(Color.WHITE);
		//Declaramos el icono a utlizar en el boton
		ImageIcon ventana=new ImageIcon(getClass().getResource("/Recursos/grafico.png"));
		//Iniciamos le boton y le pasamos el icono
		grafico=new JButton("Configurar", ventana);
		//Configuramos el boton y le pasamos el evento
		grafico.setMaximumSize(new Dimension(100,80));
		grafico.setPreferredSize(new Dimension(100,80));
		grafico.setVerticalTextPosition(SwingConstants.BOTTOM);
		grafico.setHorizontalTextPosition(SwingConstants.CENTER);
		grafico.addActionListener(new GestionBotones());
		//Agregamos el boton al panel
		panel_grafico.add(Box.createHorizontalStrut(35));
		panel_grafico.add(grafico);
		//Agregamos el panel al panel principal
		this.add(panel_grafico);
				
	}
	//Clase privada para llevan las acciones de los botones
	private class GestionBotones implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			//Si pulsamos en nuevo, llamamos al constructor de la clase usuarios
			if(e.getSource().equals(nuevo)){
				
				new Usuarios(nuevo);
			//Si pulsamos en modificar, llamamos al constructor de la clase usuarios	
			} else if(e.getSource().equals(modificar)){
				
				new Usuarios(modificar);
			//Si pulsamos en eliminar, llamamos al constructor de la clase usuarios
			} else if(e.getSource().equals(eliminar)){
				
				new Usuarios(eliminar);
			//Si pulsamos en imprimir llamamos al cuadro de configuracion de la impresora	
			} else if(e.getSource().equals(imprimir)){
				
				PrinterJob.getPrinterJob().printDialog();
			//Si pulsamos en grafico, llamamos a la funcion lookandfeel que establece el tema a elejir.					
			} else if(e.getSource().equals(grafico)){
				
				setLookandFeel();				
			//Si pulsamos en backupm, llamamos a la funcion que realiza el backup	
			} else if(e.getSource().equals(backup)){
				
				generarBackUp();
			
			} 
						
		}
				
	}
	//Clase privada para llevar la gestion de usuarios
	private class Usuarios extends JDialog{
		//Declaramos las variables a utilizar				
		private JLabel titulo, usuario, acceso, pass, l_pass, user_check;
		Font fuente;
		private JTextField t_usuario;
		private JPasswordField t_pass, c_pass;
		private JComboBox tipo_acceso, usuario_elegir;
		private JButton agregar;
		private List<UsuariosLogin> lista_usuarios;
		private String usuarioElegido;
		//Constructor de la clase, recibe por parametro el boton que desencadeno el evento		
		public Usuarios(JButton boton){
			//Configuramos la ventana de dialogo			
			super(VentanaPrincipal.getFrame(), "Gestion de usuarios", true);//Llamamos al constructor de la super clase
			this.setSize(580, 480);
			this.setLocationRelativeTo(VentanaPrincipal.getFrame());
			this.setResizable(false);
			this.setIconImage(new ImageIcon(getClass().getResource("/Recursos/Usuario.png")).getImage());
			this.setLayout(new FlowLayout(FlowLayout.CENTER, 10,25));
			
			fuente=new Font("Arial", Font.BOLD, 16);
			//Verificamos que boton fue el pulsado y llamamos a la funcion correspondiente
			if(boton.equals(nuevo)){
				
				initNuevo();
				
			} else if(boton.equals(modificar)){
				
				initModificar();
				
			} else if(boton.equals(eliminar)){
								
				initEliminar();
				
			}
			
			this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			this.setVisible(true);
						
		}
		//Funcion que es llamada cuando es pulsado el boton de nuevo
		private void initNuevo(){
			//Agregamos una etiqueta identificativa
			titulo=new JLabel("AGREGAR NUEVO USUARIO");
			titulo.setPreferredSize(new Dimension(550, 35));
			titulo.setFont(fuente);
			titulo.setHorizontalAlignment(SwingConstants.CENTER);
			//Etiqueta de usuario
			usuario=new JLabel("Usuario: ");
			usuario.setPreferredSize(new Dimension(200, 35));
			usuario.setFont(fuente);
			//Campo de texto correspondiente al usuario
			t_usuario=new JTextField();
			t_usuario.setPreferredSize(new Dimension(250,35));
			t_usuario.setFont(fuente);
			//Etiqueta de la contraseña
			pass=new JLabel("Contraseña: ");
			pass.setPreferredSize(new Dimension(200, 35));
			pass.setFont(fuente);
			//Para escribir la contraseña
			t_pass=new JPasswordField();
			t_pass.setPreferredSize(new Dimension(250,35));
			t_pass.setFont(fuente);
			//Repetir la contraseña
			l_pass=new JLabel("Repita la contraseña: ");
			l_pass.setPreferredSize(new Dimension(200, 35));
			l_pass.setFont(fuente);
			
			c_pass=new JPasswordField();
			c_pass.setPreferredSize(new Dimension(250,35));
			c_pass.setFont(fuente);
			//define el tipo de acceso
			acceso=new JLabel("Acceso: ");
			acceso.setPreferredSize(new Dimension(200, 35));
			acceso.setFont(fuente);
			//Array para rellenar el combobox
			String[] tipo={"Limitado", "Administrador"};
			//Iniciamos el combobox y le pasamos el array
			tipo_acceso=new JComboBox(tipo);
			tipo_acceso.setPreferredSize(new Dimension(250, 35));
			tipo_acceso.setFont(fuente);
			//Agregamos el boton y le agregamos el evento
			agregar=new JButton("Agregar");
			agregar.setPreferredSize(new Dimension(100,60));
			agregar.setFont(fuente);
			
			agregar.addActionListener(new ActionListener(){
				
				public void actionPerformed(ActionEvent arg0) {
					//Capturamos los campos de la contraseña para convertirla a string
					String pass_a=String.valueOf(t_pass.getPassword());
					String pass_b=String.valueOf(c_pass.getPassword());
					//Comprobamos que el usuario tenga mas de 6 caracteres					
					if(t_usuario.getText().length()<6){
						
						JOptionPane.showMessageDialog(null, "El nombre de usuario es muy corto");
						
					//Comprobamos que la contraseña tenga mas de 6 caracteres	
					} else if(t_pass.getPassword().length<6) {
						
						JOptionPane.showMessageDialog(null, "La contraseña debe tener minimo 6 caracteres");
						
					//Comprobamos que las contraseñas sean iguales	
					} else if(!pass_a.equals(pass_b)){
						
						JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden");
												
					} else {
						
						try{					
							//Abrimos sesion ocn hibernate
							SessionFactory sesion=HibernateUtil.getSessionFactory();
							Session session=sesion.openSession();
							Transaction tx=session.beginTransaction();
							//Convertimos la contraseña con la escriptacion md5, llamamos a la funcion estatica que esta en la clase Login
							String pass_md5=Login.encriptacionMD5(pass_a);
							//Capturamos el acceso, 0 es limitado y 1 administrador
							int acceso_combo=tipo_acceso.getSelectedIndex();
							//Cargamos el nuevo usuario con los datos
							UsuariosLogin new_user=new UsuariosLogin(t_usuario.getText(), pass_md5, acceso_combo);
							//Guardamos el nuevo usuario en la base de datos
							session.save(new_user);
							
							tx.commit();
							//Para mostrar que todo esta bien
							JOptionPane.showMessageDialog(null, "Usuario creado correctamente");
							cerrar();
						//Aqui en caso de que el usuario ya exista
						} catch (ConstraintViolationException e){
							
							JOptionPane.showMessageDialog(null, "Ese usuario ya existe");
							t_usuario.setText("");
							
						}
					}
					
				}
								
			});
						
			this.add(titulo);
			this.add(usuario);
			this.add(t_usuario);
			this.add(pass);
			this.add(t_pass);
			this.add(l_pass);
			this.add(c_pass);
			this.add(acceso);
			this.add(tipo_acceso);
			this.add(agregar);
						
		}
		//Funcion para modificar un usuario existente
		private void initModificar(){
			//Abrimos sesion con hibernate
			SessionFactory sesion=HibernateUtil.getSessionFactory();
			Session session=sesion.openSession();
			Transaction tx=session.beginTransaction();
			//Iniciamos las etiquetas			
			titulo=new JLabel("MODIFICAR USUARIO");
			titulo.setPreferredSize(new Dimension(550, 35));
			titulo.setFont(fuente);
			titulo.setHorizontalAlignment(SwingConstants.CENTER);
			
			user_check=new JLabel("Elija el usuario a modificar: ");
			user_check.setPreferredSize(new Dimension(250, 35));
			user_check.setFont(fuente);
			//Cargamos la lista de todos los usarios de la tabla
			lista_usuarios=getList("Select p from " + UsuariosLogin.class.getName() + " p");
			//Creamos un array para pasarlo al combobox
			String[] usuariosFinal=new String[lista_usuarios.size()];
			
			int contador=0;
			//Recorremos la lista y vamos guardando en el array
			for(UsuariosLogin str:lista_usuarios){
				
				usuariosFinal[contador]=str.getUsuario();
				contador++;
			}
			//Pasamos el array al combobox									
			usuario_elegir=new JComboBox(usuariosFinal);
			usuario_elegir.setPreferredSize(new Dimension(200, 35));
			usuario_elegir.setFont(fuente);
			//Etiquetas de usuario, contraseñas y acceso
			usuario=new JLabel("Usuario: ");
			usuario.setPreferredSize(new Dimension(200, 35));
			usuario.setFont(fuente);
			//Campo de texto de usuario, contraseña y accesp
			t_usuario=new JTextField();
			t_usuario.setPreferredSize(new Dimension(250,35));
			t_usuario.setFont(fuente);
			t_usuario.setEditable(false);//Este campo no se puede modificar ya que es la clave primaria de la tabla
			
			pass=new JLabel("Contraseña: ");
			pass.setPreferredSize(new Dimension(200, 35));
			pass.setFont(fuente);
			
			t_pass=new JPasswordField();
			t_pass.setPreferredSize(new Dimension(250,35));
			t_pass.setFont(fuente);
			
			l_pass=new JLabel("Repita la contraseña: ");
			l_pass.setPreferredSize(new Dimension(200, 35));
			l_pass.setFont(fuente);
			
			c_pass=new JPasswordField();
			c_pass.setPreferredSize(new Dimension(250,35));
			c_pass.setFont(fuente);
			
			acceso=new JLabel("Acceso: ");
			acceso.setPreferredSize(new Dimension(200, 35));
			acceso.setFont(fuente);
			
			String[] tipo={"Limitado", "Administrador"};
			
			tipo_acceso=new JComboBox(tipo);
			tipo_acceso.setPreferredSize(new Dimension(250, 35));
			tipo_acceso.setFont(fuente);
			//agregamos el boton
			agregar=new JButton("Modificar");
			agregar.setPreferredSize(new Dimension(120,60));
			agregar.setFont(fuente);
			//Agregamos al combobox de usuarios funcionalidad
			usuario_elegir.addActionListener(new ActionListener(){
				
				public void actionPerformed(ActionEvent e) {
					//Capturamos el combobox
					JComboBox combo=(JComboBox)e.getSource();
					//Capturamos el usuario seleccionado
					usuarioElegido=combo.getSelectedItem().toString();
					//Cargamos el usuario					
					UsuariosLogin usuario=(UsuariosLogin)session.get(UsuariosLogin.class, usuarioElegido);
					//Lo agregamos a las etiquetas
					t_usuario.setText(usuario.getUsuario());
					t_pass.setText(usuario.getPass());
					tipo_acceso.setSelectedIndex(usuario.getAcceso());
				
				}
								
			});
			//Agregamos funcionalidad al boton
			agregar.addActionListener(new ActionListener(){
				
				public void actionPerformed(ActionEvent e) {
					//Capturamos los campos de contraseña para convertirlo a String
					String pass_a=String.valueOf(t_pass.getPassword());
					String pass_b=String.valueOf(c_pass.getPassword());
					//Verificamos que la contraseña tenga mas de 6 caracteres
					if(t_pass.getPassword().length<6) {
						
						JOptionPane.showMessageDialog(null, "La contraseña debe tener minimo 6 caracteres");
					//Verificamos que las contraseñas sean iguales							
					} else if(!pass_a.equals(pass_b)){
						
						JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden");
												
					} else {
						//Cargamos usuario, le pasamos la nueva contraseña y el tipo de acceso
						UsuariosLogin usuario=(UsuariosLogin)session.get(UsuariosLogin.class, usuarioElegido);
						usuario.setPass(Login.encriptacionMD5(String.valueOf(t_pass.getPassword())));
						usuario.setAcceso(tipo_acceso.getSelectedIndex());
						//Actualizamos los nuevos cambios	
						session.update(usuario);
							
						tx.commit();
						//Para mostrar que todo fue bien	
						JOptionPane.showMessageDialog(null, "Usuario modificado correctamente");
						cerrar();
																
					}
				}				
			});
			
			this.add(titulo);
			this.add(user_check);
			this.add(usuario_elegir);
			this.add(usuario);
			this.add(t_usuario);
			this.add(pass);
			this.add(t_pass);
			this.add(l_pass);
			this.add(c_pass);
			this.add(acceso);
			this.add(tipo_acceso);
			this.add(agregar);
						
		}
		//Funcion cuando se llama a eliminar
		private void initEliminar(){
			//Abrimos sesion con hibernate
			SessionFactory sesion=HibernateUtil.getSessionFactory();
			Session session=sesion.openSession();
			Transaction tx=session.beginTransaction();
			//Agregamos las etiquetas			
			titulo=new JLabel("ELIMINAR USUARIO");
			titulo.setPreferredSize(new Dimension(550, 35));
			titulo.setFont(fuente);
			titulo.setHorizontalAlignment(SwingConstants.CENTER);
			
			user_check=new JLabel("Elija el usuario a eliminar: ");
			user_check.setPreferredSize(new Dimension(250, 35));
			user_check.setFont(fuente);
			//Cargamos nuevamente en un combobox los usuarios disponibles
			lista_usuarios=getList("Select p from " + UsuariosLogin.class.getName() + " p");
			
			String[] usuariosFinal=new String[lista_usuarios.size()];
			
			int contador=0;
			
			for(UsuariosLogin str:lista_usuarios){
				
				usuariosFinal[contador]=str.getUsuario();
				contador++;
			}
			//Cargamos el combobox con el array de usuarios									
			usuario_elegir=new JComboBox(usuariosFinal);
			usuario_elegir.setPreferredSize(new Dimension(200, 35));
			usuario_elegir.setFont(fuente);
			//Agregamos las etiquetas de usuario, contraseña y acceso
			usuario=new JLabel("Usuario: ");
			usuario.setPreferredSize(new Dimension(200, 35));
			usuario.setFont(fuente);
			//Agregamos los campos de textos y lo hacemos que no se puedan editar
			t_usuario=new JTextField();
			t_usuario.setPreferredSize(new Dimension(250,35));
			t_usuario.setFont(fuente);
			t_usuario.setEditable(false);
			
			pass=new JLabel("Contraseña: ");
			pass.setPreferredSize(new Dimension(200, 35));
			pass.setFont(fuente);
			
			t_pass=new JPasswordField();
			t_pass.setPreferredSize(new Dimension(250,35));
			t_pass.setFont(fuente);
			t_pass.setEditable(false);
									
			agregar=new JButton("Eliminar");
			agregar.setPreferredSize(new Dimension(100,60));
			agregar.setFont(fuente);
			//Le damos funcionalidad al combobox de usuarios
			usuario_elegir.addActionListener(new ActionListener(){
				
				public void actionPerformed(ActionEvent e) {
					//Capturamos el combobox
					JComboBox combo=(JComboBox)e.getSource();
					//Guardamos el usuario elegido
					usuarioElegido=combo.getSelectedItem().toString();
					//Cargamos usuario					
					UsuariosLogin usuario=(UsuariosLogin)session.get(UsuariosLogin.class, usuarioElegido);
					//Agreamos los datos a los campos de textos
					t_usuario.setText(usuario.getUsuario());
					t_pass.setText(usuario.getPass());
									
				}
								
			});
			//Le damos funcionalidad al boton
			agregar.addActionListener(new ActionListener(){
				
				public void actionPerformed(ActionEvent e) {
					//Cargamos usuario elegido
					UsuariosLogin usuario=(UsuariosLogin)session.get(UsuariosLogin.class, usuarioElegido);
					//Confirmamos si se quiere eliminar el usuario
					int op=JOptionPane.showConfirmDialog(null, "Seguro que quiere eliminar el usuario " + usuario.getUsuario(), "Eliminar", JOptionPane.OK_CANCEL_OPTION);
					//Si presiona si, procedemos a eliminar de la base de datos
					if(op==JOptionPane.OK_OPTION){
						
						session.delete(usuario);
						
						tx.commit();
						
						JOptionPane.showMessageDialog(null, "Usuario eliminado correctamente");
						cerrar();
						
					}
															
				}
								
			});
			
			this.add(titulo);
			this.add(user_check);
			this.add(usuario_elegir);
			this.add(usuario);
			this.add(t_usuario);
			this.add(pass);
			this.add(t_pass);
			this.add(agregar);
						
		}
		//Funcion para cerrar la ventana actual
		private void cerrar(){
			
			this.dispose();
		}
		//Funcion que devuelve una lista de los usuarios
		
		private List<UsuariosLogin> getList(String consulta){
			
			SessionFactory sesion=HibernateUtil.getSessionFactory();
			Session session=sesion.openSession();
			Transaction tx=session.beginTransaction();
			
			Query query = session.createQuery(consulta);
			
			List<UsuariosLogin> usuarios=query.list();
			
			tx.commit();
					
			return usuarios;
					
		}
					
	}
	//Funcion para establecer el tema grafico
	
	private void setLookandFeel(){
		//Creamos un panel de dialogo y lo configuramos
		JDialog panel=new JDialog(VentanaPrincipal.getFrame(), "Cambiar aspecto grafico", true);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20,20));
		panel.setSize(400, 200);
		panel.setLocationRelativeTo(VentanaPrincipal.getFrame());
		panel.setIconImage(new ImageIcon(getClass().getResource("/Recursos/grafico.png")).getImage());
		//Agregamos un combobox
		JComboBox temas=new JComboBox();
		JButton aplicar=new JButton("Aplicar");
		temas.setPreferredSize(new Dimension(350, 40));
		aplicar.setPreferredSize(new Dimension(100,60));
		//declaramos un array con los temas disponibles del sistema
		UIManager.LookAndFeelInfo[] info_temas = UIManager.getInstalledLookAndFeels();
		
		
		for (int i=0;i<info_temas.length;i++){
		    //Lo agregamos al combobox
			temas.addItem(info_temas[i].getClassName());
		    
		}
		//Agregamos el combobox y el boton
		panel.add(temas);
		panel.add(aplicar);
		//Le damos funcionalidad al boton
		aplicar.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent arg0) {
				
				try{
				    //Capturamos el tema seleccionado
					String look = temas.getSelectedItem().toString();
					UIManager.setLookAndFeel(look); // Establecemos el tema
				    SwingUtilities.updateComponentTreeUI(VentanaPrincipal.getFrame());//Actualizamos, pasamos el frame principal de la aplicacion
				    panel.dispose();//cerramos el panel actual
				    //Sobreescrimos el archivo que guarda el tema grafico
				    Properties prop = new Properties();
				    prop.load(new FileInputStream("src/Recursos.properties"));
				    prop.setProperty("tema.grafico", look);
				    OutputStream os = new FileOutputStream("src/Recursos.properties");
				    prop.store(os, null);
				    os.close();
				    				    				    
				    
				} catch (Exception e){
				        
					e.printStackTrace();
				
				}
				
			}
			
		});
				
		panel.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		panel.setVisible(true);
				
	}
	//Funcion para realizar el backup de la base de datos
	private void generarBackUp(){
			//declaramos las variables y los componentes		
			int op;
			JFileChooser fileChooser = new JFileChooser();//Para decirlo donde vamos a guardar el archivo
            fileChooser.setApproveButtonText("Guardar Backup de la Base de datos");
            FileNameExtensionFilter filter =  new FileNameExtensionFilter("Archivos sql", "sql");//Solo archivos .sql
            fileChooser.addChoosableFileFilter(filter);
            String path=null;
            String fecha=new SimpleDateFormat("yyyy-MM-dd").format(new Date());//Para añadirlo al nombre del archivo y asi saber el dia que se realizo el backup
                       
            op=fileChooser.showSaveDialog(this);//Mostramos el Jfilechooser
            
            if(op==JFileChooser.APPROVE_OPTION){//Si el usuario presiono en si, entramos
            	
            	try{
            		//Capturamos la direccion que se selecciono junto con el nombre
	            	File fileDB = fileChooser.getSelectedFile();
	            	path = fileDB.getAbsolutePath();//Obtener la direccion absoluta
	            	path=path.replace('\\', '/');//Reemplazamos \ por / ya que sino nos daria error
	            	path=path + "_" + fecha +".sql";//Agregamos la fecha y la extension
		
	                Runtime runtime = Runtime.getRuntime();//Declaramos un objeto de runtime para ejecutar el comando
		            //Comando completo a ejecutar    
	                String pathDB = "C:/Program Files/MySQL/MySQL Server 5.7/bin/mysqldump.exe -uroot -padmin --add-drop-database -B labcleaner -r" + path;
	                Process child = runtime.exec(pathDB);//ejecutamos 
	                //esperamos que termine el proceso
	                int process=child.waitFor();
	                //Si fuera igual a 0, es que todo fue correcto, sino no se pudo hacer
	                if(process==0){
	                	               
	                	JOptionPane.showMessageDialog(null, "Archivo generado correctamente", "Verificar",JOptionPane.INFORMATION_MESSAGE);
	                
	                } else {
	                	
	                	JOptionPane.showMessageDialog(null, "No se ha podido generar el archivo", "Verificar",JOptionPane.INFORMATION_MESSAGE);
	                
	                }
            	
            	} catch(Exception e){
            		
            		JOptionPane.showMessageDialog(this, "Error al crear archivo: " + e.getMessage());
            		
            	}
                        
            }
           		
	}
	//Funcion para restaurar la base de datos
	/*private void restaurarBD(){
		//Declaramos las variables y los componentes
		int op;
		JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter =  new FileNameExtensionFilter("Archivos sql", "sql");
        fileChooser.addChoosableFileFilter(filter);
        String path=null;
        //Mostramos el jfilechooser para buscar el archivo           
        op=fileChooser.showOpenDialog(this);
        
        if(op==JFileChooser.APPROVE_OPTION){//Si se presiona en si entramos en la condicionar
        	
        		File fileDB = fileChooser.getSelectedFile();//Capturamos el archivo seleccionado
        		path = fileDB.getAbsolutePath();//Obtener la direccion absoluta
        		path=path.replace('\\', '/');//reemplazamos los \ por /
        	    //Comando a ejecutar    	     	
        		String[] executeCmd = new String[]{"C:/Program Files/MySQL/MySQL Server 5.7/bin/mysql.exe", "--user=root", "--password=jaime1984", "-e", "source " + path};
        		        		
                Process runtimeProcess;
                               
                try {
                    //Creamos el proceso y le pasamos el comando a jecutar
                	runtimeProcess = Runtime.getRuntime().exec(executeCmd);
                    int processComplete = runtimeProcess.waitFor();//Esperamos a que el proceso termine para saber si ha ido bien
                    //Si es igual a 0, es que se pudo recuperar sino hubo fallos.
                    if (processComplete == 0) {
                        
                    	JOptionPane.showMessageDialog(this, "Base de datos restaurada correctamente");
                                        
                    } else {
                    
                    	JOptionPane.showMessageDialog(this, "No se pudo restaurar");
                    
                    }
                
                } catch (Exception ex) {
                
                	ex.printStackTrace();
                
                }
                 	
        }
				
	}*/
	
}
