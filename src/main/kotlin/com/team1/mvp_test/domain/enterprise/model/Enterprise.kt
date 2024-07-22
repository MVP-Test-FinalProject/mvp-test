package com.team1.mvp_test.domain.enterprise.model

import com.team1.mvp_test.domain.enterprise.dto.UpdateEnterpriseRequest
import jakarta.persistence.*

@Entity
@Table(name = "enterprise")
class Enterprise(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "email")
    val email: String,

    @Column(name = "name")
    var name: String,

    @Column(name = "ceo_name")
    var ceoName: String,

    @Column(name = "password")
    var password: String,

    @Column(name = "phone_number")
    var phoneNumber: String,

    @Column(name = "state")
    var state: EnterpriseState = EnterpriseState.PENDING,
) {
    fun update(request: UpdateEnterpriseRequest) {
        this.name = request.name
        this.ceoName = request.ceoName
        this.phoneNumber = request.phoneNumber
    }
}