package com.desafio.Sicredi.Teste;

// Importações necessárias para trabalhar com requisições HTTP, JSON e realizar testes unitários.
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

// Importação estática para asserções do JUnit.
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de teste para validar a funcionalidade de login de uma API.
 */
public class TesteValidaçãoLogin {

    /**
     * Testa o processo de login com credenciais válidas.
     * Verifica se a API retorna status 200 OK e se um token de autenticação é recebido.
     */
    @Test
    public void LoginComCredenciaisValidas() {
        try {
            // Envia uma requisição POST com credenciais válidas.
            HttpResponse<JsonNode> response = Unirest.post("https://dummyjson.com/auth/login")
                    .header("Content-Type", "application/json")
                    .body("{\n    \"username\": \"kminchelle\",\n    \"password\": \"0lelplR\"\n}")
                    .asJson();

            // Verifica se o status da resposta é 200 OK.
            assertEquals(200, response.getStatus(), "A resposta deve ter status 200 OK para um login bem-sucedido.");

            // Verifica se um token de autenticação é recebido na resposta.
            JSONObject jsonResponse = response.getBody().getObject();
            assertNotNull(jsonResponse.getString("token"), "Um token de autenticação deve ser recebido após o login bem-sucedido.");

            System.out.println("Login com Credenciais Válidas foi um sucesso\nStatus:" +response.getStatus());
        } catch (UnirestException e) {
            fail("Erro durante o teste de login: " + e.getMessage());
        }
    }

    /**
     * Testa o processo de login com credenciais inválidas.
     * Verifica se a API retorna status 401 Unauthorized ou 400 Bad Request.
     */
    @Test
    public void LoginComCredenciaisInvalidas() {
        try {
            // Envia uma requisição POST com credenciais inválidas.
            HttpResponse<JsonNode> response = Unirest.post("https://dummyjson.com/auth/login")
                    .header("Content-Type", "application/json")
                    .body("{\n    \"username\": \"wronguser\",\n    \"password\": \"wrongpass\"\n}")
                    .asJson();

            // Verifica se o status da resposta indica falha de autenticação.
            assertEquals(400, response.getStatus(), "A resposta deve ser 401 Unauthorized ou 400 Bad Request para credenciais inválidas.");

            System.out.println("Login com Credenciais  ivalidas resultou em erro:\nStatus: "+ response.getStatus());
        } catch (UnirestException e) {
            fail("Erro durante o teste de login com credenciais inválidas: " + e.getMessage());
        }
    }

    /**
     * Testa o processo de login com campos obrigatórios faltantes na requisição.
     * Verifica se a API retorna status 400 Bad Request.
     */
    @Test
    public void CamposAusentesNaRequisicao() {
        try {
            // Envia uma requisição POST sem os campos de username e password para simular campos faltantes.
            HttpResponse<JsonNode> response = Unirest.post("https://dummyjson.com/auth/login")
                    .header("Content-Type", "application/json")
                    .body("{}") // Corpo vazio para simular campos faltantes.
                    .asJson();

            // Verifica se o status da resposta é 400 Bad Request.
            assertEquals(400, response.getStatus(), "A resposta deve ser 400 Bad Request para requisição com campos faltantes.");

            System.out.println("Campos Faltantes na Requisição: Sucesso\nStatus: " + response.getStatus());
        } catch (UnirestException e) {
            fail("Erro durante o teste de login com campos faltantes: " + e.getMessage());
        }
    }

    /**
     * Testa a validade do token de autenticação recebido após um login bem-sucedido.
     * Verifica se o token permite o acesso a endpoints protegidos da API.
     */
    @Test
    public void ValidadeDoToken() {
        try {
            // Primeiro, realiza o login para obter o token de autenticação usando credenciais válidas.
            HttpResponse<JsonNode> loginResponse = Unirest.post("https://dummyjson.com/auth/login")
                    .header("Content-Type", "application/json")
                    .body("{\n    \"username\": \"kminchelle\",\n    \"password\": \"0lelplR\"\n}")
                    .asJson();

            // Verifica se o login foi bem-sucedido.
            assertEquals(200, loginResponse.getStatus(), "O login para obter o token deve ser bem-sucedido.");

            // Extrai o token de autenticação da resposta.
            String token = loginResponse.getBody().getObject().getString("token");

            // Usa o token obtido para acessar um endpoint protegido.
            HttpResponse<JsonNode> protectedResponse = Unirest.get("https://dummyjson.com/auth/products")
                    .header("Authorization", "Bearer " + token)
                    .asJson();

            // Verifica se o acesso ao endpoint protegido é bem-sucedido com o token.
            assertEquals(200, protectedResponse.getStatus(), "O token deve ser aceito e válido para acessar endpoints protegidos.");

            System.out.println("Token foi validado com sucesso na API protegida \nStatus:" + protectedResponse.getStatus());
        } catch (UnirestException e) {
            fail("Erro durante o teste de validade do token: " + e.getMessage());
        }
    }
}
