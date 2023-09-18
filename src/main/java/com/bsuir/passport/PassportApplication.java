package com.bsuir.passport;

import com.bsuir.passport.model.SampleApplication;
import com.bsuir.passport.service.SampleApplicationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.bsuir.passport.constant.FileConstant.*;

@SpringBootApplication
public class PassportApplication {

	public static void main(String[] args) {
		SpringApplication.run(PassportApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(SampleApplicationService sampleApplicationService) {
		return args -> {
			SampleApplication sa1 = new SampleApplication();
			sa1.setName("FORM33");
			sa1.setText("Выдача и обмен паспорта - истечение срока действия (25, 45 лет)");
			sa1.setUrl("http://10.0.2.2:8080" + SA_PATH + FORWARD_SLASH + "file" + FORWARD_SLASH + "FORM33" + DOT + DOCX_EXTENSION);
			sampleApplicationService.create(sa1);
			SampleApplication sa2 = new SampleApplication();
			sa2.setName("FORM44");
			sa2.setText("Выдача и обмен паспорта - непригодность для использования");
			sa2.setUrl("http://10.0.2.2:8080" + SA_PATH + FORWARD_SLASH + "file" + FORWARD_SLASH + "FORM44" + DOT + DOCX_EXTENSION);
			sampleApplicationService.create(sa2);
		};
	}

}
