package com.desafio.Sicredi.Teste;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TesteBuscarVerificarDadosUsuarios {

    @Test
    public void ListaDeUsuariosNaoEstaVazia() throws UnirestException, JSONException {
        HttpResponse<JsonNode> jsonResponse;
        try {
            jsonResponse = Unirest.get("https://dummyjson.com/users").asJson();
            assertEquals(200, jsonResponse.getStatus(), "A resposta deve ter status 200.");
        } catch (UnirestException e) {
            fail("Falha ao fazer a requisição: " + e.getMessage());
            return; // Finaliza o teste para evitar erros de NullPointerException.
        }

        JSONArray users = jsonResponse.getBody().getObject().getJSONArray("users");
        assertTrue(users.length() > 0, "A lista de usuários não deve estar vazia.");
        System.out.println("A lista de usuarios não está vazia\nStatus: " + jsonResponse.getStatus() + " - Passou");
    }
    @Test
    public void ValidacaoDeCamposImportantes() throws UnirestException, JSONException {
        HttpResponse<JsonNode> jsonResponse;
        try {
            jsonResponse = Unirest.get("https://dummyjson.com/users").asJson();
        } catch (UnirestException e) {
            fail("Falha ao fazer a requisição: " + e.getMessage());
            return; // Finaliza o teste para evitar erros de NullPointerException.
        }

        JSONArray users = jsonResponse.getBody().getObject().getJSONArray("users");
        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            assertNotNull(user.optString("username"), "O campo 'username' não deve ser nulo.");
            assertNotNull(user.optString("password"), "O campo 'password' não deve ser nulo.");
            }
        System.out.println("Os campos importantes foram achados com sucesso\nStatus: " + jsonResponse.getStatus() + " - Passou");

    }

    @Test
    public void FiltragemPorIdInexistente() throws UnirestException {
        HttpResponse<JsonNode> jsonResponse;
        try {
            jsonResponse = Unirest.get("https://dummyjson.com/users/12345").asJson();
        } catch (UnirestException e) {
            fail("Falha ao fazer a requisição: " + e.getMessage());
            return; // Finaliza o teste para evitar erros de NullPointerException.
        }

        assertEquals(404, jsonResponse.getStatus(), "Esperava-se um status 404 Not Found para um ID inexistente.");
        System.out.println("Teste de tentar buscar um usuario com id invalido foi um sucesso:\nStatus: " + jsonResponse.getStatus() + " - Passou");
    }
    @Test
    public void FiltragemPorIdExistente() throws UnirestException {
        HttpResponse<JsonNode> jsonResponse;
        try {
            jsonResponse = Unirest.get("https://dummyjson.com/users/1").asJson();
        } catch (UnirestException e) {
            fail("Falha ao fazer a requisição: " + e.getMessage());
            return; // Finaliza o teste para evitar erros de NullPointerException.
        }

        assertEquals(200, jsonResponse.getStatus(), "Esperava-se um status 404 Not Found para um ID inexistente.");
        System.out.println("Teste de busca de usuario por Id sucesso:\nStatus: " + jsonResponse.getStatus() + " - Passou");
    }

    @Test
    public void IntegridadeFormatacaoDosDados() throws UnirestException, JSONException {
        HttpResponse<JsonNode> jsonResponse;
        try {
            jsonResponse = Unirest.get("https://dummyjson.com/users").asJson();
        } catch (UnirestException e) {
            fail("Falha ao fazer a requisição: " + e.getMessage());
            return; // Finaliza o teste para evitar erros de NullPointerException.
        }

        JSONArray users = jsonResponse.getBody().getObject().getJSONArray("users");
        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            assertTrue(user.has("id") && !user.optString("id").isEmpty(), "O campo 'id' é obrigatório e não deve estar vazio.");
            assertTrue(user.has("firstName") && !user.optString("firstName").isEmpty(), "O campo 'firstName' é obrigatório e não deve estar vazio.");
            assertTrue(user.has("lastName") && !user.optString("lastName").isEmpty(), "O campo 'lastName' é obrigatório e não deve estar vazio.");
            assertTrue(user.has("username") && !user.optString("username").isEmpty(), "O campo 'username' é obrigatório e não deve estar vazio.");
            assertTrue(user.has("password") && !user.optString("password").isEmpty(), "O campo 'password' é obrigatório e não deve estar vazio.");
        }
        System.out.println("Informações importantes existem nos dados\nStatus: " + jsonResponse.getStatus() + " - Passou");
    }
}
