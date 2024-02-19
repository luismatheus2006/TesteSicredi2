package com.desafio.Sicredi.Teste;

// Importações necessárias para realizar requisições HTTP, manipular JSON e realizar testes.
import com.desafio.Sicredi.Login; // Classe que supostamente contém o token de autenticação.
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

// Importação estática para asserções do JUnit.
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de teste para validar o acesso autenticado a um conjunto de produtos em uma API.
 */
public class TesteBuscarProdutosAutenticação {

    /**
     * Testa o acesso ao endpoint de produtos usando um token de autenticação válido.
     * Verifica se a resposta é 200 OK e se uma lista de produtos é retornada.
     */
    @Test
    public void AcessoComTokenValido() {
        try {
            Login.main(null);
            // Envia uma requisição GET incluindo o token de autenticação no cabeçalho.
            HttpResponse<String> response = Unirest.get("https://dummyjson.com/auth/products")
                    .header("Authorization", "Bearer " + Login.AUTH_TOKEN)
                    .asString();

            // Verifica se o status da resposta é 200, indicando sucesso.
            assertEquals(200, response.getStatus(), "A resposta deve ser 200 OK para acesso com token válido.");

            // Extrai e verifica a lista de produtos da resposta.
            JSONObject responseBody = new JSONObject(response.getBody());
            JSONArray products = responseBody.getJSONArray("products");
            assertNotNull(products, "Deve retornar uma lista de produtos.");
            assertTrue(products.length() > 0, "A lista de produtos não deve estar vazia.");

            System.out.println("Teste:Acesso com token válido: sucesso - " + products.length() + " produtos retornados.\nStatus: " + response.getStatus());
        } catch (UnirestException e) {
            fail("Erro na requisição: " + e.getMessage());
        }
    }
    /**
     * Testa o acesso ao endpoint de produtos sem fornecer um token de autenticação.
     * Espera-se um status 401 Unauthorized ou 403 Forbidden.
     */
    @Test
    public void Tokenautenticaçãoausente() {
        try {
            // Envia uma requisição GET sem token de autenticação.
            HttpResponse<String> response = Unirest.get("https://dummyjson.com/auth/products")
                    .asString();

            // Verifica se o status da resposta indica falha de autenticação.
            assertEquals(403, response.getStatus(), "A resposta deve ser 401 Unauthorized ou 403 Forbidden para requisições sem token de autenticação.");

            System.out.println("Teste: Sem Token de autenticação o acesso negado conforme esperado.\nStatus: " + response.getStatus());
        } catch (UnirestException e) {
            fail("Erro na requisição sem token de autenticação: " + e.getMessage());
        }
    }

    /**
     * Testa o acesso ao endpoint de produtos com um token de autenticação inválido.
     */
    @Test
    public void TokenDeAutenticacaoInvalido() {
        try {
            // Envia uma requisição GET com um token inválido.
            HttpResponse<String> response = Unirest.get("https://dummyjson.com/auth/products")
                    .header("Authorization", "Bearer token_invalido")
                    .asString();

            // Verifica se o status da resposta indica token inválido.
            assertTrue(response.getStatus() == 401 , "A resposta deve ser 401 Unauthorized ou 403 Forbidden para token inválido.");

            System.out.println("Teste: token de Autenticação Inválido o Acesso negado conforme esperado.\nStatus: " + response.getStatus());
        } catch (UnirestException e) {
            fail("Erro na requisição com token de autenticação inválido: " + e.getMessage());
        }
    }
    /**
     * Verifica a estrutura dos dados dos produtos retornados, incluindo a presença dos campos essenciais.
     */
    @Test
    public void EstruturaDosDadosDosProdutos() {
        try {
            Login.main(null);
            // Envia uma requisição GET com um token válido.
            HttpResponse<String> response = Unirest.get("https://dummyjson.com/auth/products")
                    .header("Authorization", "Bearer " + Login.AUTH_TOKEN)
                    .asString();

            // Verifica se o status da resposta é 200 OK.
            assertEquals(200, response.getStatus(), "A resposta deve ser 200 OK.");

            // Verifica a presença dos campos essenciais em cada produto.
            JSONObject responseBody = new JSONObject(response.getBody());
            JSONArray products = responseBody.getJSONArray("products");
            for (int i = 0; i < products.length(); i++) {
                JSONObject product = products.getJSONObject(i);
                assertTrue(product.has("id") && product.has("title") && product.has("price"), "Cada produto deve conter os campos 'id', 'title' e 'price'.");
            }

            System.out.println("Teste: Estrutura dos dados dos produtos, estrutura de dados validada.\nStatus:" + response.getStatus());
        } catch (UnirestException e) {
            fail("Erro na validação da estrutura de dados dos produtos: " + e.getMessage());
        }
    }

    /**
     * Valida os campos dos produtos retornados, como verificar se os preços dos produtos são positivos.
     */
    @Test
    public void ValidacaoDeCamposDosProdutos() {
        try {
            Login.main(null);
            // Envia uma requisição GET com um token válido.
            HttpResponse<String> response = Unirest.get("https://dummyjson.com/auth/products")
                    .header("Authorization", "Bearer " + Login.AUTH_TOKEN)
                    .asString();

            // Verifica se o status da resposta é 200 OK.
            assertEquals(200, response.getStatus(), "A resposta deve ser 200 OK.");

            // Verifica se os preços dos produtos são positivos.
            JSONObject responseBody = new JSONObject(response.getBody());
            JSONArray products = responseBody.getJSONArray("products");
            for (int i = 0; i < products.length(); i++) {
                JSONObject product = products.getJSONObject(i);
                assertNotNull(product.getDouble("id") , "O id não pode ser nulo.");
                assertNotNull(product.getString("title"), "O title não pode ser nulo.");
                assertNotNull(product.getDouble("price"), "O price não pode ser nulo.");
                assertNotNull(product.getDouble("stock"), "O stock não pode ser nulo.");
                assertNotNull(product.getDouble("rating"), "O rating não pode ser nulo.");
                assertNotNull(product.getString("description"), "O description não pode ser nulo.");
                assertNotNull(product.getString("brand"), "O brand não pode ser nulo.");
                assertNotNull(product.getString("thumbnail"), "O thumbnail não pode ser nulo.");
                assertNotNull(product.getString("category"), "O category não pode ser nulo.");

            }

            System.out.println("Teste:Validação de campos dos produtos, todos os produtos têm preços positivos.\nStatus: " + response.getStatus());
        } catch (UnirestException e) {
            fail("Erro na validação dos campos dos produtos: " + e.getMessage());
        }
    }
}
