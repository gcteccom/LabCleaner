package Vista;

import javax.swing.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import TablasBD.HibernateUtil;

import java.awt.*;

//Esta es la clase para presentar la pantalla iniciar de la aplicacion
public class PantallaInicial extends JWindow{
	
	
	private static final long serialVersionUID = 1L;
	
	//Declaracion de los layout y componentes a utilizar
	BorderLayout borderLayout1 = new BorderLayout();
	JLabel imageLabel = new JLabel();
	JPanel southPanel = new JPanel();
	FlowLayout southPanelFlowLayout = new FlowLayout();
	JProgressBar progressBar = new JProgressBar();
	ImageIcon imageIcon;

	public PantallaInicial() {
	  
		//establecemos la imagen a utilizar y llamamos a los metodos encargados de pintar la ventana
		this.imageIcon = new ImageIcon("src/Logo Lab-Cleaner.jpg");;
		dibujaVentana();
		setLocationRelativeTo(null);
		progressBar.setMaximum(100);
		setVisible(true);
		SessionFactory sesion=HibernateUtil.getSessionFactory();
		Session session=sesion.openSession();
		velocidadDeCarga();
		
	
	}
	
	//En esta funcion establecemos el layout, colores y añadimos la imagen que es el logo de la compañia
	public void dibujaVentana() {
	
		imageLabel.setIcon(imageIcon);
		this.getContentPane().setLayout(borderLayout1);
		southPanel.setLayout(southPanelFlowLayout);
		southPanel.setBackground(Color.BLACK);
		this.getContentPane().add(imageLabel, BorderLayout.CENTER);
		this.getContentPane().add(southPanel, BorderLayout.SOUTH);
		southPanel.add(progressBar, null);
		this.pack();
	
	}
	
	//Esta funcion es la que rellena la barra del progreso, es llamada desde la funciona velocidadDeCarga
	public void setProgreso(int progress){
	    
		final int progreso = progress;
	    progressBar.setValue(progreso);
	
	}

	//Esta funcion es la parte logica, establece el tiempo que dura la imagen, en cada vuelta llama a la funcion setProgreso y cuando termina finaliza
	public void velocidadDeCarga(){
	    
		for (int i = 0; i <= 100; i++){
	      
			for (long j=0; j<100000; ++j){
	        
	    	  String poop ="" + (j + i);
	      
			}
	     
	     setProgreso(i);
		
		}

		dispose();
	
	}
		
}


