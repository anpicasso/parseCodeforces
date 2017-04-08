import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Alumno {
	public String cfUser;
	int numMaxSolSeguidas;
	List<Problema> Soluciones;
	int numSolucionesAceptadas;
	int numSolucionesDenegadas;
	int numSol1;
	int numSol2;
	int numSol3;
	
	Alumno(String user){
		cfUser = user;
		Soluciones = new ArrayList<Problema>();
		numSolucionesAceptadas = 0;
		numSolucionesDenegadas = 0;
		numSol1=0;
		numSol2=0;
		numSol3=0;
		try{
			System.out.println("Haciendo conteo de problemas de " + user);
			String html = "";
	        int i = 1;
	        int max = 1;
	        boolean seguida = false;
	        int numSeguidas = 0;
	        int cont = 1;
	        boolean estado = true;
	        String fecha;
	        while(i<=max){
	        	html = "";
	        	String x = String.valueOf(i);
	        	URL url = new URL("http://codeforces.com/submissions/" + user + "/page/" + x +"/");
	    		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
	    		String inputLine;
	            System.out.println('\t' + "pagina" + i);
				while ((inputLine = in.readLine()) != null){
				    html+=inputLine;
				}
				in.close();
				if(i==1){
					try {
						int j = html.indexOf("class=\"page-index\"");
						int jAnt = j;
						while(j!=-1){
							jAnt = j;
							j = html.indexOf("class=\"page-index\"",j+20);
						}
						jAnt += 30;
						j = html.indexOf("\"",jAnt);
						max = Integer.parseInt(html.substring(jAnt,j));
					} catch (Exception e1) {

					}
				}
				
				int j = html.indexOf("<td class=\"status-small\">");
				
				int aux = j;
				while(j!=-1){
					while(html.charAt(j)!='2'){
						j++;
					}
					fecha = html.substring(j,j+19);
					j = html.indexOf("<a href=\"/problemset/problem",j) + 29;
					int fin = j;
					while(html.charAt(fin)!='/')
						fin++;
					String concu = html.substring(j,fin);
					char prob = html.charAt(fin+1);
					concu=concu+""+prob;					
					j = html.indexOf("<span class=\"submissionVerdictWrapper\"",j);
					j = html.indexOf(";\">",j)+10;
					j = html.indexOf("<",j);
					char z = html.charAt(j-1);
					if(z=='d'){
						if(seguida){
							cont++;
							if(cont>numSeguidas){
								numSeguidas = cont;
							}
						}
						numSolucionesAceptadas++;
						seguida = true;
						estado = true;
					}else{
						estado = false;
						cont = 0;
						seguida = false;
						numSolucionesDenegadas++;
					}
					Problema auxx = null;
					try{
						auxx = new Problema(concu,estado,fecha);
						if(auxx.numSoluciones<=100 && estado){
		        			numSol1++;
		        		}else if(auxx.numSoluciones>100 && auxx.numSoluciones<5000 && estado){
		        			numSol2++;
		        		}else if(estado){
		        			numSol3++;
		        		}
					}catch(Exception e){
						e.printStackTrace();
						System.out.println(user+" "+concu);
					}
					Soluciones.add(auxx);
					j = html.indexOf("<td class=\"status-small\">",aux+30);
					aux = j;
				}
				i++;
	        }
	        numMaxSolSeguidas = numSeguidas;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString(){
		return cfUser + " - " + Soluciones.size() + " - " + numSolucionesAceptadas
				+ " - " + numSolucionesDenegadas + " - " + numMaxSolSeguidas;
	}
	
	public static void main(String[] args) throws Exception {
		Alumno a = new Alumno("abner.1496");
		System.out.println(a.toString());
	}
}
