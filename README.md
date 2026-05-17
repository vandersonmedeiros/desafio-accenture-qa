# 🚀 Desafio de Automação QA — Accenture

Bem-vindo(a) ao repositório do meu desafio técnico de automação de testes! 

Este projeto foi desenvolvido para automatizar cenários de testes End-to-End (E2E) na plataforma [DemoQA](https://demoqa.com/), cobrindo tanto a camada de interface gráfica (Web UI) quanto a camada de serviços (API REST). O foco principal aqui não foi apenas fazer o teste "passar", mas sim criar uma arquitetura limpa, escalável e fácil de dar manutenção, utilizando padrões de mercado.

---

## 🛠️ Tecnologias e Ferramentas

Para construir esse ecossistema, escolhi as stacks mais bem consolidadas no universo corporativo:

* **Java 17:** A linguagem principal do projeto.
* **Maven:** Para o gerenciamento de dependências e execução do build.
* **Selenium WebDriver (v4):** Para a interação e automação do navegador Chrome.
* **RestAssured:** Para orquestrar as requisições HTTP e validar os contratos da API.
* **Cucumber (BDD):** Para escrever os cenários de teste em linguagem humana (Gherkin), servindo como documentação viva.
* **JUnit 4:** O motor responsável por executar os testes e fazer as asserções.
* **WebDriverManager:** Para gerenciar o binário do ChromeDriver automaticamente, sem dor de cabeça com versões locais.

---

## 📐 Decisões de Arquitetura

Se olhar o código, vai perceber que as responsabilidades estão bem separadas. Fiz isso seguindo as melhores práticas da engenharia de software:

1. **Page Object Model (POM):** Na automação web, as regras de negócio nunca se misturam com os localizadores da tela (`By.id`, `By.xpath`). Cada página tem sua própria classe no pacote `pages`. Se o site mudar o layout amanhã, só precisamos atualizar um único arquivo.

2. **API Client Pattern:** Para o back-end, isolei a configuração das chamadas HTTP na classe `BookStoreClient`. Os steps do Cucumber não precisam saber como montar um header ou um JSON, eles apenas chamam o cliente e validam a resposta.

3. **Clean BDD:** Os arquivos `.feature` foram escritos de forma fluida, focando no comportamento do usuário e não em ações robóticas ("clica aqui", "digita ali"). Usei a palavra-chave `And` para evitar a repetição cansativa de `Given/When/Then`.

4. **Resiliência (Anti-Flaky):** Como a DemoQA possui muitos banners e instabilidades de renderização, implementei esperas explícitas (`WebDriverWait`), tamanho fixo de janela em modo headless e fallback com `JavaScriptExecutor` para garantir que os cliques funcionem sempre.

---

## 📁 Estrutura do Projeto

A organização de pastas segue o padrão convencional do Maven:

```text
desafio-qa-accenture/
├── .github/workflows/        # Configuração da pipeline de CI/CD no GitHub Actions
├── src/main/java/            
│   ├── api/                  # Clientes e serviços de API (Back-end)
│   └── pages/                # Mapeamento de elementos e ações das telas (Front-end)
├── src/test/java/
│   ├── runners/              # Configuração de execução do Cucumber
│   └── stepDefinitions/      # O código Java que dá vida aos passos do Gherkin
├── src/test/resources/
│   └── features/             # Nossas histórias e cenários de teste escritos em Gherkin
└── pom.xml                   # Coração do Maven com todas as dependências

⚙️ Como executar na sua máquina
Quer rodar o projeto localmente? É bem simples. Você só vai precisar ter o Java 17 e o Maven instalados e configurados nas suas variáveis de ambiente.

Clone este repositório:

git clone [https://github.com/vandersonmedeiros/desafio-accenture-qa.git](https://github.com/vandersonmedeiros/desafio-accenture-qa.git)

Acesse a pasta do projeto:

cd desafio-accenture-qa
Rode a mágica:
Execute o comando abaixo para limpar builds antigos, baixar as dependências e rodar toda a suíte de testes (API + UI):

Bash
mvn clean test
Nota: Por padrão, os testes de interface vêm configurados para rodar no modo invisível (--headless). Isso garante que a execução seja muito mais rápida e não quebre por interferências externas (como você mexer o mouse sem querer).

Quer ver o robô trabalhando e o navegador abrindo na sua tela? > É super simples: basta abrir o arquivo src/test/java/stepDefinitions/UiSteps.java e comentar ou apagar a linha chromeOptions.addArguments("--headless");.

Ah, e independente de como você rodar, no final da execução um relatório HTML amigável com o resultado dos cenários sempre será gerado na pasta target/cucumber-reports.html.

🔄 Integração Contínua (CI/CD)
Automação boa é automação que roda sozinha. Configurei uma esteira de testes utilizando o GitHub Actions. Toda vez que um novo código é enviado (push) para a branch main ou develop, uma máquina virtual Linux é levantada na nuvem, instala o Java, e roda todos os testes. Se algo quebrar, a gente fica sabendo na hora.