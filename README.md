Desafio QA Accenture - Automação (UI & API)

- Este projeto contém uma suíte completa de testes automatizados de ponta a ponta (E2E), cobrindo tanto a interface de usuário (UI) quanto a camada de serviços (API Rest) da plataforma DemoQA. O objetivo é demonstrar uma estratégia sólida de testes modernos: validação de fluxos complexos de front-end combinada com testes de contrato e integração no back-end.

- A automação foi projetada focando em alta estabilidade para pipelines de Integração Contínua (CI/CD), resiliência contra race conditions em Single Page Applications (React) e gerenciamento dinâmico de estado/autenticação em requisições REST.

🛠️ Tecnologias Utilizadas

Camada de UI & Orquestração

- Java 17 Linguagem base do projeto.
- Selenium WebDriver (v4.18.1) - Framework para interações dinâmicas com o DOM.
- Cucumber BDD - Escrita de cenários focados em comportamento utilizando a sintaxe Gherkin.
-JUnit 4 - Runner e motor de asserções dos testes.
- Maven - Gerenciador de dependências e ciclo de vida do build.
- WebDriverManager - Gerenciamento automatizado do binário do ChromeDriver.

Camada de API

- RestAssured - Framework especializado em validação e teste de serviços REST de alta performance.
- Jackson Databind - Biblioteca para serialização e desserialização de objetos Java para JSON (POJOs).

📂 Estrutura do ProjetoO projeto 

- adota o padrão Page Objects para o front-end e API Client / Service Objects para o back-end, isolando requisições e interações das asserções de teste.Plaintextdesafio-accenture-qa/

│
├── src/
│   ├── main/java/
│   │   ├── pages/                     # Interações com a UI (Ações do DOM)
│   │   │   ├── HomePage.java
│   │   │   ├── WebTablesPage.java
│   │   │   └── ...
│   │   │
│   │   └── api/                       # Clientes HTTP e Endpoints da API
│   │       ├── AccountClient.java     # Endpoints /User e /GenerateToken
│   │       └── BookStoreClient.java   # Endpoints /Books
│   │
│   └── src/test/java/
│       ├── runners/                   # Orquestrador de execução (TestRunner.java)
│       └── stepdefinitions/           # Definição dos passos (Steps de UI e API)
│           ├── UiSteps.java
│           └── ApiSteps.java
│
├── src/test/resources/features/       # Especificações Executáveis (Gherkin BDD)
│   ├── ui_web_tables.feature
│   └── api_bookstore.feature
└── pom.xml                            # Dependências (Selenium, RestAssured, Cucumber)

🚀 Como Executar a Suíte CompletaCertifique-se de ter o Java JDK 17 e o Maven configurados no seu ambiente. No terminal, execute:

- mvn clean test

O Maven executará os cenários de API via HTTP e disparará os cenários de UI em segundo plano (modo Headless) com uma resolução fixa de 1920x1080.

📑 Diário de Bordo: Desafios, Bugs e Engenharia de Soluções

- Automatizar fluxos híbridos exige atenção redobrada à volatilidade dos ambientes. Abaixo, registro de forma transparente as principais barreiras técnicas que enfrentei durante este desafio e as estratégias de engenharia utilizadas para superá-las.

1. O Fantasma da Compilação Vermelha (Escopo vs. Arquitetura)

- O Problema: Durante as primeiras refatorações das classes de página, inseri chamadas diretas de validação (Assert.fail) dentro da WebTablesPage. O código quebrou imediatamente com erros de compilação no Maven.

- A Causa: As classes de suporte residem em src/main/java, enquanto a dependência do JUnit no pom.xml estava estritamente isolada com o escopo <scope>test</scope>. O compilador barrou o acesso por violação de fronteira arquitetural. Além disso, delegar asserções para a camada de visualização fere o padrão Page Objects.

- Como superei: Removi as importações de teste do pacote main. Substituí as chamadas por exceções nativas (throw new RuntimeException()). Agora, as páginas e clientes de API apenas repassam os resultados ou falhas físicas, deixando as asserções exclusivamente na camada de stepdefinitions.

2. O Bug Invisível do React em Modo Headless

