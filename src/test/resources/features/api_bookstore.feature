Feature: Gerenciamento de Usuarios e Aluguel de Livros
  Como um leitor da BookStore
  Quero criar uma conta e consultar o catalogo
  Para que eu possa realizar o aluguel de titulos

  Scenario: Criar e autorizar um novo usuario com sucesso
    Given que possuo os dados de um novo usuario
    When solicito o cadastro na plataforma
    And realizo a autenticacao da conta
    And verifico a autorizacao de acesso
    Then o usuario deve ser autorizado com sucesso

  Scenario: Consultar o catalogo de livros disponiveis
    Given que o sistema de livros esta operacional
    When consulto a lista de livros disponiveis
    Then o catalogo deve retornar os titulos cadastrados

  Scenario: Alugar um livro e confirmar o registro no perfil
    Given que sou um usuario autenticado na plataforma
    And seleciono o primeiro livro disponivel no catalogo
    When realizo o aluguel do livro selecionado
    Then o livro alugado deve constar no perfil do usuario