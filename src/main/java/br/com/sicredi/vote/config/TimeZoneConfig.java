package br.com.sicredi.vote.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
public class TimeZoneConfig {

    @PostConstruct
    public void init() {
        // Define o fuso horário padrão para o Brasil
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
    }

}
