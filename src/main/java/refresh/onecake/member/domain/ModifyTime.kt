package refresh.onecake.member.domain

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import javax.persistence.Embeddable

@Embeddable
data class ModifyTime (

    @CreatedDate
    var createdTime: LocalDateTime = LocalDateTime.MIN,

    @LastModifiedDate
    var lastModifiedTime: LocalDateTime = LocalDateTime.MIN

)