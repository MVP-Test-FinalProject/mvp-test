package com.team1.mvp_test.domain.member.model

import com.team1.mvp_test.domain.member.dto.MemberUpdateRequest
import jakarta.persistence.*

@Entity
@Table(name = "members")
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "name")
    var name: String,

    @Column(name = "email")
    val email: String,

    @Column(name = "age")
    var age: Long,

    @Column(name = "sex")
    var sex: String,

    @Column(name = "info")
    var info: String,

    @Column(name = "provider_name")
    val providerName: String? = null,

    @Column(name = "provider_id")
    val providerId: Long? = null,

    @Column(name = "is_banned")
    val isBanned: Boolean? = false,
) {
    fun updateMember(request: MemberUpdateRequest) {
        this.name = request.name
        this.info = request.info
    }
}