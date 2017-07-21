package Modelo;

import java.io.*;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import Vista.*;


public class LabCleaner {

	public static void main(String[] args) {
		
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
		
	}

}