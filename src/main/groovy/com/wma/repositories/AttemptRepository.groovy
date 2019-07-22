package com.wma.repositories

import com.wma.domains.Attempt
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository('attemptRepository')
interface AttemptRepository extends JpaRepository<Attempt, Long> {
}