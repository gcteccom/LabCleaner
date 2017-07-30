package Modelo;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import Vista.Login;
import Vista.PantallaInicial;


public class LabCleaner {

	public static void main(String[] args) {
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		Properties prop = new Properties();
		
		if(screenSize.getHeight() == 1080 && screenSize.getWidth() == 1920) {
							
			try{
								
				InputStream is = new FileInputStream("src/Recursos.properties");
				prop.load(is);
																						
				UIManager.setLookAndFeel(prop.getProperty("tema.grafico"));
				
				is.close();
								
						
			} catch(ClassNotFoundException e){
				
				JOptionPane.showMessageDialog(null, "Error al cargar tema grafico, se cargara el tema por defecto");
				
			} catch(Exception e){
			
				JOptionPane.showMessageDialog(null, e.getMessage());
			
			}
					
			new PantallaInicial();
			new Login();
			
		} else {
			
			JOptionPane.showMessageDialog(null, "La resolucion no es correcta, por favor ajuste la resolucion a 1920 x 1080", "Error al iniciar aplicacion", JOptionPane.ERROR_MESSAGE);
			
		}
		
	}

}
