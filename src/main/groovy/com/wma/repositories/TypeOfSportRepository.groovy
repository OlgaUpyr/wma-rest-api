package com.wma.repositories

import com.wma.domains.TypeOfSport
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository('typeOfSportRepository')
interface TypeOfSportRepository extends JpaRepository<TypeOfSport, Integer> {

    TypeOfSport findById(int id)

    @Query(nativeQuery = true, value = '''SELECT st.* FROM types_of_sport st WHERE st.is_outdoor=TRUE''')
    List<TypeOfSport> findOutdoorSportTypes()

    @Query(nativeQuery = true, value = '''SELECT st.* FROM types_of_sport st WHERE st.is_indoor=TRUE''')
    List<TypeOfSport> findIndoorSportTypes()

    @Query(nativeQuery = true, value = '''SELECT st.* FROM types_of_sport st WHERE st.is_nonstadia=TRUE''')
    List<TypeOfSport> findNonstadiaSportTypes()
}

