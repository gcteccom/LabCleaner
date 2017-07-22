package Modelo;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.*;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import Vista.*;


public class LabCleaner {

	public static void main(String[] args) {
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		if(screenSize.getHeight() == 1080 && screenSize.getWidth() == 1920) {
							
			try{
								
				File archivo = new File ("src/Recursos/Tema_grafico.txt");
				FileReader fr = new FileReader (archivo);
				BufferedReader br = new BufferedReader(fr);
				String tema = br.readLine();
										
				UIManager.setLookAndFeel(tema);
				
				br.close();
						
			} catch(ClassNotFoundException e){
				
				JOptionPane.showMessageDialog(null, "Error al cargar tema grafico, se cargara el tema por defecto");
				
			} catch(Exception e){
			
				e.printStackTrace();
			
			}
					
			new PantallaInicial();
			new Login();
			
		} else {
			
			JOptionPane.showMessageDialog(null, "La resolucion no es correcta, por favor ajuste la resolucion a 1920 x 1080", "Error al iniciar aplicacion", JOptionPane.ERROR_MESSAGE);
			
		}
		
	}

}
