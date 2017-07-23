package Vista;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import TablasBD.HibernateUtil;
import TablasBD.Perchero;

//Clase encargada de la gestion del carrusel
public class Ventana_Perchero extends JDialog {
	//Declaramos todas las variables a utilizar
	private int cantidad, resultado;
	private JLabel t_esperada, t_seleccionada;
	private JButton aceptar, cerrar;
	private JCheckBox[] opciones;
	private Perchero[] perchas;
	private ArrayList<String> check_seleccionados;
	//Constructor de la clase, recibe por parametro la cantidad que se va a asignar
	public Ventana_Perchero(int cantidad){
		//Llamamos al constructor de la clase padre
		super(VentanaPrincipal.getFrame(), "Asignar Perchas", true);
		this.cantidad=cantidad;
		this.setLayout(new BorderLayout());
		this.setBounds(300, 100, 1200, 800);
		this.resultado=0;
		this.setUndecorated(true);
		//Iniciamos el arrayList
		check_seleccionados=new ArrayList<String>();
		//Configuramos el panel norte		
		initNorte();
		//Configuramos el panel central
		initCentral();
		//Configuramos el panel sur
		initSur();
		//Hacemos la ventana visible
		this.setVisible(true);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				
	}
	//Funcion para establecer el panel norte
	private void initNorte(){
		
		Font font_norte=new Font("Arial", Font.BOLD, 24);
		//Declaramos un panel con un flowlayout
		JPanel panel_norte=new JPanel(new FlowLayout(FlowLayout.CENTER));
		//Añadimos las dos etiquetas, la cantidad total y las perchas seleccionadas		
		t_esperada=new JLabel("Cantidad esperada: " + cantidad);
		t_esperada.setPreferredSize(new Dimension(300,50));
		t_esperada.setFont(font_norte);
		
		t_seleccionada=new JLabel("Cantidad seleccionada: " + resultado);
		t_seleccionada.setPreferredSize(new Dimension(300,50));
		t_seleccionada.setFont(font_norte);
		//Lo agregamos al panel y añadimos el panel a la parte norte del JDialog
		panel_norte.add(t_esperada);
		panel_norte.add(t_seleccionada);
		
		this.add(panel_norte, BorderLayout.NORTH);
				
	}
	//Funcion para establecer el panel sur
	private void initSur(){
		//Declaramos el panel con u flowlayout
		JPanel panel_sur=new JPanel(new FlowLayout(FlowLayout.CENTER, 40,20));
		
		ImageIcon icono_aceptar=new ImageIcon(getClass().getResource("/Recursos/aceptar.png"));
		ImageIcon icono_cancelar=new ImageIcon(getClass().getResource("/Recursos/cancelar.png"));
		
		//Agregamos el boton y comprobamos que la cantidad coincida con las perchas seleccionadas
		aceptar=new JButton("Aceptar", icono_aceptar);
		aceptar.setPreferredSize(new Dimension(120,60));
		aceptar.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e) {
												
				if(cantidad==resultado){
					
					cerrar();
										
				} else {
					
					JOptionPane.showMessageDialog(null, "Las cantidades no coinciden");
					
				}
				
			}
			
		});
		//Agregamos el boton cancelar, en caso que se seleccione borramos cualquier dado que este en el arraylist
		cerrar=new JButton("Cancelar", icono_cancelar);
		cerrar.setPreferredSize(new Dimension(120,60));
		cerrar.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent arg0) {
				
				resultado=0;
				check_seleccionados.clear();
				cerrar();
				
			}
						
		});
		//Agregamos los botones al panel y agregamos el panel al JDialog
		panel_sur.add(aceptar);
		panel_sur.add(cerrar);
		this.add(panel_sur, BorderLayout.SOUTH);
		
	}
	//Funcion que configura el panel central
	public void initCentral(){
		//Conectamos con la base de datos
		SessionFactory sesion=HibernateUtil.getSessionFactory();
		Session session=sesion.openSession();
		Transaction tx=session.beginTransaction();
		//Iniciamos el array de los checkboxs y de los objetos de Perchero
		opciones=new JCheckBox[495];
		perchas=new Perchero[495];
		//Declaramos el panel que vamos a utilizar
		JPanel panel_central=new JPanel(new GridLayout(20,25));
		
		//recorremos los array y lo vamos añadiendo
		for(int i=0;i<opciones.length;i++){
			
			perchas[i]=(Perchero)session.get(Perchero.class, i+1);
			//Comprobamos que no este ocupada para activarlo o no
			if(!perchas[i].isEstado()){
			
				opciones[i]=new JCheckBox("" + (i+1));
				opciones[i].addActionListener(new GestionCheckBox());
				panel_central.add(opciones[i]);
			
			} else {
			
				opciones[i]=new JCheckBox("" + (i+1));
				opciones[i].setEnabled(false);
				panel_central.add(opciones[i]);
								
			}
						
		}
		//Agregamos el panel al JDialog		
		this.add(panel_central, BorderLayout.CENTER);
				
	}
	//Funcion que devuelve el total de perchas seleccionadas
	public int getResultado(){
		
		return resultado;
		
	}
	//Clase privada que se encarga de añadir los check seleccionados
	private class GestionCheckBox implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			
			//Capturamos el checkbox
			JCheckBox check=(JCheckBox)e.getSource();
			int index=-1;
			int contador=0;
			//Si el check es seleccionado lo agregamos al arraylist y sumamos uno a resultado
			if(check.isSelected()){
				
				check_seleccionados.add(check.getText());
				resultado++;
			//En caso contrario					
			} else {
				//Recorremos el arrayList y comprobamos la posicion de ese checkbox, guardamos la posicion en index.
				for(String checks : check_seleccionados){
					
					if(check.getText().equals(checks)){
												
						index=contador;
						break;
												
					}
					contador++;
															
				}
				//Removemos el check pulsado y restamos uno a resultado
				check_seleccionados.remove(index);
				resultado--;
				
			}
			//Vamos agregando la cantidad de check seleccionados			
			t_seleccionada.setText("Cantidad seleccionada: " + resultado);
			
		}
			
	}
	//Funcion que devuelve el arraylist con las posiciones seleccionadas
	public ArrayList<String> getCheck(){
		
		return check_seleccionados;
				
	}
	//Funcion para cerrar la ventana actual
	private void cerrar(){
		
		this.dispose();
		
	}
	
}
