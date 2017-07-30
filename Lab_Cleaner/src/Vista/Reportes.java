package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;

import Controlador.BalanceGeneral;
import Controlador.GenerarBalance;
import Controlador.ReporteFecha;
import TablasBD.Factura;
import TablasBD.HibernateUtil;
//Clase Reportes, encargada de realizar de llevar el balance y realizar reportes
@SuppressWarnings({"serial", "rawtypes", "unchecked"})
public class Reportes extends JPanel {
	//Declaramos todas las variables a utilizar
	private JPanel panel_norte, panel_central;
	private JButton balance, balance_general, reportes_fecha, aceptar_accion, aceptar_reporte;
	private JLabel t_efectivo, t_venta, t_cantidad, t_accion, t_reportes, desde, hasta;
	private JTextField entrada, salida, total, contado, credito, total_venta, cantidad_contado, cantidad_credito, cantidad_total; 
	private List factura;
	private float a_entrada, a_salida, a_contado, a_credito; 
	private int a_cantidad_contado, a_cantidad_credito;
	private Date fecha_actual;
	private JComboBox op_balance;
	private JTable tabla_balance=null;
	private DefaultTableModel modelo = null;
    private JScrollPane desplazamiento = null;
    private String estado;
    private Font fuente_subtitulo;
	
