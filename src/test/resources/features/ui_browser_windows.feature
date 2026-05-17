@ui
Feature: Automacao de Janelas do Navegador na DemoQA

  Scenario: Validar a abertura de nova aba com mensagem explicita
    Given que navego ate a pagina de janelas do navegador
    When clico no botao para abrir uma nova aba
    Then uma nova aba deve ser aberta com a mensagem "This is a sample page"
