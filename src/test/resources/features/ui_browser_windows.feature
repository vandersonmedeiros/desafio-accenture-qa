@ui
Feature: Automacao de Janelas do Navegador na DemoQA

  Scenario: Validar a abertura de nova janela
    Given que acesso o site DemoQA na pagina inicial
    When escolho a opcao "Alerts, Frame & Windows" na pagina inicial
    And clico no submenu "Browser Windows"
    And clico no botao new Window
    Then certifico que uma nova janela foi aberta com a mensagem "This is a sample page"
    And fecho a nova janela aberta