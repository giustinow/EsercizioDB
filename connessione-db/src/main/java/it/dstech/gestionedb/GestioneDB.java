package it.dstech.gestionedb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GestioneDB {
	public static final String REGEX_COUNTRY_CODE = "[A-Z]{3}";
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Scanner input = new Scanner(System.in);
		Class.forName("com.mysql.cj.jdbc.Driver"); // in questo punto carichia nella JVM in esecuzione la nostra libreria 
		String password ="pavillion"; // la vostra password
		String username = "root"; // la vostra username
		String url = "jdbc:mysql://localhost:3306/world?useUnicode=true&useLegacyDatetimeCode=false&serverTimezone=UTC&createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&useSSL=false";
		Connection connessione = DriverManager.getConnection(url, username, password);
		Statement statement = connessione.createStatement();
		
//		ResultSet risultatoQuery = statement.executeQuery("select * from city where CountryCode =  \"ITA\" ;");
//		while(risultatoQuery.next()) {
//			int id = risultatoQuery.getInt(1);
//			String nome = risultatoQuery.getString("Name");
//			int pop = risultatoQuery.getInt(5);
//			System.out.println(id + " " + nome + " - " + pop);
//		}
		while(true) {
			menu();
			int scelta = input.nextInt();
			input.nextLine();
			switch (scelta) {
			case 1:{
				String countryCode = verificaCountryCode(input);
				ResultSet risultatoQuery = statement.executeQuery("select * from world.city where CountryCode =  \" countryCode \" ;");
				while(risultatoQuery.next()) {
					String nome = risultatoQuery.getString("Name");
					String codice = risultatoQuery.getString("CountryCode");
					System.out.println(nome + "  " + codice);
				}
				break;
			}
			case 2:{
				break;
			}
			case 3:{
				break;
			}
			default:
				break;
			}
		}
		
	}
	public static String verificaCountryCode(Scanner input) {
		boolean verifica = true;
		while (verifica) {
			System.out.println("Inserisci il Country Code giusto (AAA)");
			String codice = input.nextLine();
			boolean verificaData = checkData(REGEX_COUNTRY_CODE, codice);
			if (verificaData) {
				System.out.println("Country Code esatto");
				verifica = false;
				return codice;
			} else {
				System.out.println("Country Code errato. ");
			}
		}
		return null;
	}
	public static void menu() {
		System.out.println("*********************************************************");
		System.out.println("Fai la tua scelta");
		System.out.println("1. Trova una città attraverso il 'Country Code'");
		System.out.println("2. Scegli la nazione e stampa la città più popolosa");
		System.out.println("3. Stato con estensione territoriale maggiore");
		System.out.println("*********************************************************");
	}
	public static boolean checkData(String regex, String data) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(data);
		return matcher.matches();
	}
}
