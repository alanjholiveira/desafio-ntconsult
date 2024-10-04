# Desafio Técnico Back-End

Este projeto é uma aplicação desenvolvida para atender aos requisitos do desafio técnico proposto, utilizando as seguintes tecnologias:

- **Java 21**
- **Spring Boot 3**
- **Gradle**
- **Docker**
- **Testcontainers**
- **RestAssured**
- **ScheduleLock**
- **MySQL 8**
- **RabbitMQ 4**

## Descrição do Projeto

Este projeto implementa uma solução de backend que integra com um banco de dados MySQL e RabbitMQ para gerenciar mensagens de eventos e tarefas agendadas. A aplicação utiliza Spring Boot 3 como base e Java 21 para garantir um código moderno e eficiente.

Além disso, foi configurado o uso de **Testcontainers** para garantir que os testes unitários e de integração sejam realizados de forma isolada e controlada, possibilitando o uso de um banco de dados MySQL e um broker RabbitMQ reais durante os testes.

## Funcionalidades Implementadas

1. **Integração com MySQL 8**: Persistência de dados.
2. **Integração com RabbitMQ 4**: Envio e recebimento de mensagens assíncronas.
3. **Agendamento de Tarefas**: Usando ScheduleLock para garantir que tarefas agendadas sejam executadas corretamente.
4. **Testes Automatizados**: Usando RestAssured para validar as APIs e Testcontainers para testes de integração com MySQL e RabbitMQ.

## Extra: Mock para API Externa

Como a API externa necessária para o desafio estava offline, foi implementado um **mock** para simular as respostas dessa API, garantindo que a aplicação possa ser completamente testada e validada mesmo na ausência de conexão com a API real.

## Como Executar o Projeto

### Pré-requisitos

- Docker
- Java 21
- Gradle

### Passos

1. **Clone este repositório**:
    - Abra um terminal e execute o comando abaixo para clonar o repositório do projeto:
   ```bash
   git clone https://github.com/alanjholiveira/desafio-nttconsult.git

2. **Navegue até o diretório do projeto**:
    - Acesse o diretório que você acabou de clonar:
   ```bash
   cd desafio-nttconsult

3. **Suba os containers com Docker**:
    - Use o docker-compose para iniciar os serviços necessários (MySQL e RabbitMQ). 
    - Certifique-se de que o arquivo docker-compose.yml esteja configurado corretamente no diretório do projeto:
    ```bash
    docker-compose up -d

4. **Execute a aplicação**:
   - Para rodar a aplicação Spring Boot, use o Gradle:
   ```bash
   ./gradlew bootRun

5. **Execute os testes**:
   - Após a aplicação estar rodando, você pode executar os testes para garantir que tudo está funcionando corretamente:
   - Isso executa todos os testes automatizados configurados no projeto.
   ```bash
   ./gradlew test
   
6. **Acesse a aplicação**:
   - A aplicação deve estar acessível em http://localhost:8080. 
   - Você pode usar um navegador ou ferramentas como Postman para interagir com as APIs.


7. **Acesse a documentação do Swagger**:
   - Após a aplicação estar em execução, a documentação da API estará disponível em:
   ```bash
   http://localhost:8080/swagger-ui/index.html
