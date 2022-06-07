package refresh.onecake.member.domain.exception

enum class ExceptionType(
    val statusCode: Int,
    val responseMessage: String
) {

    UNAUTHORIZED(401, "Authentication Exception"),
    ;

}