@ui
Feature: Interacao com a Progress Bar

  Scenario: Validar o estado inicial da barra de progresso ao acessar
    Given que estou na tela de "Progress Bar" dentro de "Widgets"
    Then o valor da barra deve iniciar em 0%

  Scenario: Validar a pausa da barra de progresso antes dos 25%
    Given que estou na tela de "Progress Bar" dentro de "Widgets"
    When clico no botao Start para iniciar a barra
    And pauso a barra de progresso antes dos 25%
    Then o valor da barra deve ser menor ou igual a 25%

  Scenario: Validar o reset da barra de progresso apos atingir 100%
    Given que estou na tela de "Progress Bar" dentro de "Widgets"
    When clico no botao Start para iniciar a barra
    And aguardo a barra de progresso chegar a 100%
    And clico no botao para resetar a barra
    Then o valor da barra deve retornar para 0%