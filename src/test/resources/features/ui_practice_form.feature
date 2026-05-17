@ui
Feature: Preenchimento do Practice Form na DemoQA

  Scenario: Preencher e enviar formulario com sucesso
    Given que acesso o site DemoQA na pagina inicial
    When escolho a opcao "Forms" na pagina inicial
    And clico no submenu "Practice Form"
    And preencho todo o formulario com valores aleatorios
    And realizo o upload do arquivo de texto obrigatorio
    And clico no botao de enviar o formulario
    Then garantir que um popup foi aberto apos o submit
    And fechar o popup