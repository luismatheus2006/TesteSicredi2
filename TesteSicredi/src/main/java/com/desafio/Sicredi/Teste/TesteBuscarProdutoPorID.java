package com.desafio.Sicredi.Teste;

// Importações necessárias para as requisições HTTP, tratamento de exceções, manipulação de JSON e testes
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de teste para verificar a funcionalidade de busca de produtos por ID na API.
 */
public class TesteBuscarProdutoPorID {

    /**
     * Testa a busca de um produto usando um ID válido.
     * Verifica se a API retorna status 200 OK e se os dados do produto correspondem ao ID solicitado.
     */
    @Test
        public void ProdutoComIdValido() {
        try {
            int produtoId = 2; // ID de exemplo para um produto conhecido que existe na API
            HttpResponse<String> response = Unirest.get("https://dummyjson.com/products/" + produtoId).asString();

            // Verifica se o status da resposta é 200 OK, indicando sucesso na busca
            assertEquals(200, response.getStatus(), "A resposta deve ser 200 OK para um ID de produto válido.");
            System.out.println("Teste buscar produto com Id valido:\nStatus " + response.getStatus());
            // Verifica se os dados do produto correspondem ao ID solicitado
            JSONObject product = new JSONObject(response.getBody());
            assertEquals(produtoId, product.getInt("id"), "O ID do produto retornado deve corresponder ao solicitado.");
        } catch (UnirestException e) {
            fail("Erro ao fazer a requisição: " + e.getMessage());
        }
    }

    /**
     * Testa a busca de um produto usando um ID inexistente.
     * Verifica se a API retorna status 404 Not Found, indicando que o produto não foi encontrado.
     */
    @Test
    public void ProdutoComIdInexistente() {
        try {
            int produtoIdInexistente = 999999; // ID de exemplo para um produto que se sabe não existir
            HttpResponse<String> response = Unirest.get("https://dummyjson.com/products/" + produtoIdInexistente).asString();

            // Verifica se o status da resposta é 404 Not Found
            assertEquals(404, response.getStatus(), "A resposta deve ser 404 Not Found para um ID de produto inexistente.");
            System.out.println("Teste produto com id inexistente:\nStatus " + response.getStatus());
        } catch (UnirestException e) {
            fail("Erro ao fazer a requisição com ID inexistente: " + e.getMessage());
        }
    }

    /**
     * Testa a busca de um produto usando um ID inválido.
     * Verifica se a API retorna status 404 Not Found ou 400 Bad Request, indicando que o ID fornecido é inválido.
     */
    @Test
    public void ProdutoComIdInvalido() {
        try {
            String produtoIdInvalido = "invalid_id"; // ID de exemplo inválido
            HttpResponse<String> response = Unirest.get("https://dummyjson.com/products/" + produtoIdInvalido).asString();

            // Verifica se o status da resposta é 404 Not Found ou 400 Bad Request
            assertEquals(404, response.getStatus(), "A resposta deve ser 400 Bad Request ou 404 Not Found para um ID de produto inválido.");
            System.out.println("Teste produto com id invalido:\nStatus " + response.getStatus());
        } catch (UnirestException e) {
            fail("Erro ao fazer a requisição com ID inválido: " + e.getMessage());
        }
    }
}
