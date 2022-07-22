package refresh.onecake.member.adapter.infra.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.SpringDocUtils
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
//TODO :: profile 도입 후 추가
//@Profile("!" + EnvironmentConstants.SPRING_PROFILE_PRODUCTION)
class SwaggerConfig(
//    private val buildProperties: BuildProperties
) : WebMvcConfigurer {
    @Bean
    fun openApi(): OpenAPI = OpenAPI()
//        .components(
//            Components()
//                .addSecuritySchemes(
//                    "Authorization",
//                    SecurityScheme()
//                        .name("Authorization")
//                        .description("사용자 인증 토큰")
//                        .type(SecurityScheme.Type.HTTP)
//                        .scheme("bearer")
//                        .bearerFormat("JWT")
//                        .`in`(SecurityScheme.In.HEADER)
//                )
//        )
        .info(
            Info()
//              .title(buildProperties.name)
//              .version(buildProperties.version)
                .description("OneCake API")
        )

    init {
        // request parameter 무시하기 위해 설정
        SpringDocUtils.getConfig()
//            .addRequestWrapperToIgnore(
//                AuthenticationUser::class.java,
//            )
    }
}