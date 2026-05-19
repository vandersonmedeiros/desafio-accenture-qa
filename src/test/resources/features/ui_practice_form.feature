@ui
Feature: Formulario de Pratica

  Scenario: Enviar o formulario de cadastro com sucesso
    Given que acesso o site DemoQA na pagina inicial
    When escolho a opcao "Forms" na pagina inicial
    And clico no submenu "Practice Form"
    And submeto o formulario com dados validos e o anexo obrigatorio
    Then o sistema deve confirmar o recebimento das informacoes