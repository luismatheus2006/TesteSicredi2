package com.desafio.Sicredi.Teste;

// Importações necessárias para realizar requisições HTTP, manipulação de JSON, e execução de testes.
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

// Importações estáticas para asserções do JUnit.
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de teste para validar a funcionalidade de listagem de produtos de uma API.
 */
public class TesteBuscarProdutos {

    /**
     * Método auxiliar para buscar todos os produtos da API.*/
    public static JSONArray fetchProducts(String params) throws UnirestException, JSONException {
        HttpResponse<String> response = Unirest.get("https://dummyjson.com/products" + params).asString();
        // Verifica se a resposta HTTP é 200 OK.
        assertEquals(200, response.getStatus(), "A resposta deve ter status 200.");
        // Converte o corpo da resposta em JSONObject e retorna o array de produtos.
        JSONObject jsonResponse = new JSONObject(response.getBody());
        return jsonResponse.getJSONArray("products");
    }

    /**
     * Testa a listagem de todos os produtos, verificando se a lista não está vazia e listando os produtos no console.*/
    @Test
    public void BuscarTodosProdutosListados() throws UnirestException, JSONException {
        JSONArray products = fetchProducts("");
        // Verifica se a lista de produtos não está vazia.
        assertTrue(products.length() > 0, "A lista de produtos deve conter produtos.");
        // Verifica Status.
        HttpResponse<String> response = Unirest.get("https://dummyjson.com/products").asString();
        assertEquals(200,response.getStatus());
        // Itera sobre a lista de produtos e imprime detalhes básicos de cada produto.
        System.out.println("Listando todos os produtos:");
        for (int i = 0; i < products.length(); i++) {
            JSONObject product = products.getJSONObject(i);
            // Verifica se cada produto tem um ID.
            assertNotNull(product.getString("id"), "Cada produto deve ter um ID.");
            System.out.println("Produto " + (i + 1) + ": " + product.getString("title"));
        }
        System.out.println("Teste listarTodosProdutos concluído com sucesso.");
    }

    /**
     * Valida os dados dos produtos retornados pela API, verificando, por exemplo, se os preços dos produtos são positivos.
     * @throws UnirestException Se ocorrer um erro na requisição HTTP.
     * @throws JSONException Se ocorrer um erro na manipulação do objeto JSON.
     */
    @Test
    public void ValidarDadosDosProdutos() throws UnirestException, JSONException {
        JSONArray products = fetchProducts("");
        System.out.println("Validando dados dos produtos:");
        for (int i = 0; i < products.length(); i++) {
            JSONObject product = products.getJSONObject(i);
            // Verifica se o preço de cada produto é positivo.
            assertTrue(product.getDouble("price") > 0, "O preço do produto deve ser positivo.");
            System.out.println("Produto " + (i + 1) + ": " + product.getString("title") + ", Preço: " + product.getDouble("price"));
        }
        System.out.println("Teste validar Dados Dos Produtos concluído com sucesso.");
    }
    @Test
    public void VerificarTotalDeProdutos() throws UnirestException, JSONException {
        // Faz a requisição para a API e obtém o response.
        HttpResponse<String> response = Unirest.get("https://dummyjson.com/products").asString();

        // Converte o response em um objeto JSON.
        JSONObject responseObject = new JSONObject(response.getBody());

        // Extrai o valor do campo 'total' do objeto JSON.
        int totalDeProdutos = responseObject.getInt("total");

        // Verifica se o total de produtos é igual a 100.
        assertEquals(100, totalDeProdutos, "O total de produtos listado no response deve ser 100.");

        // Imprime uma mensagem de sucesso no console se o teste passar.
        System.out.println("Teste verificarTotalDeProdutosNoResponse concluído com sucesso.\nTotal de produtos: " + totalDeProdutos);
    }
}
