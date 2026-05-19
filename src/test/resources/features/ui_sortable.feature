@ui
Feature: Reordenacao de Elementos

  Scenario: Reordenar o ultimo item da lista para o topo
    Given que estou na tela de "Sortable" dentro de "Interactions"
    When movo o ultimo elemento para a primeira posicao da lista
    Then o elemento deve ser exibido como o primeiro item da lista