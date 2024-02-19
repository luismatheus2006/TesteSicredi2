package com.desafio.Sicredi;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

public class Login {
    public static String AUTH_TOKEN = null; // Variável estática para armazenar o token
    public static String USER_ID = null; // Variável estática para armazenar o id do usuário
    public static void main(String[] args) {
        Unirest.setTimeouts(0, 0);
        try {
            HttpResponse<String> response = Unirest.post("https://dummyjson.com/auth/login")
                    .header("Content-Type", "application/json")
                    .body("{\n    \"username\": \"atuny0\",\n    \"password\": \"9uQFF1Lh\"\n}")
                    .asString();

            if (response.getStatus() == 200) {
                JSONObject jsonResponse = new JSONObject(response.getBody());
                AUTH_TOKEN = jsonResponse.getString("token"); // Armazena o token na variável estática
                USER_ID = String.valueOf(jsonResponse.getInt("id")); // Armazena o id na variável estática, convertendo para String
            } else {
                System.out.println("Falha na autenticação: " + response.getStatusText());
            }
        } catch (UnirestException e) {
            System.err.println("Erro ao fazer a requisição: " + e.getMessage());
        }
    }
}