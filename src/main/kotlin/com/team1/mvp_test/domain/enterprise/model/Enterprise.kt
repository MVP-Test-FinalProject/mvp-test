package com.team1.mvp_test.domain.enterprise.model

import com.team1.mvp_test.domain.enterprise.dto.UpdateEnterpriseRequest
import jakarta.persistence.*
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

@Entity
@Table(name = "enterprise")
class Enterprise(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "email")
    @field:Pattern(regexp = "^[a-z0-9]+[a-z0-9._%+-]*@[a-z0-9.-]+\\.[a-z]$")
    val email: String,

    @Column(name = "name")
    @field:Size(min = 1)
    var name: String,

    @Column(name = "ceo_name")
    @field:Size(min = 1)
    var ceoName: String,

    @Column(name = "password")
    @field:Size(min = 8)
    @field:Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,}$")
    var password: String,

    @Column(name = "phone_number")
    @field:Size(min = 11, max = 11)
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