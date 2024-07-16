package com.team1.mvp_test.domain.enterprise.model

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
    val name: String,

    @Column(name = "ceo_name")
    var ceoName: String,

    @Column(name = "password")
    var password: String,

    @Column(name = "phone_number")
    var phoneNumber: String,

    @Column(name = "is_confirmed")
    var isConfirmed: Boolean? = false,

    @Column(name = "is_blocked")
    var isBlocked: Boolean? = false,
) {

}