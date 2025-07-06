package com.astrocharts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.kalwit.services.AstroService;

@SpringBootApplication
public class AstroChartsApplication {

	@Bean
	public AstroService astroService() {
		return new AstroService();
	}

    public static void main(String[] args) {
        SpringApplication.run(AstroChartsApplication.class, args);
    }
}
