package Controlador;

import java.awt.Desktop;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JTable;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import TablasBD.Clientes;
import TablasBD.Factura;
import TablasBD.HibernateUtil;
import Vista.PanelVentas;
//Funcion para generar el ticket de venta
public class GenerarTicket {
	//Declaramos la variables
	private int id_factura;
	private JTable tabla_factura;
	private int filas_total;
	private PdfPTable tabla;
	private boolean estado;
	private ArrayList<String> perchas_asignadas; 
	private String pagado, devuelta;
	private Clientes cliente;
	//Contructor de la clase, recibe el id de la factura, si esta pagada o no, el arraylist de las perchas asignandas, el monto pagado y el monto devuelto
	public GenerarTicket(int id_factura, boolean estado, ArrayList<String> perchas_asignadas, String pagado, String devuelta){
		//Iniciamos las variables y llamamos a la clase estatica getDatosTabla para saber las descripcion de los servicios
		this.id_factura=id_factura;
		tabla_factura=PanelVentas.getDatosTabla();
		this.estado=estado;
		this.perchas_asignadas=perchas_asignadas;
		this.pagado=pagado;
		this.devuelta=devuelta;
		//Llamamos a la fucion generarTicket que inicializa todo
		generarTicket();
		
	}
	
	public void generarTicket(){
				
		try {
			//Conectamos con la base de datos
			SessionFactory sesion=HibernateUtil.getSessionFactory();
			Session session=sesion.openSession();
			Transaction tx=session.beginTransaction();
			//Cargamos la factura
			Factura factura=(Factura)session.get(Factura.class, id_factura);
			//Cargamos el cliente
			cliente=factura.getClientes();
			
			//Capturamos la cantidad de fila de la tabla de factura y definimos las diferentes fuentes que vamos a utilizar de la libreria iText
			filas_total=tabla_factura.getRowCount();
			Font header=new Font(FontFamily.HELVETICA, 7);
			Font titulo=new Font(FontFamily.TIMES_ROMAN, 12);
			Font cuerpo=new Font(FontFamily.COURIER, 9);
			Font cuerpo2=new Font();
			cuerpo2.setFamily("Arial");
			cuerpo2.setSize(10);
			cuerpo2.setStyle(Font.BOLD);
			//Establece el tamaño del ticket, si son muchas filas se va alargando
			Rectangle pagesize=new Rectangle(227, 400+(filas_total*25));
			//Capturamos la fecha
			Calendar fecha = Calendar.getInstance();
			//Capturamos la imagen que va al pie del ticket
			Image imagen = Image.getInstance(this.getClass().getResource("/Recursos/PDF - LabCleaner.jpg"));
			imagen.scalePercent(25);
			imagen.setAlignment(Element.ALIGN_CENTER);
			//Abrimos el Documento de la libreria iText, declaramos un archivo de escritura
			Document ticket=new Document(pagesize, 5,5,5,5);
			FileOutputStream ficheroPdf = new FileOutputStream("src/Tickets/" + id_factura + "-" + cliente.getDni() + ".pdf");
			PdfWriter.getInstance(ticket,ficheroPdf).setInitialLeading(20);
			//Abrimos archivo
			ticket.open();
			//Deficimos los parrafos y la imagen y todo lo relativo a la informacion de la empresa
			Paragraph parrafo=new Paragraph("Calle Duarte No. 9 casi esq. Contitucion", header);
			parrafo.setAlignment(Element.ALIGN_CENTER);
							
			ticket.add(imagen);
			ticket.add(parrafo);
			
			parrafo=new Paragraph("A Coruña, Tel. 888-888-8888", header);
			parrafo.setAlignment(Element.ALIGN_CENTER);
			ticket.add(parrafo);
			
			parrafo=new Paragraph("NIF: 1-3130205-1\n", header);
			parrafo.setAlignment(Element.ALIGN_CENTER);
			ticket.add(parrafo);
			//Numero de ticket que es igual al numero de la factura en la base de datos
			parrafo=new Paragraph("Ticket Nº: " + id_factura + "\n", titulo);
			parrafo.setAlignment(Element.ALIGN_CENTER);
			ticket.add(parrafo);
			//fecha y hora
			int dia=fecha.get(Calendar.DAY_OF_MONTH);
			int mes=fecha.get(Calendar.MONTH)+1;
			if(mes==12){
				mes=1;
			}
			int ano=fecha.get(Calendar.YEAR);
			int hora = fecha.get(Calendar.HOUR_OF_DAY);
	        int minuto = fecha.get(Calendar.MINUTE);
	        int segundo = fecha.get(Calendar.SECOND);
			
			ticket.add(new Paragraph("Fecha: " + dia + "/" + mes + "/" +  ano + "         Hora: " + hora + ":" + minuto + ":" + segundo + "\n", cuerpo));
			
			ticket.add(new Paragraph("Cliente: " + cliente.getNombre() + " " + cliente.getApellido(), cuerpo2));
			ticket.add(new Paragraph("NIF: " + cliente.getDni(), cuerpo2));
			ticket.add(new Paragraph("------------------------------------------------------"));
			//Definimos una tabla de 4 columnas para la descripcion del ticket de venta
			tabla=new PdfPTable(4);
			//Tamaño de las columnas
			float[] medidaCeldas = {2.00f, 0.65f, 0.85f, 0.85f};
						
			tabla.setWidthPercentage(100);
			tabla.setWidths(medidaCeldas);
			
			PdfPCell celda1 = new PdfPCell(new Paragraph("Descripcion", header));
			PdfPCell celda2 = new PdfPCell(new Paragraph("Cant.", header));
			PdfPCell celda3 = new PdfPCell(new Paragraph("Precio", header));
			PdfPCell celda4 = new PdfPCell(new Paragraph("PVP", header));
			//Definicion de bordes: sin bordes
			celda1.setBorder(Rectangle.BOTTOM);
			celda2.setBorder(Rectangle.BOTTOM);
			celda3.setBorder(Rectangle.BOTTOM);
			celda4.setBorder(Rectangle.BOTTOM);
			//Agregamos las celdas			
			tabla.addCell(celda1);
            tabla.addCell(celda2);
            tabla.addCell(celda3);
            tabla.addCell(celda4);
                        
            //Recorremos el total de filas de la tabla factura
            for(int i=0;i<filas_total;i++){
            	//Definicion de la informacion de las celdas  
            	PdfPCell aux_celda1 = new PdfPCell(new Paragraph(tabla_factura.getValueAt(i, 1).toString(), header));
    			PdfPCell aux_celda2 = new PdfPCell(new Paragraph(tabla_factura.getValueAt(i, 3).toString(), header));
    			PdfPCell aux_celda3 = new PdfPCell(new Paragraph(tabla_factura.getValueAt(i, 2).toString(), header));
    			PdfPCell aux_celda4 = new PdfPCell(new Paragraph(tabla_factura.getValueAt(i, 4).toString(), header));
    			//Sin bordes
    			aux_celda1.setBorder(Rectangle.NO_BORDER);
    			aux_celda2.setBorder(Rectangle.NO_BORDER);
    			aux_celda3.setBorder(Rectangle.NO_BORDER);
    			aux_celda4.setBorder(Rectangle.NO_BORDER);
    			//Agregamos las celdas            	
            	tabla.addCell(aux_celda1);
            	tabla.addCell(aux_celda2);
            	tabla.addCell(aux_celda3);
            	tabla.addCell(aux_celda4);
            	            	
            }
			//Celdas vacia solo para definir un borde superior
            PdfPCell celdavacia1 = new PdfPCell(new Paragraph("", header));
			PdfPCell celdavacia2 = new PdfPCell(new Paragraph("", header));
			PdfPCell celdavacia3 = new PdfPCell(new Paragraph("", header));
			PdfPCell celdavacia4 = new PdfPCell(new Paragraph("", header));
			
			celdavacia1.setBorder(Rectangle.TOP);
			celdavacia2.setBorder(Rectangle.TOP);
			celdavacia3.setBorder(Rectangle.TOP);
			celdavacia4.setBorder(Rectangle.TOP);
						
			tabla.addCell(celdavacia1);
            tabla.addCell(celdavacia2);
            tabla.addCell(celdavacia3);
            tabla.addCell(celdavacia4);
            //Agregamos la tabla al ticket                  
            ticket.add(tabla);
            //Parrafo que indica el total de articulos
            parrafo=new Paragraph("Total de articulos: " + factura.getPrendas(), cuerpo2);
			parrafo.setAlignment(Element.ALIGN_LEFT);
			ticket.add(parrafo);
            //Total factura
			parrafo=new Paragraph("PVP Total:   $" + factura.getTotal(), cuerpo2);
			parrafo.setAlignment(Element.ALIGN_RIGHT);
			ticket.add(parrafo);
			//Si se paga en el momento mostramos el efectivo pagado y devuelto
			if(estado){
				
				parrafo=new Paragraph("Efectivo Pagado:       $" + pagado, header);
				parrafo.setAlignment(Element.ALIGN_RIGHT);
				ticket.add(parrafo);
				
				parrafo=new Paragraph("Devuelta:        " + devuelta + "\n\n", header);
				parrafo.setAlignment(Element.ALIGN_RIGHT);
				ticket.add(parrafo);
			//Sino solo mostramos que es una venta a credito					
			} else {
				
				parrafo=new Paragraph("Venta a Credito\n\n", cuerpo2);
				parrafo.setAlignment(Element.ALIGN_RIGHT);
				ticket.add(parrafo);
								
			}
			//Agregamos las posiciones en el carrusel
			parrafo=new Paragraph("Posicion en el carrusel: ", header);
			parrafo.setAlignment(Element.ALIGN_LEFT);
			ticket.add(parrafo);
			
			for(String str: perchas_asignadas){
				
				Chunk linea=new Chunk(str + ",", header);
				ticket.add(linea);
								
			}
			//Espacio hacia debajo
			parrafo=new Paragraph("\n\n", header);
			parrafo.setAlignment(Element.ALIGN_CENTER);
			ticket.add(parrafo);
			//Mensaje solo para mostrar
			parrafo=new Paragraph("Gracias por utilizar nuestro servicio", cuerpo2);
			parrafo.setAlignment(Element.ALIGN_CENTER);
			ticket.add(parrafo);
			
			parrafo=new Paragraph("LabCleaner - Un nuevo concepto de Lavanderia", header);
			parrafo.setAlignment(Element.ALIGN_CENTER);
			ticket.add(parrafo);
			//Cerramos ticket			           
			ticket.close();
			//Cerramos el flujo
			ficheroPdf.close();
			//Abrimos archivo
			
			tx.commit();
			
					
		} catch (FileNotFoundException e) {
			 
			e.printStackTrace();
		
		} catch (DocumentException e) {
		
			e.printStackTrace();
		
		} catch (IOException e) {
			
			e.printStackTrace();
		
		}
				
	}
	
