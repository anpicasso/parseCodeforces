import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Problema {
	int Concurso;
	char numProblema;
	int numSoluciones;
	boolean estado;
	Date fechaHora;
	
	public static Date setDate(String from) throws ParseException{
	    Date d = new Date();
	    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    d = df.parse(from);
	    return d;
	}
	
	public static long getDateDiffSeconds(Date date1, Date date2) {
	    long diffInMillies = date2.getTime() - date1.getTime();
	    return TimeUnit.SECONDS.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}
	
	public static String computeDiff(Date date1, Date date2) {
	    long diffInMillies = date2.getTime() - date1.getTime();
	    List<TimeUnit> units = new ArrayList<TimeUnit>(EnumSet.allOf(TimeUnit.class));
	    Collections.reverse(units);
	    Map<TimeUnit,Long> result = new LinkedHashMap<TimeUnit,Long>();
	    long milliesRest = diffInMillies;
	    for ( TimeUnit unit : units ) {
	        long diff = unit.convert(milliesRest,TimeUnit.MILLISECONDS);
	        long diffInMilliesForUnit = unit.toMillis(diff);
	        milliesRest = milliesRest - diffInMilliesForUnit;
	        result.put(unit,diff);
	    }
	    return String.format("%d:%d:%d:%d",result.get(TimeUnit.DAYS),
						    		result.get(TimeUnit.HOURS),
						    		result.get(TimeUnit.MINUTES),
						    		result.get(TimeUnit.SECONDS));
	}
	
	Problema(String x, boolean y, String fecha) throws ParseException{
		Concurso = Integer.parseInt(x.substring(0,x.length()-1));
		numProblema = x.charAt(x.length()-1);
		estado = y;
		fechaHora = setDate(fecha);
		String concurso = String.valueOf(Concurso);
    	URL url;
    	BufferedReader in;
		try {
			url = new URL("http://codeforces.com/contest/" + concurso +"/");
			in = new BufferedReader(new InputStreamReader(url.openStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null){
				String aux = "/contest/" + concurso + "/status/" + numProblema + "";
				int j = inputLine.indexOf(aux);
				if(j!=-1){
					j = inputLine.indexOf("x") + 1;
					int fin = inputLine.indexOf("<",j);
					numSoluciones = Integer.parseInt(inputLine.substring(j, fin));
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	@Override
	public String toString(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return Concurso + "" + numProblema + " - " + numSoluciones + " - " + estado + " - " +
			dateFormat.format(fechaHora);
	}
	
	public static void main(String[] args) throws Exception {
		/*Problema x = new Problema("365B",true,"2016-11-17 21:30:10");
		System.out.println(x);
		x = new Problema("4A",true,"2016-11-17 21:30:10");
		System.out.println(x);
		x = new Problema("14A",false,"2016-11-17 21:30:10");
		System.out.println(x);*/
		
		Date date1, date2;
		date2 = setDate("2016-11-17 21:30:10");
		date1 = setDate("2016-11-17 21:27:00");	
		System.out.println(computeDiff(date1, date2) + "\n" + getDateDiffSeconds(date1, date2));
	}
}


