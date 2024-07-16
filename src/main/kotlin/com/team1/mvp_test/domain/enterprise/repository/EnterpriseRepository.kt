package com.team1.mvp_test.domain.enterprise.repository

import com.team1.mvp_test.domain.enterprise.model.Enterprise
import org.springframework.data.jpa.repository.JpaRepository

interface EnterpriseRepository : JpaRepository<Enterprise, Long> {
}