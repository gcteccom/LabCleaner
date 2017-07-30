package Vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import TablasBD.HibernateUtil;
import TablasBD.Perchero;

//Clase para ver el estado del carrusel, las posiciones ocupadas o no
@SuppressWarnings("serial")
public class Carrusel extends JPanel {
	//Declaramos el array de las etiquetas y las columnas y filas
	private static int COLUMNAS=25;
	private static int FILAS=20;
	private JLabel[] labels;
	//Constructor de la clase
	public Carrusel(int acceso){
		//Activamos y desactivamos los botones correspondientes
		VentanaPrincipal.Ventas.setEnabled(true);
		VentanaPrincipal.salida_factura.setEnabled(true);
		VentanaPrincipal.gestion_perchero.setEnabled(false);
		VentanaPrincipal.Clientes.setEnabled(true);
		VentanaPrincipal.Servicios.setEnabled(true);
		VentanaPrincipal.Reportes.setEnabled(true);
		VentanaPrincipal.Configuracion.setEnabled(true);
		
		if(acceso==0){
			
			VentanaPrincipal.Reportes.setEnabled(false);
			VentanaPrincipal.Configuracion.setEnabled(false);
			
		}
		//Establecemos un gridlayout e iniciamos el array			
		this.setBackground(Color.WHITE);
		this.setBorder(new TitledBorder("Estado del Carrusel"));
		this.setLayout(new GridLayout(FILAS, COLUMNAS));
		labels=new JLabel[495];
		//Llamamos a la funcion init para iniciar los componentes
		init();
		
	}
	
	private void init(){
		//Abrimos session
		SessionFactory sesion=HibernateUtil.getSessionFactory();
		Session session=sesion.openSession();
		Transaction tx=session.beginTransaction();
		//Establecemos la fuente a utilizar
		Font font=new Font("Arial", Font.PLAIN, 12);
		//Recorremos el array 495 veces por que esas son la cantidad de perchas del carrusel		
		for (int i=0;i<495;i++){
	        //Cargamos las perchas una a una 
			Perchero percha=(Perchero)session.get(Perchero.class, i+1);
			//Verificamos si esta ocupada o no
			boolean estado=percha.isEstado();
			//Si esta ocupada configuramos lo siguiente			
			if(estado){
				//Capturamos el nif del cliente por la que esta ocupada, iniciamos la etiqueca con la posicion y el nif del cliente,establecemos el fondo rojo
				String nif=percha.getClientes().getDni();
				labels[i] = new JLabel("<html><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + (i+1) + "</p><p>" + nif + "</p>");
				labels[i].setForeground(Color.WHITE);
				labels[i].setBackground(Color.RED);
								
								
			} else {
				//Sino esta ocupada, iniciamos e indicamos que esta libre, establecemos el fondo verde
				labels[i] = new JLabel("<html><p>" + (i+1) + "</p><p>Libre</p>");
				labels[i].setBackground(new Color(5,120,16));
				labels[i].setForeground(Color.WHITE);
				
			
			}
			//Configuramos el label que las letras esten centrada, la fuente y el borde
			labels[i].setOpaque(true);
			labels[i].setHorizontalAlignment(SwingConstants.CENTER);
			labels[i].setFont(font);
			labels[i].setBorder(LineBorder.createGrayLineBorder());
	        this.add(labels[i]);
						
		}
		
		tx.commit();
	}
	
}
