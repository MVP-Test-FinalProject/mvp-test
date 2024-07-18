package com.team1.mvp_test.domain.mvptest.model

import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import jakarta.persistence.Table

@Table(name = "category_map")
@Entity
class CategoryMap(
    @EmbeddedId
    val id: CategoryMapId,

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "category_id")
    val category: Category

) {

}