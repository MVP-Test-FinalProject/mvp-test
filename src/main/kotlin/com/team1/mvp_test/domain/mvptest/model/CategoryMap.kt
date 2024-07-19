package com.team1.mvp_test.domain.mvptest.model

import jakarta.persistence.*

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