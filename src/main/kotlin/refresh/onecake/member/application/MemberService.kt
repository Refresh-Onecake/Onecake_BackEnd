package refresh.onecake.member.application

import org.springframework.stereotype.Service
import refresh.onecake.member.domain.MemberRepository
import refresh.onecake.response.adapter.dto.DefaultResponseDto
import refresh.onecake.store.domain.StoreRepository

@Service
class MemberService (
    private val memberRepository: MemberRepository,
    private val storeRepository: StoreRepository
){

    fun resign(): DefaultResponseDto {
        val id = SecurityUtil.getCurrentMemberId()

        val member = memberRepository.getById(id)
        member.isActivated = false
        memberRepository.save(member)

        val store = storeRepository.findStoreById(id)
        store.isActivated = false
        storeRepository.save(store)

        return DefaultResponseDto(true, "탈퇴 처리가 완료되었습니다.")
    }
}