	//Constructor de la clase, activamos y desactivamos los botones correspondientes.
	public Reportes(){
				
		VentanaPrincipal.Ventas.setEnabled(true);
		VentanaPrincipal.salida_factura.setEnabled(true);
		VentanaPrincipal.gestion_perchero.setEnabled(true);
		VentanaPrincipal.Clientes.setEnabled(true);
		VentanaPrincipal.Servicios.setEnabled(true);
		VentanaPrincipal.Reportes.setEnabled(false);
		VentanaPrincipal.Configuracion.setEnabled(true);
		//Establecemos el fondo blanco y el layout
		this.setBackground(Color.WHITE);
		this.setLayout(new BorderLayout());
		//Capturales la fecha actual del paquete sql
		java.util.Date fecha_util=new java.util.Date();
		fecha_actual=new Date(fecha_util.getTime());
		fuente_subtitulo=new Font("Serif", Font.PLAIN, 20);
		
		//LLamamos a la funcion para establecer el panel norte
		setPanelNorte();
		
	}
	//Funcion que establece el panel norte, donde estan los botones de opciones
	private void setPanelNorte(){
		//Iniciamos el panel y lo configuramos
		panel_norte=new JPanel();
		panel_norte.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
		panel_norte.setBorder(new TitledBorder("Operaciones"));
		panel_norte.setBackground(Color.WHITE);
		//Iniciamos los botones y le pasamos el evento
		balance=new JButton("Balance del dia");
		balance.setPreferredSize(new Dimension(150,40));
		balance.addActionListener(new GestionBotones());
						
		balance_general=new JButton("Balance general");
		balance_general.setPreferredSize(new Dimension(150,40));
		balance_general.addActionListener(new GestionBotones());
		
		reportes_fecha=new JButton("Reportes por fecha");
		reportes_fecha.setPreferredSize(new Dimension(150,40));
		reportes_fecha.addActionListener(new GestionBotones());
		//Agregamos los botones al panel
		panel_norte.add(balance);
		panel_norte.add(balance_general);
		panel_norte.add(reportes_fecha);
		
		//Agregamos el panel a la parte norte		
		this.add(panel_norte, BorderLayout.NORTH);
				
	}
	//Clase privada encargada de llevar las acciones de los botones
	private class GestionBotones implements ActionListener{

		
		public void actionPerformed(ActionEvent e) {
			//Si pulsamos en balance, establece el panel de balance en el centro
			if(e.getSource().equals(balance)){
				
				setPanelBalance();
			//Si pulsamos en balance_general, establece el panel de balance general en el centro				
			} else if(e.getSource().equals(balance_general)){
								
				setPanelGeneral();
			//Si pulsamos en reportes, establece el panel de reporte en el centro
			} else if(e.getSource().equals(reportes_fecha)){
				
				setPanelReportes();
				
			}
			
		}
		//Funcion que establece el panel de balance
		private void setPanelBalance(){
			//Verificamos si habia un panel anterior para retirarlo
			if(panel_central!=null){
				
				removePanel();
				
			}
			//Activamos y desactivamos los botones correspondiente
			balance.setEnabled(false);
			balance_general.setEnabled(true);
			reportes_fecha.setEnabled(true);
			//Establecemos el layout y el color de fondo
			panel_central=new JPanel(null);
			panel_central.setBackground(Color.WHITE);
			
			Font fuente_titulo=new Font("Serif", Font.BOLD, 24);
			
			//Distintos colores a utilizar
			Color color_verde=new Color(21,192,65);
			Color color_fuente=new Color(243,250,245);
			Color color_azul=new Color(93, 173, 226);
			Color color_rojo=new Color(237, 80, 80);
			//Iniciamos las etiquetas y las configuramos
			t_efectivo=new JLabel("EFECTIVO EN CAJA");
			t_efectivo.setBounds(150, 30, 250, 40);
			t_efectivo.setFont(fuente_titulo);
			t_efectivo.setBackground(color_verde);
			t_efectivo.setForeground(color_fuente);
			t_efectivo.setOpaque(true);
			t_efectivo.setBorder(new BevelBorder(BevelBorder.RAISED));
			t_efectivo.setHorizontalAlignment(SwingConstants.CENTER);
			agregarLabel("ENTRADA DE EFECTIVO:",80, 100, 250, 40,fuente_subtitulo, color_verde, color_fuente);
			agregarLabel("SALIDA DE EFECTIVO:",80, 160, 250, 40,fuente_subtitulo, color_verde, color_fuente);
			agregarLabel("BALANCE TOTAL:",80, 220, 250, 40,fuente_subtitulo, color_verde, color_fuente);
			//Agregamos los campos de textos 			
			entrada=agregarTexto(350,100,150,40,fuente_subtitulo);
			salida=agregarTexto(350,160,150,40,fuente_subtitulo);
			total=agregarTexto(350,220,150,40,fuente_subtitulo);
			//Mas etiquetas			
			t_venta=new JLabel("VENTAS");
			t_venta.setBounds(750, 30, 250, 40);
			t_venta.setFont(fuente_titulo);
			t_venta.setBackground(color_azul);
			t_venta.setForeground(color_fuente);
			t_venta.setOpaque(true);
			t_venta.setBorder(new BevelBorder(BevelBorder.RAISED));
			t_venta.setHorizontalAlignment(SwingConstants.CENTER);
			agregarLabel("VENTAS A CONTADO:",680, 100, 250, 40,fuente_subtitulo, color_azul, color_fuente);
			agregarLabel("VENTAS A CREDITO:",680, 160, 250, 40,fuente_subtitulo, color_azul, color_fuente);
			agregarLabel("TOTAL EN VENTAS:",680, 220, 250, 40,fuente_subtitulo, color_azul, color_fuente);
			//Mas campos de textos
			contado=agregarTexto(950,100,150,40,fuente_subtitulo);
			credito=agregarTexto(950,160,150,40,fuente_subtitulo);
			total_venta=agregarTexto(950,220,150,40,fuente_subtitulo);
			//Mas etiquetas
			t_cantidad=new JLabel("CANTIDAD EN VENTAS");
			t_cantidad.setBounds(1350, 30, 280, 40);
			t_cantidad.setFont(fuente_titulo);
			t_cantidad.setBackground(color_rojo);
			t_cantidad.setForeground(color_fuente);
			t_cantidad.setOpaque(true);
			t_cantidad.setBorder(new BevelBorder(BevelBorder.RAISED));
			t_cantidad.setHorizontalAlignment(SwingConstants.CENTER);
			agregarLabel("CANTIDAD A CONTADO:",1280, 100, 250, 40,fuente_subtitulo, color_rojo, color_fuente);
			agregarLabel("CANTIDAD A CREDITO:",1280, 160, 250, 40,fuente_subtitulo, color_rojo, color_fuente);
			agregarLabel("CANTIDAD TOTAL:",1280, 220, 250, 40,fuente_subtitulo, color_rojo, color_fuente);
			//Mas campo de texto
			cantidad_contado=agregarTexto(1550,100,150,40,fuente_subtitulo);
			cantidad_credito=agregarTexto(1550,160,150,40,fuente_subtitulo);
			cantidad_total=agregarTexto(1550,220,150,40,fuente_subtitulo);
			//Etiqueta del combobox
			t_accion=new JLabel("Que desea hacer: ");
			t_accion.setBounds(100, 400, 150, 35);
			t_accion.setFont(fuente_subtitulo);
			//Array de las opciones del combobox
			String[] nombres={"Generar archivo PDF", "Imprimir balance"};
			//Iniciamos el combobox y lo configuramos
			op_balance=new JComboBox(nombres);
			op_balance.setBounds(260, 400, 250, 35);
			op_balance.setFont(fuente_subtitulo);
			op_balance.setLightWeightPopupEnabled(false);
			//Agregamos el boton y le pasamos el evento			
			aceptar_accion=new JButton("Aceptar");
			aceptar_accion.setBounds(520, 400, 100, 35);
			aceptar_accion.addActionListener(new ActionListener(){
				
				public void actionPerformed(ActionEvent arg0) {
					//Aqui verificamos si se selecciono generar pdf o imprimir, en cualquiera de los casos llamamos a la clase Generar Balance
					if(op_balance.getSelectedIndex()==0){
						//Aqui solo mostramos los datos en pdf
						GenerarBalance balance=new GenerarBalance(a_entrada, a_salida, a_contado, a_credito,a_cantidad_contado, a_cantidad_credito);
						balance.show();
						
					} else if (op_balance.getSelectedIndex()==1){
						//Aqui imprimimos
						GenerarBalance balance=new GenerarBalance(a_entrada, a_salida, a_contado, a_credito,a_cantidad_contado, a_cantidad_credito);
						balance.print();
						
					}
					
				}
				
			});
			//Agregamos los componentes al panel			
			panel_central.add(t_efectivo);			
			panel_central.add(entrada);
			panel_central.add(salida);
			panel_central.add(total);
			
			panel_central.add(t_venta);
			panel_central.add(contado);
			panel_central.add(credito);
			panel_central.add(total_venta);
			
			panel_central.add(t_cantidad);
			panel_central.add(cantidad_contado);
			panel_central.add(cantidad_credito);
			panel_central.add(cantidad_total);
			
			panel_central.add(t_accion);
			panel_central.add(op_balance);
			panel_central.add(aceptar_accion);
			
			//Capturamos la suma de las entradas			
			factura=getList("select sum(p.total) from " + Factura.class.getName() + " p where total>0 and fecha_pago='" + fecha_actual + "'");
			Double num=(Double)factura.get(0);
			//Si es nula, le pasamos 0
			if(num==null){
				
				a_entrada=0;
			//Sino, convertimos el valor a float
			} else {
			
				a_entrada=num.floatValue();
			}
			//Capturamos la suma de las salidas
			factura=getList("select sum(p.total) from " + Factura.class.getName() + " p where total<0 and fecha_pago='" + fecha_actual + "'");
			num=(Double)factura.get(0);
			//Si es nula es 0
			if(num==null){
				
				a_salida=0;
			//Sino convertimos el positivo y lo convertimos a floar	
			} else {
			
				a_salida=-num.floatValue();
			
			}
			//Para representar los valores decimales
			DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
		    simbolo.setDecimalSeparator('.');
		    simbolo.setGroupingSeparator(',');
			DecimalFormat decimal=new DecimalFormat("###,###.##", simbolo);
			//establecemos el texto en los campos de textos
			entrada.setText("$" + a_entrada);
			salida.setText("$" + a_salida);
			total.setText("$" + decimal.format((a_entrada-a_salida)));
			
			//Aqui capturamos la suma de los pagos a contado
			factura=getList("select sum(p.total) from " + Factura.class.getName() + " p where fecha='" + fecha_actual + "' and fecha_pago='" + fecha_actual + "' and prendas>0");
			num=(Double)factura.get(0);
			
			if(num==null){
				
				a_contado=0;
				
			} else {
			
				a_contado=num.floatValue();
			
			}
			//Aqui capturamos la suma de los pagos a creditos
			factura=getList("select sum(p.total) from " + Factura.class.getName() + " p where fecha='" + fecha_actual + "' and fecha_pago is null and prendas>0");
			num=(Double)factura.get(0);
			
			if(num==null){
				
				a_credito=0;
				
			} else {
			
				a_credito=num.floatValue();
			
			}
			//Establecemos los valores en los campos de textos
			contado.setText("$" + a_contado);
			credito.setText("$" + a_credito);
			total_venta.setText("$" + decimal.format(a_contado+a_credito));
			//Contamos la cantidad de ventas al contado
			factura=getList("select count(p) from " + Factura.class.getName() + " p where fecha='" + fecha_actual + "' and fecha_pago='" + fecha_actual + "' and prendas>0");
			Long num2=(Long)factura.get(0);
			
			a_cantidad_contado=num2.intValue();
			//Capturamos la cantidad de venta a credito						
			factura=getList("select count(p) from " + Factura.class.getName() + " p where fecha='" + fecha_actual + "' and fecha_pago is null and prendas>0");
			num2=(Long)factura.get(0);
			
			a_cantidad_credito=num2.intValue();
			//Establecemos los valores en los campos de textos	
			cantidad_contado.setText("" + a_cantidad_contado);
			cantidad_credito.setText("" + a_cantidad_credito);
			cantidad_total.setText("" + (a_cantidad_contado+a_cantidad_credito));
									
			//Llamamos a la funcion setPanelCentral que establece el panel en el centro
			setPanelCentral(panel_central);
						
		}
		//Funcion que establece el panel del balance general
		private void setPanelGeneral(){
			//Removemos si habia un panel puesto anteriormente
			if(panel_central!=null){
				
				removePanel();
				
			}
									
			//Activamos y desactivamos los botones correspondientes
			balance.setEnabled(true);
			balance_general.setEnabled(false);
			reportes_fecha.setEnabled(true);
			//Iniciamos el panel y establecemos el color de fondo
			panel_central=new JPanel(new BorderLayout());
			panel_central.setBackground(Color.WHITE);
			//Iniciamos la tabla que no sea editable
			tabla_balance=new JTable(){
				
				public boolean isCellEditable(int rowIndex, int colIndex) {
					 
					return false;
				}
				
			};
			//Iniciamos el modelo de la tabla y la barra de desplazamiento
			modelo = new DefaultTableModel();
		    desplazamiento = new JScrollPane(tabla_balance);
		    //Array con los nombre de las columnas
		    String[] columnas={"Nº Factura", "NIF", "Nombre", "Apellido", "Prendas", "Total PVP", "Estado"};
		    //Establecemos las columnas
		    modelo.setColumnIdentifiers(columnas);
		    //Indicamos que la barra se desplaze las veces necesarias		    	    	    
		    desplazamiento.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	        desplazamiento.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	        //Columnas auto ajustables
	        tabla_balance.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
	        tabla_balance.setFillsViewportHeight(true);
	        
	        //Para indicar que el valor este centrado en la celda
	        DefaultTableCellRenderer render=new DefaultTableCellRenderer();
	        render.setHorizontalAlignment(SwingConstants.CENTER);
	                       
	        tabla_balance.setModel(modelo);
	        
	        tabla_balance.getColumnModel().getColumn(0).setCellRenderer(render);
	        tabla_balance.getColumnModel().getColumn(1).setCellRenderer(render);
	        tabla_balance.getColumnModel().getColumn(4).setCellRenderer(render);
	        tabla_balance.getColumnModel().getColumn(5).setCellRenderer(render);
	        tabla_balance.getColumnModel().getColumn(6).setCellRenderer(render);
	        //Establecemos el ancho de las columnas
	        TableColumnModel columnModel = tabla_balance.getColumnModel();
	        columnModel.getColumn(0).setPreferredWidth(150);
	        columnModel.getColumn(1).setPreferredWidth(200);
	        columnModel.getColumn(2).setPreferredWidth(400);
	        columnModel.getColumn(3).setPreferredWidth(400);
	        columnModel.getColumn(4).setPreferredWidth(150);
	        columnModel.getColumn(5).setPreferredWidth(200);
	        columnModel.getColumn(6).setPreferredWidth(200);
	        //Iniciamos session en hibernate
	        SessionFactory sesion=HibernateUtil.getSessionFactory();
			Session session=sesion.openSession();
			Transaction tx=session.beginTransaction();
	        //Capturamos el listado de factura con la fecha actual
	        factura=getList("select p.id from " + Factura.class.getName() + " p where fecha='" + fecha_actual + "'");
			//Recorremos con un array el listado y vamos cargado las facturas	        
			for(int i=0;i<factura.size();i++){
				//Cargamos la factura con el id recojido						
				Factura mifactura=(Factura)session.get(Factura.class, (Integer)factura.get(i));
				//Guardamos los valores
				int id=mifactura.getId();
				String nif=mifactura.getClientes().getDni();
				String nombre=mifactura.getClientes().getNombre();
				String apellido=mifactura.getClientes().getApellido();
				int prendas=mifactura.getPrendas();
				float total=mifactura.getTotal();
				boolean aux=mifactura.isEstado();
				
				if(aux){
					
					estado="Pagado";
				
				}else{
					
					estado="Pendiente";
					
				}
				//Agregamos la fila a la tabla
				modelo.addRow(new Object[]{id, nif, nombre, apellido, prendas, total, estado});
							
			}
            
			tx.commit();
			//Creamos otro panel que va en la parte sur
			JPanel panel_sur=new JPanel(new FlowLayout(FlowLayout.LEFT, 20,15));
			panel_sur.setBackground(Color.WHITE);
			//Etiqueta para mostrar titulo
			t_accion=new JLabel("Que desea hacer: ");
			t_accion.setPreferredSize(new Dimension(150,35));
			t_accion.setFont(fuente_subtitulo);
			
			String[] nombres={"Generar archivo PDF", "Imprimir balance general"};
			//Iniciamos el combobox
			op_balance=new JComboBox(nombres);
			op_balance.setPreferredSize(new Dimension(250,35));
			op_balance.setFont(fuente_subtitulo);
			op_balance.setLightWeightPopupEnabled(false);
			//Iniciamos el boton y le pasamos el evento			
			aceptar_accion=new JButton("Aceptar");
			aceptar_accion.setPreferredSize(new Dimension(100,35));
			aceptar_accion.addActionListener(new ActionListener(){
				
				public void actionPerformed(ActionEvent arg0) {
					//En caso que sea la primera seleccion, generamos el balance con la clase BalanceGeneral y mostramos
					if(op_balance.getSelectedIndex()==0){
						
						BalanceGeneral general=new BalanceGeneral(tabla_balance);
						general.show();
					//Aqui imprimimos y no mostramos
					} else if (op_balance.getSelectedIndex()==1){
						
						BalanceGeneral general=new BalanceGeneral(tabla_balance);
						general.print();
						
					}
					
				}
								
			});
	        //Agregamos los componentes a los panales	        
	        panel_sur.add(t_accion);
	        panel_sur.add(op_balance);
	        panel_sur.add(aceptar_accion);
			
			panel_central.add(desplazamiento, BorderLayout.CENTER);
	        panel_central.add(panel_sur, BorderLayout.SOUTH);
	        
	        //Establecemos el panel con la funcion setPanelCentral
			setPanelCentral(panel_central);
					
		}
		//Funcion para establecer el panel de reportes por fecha		
		private void setPanelReportes(){
			//Removemos cualquier panel anterior
			if(panel_central!=null){
				
				removePanel();
				
			}
			//Activamos y desactivamos los botones correspondientes
			balance.setEnabled(true);
			balance_general.setEnabled(true);
			reportes_fecha.setEnabled(false);
			//Iniciamos el panel
			panel_central=new JPanel(null);
			panel_central.setBackground(Color.WHITE);
			
			Font fuente_titulo=new Font("Serif", Font.BOLD, 24);
			//Agregamos las etiquetas
			t_reportes=new JLabel("SELECCIONE LAS FECHAS");
			t_reportes.setBounds(240, 20, 350, 40);
			t_reportes.setFont(fuente_titulo);
			t_reportes.setBackground(new Color(93, 173, 226));
			t_reportes.setForeground(Color.WHITE);
			t_reportes.setOpaque(true);
			t_reportes.setBorder(new BevelBorder(BevelBorder.RAISED));
			t_reportes.setHorizontalAlignment(SwingConstants.CENTER);
			
			desde=new JLabel("Desde: ");
			desde.setBounds(240, 100, 150, 35);
			desde.setFont(fuente_titulo);
			
			hasta=new JLabel("Hasta: ");
			hasta.setBounds(240, 160, 150, 35);
			hasta.setFont(fuente_titulo);
			//Aqui utilizamos la libreria de JDatePicker, elegimos el sqlDateModel para utilizar el formato sql						
			SqlDateModel model = new SqlDateModel();
			SqlDateModel model2 = new SqlDateModel();
			
			//Iniciamos las propiedades del JDatePicker
			Properties p = new Properties();
			p.put("text.today", "Hoy");
			p.put("text.month", "Mes");
			p.put("text.year", "Año");
			//Iniciamos el panel del JDate y le pasamos el modelo y la propiedad
			JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
			JDatePanelImpl datePanel2 = new JDatePanelImpl(model2, p);
			// Instaciamos el objeto de JDatePicker y le pasamos el panel
			JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateComponentFormatter());
			JDatePickerImpl datePicker2 = new JDatePickerImpl(datePanel2, new DateComponentFormatter());
			//Establecemos las posiciones y le indicamos que nos muestre la fecha actual
			datePicker.setBounds(400, 100, 200, 30);
			datePicker2.setBounds(400, 160, 200, 30);
			model.setSelected(true);
			model2.setSelected(true);
			//Agregamos el boton y le pasamos el evento						
			aceptar_reporte=new JButton("Generar");
			aceptar_reporte.setBounds(350, 230, 120, 60);
			aceptar_reporte.addActionListener(new ActionListener(){
				
				public void actionPerformed(ActionEvent arg0) {
					//Capturamos las dos fechas en formato sql
					Date fecha_desde = (java.sql.Date) datePicker.getModel().getValue();
					Date fecha_hasta = (java.sql.Date) datePicker2.getModel().getValue();
					//LLamamos a la clase ReporteFecha y le pasamos las fechas por parametro
					ReporteFecha reporte=new ReporteFecha(fecha_desde, fecha_hasta);
					reporte.show();
					
				}
								
			});
			//Agregamos los componentes al panel										
			panel_central.add(t_reportes);
			panel_central.add(desde);
			panel_central.add(datePicker);
			panel_central.add(hasta);
			panel_central.add(datePicker2);
			panel_central.add(aceptar_reporte);
			//Llamamos a la funcion para establecer el panel			
			setPanelCentral(panel_central);
						
		}
		//Funcion para agregar etiquetas
		private void agregarLabel(String name, int x, int y, int ancho, int alto, Font fuente, Color color_fondo, Color color_fuente){
			
			JLabel label=new JLabel(name);
			label.setBounds(x, y, ancho, alto);
			label.setFont(fuente);
			label.setBackground(color_fondo);
			label.setForeground(color_fuente);
			label.setOpaque(true);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			panel_central.add(label);
						
		}
		//Funcion que devuelve un JTextField configurado
		private JTextField agregarTexto(int x, int y, int ancho, int alto, Font fuente){
			
			JTextField text=new JTextField();
			text.setBounds(x, y, ancho, alto);
			text.setFont(fuente);
			text.setHorizontalAlignment(SwingConstants.RIGHT);
			text.setEditable(false);
			
			return text;
						
		}
				
	}
	//Funcion para establece el panel central
	private void setPanelCentral(JPanel panel){
				
		
		this.add(panel, BorderLayout.CENTER);
				
	}
	//Funcion para remover el panel central
	private void removePanel(){
		
		this.remove(panel_central);
		
	}
	//Funcion que devuelve una lista, recibe la consulta por parametro
	private List getList(String consulta){
		
		SessionFactory sesion=HibernateUtil.getSessionFactory();
		Session session=sesion.openSession();
		Transaction tx=session.beginTransaction();
		
		Query query = session.createQuery(consulta);
		
		List servicios=query.list();
		
		tx.commit();
				
		return servicios;
				
	}
		
}
