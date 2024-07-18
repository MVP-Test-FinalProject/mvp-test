package com.team1.mvp_test.domain.mvptest.model

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
class CategoryMapId(
    @Column(name = "category_id")
    val categoryId: Long?,

    @Column(name = "mvp_test_id")
    val mvpTestId: Long?
) : Serializable {
    // Default constructor for JPA
    constructor() : this(0L, 0L)

    // Equals and hashCode methods should be overridden for composite keys
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CategoryMapId
        return categoryId == that.categoryId && mvpTestId == that.mvpTestId
    }

    override fun hashCode(): Int {
        return 31 * categoryId.hashCode() + mvpTestId.hashCode()
    }
}