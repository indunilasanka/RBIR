package aztec.rbir_rest2;

import aztec.rbir_backend.globals.Global;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan("aztec")
public class SpringBootWebApplication {

	
    @SuppressWarnings("unchecked")
	public static void main(String[] args) {
        new Global();
        SpringApplication.run(SpringBootWebApplication.class, args);
    }
}
