@ui
Feature: Janelas do Navegador

  Scenario: Visualizar conteudo informativo em uma nova janela
    Given que acesso o site DemoQA na pagina inicial
    When escolho a opcao "Alerts, Frame & Windows" na pagina inicial
    And clico no submenu "Browser Windows"
    And aciono a abertura de uma nova janela
    Then a nova janela deve exibir a mensagem "This is a sample page"