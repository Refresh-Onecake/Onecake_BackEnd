package refresh.onecake.member.adapter.api

class SwaggerNotes {

    companion object {
        const val SIGNUP_MEMO =
        """ 
            NOTE
            - signup 
            
            REQUEST HEADER
            - NONE
            
            REQUEST BODY(POST)
            - String "user_id"
            - String "password"
            - String "name"
            - String "phone_number"
            - String "member_type"
           
            RESPONSE BODY
            - 200
                {
                    "success": true,
                    "message": "회원가입을 성공했습니다."
                }
            - 400
                {
                    "success": false,
                    "message": "중복된 아이디 입니다."
                }  
        """

        const val LOGIN_MEMO =
        """
            NOTE
            - login 
            
            REQUEST HEADER
            - NONE
            
            REQUEST BODY(POST)
            - String "user_id"
            - String "password"
           
            RESPONSE BODY
            - 200
                {
                    "grantType": "bearer",
                    "accessToken": "eyJsdfseU~~~~~",
                    "refreshToken": "AiOjsdfsesMDI~~~~~",
                    "accessTokenExpiresIn": 1655099800307
                }
            - 401
                {
                    body 없고 status code만 401
                }
        """

        const val REISSUE_MEMO =
            """
            NOTE
            - reissue
            
            REQUEST HEADER
            - NONE
            
            REQUEST BODY(POST)
            - String "accessToken"
            - String "refreshToken"
           
            RESPONSE BODY
            - 200
                {
                    "grantType": "bearer",
                    "accessToken": "eyJhbGEFdsfsJIU~~~~~",
                    "refreshToken": "AiOjEfsSfU3MDI~~~~~",
                    "accessTokenExpiresIn": 1655099800307
                }
            - 400
                {
                    "grantType": "",
                    "accessToken": "",
                    "refreshToken": "",
                    "accessTokenExpiresIn": -1
                }
                - refresh token이 유효하지 않거나
                - 로그아웃된 사용자이거나
                - 토큰의 유저정보가 일치하지 않을 때
        """
    }
}