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
		Class.forName("com.mysql.cj.jdbc.Driver"); 
		String password = "pavillion"; 
		String username = "root"; 
		String url = "jdbc:mysql://localhost:3306/world?useUnicode=true&useLegacyDatetimeCode=false&serverTimezone=UTC&createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&useSSL=false";
		Connection connessione = DriverManager.getConnection(url, username, password);
		Statement statement = connessione.createStatement();

		while (true) {
			menu();
			int scelta = input.nextInt();
			input.nextLine();
			switch (scelta) {
			case 1: {
				boolean verifica = false;
				String countryCode = verificaCountryCode(input);
				countryCodeQuery(statement, countryCode);
				if(!verifica) {
					System.out.println("Questa Country Code non esiste");
				}
				break;
			}
			case 2: {
				boolean verifica = false;
				System.out.println("Inserisci una nazione di cui vuoi vederne la città più popolosa");
				String nazione = input.nextLine();
				verifica = countryNameQuery(statement, verifica, nazione);
				if(!verifica) {
					System.out.println("Questa nazione non esiste");
				}
				break;
			}
			case 3: {
				boolean verifica = false;
				System.out.println("Inserisci una forma di governo di cui vuoi vedere la nazione con l'estensione maggiore");
				String nazione = input.nextLine();
				verifica = countrySizeQuery(statement, verifica, nazione);
				if(!verifica) {
					System.out.println("Questa forma di governo non esiste");
				}
				break;
			}
			default:
				break;
			}
		}

	}

	public static boolean countrySizeQuery(Statement statement, boolean verifica, String nazione) throws SQLException {
		ResultSet risultatoQuery = statement.executeQuery(
				"select country.code, country.name, country.SurfaceArea, country.GovernmentForm from world.country where country.GovernmentForm = \"" + nazione + "\"" + "order by country.SurfaceArea DESC limit 0,1");
		while (risultatoQuery.next()) {
			String code = risultatoQuery.getString("Code");
			String nomeCitta = risultatoQuery.getString("Name");
			int surfaceArea = risultatoQuery.getInt("SurfaceArea");
			String govermentForm = risultatoQuery.getString("GovernmentForm");
			System.out.println(code + " " + nomeCitta + " " + surfaceArea + " " + govermentForm);
			verifica = true;
		}
		return verifica;
	}

	public static boolean countryNameQuery(Statement statement, boolean verifica, String nazione) throws SQLException {
		ResultSet risultatoQuery = statement.executeQuery(
				"select country.Code, city.Name, country.Continent, city.District, city.Population from world.country inner join city on world.country.Code = world.city.CountryCode where country.Name = \"" + nazione + "\"" + "limit 0,1");
		while (risultatoQuery.next()) {
			String code = risultatoQuery.getString("Code");
			String nomeCitta = risultatoQuery.getString("Name");
			String continent = risultatoQuery.getString("Continent");
			String district = risultatoQuery.getString("District");
			int population = risultatoQuery.getInt("Population");
			System.out.println(code + " " + nomeCitta + " " + continent + " " + district + " "	+ population);
			verifica = true;
		}
		return verifica;
	}

	public static void countryCodeQuery(Statement statement, String countryCode) throws SQLException {
		ResultSet risultatoQuery = statement
				.executeQuery("select * from city where CountryCode =  \"" + countryCode + "\"");
		while (risultatoQuery.next()) {
			String nome = risultatoQuery.getString("Name");
			String codice = risultatoQuery.getString("CountryCode");
			System.out.println(nome + "  " + codice);
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
