package com.wma.repositories

import com.wma.domains.WMAParameters
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository('WMAFactorsRepository')
interface WMAFactorsRepository extends JpaRepository<WMAParameters, Integer> {
    WMAParameters findById(int id)
}
