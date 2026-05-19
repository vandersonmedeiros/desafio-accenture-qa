@ui
Feature: Gerenciamento de Registros

  Scenario: Adicionar um novo registro na tabela com sucesso
    Given que estou na tela de "Web Tables" dentro de "Elements"
    When cadastro um novo colaborador com os dados "teste01", "sobrenome01", "teste01@gmail.com", "30", "15000", "QA"
    Then o colaborador "teste01" deve constar na listagem

  Scenario: Editar o departamento de um registro recem-criado
    Given que estou na tela de "Web Tables" dentro de "Elements"
    And cadastro um novo colaborador com os dados "teste01", "sobrenome01", "teste01@gmail.com", "30", "15000", "QA"
    When altero o departamento do colaborador "teste01" para "testeEditado"
    Then o colaborador "teste01" deve exibir o novo departamento "testeEditado"

  Scenario: Deletar um registro recem-criado da tabela
    Given que estou na tela de "Web Tables" dentro de "Elements"
    And cadastro um novo colaborador com os dados "teste01", "sobrenome01", "teste01@gmail.com", "30", "15000", "QA"
    When removo o colaborador "teste01"
    Then o colaborador "teste01" nao deve ser exibido na listagem

  Scenario: Insercao em lote e exclusao dinamica de multiplos registros
    Given que estou na tela de "Web Tables" dentro de "Elements"
    When realizo o cadastro massivo dos seguintes colaboradores:
      | firstName | lastName    | email             | age | salary | department |
      | teste01   | sobrenome01 | teste01@gmail.com | 30  | 10000  | TI         |
      | teste02   | sobrenome02 | teste02@gmail.com | 31  | 11000  | TI         |
      | teste03   | sobrenome03 | teste03@gmail.com | 32  | 12000  | TI         |
      | teste04   | sobrenome04 | teste04@gmail.com | 33  | 13000  | TI         |
      | teste05   | sobrenome05 | teste05@gmail.com | 34  | 14000  | TI         |
      | teste06   | sobrenome06 | teste06@gmail.com | 35  | 15000  | TI         |
      | teste07   | sobrenome07 | teste07@gmail.com | 36  | 16000  | TI         |
      | teste08   | sobrenome08 | teste08@gmail.com | 37  | 17000  | TI         |
      | teste09   | sobrenome09 | teste09@gmail.com | 38  | 18000  | TI         |
      | teste10   | sobrenome10 | teste10@gmail.com | 39  | 19000  | TI         |
      | teste11   | sobrenome11 | teste11@gmail.com | 40  | 20000  | TI         |
      | teste12   | sobrenome12 | teste12@gmail.com | 41  | 21000  | TI         |
    Then removo todos os colaboradores cadastrados massivamente
    And a listagem deve retornar ao estado inicial