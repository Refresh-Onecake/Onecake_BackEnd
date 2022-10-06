package refresh.onecake;

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.web.filter.HiddenHttpMethodFilter
import java.time.ZoneId
import java.util.*
import javax.annotation.PostConstruct


@EnableJpaAuditing
@SpringBootApplication
class OnecakeApplication {
	@PostConstruct
	fun started() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"))
	}
}

fun main(args: Array<String>) {
	runApplication<OnecakeApplication>(*args)
}
