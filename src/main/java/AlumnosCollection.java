import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class AlumnosCollection {
	
	List<Alumno> Alumnos;
	long minTempo;
	
	AlumnosCollection(String archivo) throws IOException{
		Alumnos = new ArrayList<Alumno>();
		FileReader fileReader = new FileReader(archivo);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        
        Alumno x = null;
        String line;
        while((line = bufferedReader.readLine()) != null) {
        	x = new Alumno(line);
        	Alumnos.add(x);
        }
        bufferedReader.close();  
	}
	
	void reporteIndividual() throws IOException{
		System.out.println("Generando Reporte Individual...");
		Workbook wb = new XSSFWorkbook();
		String[] th = {
				"Usuario","Max Soluciones Seguidas","Soluciones Aceptadas",
				"Soluciones Denegadas","num Sol<=100","num Sol<=5000 y >=100",
				"num Sol>=5000","Tiempo minimo (segundos)"
		};
		String th2[] = {
				"Concurso","Problema","Num. Soluciones","Fecha y Hora","Time dif.(actual y anterior)","Time dif. (segundos)"
		};
			    
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		for(Alumno alumno:Alumnos){
			long minTime = 999999999;
			Sheet sheet = wb.createSheet(alumno.cfUser);
			Row row = sheet.createRow(0);
			Row row1 = sheet.createRow(1);
			Cell cell;
			Object[] obj = {
					alumno.cfUser, alumno.numMaxSolSeguidas, alumno.numSolucionesAceptadas,
					alumno.numSolucionesDenegadas,alumno.numSol1,alumno.numSol2,
					alumno.numSol3
			};
			int i = 0;
			int width1 = 0;
			for(String x:th){
				cell = row.createCell(i);
				cell.setCellValue(x);
				try{
					cell = row1.createCell(i);
					cell.setCellValue(obj[i].toString());
					if(obj[i].getClass().equals(Integer.class)){
						cell.setCellValue(new Double(obj[i].toString()));
					}
				}catch(Exception e){
				}
				width1 = ((int)(x.length() * 1.14388))*256;
				sheet.setColumnWidth(i, width1);
				i++;
			}
			row = sheet.createRow(3);
			i = 0;
			for(String x:th2){
				cell = row.createCell(i);
				cell.setCellValue(x);
				int width2 = ((int)(x.length() * 1.14388))*256;
				if(width2>width1){
					sheet.setColumnWidth(i, width2);
				}
				i++;
			}
			i=0;
			List<Problema> Problemas = new ArrayList<>();
			for(Problema problema:alumno.Soluciones){
				if(problema.estado){
					Problemas.add(problema);
				}
			}
			for(Problema problema:Problemas){
					row = sheet.createRow(i + 4);
					Object[] obj2 = new Object[6];
					obj2[0] = problema.Concurso;
					obj2[1] = problema.numProblema;
					obj2[2] = problema.numSoluciones;
					obj2[3] = dateFormat.format(problema.fechaHora);
					obj2[4] = "-";
					obj2[5] = "-";
					try {
						obj2[4] = Problema.computeDiff(Problemas.get(i + 1).fechaHora, problema.fechaHora);
						long x = Problema.getDateDiffSeconds(Problemas.get(i + 1).fechaHora,
								problema.fechaHora);
						obj2[5] = x;
						if (x < minTime && x != 0) {
							minTime = x;
						}
					} catch (Exception e) {
					}
					int j = 0;
					for (Object x : obj2) {
						cell = row.createCell(j);
						cell.setCellValue(x.toString());
						if (x.getClass().equals(Integer.class) || x.getClass().equals(Long.class)) {
							cell.setCellValue(new Double(x.toString()));
						}
						j++;
					}
					i++;
				}
			cell = row1.createCell(7);
			cell.setCellValue(new Double(String.valueOf(minTime)));
		}
		try {
            FileOutputStream fileOut = new FileOutputStream("reporteIndividual.xlsx");
            wb.write(fileOut);
            fileOut.close();
            System.out.println("\tReporte Individual Generado.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("\tError Generando Reporte Individual");
        }
		wb.close();
	}

	void reporteGeneral() throws IOException{
		System.out.println("Generando Reporte General...");
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("Reporte General Individual");
		Row row = sheet.createRow(0);
		Cell cell;
		String[] th = {
				"Usuario","Max Soluciones Seguidas","Soluciones Aceptadas",
				"Soluciones Denegadas","num Sol<=100","num Sol<=5000 y >=100",
				"num Sol>=5000","Tiempo minimo (segundos)"
		};
		int i=0;
		for(String x:th){
			cell = row.createCell(i);
			cell.setCellValue(x);
			int width = ((int)(x.length() * 1.14388))*256;
			sheet.setColumnWidth(i, width);
			i++;
		}
		i = 1;
		for(Alumno alumno:Alumnos){
			row = sheet.createRow(i);
			Object[] obj = {
					alumno.cfUser, alumno.numMaxSolSeguidas, alumno.numSolucionesAceptadas,
					alumno.numSolucionesDenegadas,alumno.numSol1,alumno.numSol2,
					alumno.numSol3
			};
			int j = 0;
			for(Object x:obj){
				cell = row.createCell(j);
				cell.setCellValue(x.toString());
				if(obj[j].getClass().equals(Integer.class) || obj[j].getClass().equals(Long.class)){
					cell.setCellValue(new Double(obj[j].toString()));
				}
				j++;
			}
			long minTime = 999999999;
			int w = 0;
			List<Problema> Problemas = new ArrayList<>();
			for(Problema problema:alumno.Soluciones){
				if(problema.estado){
					Problemas.add(problema);
				}
			}
			for(Problema problema:Problemas){
				try{
					long x = Problema.getDateDiffSeconds(Problemas.get(w+1).fechaHora, problema.fechaHora);	
					if(x<minTime && x!=0){
						minTime = x;
					}
				}catch (Exception e){
				}
				w++;
			}
			cell = row.createCell(7);
			cell.setCellValue(new Double(minTime));
			i++;
		}
		
		// Reporte de Comparaciones
		sheet = wb.createSheet("Problemas repetidos entre alumnos");
		row = sheet.createRow(0);
		i = 1;
		for(Alumno alumno:Alumnos){
			cell = row.createCell(i);
			cell.setCellValue(alumno.cfUser);
			i++;
		}
		i = 1;
		for(Alumno alumno1:Alumnos){
			row = sheet.createRow(i);
			cell = row.createCell(0);
			cell.setCellValue(alumno1.cfUser);
			int j = 1;
			for(Alumno alumno2:Alumnos){
				cell = row.createCell(j);
				int repetidos = 0;
				if(!alumno1.cfUser.equals(alumno2.cfUser)){
					for(Problema problema1:alumno1.Soluciones){
						for(Problema problema2:alumno2.Soluciones){
							if((problema1.estado && problema2.estado)
									&& (problema1.Concurso == problema2.Concurso)
									&& (problema1.numProblema == problema2.numProblema)){
								repetidos++;
							}
						}
					}
					cell.setCellValue(repetidos);
				}else{
					cell.setCellValue("-");
				}
				j++;
			}
			i++;
		}
		try {
            FileOutputStream fileOut = new FileOutputStream("reporteGeneral.xlsx");
            wb.write(fileOut);
            fileOut.close();
            System.out.println("\tReporte General Generado.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("\tError Generando Reporte General");
        }
		wb.close();
	}

	public static void main(String[] args) throws Exception {
		AlumnosCollection Alumnos = new AlumnosCollection("alumnos.txt");	
		Alumnos.reporteIndividual();
		Alumnos.reporteGeneral();
		//Runtime runtime = Runtime.getRuntime();
	    //Process proc = runtime.exec("shutdown -s -t 5");
	    //proc = runtime.exec("Taskkill /IM eclipse.exe /F");
	    //System.exit(0);
	}
}
