# Gerenciador de Tarefas - Desafio Técnico

## Autor
Thiago Henriques
thiagohnunes.w@gmail.com

## Objetivo do Projeto
Desenvolvimento de uma aplicação web para gerenciamento de tarefas como parte do processo seletivo para a vaga de eságio para Desenvolvedor Java. O sistema permite o cadastro, visualização, edição, exclusão e conclusão de tarefas, seguindo os requisitos fornecidos.

## Link da Aplicação Online
A aplicação está deployada e pode ser acessada através do seguinte link:
**[https://gerenciador-tarefas-n4ew.onrender.com/](https://gerenciador-tarefas-n4ew.onrender.com/)**

## Funcionalidades Implementadas
O sistema de Gerenciamento de Tarefas possui as seguintes funcionalidades:
* **Cadastro de Novas Tarefas:** Permite a criação de tarefas com título, descrição, responsável, prioridade e deadline.
* **Listagem de Tarefas:** Exibe todas as tarefas cadastradas com opção de filtros por número (ID), título/descrição, responsável e situação.
* **Edição de Tarefas:** Permite a alteração dos dados de uma tarefa existente.
* **Exclusão de Tarefas:** Permite a remoção de tarefas do sistema.
* **Conclusão de Tarefas:** Permite marcar uma tarefa como "CONCLUIDA", alterando sua situação.
* **Validação de Formulário:** Campos obrigatórios são validados no cadastro e edição.
* **Interface Responsiva (Básica):** Ajustes para melhor visualização em telas menores na listagem de tarefas.
* **Deploy na Nuvem:** Aplicação implantada na plataforma Render.com.
* **Testes Unitários:** Testes básicos para a camada de serviço (`TarefaService`) utilizando JUnit 5 e H2.

## Tecnologias Utilizadas
* **Back-end:**
    * Java 8
    * JavaServer Faces (JSF) 2.2 (Implementação Mojarra 2.2.20)
    * Java Persistence API (JPA) (Implementação Hibernate 5.6.14.Final)
    * Servlet API 4.0.1
* **Banco de Dados:**
    * PostgreSQL (para desenvolvimento local e produção na nuvem)
    * H2 Database (para testes unitários/integração)
* **Build e Gerenciamento de Dependências:**
    * Apache Maven
* **Testes:**
    * JUnit 5
* **Servidor de Aplicação (em desenvolvimento/deploy):**
    * Apache Tomcat 9 (utilizado via Docker para o deploy no Render)
* **Containerização (para Deploy):**
    * Docker
* **Plataforma de Deploy (Cloud):**
    * Render.com
* **Front-end:**
    * XHTML (com Facelets)
    * CSS3

## Instruções para Executar o Projeto Localmente

### Pré-requisitos
* Java Development Kit (JDK) 8
* Apache Maven 3.6+
* Servidor PostgreSQL (versão 12+ recomendada)
* Um servidor de aplicação compatível com Servlet 4.0 / JSF 2.2 (ex: Apache Tomcat 9)
* Git

### Passos para Configuração Local
1.  **Clonar o Repositório:**
    ```bash
    git clone [https://github.com/Thiaghoul/gerenciador-tarefas-recrutamento.git](https://github.com/Thiaghoul/gerenciador-tarefas-recrutamento.git)
    cd gerenciador-tarefas-recrutamento
    ```
2.  **Configurar o Banco de Dados PostgreSQL Local:**
    * Crie um banco de dados chamado `gerenciador-tarefas`.
    * Configure um usuário e senha para acessar este banco.
    * Ajuste as propriedades de conexão no arquivo `src/main/resources/META-INF/persistence.xml` para corresponderem à sua configuração local:
        ```xml
        <property name="javax.persistence.jdbc.url" value="jdbc:postgresql://localhost:5432/gerenciador-tarefas"/>
        <property name="javax.persistence.jdbc.user" value="SEU_USUARIO_LOCAL"/>
        <property name="javax.persistence.jdbc.password" value="SUA_SENHA_LOCAL"/>
        <property name="hibernate.hbm2ddl.auto" value="update"/>
        ```
3.  **Construir o Projeto com Maven:**
    ```bash
    mvn clean package
    ```
    Isso irá gerar o arquivo `gerenciador-tarefas-recrutamento.war` na pasta `target/`.

4.  **Implantar no Servidor Tomcat Local:**
    * Copie o arquivo `target/gerenciador-tarefas-recrutamento.war` para a pasta `webapps` do seu Tomcat.
    * Inicie o Tomcat.
    * A aplicação estará acessível em `http://localhost:8080/gerenciador-tarefas-recrutamento/`.
    * **Alternativamente (via IntelliJ):** Configure um servidor Tomcat local no IntelliJ IDEA, adicione o artefato "war exploded" do projeto para deploy e execute a configuração do servidor.

## Estrutura do Projeto (Principais Diretórios)
* `src/main/java`: Código fonte Java (Beans JSF, Entidades JPA, Enums, DAOs, Services).
* `src/main/resources`: Arquivos de configuração (ex: `META-INF/persistence.xml`).
* `src/main/webapp`: Arquivos da aplicação web (páginas XHTML, `WEB-INF/`, CSS).
    * `WEB-INF/templates/template.xhtml`: Template base das páginas.
    * `resources/css/estilo.css`: Folha de estilos principal.
* `src/test/java`: Código fonte dos testes unitários.
* `src/test/resources`: Arquivos de configuração para testes (ex: `META-INF/persistence.xml` para H2).
* `Dockerfile`: Define a imagem Docker para deploy no Render.com.
* `pom.xml`: Arquivo de configuração do Maven.

## Itens Entregues
* Código fonte completo do projeto no repositório GitHub: [https://github.com/Thiaghoul/gerenciador-tarefas-recrutamento.git](https://github.com/Thiaghoul/gerenciador-tarefas-recrutamento.git)
* Link da aplicação deployada no Render.com: [https://gerenciador-tarefas-n4ew.onrender.com/](https://gerenciador-tarefas-n4ew.onrender.com/)

## Observações Adicionais
* O deploy da aplicação foi realizado utilizando Docker e a plataforma Render.com, com o banco de dados PostgreSQL também hospedado no Render.
* Foram implementados testes unitários para a camada de serviço utilizando JUnit 5 e banco de dados H2 em memória.

---
