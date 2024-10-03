package br.com.sicredi.vote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
//@EnableFeignClients //Comentado porque API está offline e foi criado mock com comportamento semelhante
@EnableJpaAuditing
public class DesafioPautaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesafioPautaApplication.class, args);
	}

}
