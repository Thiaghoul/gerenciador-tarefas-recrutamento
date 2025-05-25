# Etapa 1: Build da aplicação com Maven (Multi-stage build)
# Usar uma imagem Maven que já tenha o JDK 8
FROM maven:3.8-openjdk-8 AS builder

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia primeiro o pom.xml para aproveitar o cache de dependências do Docker
COPY pom.xml .

# Baixa as dependências (se o pom.xml não mudou, esta camada será cacheada)
RUN mvn dependency:go-offline -B

# Copia o restante do código fonte
COPY src ./src

# Constrói o artefato .war (pulando os testes)
# O WAR será gerado em /app/target/SEU_FINAL_NAME.war
RUN mvn -DskipTests clean package


# Etapa 2: Configurar o ambiente de execução com Tomcat
# Usar uma imagem oficial do Tomcat que já tenha o JDK 8 (Corretto é uma boa opção)
FROM tomcat:9.0-jdk8-corretto

# Define o nome do seu WAR. Ajuste SEU_FINAL_NAME para o <finalName> no seu pom.xml
# No seu pom.xml, o finalName é "gerenciador-tarefas-recrutamento"
ARG WAR_FILE_NAME=gerenciador-tarefas-recrutamento.war

# Remove o conteúdo padrão da pasta webapps do Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copia o .war construído na etapa anterior para a pasta webapps do Tomcat.
# Para simplificar o acesso (ex: http://localhost:8080/), renomeamos para ROOT.war
# Se quiser acessar com /gerenciador-tarefas-recrutamento, copie como está.
COPY --from=builder /app/target/${WAR_FILE_NAME} /usr/local/tomcat/webapps/ROOT.war

# A porta padrão do Tomcat (8080) já é exposta pela imagem base.
# O Render fará o mapeamento da porta $PORT para a porta 8080 do container.

# O comando CMD para iniciar o Tomcat (`catalina.sh run`) já está na imagem base.
