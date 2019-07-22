package com.wma.repositories

import com.wma.domains.Team
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository('teamRepository')
interface TeamRepository extends JpaRepository<Team, Long> {
    Team findById(long id)

    List<Team> findAll()
}
