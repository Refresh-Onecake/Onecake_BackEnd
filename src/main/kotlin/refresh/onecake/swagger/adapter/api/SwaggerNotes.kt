package refresh.onecake.swagger.adapter.api

class SwaggerNotes {

    companion object {
        const val LOGIN_CONTROLLER_TAG = "Auth"
        const val S3_IMAGE_CONTROLLER_TAG = "S3 Image Upload"
        const val SELLER_CONTROLLER_TAG = "Seller"

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
                
            - 401
                - 토큰 만료 시간이 다 되었을 때
        """

        const val REGISTER_STORE_MEMO =
            """
                NOTE
                - register store
                
                REQUEST HEADER
                - bearer token
                - content-type : multipart/form-data
                
                REQUEST BODY
                - json : applyStoreRequestDto
                - image : multipartFile(image 형식)
                
                RESPONSE BODY
                - 200
                    {
                        "success": true,
                        "message": "입점 신청을 완료하였습니다."
                    }
                    ,
                    {
                        "success": false,
                        "message": "이미 입점한 판매자 입니다."
                    }
            """
    }
}