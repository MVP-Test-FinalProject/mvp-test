package com.team1.mvp_test.domain.mvptest.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import com.team1.mvp_test.domain.mvptest.model.MvpTest
import com.team1.mvp_test.domain.mvptest.model.QMvpTest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface MvpTestRepository : JpaRepository<MvpTest, Long>, MvpTestQueryDslRepository {
}

interface MvpTestQueryDslRepository {
    fun findAllUnsettledMvpTests(todayDate: LocalDateTime): List<MvpTest>
}

class MvpTestQueryDslRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : MvpTestQueryDslRepository {

    private val mvpTest: QMvpTest = QMvpTest.mvpTest
    override fun findAllUnsettledMvpTests(todayDate: LocalDateTime): List<MvpTest> {
        val builder = BooleanBuilder()
            .and(mvpTest.testEndDate.before(todayDate))
            .and(mvpTest.settlementDate.isNull)
        return queryFactory.selectFrom(mvpTest)
            .where(builder)
            .fetch()

    }
}
