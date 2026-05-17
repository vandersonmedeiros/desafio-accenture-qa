@ui
Feature: Automacao do Formulario de Pratica na DemoQA

  Scenario: Preencher e enviar o formulario de pratica com sucesso
    Given que navego ate a pagina do formulario de pratica
    When preencho os campos de nome "Vanderson" e sobrenome "Oliveira"
    And seleciono o genero masculino e insiro o telefone "1199999999"
    And realizo o upload do arquivo de texto obrigatorio
    And clico no botao de enviar o formulario
    Then o popup de confirmacao deve ser exibido com os dados enviados
