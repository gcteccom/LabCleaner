package Controlador;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import javax.swing.JOptionPane;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import TablasBD.Factura;
import TablasBD.HibernateUtil;
import Vista.VentanaPrincipal;
//Clase encargada de generar el reporte por fechas
@SuppressWarnings("unchecked")
public class ReporteFecha {
	//Declaramos las variables a utilizar
	private Date fecha_desde, fecha_hasta;
	private List<Factura> listado;
	private int dia, mes, ano;
	private String nombre_archivo;
	private PdfPTable tabla;
	private float total_pvp;
	//Constructor de la clase, recibe las dos fecha por parametro
	public ReporteFecha(Date fecha_desde, Date fecha_hasta){
		
		this.fecha_desde=fecha_desde;
		this.fecha_hasta=fecha_hasta;
		//Para calcular el importe totas de ventas
		total_pvp=0;
		//Llamamos a la siguiente funcion para generar el archivo
		generarReporte();
				
	}
	
	private void generarReporte(){
		//Capturamos la lista con los valores devueltos
		listado=getList("select p from " + Factura.class.getName() + " p where fecha>='" + fecha_desde + "' and fecha<='" + fecha_hasta + "' and dni!='nulo'");
		
		try{
			//Establecemos las fuentes a utilizar
			Font titulo=new Font(FontFamily.TIMES_ROMAN, 18);
			Font cuerpo=new Font(FontFamily.HELVETICA, 9);
			Font cuerpo2=new Font();
			cuerpo2.setFamily("Arial");
			cuerpo2.setSize(11);
			cuerpo2.setStyle(Font.BOLD);
			//Capturamos la fecha y hora		
			Calendar fecha = Calendar.getInstance();
			
			dia=fecha.get(Calendar.DAY_OF_MONTH);
			mes=fecha.get(Calendar.MONTH)+1;
			if(mes==12){
				mes=1;
			}
			ano=fecha.get(Calendar.YEAR);
			int hora = fecha.get(Calendar.HOUR_OF_DAY);
	        int minuto = fecha.get(Calendar.MINUTE);
	        int segundo = fecha.get(Calendar.SECOND);
			//Para capturar el nombre que le queremos dar al archivo
	        nombre_archivo=JOptionPane.showInputDialog(VentanaPrincipal.getFrame(), "Introduzca el nombre del PDF a generar");
	        //Abrimos documento con tamaño estandar
			Document ticket=new Document();
			//abrimos el flujo y creamos el archivo 
			FileOutputStream ficheroPdf = new FileOutputStream("src/Reportes/Reportes por fecha/" +  nombre_archivo +  ".pdf");
			PdfWriter.getInstance(ticket,ficheroPdf).setInitialLeading(20);
			//Abrimos documento
			ticket.open();
			//Agregamos el titulo
			Paragraph parrafo=new Paragraph("Reporte desde " + fecha_desde + " hasta " + fecha_hasta + "\n\n", titulo);
			parrafo.setAlignment(Element.ALIGN_CENTER);
			
			ticket.add(parrafo);
			//Agregamos la fecha y hora
			ticket.add(new Paragraph("Fecha: " + dia + "/" + mes + "/" +  ano + "   Hora: " + hora + ":" + minuto + ":" + segundo + "\n\n", cuerpo));
			//Creamos una tabla de ocho columnas
			tabla=new PdfPTable(8);
			//Damos medidas a las columnas
			float[] medidaCeldas = {1.00f, 1.50f, 1.50f, 1.50f, 1.50f, 1.00f, 1.50f, 1.00f};
			
			tabla.setWidthPercentage(100);
			tabla.setWidths(medidaCeldas);
			//Damos nombre a las columnas					
			tabla.addCell(new PdfPCell(new Paragraph("Nº Factura", cuerpo2)));
            tabla.addCell(new PdfPCell(new Paragraph("NIF", cuerpo2)));
            tabla.addCell(new PdfPCell(new Paragraph("Fecha", cuerpo2)));
            tabla.addCell(new PdfPCell(new Paragraph("Fecha de Pago", cuerpo2)));
            tabla.addCell(new PdfPCell(new Paragraph("Fecha de entrega", cuerpo2)));
            tabla.addCell(new PdfPCell(new Paragraph("Prendas", cuerpo2)));
            tabla.addCell(new PdfPCell(new Paragraph("Total PVP", cuerpo2)));
            tabla.addCell(new PdfPCell(new Paragraph("Estado", cuerpo2)));
			//Recorremos el listado de factura
            for(Factura str: listado){
            	 //Capturamos el valor de estado 
            	boolean aux_estado=str.isEstado();
            	String estado;
            	
            	if(aux_estado){
            		//Si es true, establecemos como pagado y sumamos el valor del campo total_pvp
            		estado="Pagado";
            		total_pvp+=str.getTotal();
            	
            	} else {
            		
            		estado="Pendiente";
            		
            	}
            	//Creamos las filas y vamos añadiendo los valores que capturamos del listado
            	PdfPCell aux_celda1 = new PdfPCell(new Paragraph("" + str.getId(), cuerpo));
    			PdfPCell aux_celda2 = new PdfPCell(new Paragraph(str.getClientes().getDni(), cuerpo));
    			PdfPCell aux_celda3 = new PdfPCell(new Paragraph("" + str.getFecha(), cuerpo));
    			PdfPCell aux_celda4 = new PdfPCell(new Paragraph("" + str.getFechaPago(), cuerpo));
    			PdfPCell aux_celda5 = new PdfPCell(new Paragraph("" + str.getFechaEntrega(), cuerpo));
    			PdfPCell aux_celda6 = new PdfPCell(new Paragraph("" + str.getPrendas(), cuerpo));
    			PdfPCell aux_celda7 = new PdfPCell(new Paragraph("" + str.getTotal(), cuerpo));
    			PdfPCell aux_celda8 = new PdfPCell(new Paragraph(estado, cuerpo));
    			//Agregamos las celdas
            	tabla.addCell(aux_celda1);
            	tabla.addCell(aux_celda2);
            	tabla.addCell(aux_celda3);
            	tabla.addCell(aux_celda4);
            	tabla.addCell(aux_celda5);
            	tabla.addCell(aux_celda6);
            	tabla.addCell(aux_celda7);
            	tabla.addCell(aux_celda8);
            	            	
            }
            //Agregamos la tabla
            ticket.add(tabla);
            //Añadimos la ultima linea que dice el total de factura y el total de pvp
            ticket.add(new Paragraph("Total facturas: " + listado.size() + "                    Total de ventas pagadas: " + total_pvp, cuerpo2));
            //Cerramos el documento y el flujo
            ticket.close();
			
			ficheroPdf.close();
					
		} catch(Exception e){
			
			e.printStackTrace();
			
		}
				
	}
	//Funcion para mostrar el archivo generado
	public void show(){
		
	    try {
	    	
	    	File path = new File ("src/Reportes/Reportes por fecha/" +  nombre_archivo +  ".pdf");
			Desktop.getDesktop().open(path);
		
	    } catch (IOException e) {
		
			e.printStackTrace();
		
	    }
				
	}
	//Funcion que devuelve un List de factura
	
	private List<Factura> getList(String consulta){
		
		SessionFactory sesion=HibernateUtil.getSessionFactory();
		Session session=sesion.openSession();
		Transaction tx=session.beginTransaction();
		
		Query query = session.createQuery(consulta);
		
		List<Factura> listado=query.list();
		
		tx.commit();
				
		return listado;
				
	}

}
