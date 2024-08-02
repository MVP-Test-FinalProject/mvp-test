package com.team1.mvp_test.domain.mvptest.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import com.team1.mvp_test.domain.member.model.MemberTestState
import com.team1.mvp_test.domain.member.model.QMember
import com.team1.mvp_test.domain.member.model.QMemberTest
import com.team1.mvp_test.domain.mvptest.dto.MemberInfoResponse
import com.team1.mvp_test.domain.mvptest.model.MvpTest
import com.team1.mvp_test.domain.mvptest.model.QMvpTest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface MvpTestRepository : JpaRepository<MvpTest, Long>, MvpTestQueryDslRepository {
    fun findAllByEnterpriseId(enterpriseId: Long): List<MvpTest>
}

interface MvpTestQueryDslRepository {
    fun findAllUnsettledMvpTests(todayDate: LocalDateTime): List<MvpTest>
    fun findMemberList(testId: Long, enterpriseId: Long): List<MemberInfoResponse>
}

class MvpTestQueryDslRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : MvpTestQueryDslRepository {

    private val mvpTest: QMvpTest = QMvpTest.mvpTest
    private val memberTest: QMemberTest = QMemberTest.memberTest
    private val member: QMember = QMember.member

    override fun findAllUnsettledMvpTests(todayDate: LocalDateTime): List<MvpTest> {
        val builder = BooleanBuilder()
            .and(mvpTest.testEndDate.before(todayDate))
            .and(mvpTest.settlementDate.isNull)
        return queryFactory.selectFrom(mvpTest)
            .where(builder)
            .fetch()
    }

    override fun findMemberList(testId: Long, enterpriseId: Long): List<MemberInfoResponse> {
        return queryFactory
            .select(
                Projections.constructor(
                    MemberInfoResponse::class.java,
                    member.id,
                    member.email,
                    member.phoneNumber,
                    member.name,
                )
            )
            .from(memberTest)
            .join(memberTest.member, member)
            .where(
                memberTest.test.id.eq(testId)
                    .and(memberTest.test.enterpriseId.eq(enterpriseId))
                    .and(memberTest.state.eq(MemberTestState.APPROVED))
            )
            .fetch()
    }
}
