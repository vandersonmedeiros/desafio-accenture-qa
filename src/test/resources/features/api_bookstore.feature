Feature: Gerenciamento de Usuarios e Aluguel de Livros na DemoQA

  Scenario: Criar e autorizar um novo usuario com sucesso
    Given que possuo as credenciais de um novo usuario
    When envio uma requisicao para cadastrar o usuario
    And solicito a geracao do token de acesso
    And valido a autorizacao das credenciais na API
    Then o usuario deve ser autenticado e autorizado com sucesso

  Scenario: Consultar o catalogo de livros disponiveis
    Given que a API de livros esta operacional
    When envio uma requisicao para listar os livros
    Then o catalogo deve retornar a lista de livros cadastrados

  Scenario: Alugar um livro e confirmar o registro no perfil
    Given que possuo um usuario autenticado na plataforma
    And identifico o primeiro livro disponivel no catalogo
    When envio uma requisicao para alugar este livro
    And consulto o perfil do usuario
    Then o livro alugado deve estar registrado com sucesso no perfil