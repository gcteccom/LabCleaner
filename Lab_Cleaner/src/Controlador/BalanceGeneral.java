package Controlador;

import java.awt.Desktop;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Properties;

import javax.swing.JTable;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
//Clase que genera el pdf del balance general
public class BalanceGeneral {
	//Declaramos las variables a utilizar
	private JTable tabla_balance;
	int dia, mes, ano;
	private PdfPTable tabla;
	private Properties prop;
	//Constructor de la clase, recibe la tabla con los valores
	public BalanceGeneral(JTable tabla_balance){
		
		this.tabla_balance=tabla_balance;
		//Llamamos a la funcion generarReporte que se encarga de todo el proceso
		generarReporte();
		
		
	}
	//Funcion que genera el archivo pdf
	private void generarReporte(){
		
		try{
			prop = new Properties();
			InputStream is = new FileInputStream("src/Recursos.properties");
			prop.load(is);
			//Declaramos la fuente a utilizar
			Font titulo=new Font(FontFamily.TIMES_ROMAN, 18);
			Font cuerpo=new Font(FontFamily.HELVETICA, 9);
			Font cuerpo2=new Font();
			cuerpo2.setFamily("Arial");
			cuerpo2.setSize(11);
			cuerpo2.setStyle(Font.BOLD);
			//Para capturar la fecha y hora		
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
			//Creamos documento con el tamaño estandar					
			Document ticket=new Document();
			//Abrimos flujo y creamos el archivo
			FileOutputStream ficheroPdf = new FileOutputStream(prop.getProperty("balance.general") + "/Balance General/" + dia + "-" + mes + "-" + ano +  ".pdf");
			PdfWriter.getInstance(ticket,ficheroPdf).setInitialLeading(20);
			//Abrimos documento
			ticket.open();
			//Paramos el titulo
			Paragraph parrafo=new Paragraph("BALANCE GENERAL DEL DIA\n\n", titulo);
			parrafo.setAlignment(Element.ALIGN_CENTER);
			
			ticket.add(parrafo);
			//Establecemos la fecha y hora
			ticket.add(new Paragraph("Fecha: " + dia + "/" + mes + "/" +  ano + "   Hora: " + hora + ":" + minuto + ":" + segundo + "\n\n", cuerpo));
			//Creamos una tabla de 7 columnas
			tabla=new PdfPTable(7);
			//Establecemos las medidas de las columnas
			float[] medidaCeldas = {1.00f, 1.50f, 2.00f, 2.00f, 1.00f, 1.00f, 1.00f};
			
			tabla.setWidthPercentage(100);
			tabla.setWidths(medidaCeldas);
			//Ponemos nombre a las columnas con la fuente correspondiente					
			tabla.addCell(new PdfPCell(new Paragraph("Nº Factura", cuerpo2)));
            tabla.addCell(new PdfPCell(new Paragraph("NIF", cuerpo2)));
            tabla.addCell(new PdfPCell(new Paragraph("Nombre", cuerpo2)));
            tabla.addCell(new PdfPCell(new Paragraph("Apellido", cuerpo2)));
            tabla.addCell(new PdfPCell(new Paragraph("Prendas", cuerpo2)));
            tabla.addCell(new PdfPCell(new Paragraph("Total PVP", cuerpo2)));
            tabla.addCell(new PdfPCell(new Paragraph("Estado", cuerpo2)));
            //Recorremos la tabla
            //Recorremos el total de filas de la tabla factura
            for(int i=0;i<tabla_balance.getRowCount();i++){
            	//Definicion de la informacion de las celdas a traves de la tabla que recibe por parametro  
            	PdfPCell aux_celda1 = new PdfPCell(new Paragraph(tabla_balance.getValueAt(i, 0).toString(), cuerpo));
    			PdfPCell aux_celda2 = new PdfPCell(new Paragraph(tabla_balance.getValueAt(i, 1).toString(), cuerpo));
    			PdfPCell aux_celda3 = new PdfPCell(new Paragraph(tabla_balance.getValueAt(i, 2).toString(), cuerpo));
    			PdfPCell aux_celda4 = new PdfPCell(new Paragraph(tabla_balance.getValueAt(i, 3).toString(), cuerpo));
    			PdfPCell aux_celda5 = new PdfPCell(new Paragraph(tabla_balance.getValueAt(i, 4).toString(), cuerpo));
    			PdfPCell aux_celda6 = new PdfPCell(new Paragraph(tabla_balance.getValueAt(i, 5).toString(), cuerpo));
    			PdfPCell aux_celda7 = new PdfPCell(new Paragraph(tabla_balance.getValueAt(i, 6).toString(), cuerpo));
    			
    			//Agregamos las celdas            	
            	tabla.addCell(aux_celda1);
            	tabla.addCell(aux_celda2);
            	tabla.addCell(aux_celda3);
            	tabla.addCell(aux_celda4);
            	tabla.addCell(aux_celda5);
            	tabla.addCell(aux_celda6);
            	tabla.addCell(aux_celda7);
            	            	
            }
            //Agregamos la tabla al documento
            ticket.add(tabla);
            //Cerramos documento y el flujo
            ticket.close();
			
			ficheroPdf.close();
						
		
		} catch(Exception e){
			
			e.printStackTrace();
			
		}
		
	}
	//Funcion para abrir el archivo generado
	public void show(){
		
	    try {
	    	
	    	File path = new File (prop.getProperty("balance.general") + "/Balance General/" + dia + "-" + mes + "-" + ano +  ".pdf");
			Desktop.getDesktop().open(path);
		
	    } catch (IOException e) {
		
			e.printStackTrace();
		
	    }
		
		
	}
	//Funcion para imprimir el archivo generado
	@SuppressWarnings("unused")
	public void print(){
		
		PrinterJob job = PrinterJob.getPrinterJob();
	    job.printDialog();
	    String impresora=job.getPrintService().getName();
	 
	    Desktop desktop = Desktop.getDesktop();
	    File fichero = new File(prop.getProperty("balance.general") + "/Balance General/" + dia + "-" + mes + "-" + ano +  ".pdf");
	      
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
