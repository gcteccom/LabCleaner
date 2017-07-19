package Controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConexionBBDD {
	
	//Declaramos la variable conexion
	Connection conexion = null;
	
	public ConexionBBDD(){
		
		try{
			//Cargamos el driver y establecemos la conexion con la base de datos
			Class.forName("com.mysql.jdbc.Driver");
			conexion=DriverManager.getConnection("jdbc:mysql://localhost:3306/labcleaner", "root", "admin");
					
		}catch(Exception e){
			
			e.printStackTrace();
	
		}
		
		
		
	}
	//Funcion que devuelve la conexion
	public Connection getConexion(){
		
		return conexion;
	
	}
	//Funcion para cerrar la conexion
	public void cerrarConexion(){
		
		try {
		
			conexion.close();
		
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	
	}
	
	
	
	
	

}
