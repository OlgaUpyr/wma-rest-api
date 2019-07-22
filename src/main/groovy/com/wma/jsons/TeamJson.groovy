package com.wma.jsons

import com.wma.domains.Team
import org.hibernate.validator.constraints.NotEmpty

class TeamJson {
    long id
    @NotEmpty(message = 'Team name is invalid')
    String name
    String country
    String region
    boolean isAttached

    TeamJson() {}

    TeamJson(Team team, boolean isAttached) {
        this.id = team.id
        this.name = team.name
        this.country = team.country
        this.region = team.region
        this.isAttached = isAttached
    }
}