- O Problema:  No modo guiado (tela aberta), o filtro de registros funcionava. Em modo Headless via terminal, o cenário unitário acusava que o colaborador criado não era encontrado no grid.A Causa: O DemoQA usa React. Os comandos tradicionais clear() e sendKeys() do Selenium digitavam rápido demais no Headless, atropelando o ciclo de atualização do estado da árvore virtual (Virtual DOM). O texto mudava na tela, mas o estado interno do componente continuava vazio, impedindo o disparo do filtro.

- Como superei: Criei um mecanismo de preenchimento síncrono que intercepta o protótipo nativo do elemento e força o React a capturar o evento de input imediatamente: JavaScriptvar 

    var setter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;
    setter.call(arguments[0], arguments[1]);
    arguments[0].dispatchEvent(new Event('input', { bubbles: true }));

3. O Gargalo da Paginação e Conflitos na Exclusão em Lote

- O Problema: O cenário massivo cadastrava 12 colaboradores com sucesso, mas falhava de forma intermitente ao tentar excluir o segundo registro (teste02).

- A Causa: O grid possui paginação nativa limitada a 10 linhas. Ao inserir 12 itens, os dados transbordavam para a Página 2. O loop tentava buscar o elemento e disparava Timeout porque ele estava escondido na outra página. Filtrar e limpar a busca 24 vezes seguidas também gerava travamentos de renderização.

- Como superei: Inverti a lógica de manipulação. Criei um método que altera dinamicamente o select de paginação para exibir até 50 registros na mesma tela logo no início do passo. Com todos os dados consolidados na primeira página, o laço de passos realiza as exclusões diretamente por identificação de linha, eliminando requisições repetidas de busca.

4. Concorrência e a Pegadinha do Código 406 na BookStore APIO 

- Problema: Ao rodar os testes de API de forma isolada, os endpoints de criação de usuário passavam. Ao reexecutar a suíte, o teste quebrava com erro 406 Not Acceptable.

- A Causa: O banco de dados do DemoQA não limpa os dados automaticamente entre execuções. Usar uma massa de dados estática (como um nome fixo "usuarioAccenture") fazia com que a API rejeitasse a criação na segunda rodada porque o usuário já existia no banco de dados da aplicação.

- Como superei: Implementei a geração de payload dinâmico utilizando a classe UUID do Java. Cada execução gera uma string aleatória única para o userName e gerencia senhas complexas via código. Isso garantiu testes totalmente independentes, idempotentes e imunes ao estado prévio do banco de dados.

5. O Labirinto da Autenticação em Cadeia (Bearer Tokens)

- O Problema: O cenário de aluguel de livros falhava constantemente com o código de status 401 Unauthorized, mesmo o usuário tendo sido criado no passo anterior.

- A Causa: A BookStore API exige autenticação estrita por Bearer Token gerado via endpoint /GenerateToken. Os testes tentavam fazer a requisição de aluguel postando o livro antes de validar se aquele usuário específico estava de fato autorizado no microsserviço.

- Como superei: Desenvolvi um fluxo encadeado no AccountClient. O método de setup captura o ID gerado no POST de criação, dispara imediatamente a geração do token, valida a autorização e armazena esse token em uma variável de contexto. Nas requisições subsequentes do BookStoreClient, o header .header("Authorization", "Bearer " + token) é injetado dinamicamente de forma transparente, amarrando todo o ciclo de vida da sessão da API.

🎯 Cobertura de Cenários AutomatizadosInterface (UI)Browser Windows: Validação de abertura, troca de foco e leitura de mensagens em novas janelas do navegador.

- Practice Form: Upload de arquivos e submissão de formulários com dados dinâmicos.

- Progress Bar: Controle fino e asserção de estados da barra de progresso (Início, Pausa $\le$ 25% e Reset após 100%).

- Sortable: Manipulação física e validação de ordenação por drag and drop.

- Web Tables: Operações completas de CRUD (Inserção, Filtro, Edição de campos, Exclusão unitária e Limpeza em lote).

- Serviços (API)Account Service: Registro de novas credenciais, geração de Bearer Tokens e validação de autorização de acessos.

- BookStore Service: Consulta ao catálogo de livros e fluxo de aluguel de títulos com verificação de persistência no perfil do cliente.