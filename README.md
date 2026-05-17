# Accenture QA Automation Challenge

Este projeto contém a automação de testes de ponta a ponta para a plataforma DemoQA, cobrindo validações completas de API e de Interface de Usuário (UI). A arquitetura foi projetada seguindo padrões de nível corporativo para garantir escalabilidade, legibilidade e fácil manutenção.

---

## 🚀 Tecnologias Utilizadas

* **Linguagem:** Java 17
* **Gerenciador de Dependências:** Maven
* **Automação de API:** RestAssured
* **Automação Web:** Selenium WebDriver
* **Abordagem de Testes:** BDD (Behavior-Driven Development) com Cucumber JVM
* **Executor de Testes & Asserts:** JUnit 4
* **Gerenciamento de Drivers:** WebDriverManager
* **CI/CD:** GitHub Actions

---

## 📐 Arquitetura do Projeto

O projeto utiliza padrões de design consolidados no mercado para separar as responsabilidades de forma clara:

* **API Client Pattern:** Camada isolada dentro de `src/main/java/api` para gerenciar chamadas HTTP, payloads e endpoints de forma centralizada.
* **Page Object Model (POM):** Estrutura em `src/main/java/pages` para encapsular os seletores da web e interações lógicas, protegendo os testes contra mudanças de layout.
* **BDD Features:** Especificações escritas em Gherkin na pasta `src/test/resources/features` utilizando palavras-chave nativas em inglês com descrições das ações em português.
* **Step Definitions:** Vinculação direta do comportamento de negócio com o código técnico dentro de `src/test/java/stepDefinitions`.

---

## 🌿 Estratégia de Branches (Git Flow)

Para manter a integridade do código, o repositório adota uma estratégia rígida de ramificações:

* `main`: Armazena o código estável, testado e pronto para produção.
* `develop`: Ramificação utilizada para o desenvolvimento diário e integração de novas funcionalidades.

---

## 🛠️ Pré-requisitos para Execução Local

Antes de rodar os testes em uma máquina macOS limpa, certifique-se de ter os componentes abaixo configurados via Homebrew:

```bash
brew install maven
brew install openjdk@17

🏃 Como Executar os Testes
Para limpar o ambiente, compilar o código e disparar o ciclo completo de execução (API e UI) via linha de comando, navegue até a raiz do projeto e execute:

mvn clean test

🌐 Integração Contínua (CI/CD)
O projeto possui uma esteira automatizada configurada via GitHub Actions (.github/workflows/main.yml). Toda vez que um comando git push ou um Pull Request é direcionado para as ramificações main ou develop, a pipeline executa os seguintes passos em um ambiente Linux isolado:

Inicialização do ambiente virtual

Configuração do Java JDK 17 (Temurin)

Execução automatizada do comando do Maven

Geração do relatório de testes