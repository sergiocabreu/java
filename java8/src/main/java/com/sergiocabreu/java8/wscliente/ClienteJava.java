package com.sergiocabreu.java8.wscliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ClienteJava {

	public static void main(String[] args) {
		try {
			URL url = new URL("http://localhost:8080/deuzika/ws/consultar_ocorrencia.do?emailUsuario=sergio@email.com");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			System.out.println("Output from Server .... \n");
			
			StringBuilder json = new StringBuilder();
			while ((output = br.readLine()) != null) {
				//output += output;//System.out.println(output);
				//System.out.println(output);
				json.append(output);
				         //System.out.println(output);
			}

			conn.disconnect();
			criaObjetoJasonComParse(json.toString());
			//criaObjetoComJason(json.toString());

		} catch (IOException e ) {
			e.printStackTrace();
		}		
	}
	
	public static void criaObjetoJasonComParse(String json){
		System.out.println("criaObjetoJasonComParse");
		
		JsonParser parser = new JsonParser();		
		JsonElement je = parser.parse(json);
		JsonObject jsonObject =  je.getAsJsonObject();
		JsonArray array =  (JsonArray) jsonObject.get("msg");		
		Gson gson = new Gson();
		
		for ( JsonElement e : array ){						
			Ocorrencia ocorrencia = gson.fromJson(gson.toJson(e), Ocorrencia.class);
			
			System.out.println(ocorrencia.getDescricao());
			System.out.println(ocorrencia.getDataFim());
			System.out.println(ocorrencia.getDataFim().getTime());
			System.out.println(ocorrencia.getDataInicio());
			System.out.println(ocorrencia.getDataInicio().getTime());
			
			System.out.println(new Date().getTime());
		}
	}
	
	
	public static void criaObjetoComJason(String json){
		Gson gson = new Gson();
		
		JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
						
		JsonArray array =  (JsonArray) jsonObject.get("msg");
		
/*		for ( JsonElement e : array ){
			System.out.println(e.getAsString());
		}*/
		
/*		System.out.println(jo.getAsJsonArray("msg"));
		System.out.println(je);*/


		
		//System.out.println(objectParent.);
	}

}

