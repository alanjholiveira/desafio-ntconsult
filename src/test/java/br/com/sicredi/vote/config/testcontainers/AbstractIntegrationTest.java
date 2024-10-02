package br.com.sicredi.vote.config.testcontainers;

import br.com.sicredi.vote.DesafioPautaApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.RabbitMQContainer;

@SpringBootTest(classes = DesafioPautaApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractIntegrationTest {

    static final RabbitMQContainer rabbitMQContainer;
    static final MySQLContainer<?> mysqlContainer;

    static {
        mysqlContainer =  new MySQLContainer<>("mysql:8.0.34-debian")
                .withUsername("test")
                .withPassword("password")
                .withDatabaseName("test")
                .withReuse(true);
        rabbitMQContainer = new RabbitMQContainer("rabbitmq:4.0.2-management-alpine")
                .withReuse(true);
        rabbitMQContainer.start();
        mysqlContainer.start();
    }
    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("cloud.local.messaging.host", rabbitMQContainer::getContainerIpAddress);
        registry.add("event.rabbitmq.host", rabbitMQContainer::getHost);
        registry.add("event.rabbitmq.port", rabbitMQContainer::getFirstMappedPort);
        registry.add("event.rabbitmq.password", rabbitMQContainer::getAdminPassword);
        registry.add("event.rabbitmq.username", rabbitMQContainer::getAdminUsername);

        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
    }

}
