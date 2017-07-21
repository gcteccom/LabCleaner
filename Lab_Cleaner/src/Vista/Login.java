package Vista;

import java.awt.*;
import java.awt.event.*;
import java.security.*;
import java.sql.*;
import javax.swing.*;
import Controlador.ConexionBBDD;


//Clase encargada del login del usuario a la aplicacion
public class Login extends JFrame {
	
	//Declaramos los componentes que vamos a utilizar
	private static final long serialVersionUID = 1L;
	private JButton aceptar, salir;
	private JComboBox<Object> user_texto;
	private JPasswordField pass_texto;
	private JLabel candado;
	private ImageIcon img_candado;
	private JPanel panel;
	private JLabel user, pass, msj;
	private ConexionBBDD db;
	private Connection conexion;
	
	//Constructor de la clase, aqui iniciamos los componentes y damos funcionalidad a la pantalla
	public Login(){
		
		//Establecemos los datos de la ventana
		this.setBounds(750, 400, 350, 200);
		this.setLayout(new BorderLayout());
		this.setResizable(false);
		this.setTitle("Login");
		//Le ponemos un icono a la ventana
		Image icono = Toolkit.getDefaultToolkit().getImage("src/Recursos/Icono Lab-Cleaner.png");
		this.setIconImage(icono);
		
		//Iniciamos el panel y lo incluimos en el JFrame
		panel=new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setLayout(null);
		this.add(panel, BorderLayout.CENTER);
		
		//Label  del usuario
		user=new JLabel("Usuario: ");
		user.setBounds(20, 50, 50, 20);
		panel.add(user);
		//label del campo de la contraseña
		pass=new JLabel("Contraseña: ");
		pass.setBounds(20, 80, 100, 20);
		panel.add(pass);
		//label del mensaje para introducir los datos
		msj=new JLabel("Por favor, introduzca los datos de acceso.");
		msj.setBounds(40, 10, 250, 30);
		panel.add(msj);
		//Campo para elegir o escribir el usuario
		user_texto=new JComboBox<Object>(rellenarUsuarios());
		user_texto.setEditable(true);
		
		user_texto.setBounds(120, 50, 200, 22);
		panel.add(user_texto);
		//Campo para ingresar la contraseña
		pass_texto=new JPasswordField();
		pass_texto.setBounds(120, 80, 100, 22);
		panel.add(pass_texto);
		//Imagen del candado que va al lado del campo de la contraseña
		img_candado=new ImageIcon("src/Recursos/candado.png");
		candado=new JLabel(img_candado);
		candado.setBounds(200, 75, 100, 30);
		panel.add(candado);
		
		ImageIcon icono_aceptar=new ImageIcon("src/Recursos/aceptar_16.png");
		ImageIcon icono_salir=new ImageIcon("src/Recursos/salir.png");
				
		//Boton de aceptar
		aceptar=new JButton("Aceptar", icono_aceptar);
		aceptar.setBounds(50, 120, 100, 30);
		panel.add(aceptar);
		//Boton para salir
		salir=new JButton("Salir", icono_salir);
		salir.setBounds(200, 120, 100, 30);
		panel.add(salir);
		//Aqui en el campo para introducir la contraseña añadimos un keylistener para tener la opcion de ingresar al presionar la tecla enter
		//Llama a la funcion encargada de comprobar si el login es correcto
		pass_texto.addKeyListener(new KeyAdapter(){
			
			public void keyPressed(KeyEvent e){
				
				if (e.getKeyCode()==KeyEvent.VK_ENTER){
					
					comprobarLogin();
					
				}
				
			}
			
			
			
		});
		//añadimos un actionlistener al boton aceptar, llama a la funcion encargada de comprobar si el login es correcto 
		aceptar.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				comprobarLogin();
				
								
			}
				
			
		});	

		//añadimos un actionlistener al boton salir para salir de la aplicacion
		salir.addActionListener(new ActionListener(){

			
			public void actionPerformed(ActionEvent arg0) {
				cerrarLogin();
				
			}
						
		});
		//Por ultimo en el constructor hacemos visible la ventana y establecemos que pasa si cerramos la ventana
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
	}
	
	//Funcion para cerrar la ventana del login
	public void cerrarLogin(){
		
		this.dispose();
	
	}
	//Funcion para rellenar el campo de usuarios
	private String[] rellenarUsuarios(){
		
		String[] usuarios;
		String consulta="select usuario from usuarios_login";
		int contador=0;
		
		try{
			
			db=new ConexionBBDD();
			conexion=db.getConexion();
			Statement st=conexion.createStatement();
			ResultSet rs=st.executeQuery(consulta);
			
			rs.last();
			
			int tamano=rs.getRow();
			usuarios=new String[tamano];
			
			rs.beforeFirst();
			
			while(rs.next()){
				
				usuarios[contador]=rs.getString("usuario");
				contador++;
				
			}
									
			return usuarios;
			
			
		} catch(Exception e){
			
			e.printStackTrace();
			return null;
			
		}
			
		
	}
	//Funcion encargada de comprobar los datos de acceso
	public void comprobarLogin(){
		//Aqui capturamos los valores de los campos usuarios y contraseña, en el caso de la contraseña usamos la encriptacion MD5 
		String usuario=user_texto.getSelectedItem().toString();
		String clave=encriptacionMD5(String.valueOf(pass_texto.getPassword()));
		boolean loginOK=false;//Variable para saber si el login fue correcto o no
						
		
		try{
			//Establecemos conexion con la bbdd y capturamos los valores de la tabla usuarios_login
			db=new ConexionBBDD();
			conexion=db.getConexion();
			Statement st=conexion.createStatement();
			ResultSet rs=st.executeQuery("select * from usuarios_login");
		
		//Recorremos el resultset y comprobamos si alguno coincide
		while(rs.next()){
			
			if(usuario.equals(rs.getString("usuario")) && clave.equals(rs.getString("pass"))){
				//Si coincidiera alguna llamamos a la clase VentanaPrincipal y pasamos el tipo de acceso por parametro, cerramos el login
				new VentanaPrincipal(rs.getInt("acceso"));
				loginOK=true;
				cerrarLogin();
										
			}
									
		}
		//Si el login no fue correcto mostramos mensaje por pantalla
		if(!loginOK){
			
			JOptionPane.showMessageDialog(panel, "Usuario/Contraseña incorrecto, por favor intente de nuevo");
			
		}
						
		//cerramos conexion con la bbdd
		db.cerrarConexion();
		conexion.close();
		
		//Capturamos cualquier posible error, mostramos msj y cerramos la aplicacion
		}catch(Exception e){
			
			JOptionPane.showMessageDialog(panel, "Error critico, no se puede iniciar la aplicacion" + e.getMessage());
			System.exit(0);
			
		}
		
		
	}
		
	//Funcion para encriptar la contraseña con el algoritmo de encriptacion MD5
	public static String encriptacionMD5(String textoPlano){
	    
		try{
	       
		   final char[] HEXADECIMALES = { '0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f' };
	 
	       MessageDigest msgdgt = MessageDigest.getInstance("MD5");
	       byte[] bytes = msgdgt.digest(textoPlano.getBytes());
	       StringBuilder strCryptMD5 = new StringBuilder(2 * bytes.length);
	       for (int i = 0; i < bytes.length; i++){
	           
	    	   int low = (int)(bytes[i] & 0x0f);
	           int high = (int)((bytes[i] & 0xf0) >> 4);
	           strCryptMD5.append(HEXADECIMALES[high]);
	           strCryptMD5.append(HEXADECIMALES[low]);
	       
	       }
	    
	       return strCryptMD5.toString();
	    
		} catch (NoSuchAlgorithmException e) {
	      
	    	return null;
	    
		}
	
	}
		

}
