# Etapa de build.
FROM maven:3.8.5-openjdk-17 AS build

# Diretório de trabalho.
WORKDIR /app

# Copiar apenas o arquivo pom.xml e baixar as dependências.
COPY pom.xml .

## Executar o comando para baixar as dependências do Maven e prepará-las para um build offline
RUN mvn dependency:go-offline -B

# Copiar o restante dos arquivos de código-fonte.
COPY src ./src

# Construir a aplicação.
RUN mvn clean package -DskipTests
# A flag '-DskipTests' evita a execução de testes durante o build para acelerar o processo.



# Etapa de runtime
FROM openjdk:17-slim AS runtime
# Define a imagem base para a etapa de runtime com OpenJDK 17 em uma versão slim.
# A versão slim é menor e mais eficiente para executar a aplicação.

WORKDIR /app


# Copiar o arquivo JAR construido da etapa de build para o diretório de trabalho.
COPY --from=build /app/target/geradorThumbnail-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE  8080 

# Comando para executar a aplicação.
ENTRYPOINT [ "java", "-jar", "app.jar" ]
