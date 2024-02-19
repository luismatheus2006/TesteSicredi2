package com.desafio.Sicredi.Teste;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TesteBuscarStatusAplicação {
    // Teste para verificar a comunicação com a API e a funcionalidade do endpoint /test

    @Test
    public void VerificarStatusDaAplicacao() throws UnirestException {
        // Envia uma requisição GET para o endpoint /test e espera uma resposta JSON
        HttpResponse<JsonNode> response = Unirest.get("https://dummyjson.com/test").asJson();

        // Verifica se o status HTTP da resposta é 200, indicando sucesso
        assertEquals(200, response.getStatus(), "A resposta deve ter status 200 para uma requisição bem-sucedida.");

        // Obtém o corpo da resposta e verifica se os campos 'status' e 'method' estão corretos
        JSONObject responseBody = response.getBody().getObject();
        assertEquals("ok", responseBody.getString("status"), "O status da resposta deve ser 'ok'.");
        assertEquals("GET", responseBody.getString("method"), "O método da requisição deve ser 'GET'.");

        // Imprime os valores de 'status' e 'method' para verificação
        System.out.println("O Status foi buscado com sucesso\nStatus: " + responseBody.getString("status") + "\nMethod: " + responseBody.getString("method"));
    }

    @Test
    public void EndpointIncorreto() throws UnirestException {
        // Envia uma requisição GET para um endpoint inexistente para testar o tratamento de erros
        HttpResponse<String> response = Unirest.get("https://dummyjson.com/tesa").asString();

        // Verifica se o status HTTP da resposta é 404, indicando que o endpoint não foi encontrado
        assertEquals(404, response.getStatus(), "A resposta deve ter status 404 para indicar que o endpoint não foi encontrado.");

        // Imprime o status da resposta para verificação
        System.out.println("A requisição incorreta gerou um erro.\nStatus: " + response.getStatus() + "\nNot Found");

        // Verifica se o Content-Type da resposta indica um conteúdo JSON antes de tentar analisá-lo
        String contentType = response.getHeaders().getFirst("Content-Type");
        if (contentType != null && contentType.contains("application/json")) {
            // A resposta é JSON, prossegue com a análise
            try {
                JSONObject jsonResponse = new JSONObject(response.getBody());
                // Aqui podem ser feitas verificações adicionais sobre o corpo da resposta JSON
            } catch (JSONException e) {
                // Caso ocorra um erro ao analisar a resposta JSON, o teste falha
                fail("Esperava-se um JSON válido, mas ocorreu um erro ao analisar a resposta.");
            }
        } else {
            // Se o Content-Type não indicar um conteúdo JSON, outras verificações podem ser feitas conforme necessário
        }
    }

    @Test
    public void UsoDeMetodoHTTPIncorreto() throws UnirestException {
        // Envia uma requisição POST para o endpoint /test, que espera apenas o método GET, para testar o tratamento de métodos HTTP incorretos
        HttpResponse<String> response = Unirest.post("https://dummyjson.com/test").asString();

        // Verifica se o status HTTP da resposta é 405, indicando que o método não é permitido
        assertEquals(405, response.getStatus(), "\n\nA resposta deve ter status 405 para indicar Método Não Permitido.");

        // Verifica se a resposta contém uma mensagem indicando que o método HTTP utilizado não é suportado
        String contentType = response.getHeaders().getFirst("Content-Type");
        if (contentType != null && contentType.contains("application/json")) {
            try {
                JSONObject jsonResponse = new JSONObject(response.getBody());
                String errorMessage = jsonResponse.optString("message", "");
                assertTrue(errorMessage.contains("Método Não Permitido"), "A mensagem deve indicar que o método HTTP utilizado não é suportado.");
            } catch (Exception e) {
                // Caso ocorra um erro ao analisar a resposta JSON, o teste falha
                fail("Esperava-se uma resposta JSON com uma mensagem de erro, mas não foi possível analisar a resposta.");
            }
        } else {
            // Se a resposta não for JSON, pode-se verificar se o corpo da resposta em texto contém alguma indicação do erro
            assertTrue(response.getBody().contains("Método Não Permitido"), "A resposta deve conter uma mensagem indicando que o método não é suportado.");
        }
    }

    private void fail(String message) {
        // Implementa a lógica de falha, por exemplo, usando Assertions.fail(message) do JUnit
        throw new AssertionError(message);
    }
}
