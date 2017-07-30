package Controlador;

import java.awt.Desktop;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfWriter;
//Clase para general el balance del dia
public class GenerarBalance {
	//Declaramos las variables a utilizar
	float entrada, salida, contado, credito;
	int cantidad_contado, cantidad_credito, dia, mes, ano;
	//Constructor de la clase
	public GenerarBalance(float entrada, float salida, float contado, float credito, int cantidad_contado, int cantidad_credito){
		
		this.entrada=entrada;
		this.salida=salida;
		this.contado=contado;
		this.credito=credito;
		this.cantidad_contado=cantidad_contado;
		this.cantidad_credito=cantidad_credito;
		//Llamamos a la funcion generarBalance que realiza toda la funcion
		generarBalance();
		
	}
	//Funcion que genera el archivo pdf
	private void generarBalance(){
		
		try{
			//Declaramos las fuentes a utilizar
			Font titulo=new Font(FontFamily.TIMES_ROMAN, 12);
			Font cuerpo=new Font(FontFamily.COURIER, 9);
			Font cuerpo2=new Font();
			cuerpo2.setFamily("Arial");
			cuerpo2.setSize(10);
			cuerpo2.setStyle(Font.BOLD);
			//Establecemos el tamaño de pagina
			Rectangle pagesize=new Rectangle(227, 400);
			//Instaciamos la clase Calendar para capturar fecha y hora
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
			//Creamos un documento de la libreria iText, le pasamos el tamaño de pagina y los margenes					
			Document ticket=new Document(pagesize, 5,5,5,5);
			//Creamos flujo y el nombre del fichero
			FileOutputStream ficheroPdf = new FileOutputStream("src/Reportes/Balance del dia/Balance del dia " + dia + "-" + mes + "-" + ano +  ".pdf");
			PdfWriter.getInstance(ticket,ficheroPdf).setInitialLeading(20);
			//Abrimos documento
			ticket.open();
			//Agregamos el primer parrafo
			Paragraph parrafo=new Paragraph("BALANCE DEL DIA \n\n", titulo);
			parrafo.setAlignment(Element.ALIGN_CENTER);
			
			ticket.add(parrafo);
			//Agregamos la fecha y hora
			ticket.add(new Paragraph("Fecha: " + dia + "/" + mes + "/" +  ano + "         Hora: " + hora + ":" + minuto + ":" + segundo + "\n\n", cuerpo));
			//Agregamos los valores de efectivo en caja
			parrafo=new Paragraph("EFECTIVO EN CAJA", cuerpo2);
			parrafo.setAlignment(Element.ALIGN_CENTER);
			
			ticket.add(parrafo);
			
			ticket.add(new Paragraph("Entrada de efectivo:   " + entrada, cuerpo));
			ticket.add(new Paragraph("Salida de efectivo:    " + salida, cuerpo));
			ticket.add(new Paragraph("Balance total:         " + (entrada-salida) + "\n\n", cuerpo));
			//Aqui agregamos los valores de las ventas
			parrafo=new Paragraph("VENTAS", cuerpo2);
			parrafo.setAlignment(Element.ALIGN_CENTER);
			
			ticket.add(parrafo);
			
			ticket.add(new Paragraph("Ventas a contado:   " + contado, cuerpo));
			ticket.add(new Paragraph("Ventas a credito:   " + credito, cuerpo));
			ticket.add(new Paragraph("Total de ventas:    " + (contado+credito) + "\n\n", cuerpo));
			//Aqui agregamos los valores de la cantidad de ventas
			parrafo=new Paragraph("CANTIDAD EN VENTAS", cuerpo2);
			parrafo.setAlignment(Element.ALIGN_CENTER);
			
			ticket.add(parrafo);
			
			ticket.add(new Paragraph("Cantidad a contado:   " + cantidad_contado, cuerpo));
			ticket.add(new Paragraph("Cantidad a credito:   " + cantidad_credito, cuerpo));
			ticket.add(new Paragraph("Cantidad total:       " + (cantidad_contado+cantidad_credito), cuerpo));
			//Cerramos ticket y flujo
			ticket.close();
			ficheroPdf.close();
						
			
		
		} catch(Exception e){
			
			e.printStackTrace();
			
		}
				
	}
	//Funcion que abre el archivo
	public void show(){
		
	    try {
	    	
	    	File path = new File ("src/Reportes/Balance del dia/Balance del dia " + dia + "-" + mes + "-" + ano +  ".pdf");
			Desktop.getDesktop().open(path);
		
	    } catch (IOException e) {
		
			e.printStackTrace();
		
	    }
		
		
	}
	//Funcion que llama a imprimir el documento
	@SuppressWarnings("unused")
	public void print(){
		//Instacionamos la clase Printer para que nos de a elegir la impresora
		PrinterJob job = PrinterJob.getPrinterJob();
	    job.printDialog();
	    String impresora=job.getPrintService().getName();
	    //Abrimos el fichero
	    Desktop desktop = Desktop.getDesktop();
	    File fichero = new File("src/Reportes/Balance del dia/Balance del dia " + dia + "-" + mes + "-" + ano +  ".pdf");
	    //Si se puede imprimir, abrimos el proceso  
	    if (desktop.isSupported(Desktop.Action.PRINT)){
	    	
	    	try {
	              try{
	               
	            	  Process pr = Runtime.getRuntime().exec("Rundll32 printui.dll,PrintUIEntry /y /n \""+impresora+"\"");
	              
	              }catch(Exception ex){
	              
	            	  System.out.println("Ha ocurrido un error al ejecutar el comando. Error: "+ex);
	              }
	              //Imprimimos archivo si todo esta correcto
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
