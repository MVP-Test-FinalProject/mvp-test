package com.team1.mvp_test.domain.member.model

import com.team1.mvp_test.domain.member.dto.MemberUpdateRequest
import com.team1.mvp_test.domain.member.dto.SignUpInfoRequest
import com.team1.mvp_test.domain.oauth.provider.OAuthProvider
import jakarta.persistence.*

@Entity
@Table(name = "members")
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "name")
    var name: String? = null,

    @Column(name = "email")
    val email: String,

    @Column(name = "age")
    var age: Int? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "sex")
    var sex: Sex? = null,

    @Column(name = "info")
    var info: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "provider_name")
    val providerName: OAuthProvider? = null,

    @Column(name = "provider_id")
    val providerId: String? = null,

    @Column(name = "signup_state")
    var signUpState: Boolean = false,
) {
    fun updateMember(request: MemberUpdateRequest) {
        this.name = request.name
        this.info = request.info
    }

    fun updateSignUpInfo(request: SignUpInfoRequest) {
        name = request.name
        age = request.age
        sex = request.sex
        info = request.info
        signUpState = true
    }
}