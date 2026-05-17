Feature: Autenticacao e Aluguel de Livros na DemoQA

  Scenario: Validar o ciclo de vida do usuario e aluguel de livros via API
    Given que inicializo a base da API DemoQA
    When envio uma requisicao POST para criar um novo usuario
    Then o usuario deve ser criado com sucesso retornando o ID
    When envio uma requisicao POST para gerar o token de acesso
    Then o token deve ser gerado com sucesso
    When envio uma requisicao POST para validar a autorizacao
    Then a resposta de autorizacao deve ser verdadeira
    When envio uma requisicao GET para listar os livros disponiveis
    Then a lista de livros deve ser retornada com sucesso
    When envio uma requisicao POST para alugar o primeiro livro da lista
    Then o livro deve ser associado ao usuario com sucesso
    When envio uma requisicao GET para consultar o perfil do usuario
    Then o livro alugado deve constar no perfil do usuario