	public void show(){
				
	    try {
	    	
	    	File path = new File ("src/Tickets/" + id_factura + "-" + cliente.getDni() + ".pdf");
			Desktop.getDesktop().open(path);
		
	    } catch (IOException e) {
		
			e.printStackTrace();
		
	    }
		
		
	}
	
	@SuppressWarnings("unused")
	public void print(){
		
		PrinterJob job = PrinterJob.getPrinterJob();
	    job.printDialog();
	    String impresora=job.getPrintService().getName();
	 
	    Desktop desktop = Desktop.getDesktop();
	    File fichero = new File("src/Tickets/" + id_factura + "-" + cliente.getDni() + ".pdf");
	      
	    if (desktop.isSupported(Desktop.Action.PRINT)){
	    	
	    	try {
	              try{
	               
	            	  Process pr = Runtime.getRuntime().exec("Rundll32 printui.dll,PrintUIEntry /y /n \""+impresora+"\"");
	              
	              }catch(Exception ex){
	              
	            	  System.out.println("Ha ocurrido un error al ejecutar el comando. Error: "+ex);
	              }
	        
	              desktop.print(fichero);
	        
	    	} catch (Exception e){
	
	    		System.out.print("El sistema no permite imprimir usando la clase Desktop");
	    		e.printStackTrace();
	    	}
	
	    } else {
	    	
	    	System.out.print("El sistema no permite imprimir usando la clase Desktop");
	    }
	
	}

}
