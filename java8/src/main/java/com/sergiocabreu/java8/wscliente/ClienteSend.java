package com.sergiocabreu.java8.wscliente;

import java.io.IOException;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class ClienteSend {

	public static void main(String[] args) {
		Ocorrencia ocorrencia = new Ocorrencia();
		ocorrencia.setDescricao("Ocorrênia 3.");
		
		try {
			wsSalvarOcorrencia();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void wsSalvarOcorrencia() throws IOException{
		
		
	    Client client = Client.create();

        WebResource webResource = client.resource("http://172.26.23.54:8080/deuzika/ws/salvar_ocorrencia.do");

        String input = "{\"json\":\""+jsonOcorrencia()+"\"}";

        //ClientResponse response = webResource.type("application/json").post(ClientResponse.class, jsonOcorrencia());
        ClientResponse response = webResource.type("application/json").post(ClientResponse.class, input);

        System.out.println("STATUS: "+response.getStatus());
        
/*        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus());
        }*/

        System.out.println("Output from Server .... \n");
        String output = response.getEntity(String.class);
        System.out.println(output);

       
	}
	
/*	public static void wsSalvarOcorrencia() throws IOException{
		
		URL url = new URL("http://172.26.23.54:8080/deuzika/ws/salvar_ocorrencia.do");
		// Cria um objeto HttpURLConnection:
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		
		// Define o método da requisição:
		request.setRequestMethod("POST");
		
        // Define o content-type:
        request.setRequestProperty("Content-Type", "application/json");
        
        // Define que a conexão pode enviar informações e obtê-las de volta:
        request.setDoOutput(true);
        request.setDoInput(true);

        // Conecta na URL:
        //request.connect();
        
        request.addRequestProperty("json", jsonOcorrencia());
        //new webservicet

        
		int response = request.getResponseCode();
      
      System.out.println("CÓDIGO DE RETORNO: " +response);
       
	}*/
	
/*	public static void wsSalvarOcorrencia() throws IOException{
	
		URL url = new URL("http://172.26.23.54:8080/deuzika/ws/salvar_ocorrencia.do");
		// Cria um objeto HttpURLConnection:
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		
		// Define o método da requisição:
		request.setRequestMethod("POST");
		
        // Define o content-type:
        request.setRequestProperty("Content-Type", "application/json");
        
        // Define que a conexão pode enviar informações e obtê-las de volta:
        request.setDoOutput(true);
        request.setDoInput(true);

        // Conecta na URL:
        request.connect();
        
        // Escreve o objeto JSON usando o OutputStream da requisição:
        try (OutputStream outputStream = request.getOutputStream()) {
            outputStream.write(jsonOcorrencia().getBytes("UTF-8"));
        }
        
        OutputStreamWriter out = new OutputStreamWriter(request.getOutputStream());
		out.write(jsonOcorrencia());
		out.close();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
		 
		while (in.readLine() != null) {
		}
		
		//System.out.println("\nCrunchify REST Service Invoked Successfully..");
		in.close();
        
		int response = request.getResponseCode();
      
      System.out.println("CÓDIGO DE RETORNO: " +response);
       
	}*/
	
	public static String jsonOcorrencia(){
		
		Gson gson = new Gson();
		String json = gson.toJson(getOcorrencia());
		
		return json;
	}
	
	public static Ocorrencia getOcorrencia(){
		
		Ocorrencia ocorrencia = new Ocorrencia();
		ocorrencia.setDescricao("Ocorrencia 1");		
		
		return ocorrencia;
		
		
		
	}
}
