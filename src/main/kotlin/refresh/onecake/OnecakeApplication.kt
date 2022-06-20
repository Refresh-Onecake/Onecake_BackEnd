package refresh.onecake;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class OnecakeApplication

fun main(args: Array<String>) {
	runApplication<OnecakeApplication>(*args)
}
