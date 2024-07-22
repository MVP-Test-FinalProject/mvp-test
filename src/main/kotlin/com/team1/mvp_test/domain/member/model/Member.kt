package com.team1.mvp_test.domain.member.model

import com.team1.mvp_test.domain.member.dto.MemberUpdateRequest
import com.team1.mvp_test.domain.member.dto.SignUpInfoRequest
import com.team1.mvp_test.domain.oauth.provider.OAuthProvider
import jakarta.persistence.*
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size

@Entity
@Table(name = "members")
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "name")
    @field:Size(min = 1)
    var name: String? = null,

    @Column(name = "email")
    val email: String,

    @Column(name = "age")
    @field:Min(value = 15)
    var age: Int? = null,

    @Column(name = "sex")
    var sex: String? = null,

    @Column(name = "info")
    @field:Size(max = 200)
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