package com.challenge.Apirest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@SpringBootApplication
public class JemersoftChallengeApplication {

	public JemersoftChallengeApplication() throws IOException {
	}

	public static void main(String[] args) {
		SpringApplication.run(JemersoftChallengeApplication.class, args);
		/*try {
			URL url = new URL("https://pokeapi.co/api/v2/pokemon?offset=0&limit=60");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();

			int responseCode = conn.getResponseCode();
			if (responseCode != 200) {
				throw new RuntimeException("Error " + responseCode);
			} else {
				StringBuilder informationString = new StringBuilder();
				Scanner input = new Scanner(url.openStream());
				while (input.hasNext()) {
					informationString.append(input.nextLine());
				}
				input.close();
				System.out.println(informationString);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
}
