package com.desafio.Sicredi.Teste;

// Importando as bibliotecas necessárias para realizar requisições HTTP, tratar exceções e trabalhar com JSON.
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

// Importando asserções do JUnit para validar as respostas das requisições.
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de teste para validar a funcionalidade de criação de produtos através de uma API.
 */
public class TesteCriaçãoProdutos {

    /**
     * Testa a criação de um produto fornecendo todos os campos esperados pela API.
     */
    @Test
    public void CriarProdutoComTodosOsCampos() {
        try {
            // Envia uma requisição POST para a API com um corpo contendo todos os campos necessários para criar um produto.
            HttpResponse<String> response = Unirest.post("https://dummyjson.com/products/add")
                    .header("Content-Type", "application/json")
                    .body("{\n" +
                            "\"title\": \"Perfume Oil\",\n" +
                            "\"description\": \"Mega Discount, Impression of A...\",\n" +
                            "\"price\": 13,\n" +
                            "\"discountPercentage\": 8.4,\n" +
                            "\"rating\": 4.26,\n" +
                            "\"stock\": 65,\n" +
                            "\"brand\": \"Impression of Acqua Di Gio\",\n" +
                            "\"category\": \"fragrances\",\n" +
                            "\"thumbnail\": \"https://i.dummyjson.com/data/products/11/thumbnail.jpg\"\n" +
                            "}")
                    .asString();

            // Verifica se a resposta tem o status HTTP 201, indicando que o produto foi criado com sucesso.
            assertEquals(201, response.getStatus(), "\n\nO código de status deve ser 201 (Created).");

            // Analisa a resposta para verificar se os dados do produto criado correspondem aos dados enviados.
            JSONObject jsonResponse = new JSONObject(response.getBody());
            assertNotNull(jsonResponse.getString("id"), "O produto criado deve ter um 'id' único.");
            assertEquals("Perfume Oil", jsonResponse.getString("title"), "O título do produto deve ser 'Perfume Oil'.");

            // Imprime no console os detalhes do produto criado.
            System.out.println("Produto criado com sucesso: " + jsonResponse.toString(2));
        } catch (UnirestException e) {

        }
    }

    // Outros testes, como a criação de produtos apenas com campos obrigatórios, foram omitidos para brevidade.

    /**
     * Testa a tentativa de criar um produto sem fornecer um ou mais campos obrigatórios.
     */
    @Test
    public void CriarProdutoSemBody() {
        try {
            // Envia uma requisição POST para a API sem passar dados no body para cadastro de produto .
            HttpResponse<String> response = Unirest.post("https://dummyjson.com/products/add")
                    .header("Content-Type", "application/json")
                    .body("")
                    .asString();

            // Verifica se a resposta tem o status HTTP 400.
            assertEquals(400, response.getStatus(), "\n\nO código de status deve ser 400 (Bad Request) quando o body está ausente.");

            // Imprime no console que a tentativa de criação do produto sem o body que resultou em erro, como esperado.
            System.out.println("Tentativa de criar produto com body ausente que resultou em erro, conforme esperado.");
        } catch (UnirestException e) {
            // Captura e reporta qualquer erro ocorrido durante a requisição.
            fail("Erro ao tentar criar produto com body ausente: " + e.getMessage());
        }
    }

    /**
     * Testa a tentativa de criar um produto fornecendo tipos de dados inválidos para todos os campos esperados pela API.
     */
    @Test
    public void CriarProdutoComTiposDeDadosInvalidos() {
        try {
            // Envia uma requisição POST para a API com um corpo contendo tipos de dados inesperados para todos os campos.
            HttpResponse<String> response = Unirest.post("https://dummyjson.com/products/add")
                    .header("Content-Type", "application/json")
                    .body("{\n" +
                            "\"title\": 12345,\n" +  // 'title' esperado como string, fornecido como numérico.
                            "\"description\": false,\n" +  // 'description' esperado como string, fornecido como booleano.
                            "\"price\": \"free\",\n" +  // 'price' esperado como numérico, fornecido como string.
                            "\"discountPercentage\": \"ten percent\",\n" +  // 'discountPercentage' esperado como numérico, fornecido como string.
                            "\"rating\": \"five stars\",\n" +  // 'rating' esperado como numérico, fornecido como string.
                            "\"stock\": \"plenty\",\n" +  // 'stock' esperado como numérico, fornecido como string.
                            "\"brand\": null,\n" +  // 'brand' esperado como string, fornecido como null.
                            "\"category\": 100,\n" +  // 'category' esperado como string, fornecido como numérico.
                            "\"thumbnail\": 404\n" +  // 'thumbnail' esperado como URL string, fornecido como numérico.
                            "}")
                    .asString();

            // Verifica se a resposta tem o status HTTP 400, indicando que a requisição foi rejeitada devido a tipos de dados inválidos.
            assertEquals(400, response.getStatus(), "\n\nO código de status deve ser 400 (Bad Request) quando todos os campos contêm tipos de dados inválidos.");

            // Imprime no console que a tentativa de criação do produto com todos os tipos de dados inválidos resultou em erro, conforme esperado.
            System.out.println("Tentativa de criar produto com todos os tipos de dados inválidos resultou em erro, conforme esperado. Status: " + response.getStatus());
        } catch (UnirestException e) {
            // Captura e reporta qualquer erro ocorrido durante a requisição.
            fail("Erro ao tentar criar produto com todos os tipos de dados inválidos: " + e.getMessage());
        }
        // Os testes para valores de campo inválidos e formatos de URL de miniatura inválidos seguem uma lógica similar e foram omitidos para brevidade.

        // Método para testar a criação de produto com tipos de dados inválidos também foi omitido.

        // O teste para visualização após criação foi comentado e não incluído nesta versão.
    }
}
