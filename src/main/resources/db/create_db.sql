/*CREATE DATABASE wma;*/

/*==============================================================*/
/* Table: Competitions                                          */
/*==============================================================*/
create table competitions (
   competition_id        BIGSERIAL            not null,
   name                  TEXT                 null,
   venue                 TEXT                 null,
   competition_type      TEXT                 null,
   competition_class     TEXT                 null,
   start_date            DATE                 null,
   end_date              DATE                 null,
   participants_limit    INTEGER              null,
   constraint PK_COMPETITIONS primary key (competition_id)
);

create unique index competitions_PK on competitions (competition_id);
create index index_wma_competition_start on competitions (start_date);
create index index_wma_competition_end on competitions (end_date);

/*==============================================================*/
/* Table: Teams                                                 */
/*==============================================================*/
create table teams (
   team_id              BIGSERIAL            not null,
   name                 TEXT                 null,
   country              TEXT                 null,
   region               TEXT                 null,
   constraint PK_TEAMS primary key (team_id)
);

create unique index teams_PK on teams (team_id);
create index index_wma_team_name on teams (name);

/*==============================================================*/
/* Table: AgeCategories                                         */
/*==============================================================*/
create table age_categories (
   category_id          SERIAL               not null,
   category             TEXT                 null,
   gender               BOOLEAN              null,
   age_from             INTEGER              null,
   age_to               INTEGER              null,
   constraint PK_AGECATEGORIES primary key (category_id)
);

create unique index age_categories_PK on age_categories (category_id);

/*==============================================================*/
/* Table: Sportsmen                                             */
/*==============================================================*/
create table sportsmen (
   sportsman_id         BIGSERIAL            not null,
   category_id          SERIAL               not null,
   first_name           TEXT                 null,
   last_name            TEXT                 null,
   first_name_eng       TEXT                 null,
   last_name_eng        TEXT                 null,
   dob                  DATE                 null,
   gender               TEXT                 null,
   address              TEXT                 null,
   phone                TEXT                 null,
   email                TEXT                 null,
   constraint PK_SPORTSMEN primary key (sportsman_id)
);

create unique index sportsmen_PK on sportsmen (sportsman_id);
create  index has_age_category_FK on sportsmen (category_id);
create index index_wma_sportsman_email on sportsmen (email);

alter table sportsmen
   add constraint FK_SPORTSME_HAS_AGE_C_AGECATEG foreign key (category_id)
      references age_categories (category_id)
      on delete restrict on update restrict;

/*==============================================================*/
/* Table: Participants                                          */
/*==============================================================*/
create table participants (
   participant_id        BIGSERIAL            not null,
   competition_id        BIGINT               not null,
   team_id               BIGINT               not null,
   sportsman_id          BIGINT               not null,
   personal_score        BOOLEAN              not null,
   constraint PK_PARTICIPANTS primary key (participant_id)
);

create unique index participants_1_PK on participants (participant_id);
create unique index participants_2_PK on participants (competition_id, team_id, sportsman_id);
create  index include_competition_FK on participants (competition_id);
create  index include_team_FK on participants (team_id);
create  index include_sportsman_FK on participants (sportsman_id);

alter table participants
   add constraint FK_PARTICIP_INCLUDE_C_COMPETIT foreign key (competition_id)
      references competitions (competition_id)
      on delete restrict on update restrict;

alter table participants
   add constraint FK_PARTICIP_INCLUDE_S_SPORTSME foreign key (sportsman_id)
      references sportsmen (sportsman_id)
      on delete restrict on update restrict;

alter table participants
   add constraint FK_PARTICIP_INCLUDE_T_TEAMS foreign key (team_id)
      references teams (team_id)
      on delete restrict on update restrict;

/*==============================================================*/
/* Table: TypesOfSport                                          */
/*==============================================================*/
create table types_of_sport (
   type_id               SERIAL               not null,
   type                  TEXT                 null,
   record_male           NUMERIC(8, 3)        null,
   record_female         NUMERIC(8, 3)        null,
   is_jumping            BOOLEAN              null,
   is_running            BOOLEAN              null,
   is_throwing           BOOLEAN              null,
   is_outdoor            BOOLEAN              null,
   is_indoor             BOOLEAN              null,
   is_nonstadia          BOOLEAN              null,
   constraint PK_TYPESOFSPORT primary key (type_id)
);

create unique index types_of_sport_PK on types_of_sport (type_id);

/*==============================================================*/
/* Table: Results                                               */
/*==============================================================*/
create table results (
   result_id             BIGSERIAL            not null,
   type_id               SERIAL               not null,
   participant_id        BIGINT               not null,
   final_result          NUMERIC(8, 3)        null,
   wma                   NUMERIC(5, 2)        null,
   constraint PK_RESULTS primary key (result_id)
);

create unique index results_PK on results (type_id, participant_id);
create  index result_in_sports_discipline_FK on results (type_id);
create  index participant_result_FK on results (participant_id);

alter table results
   add constraint FK_RESULTS_PARTICIPA_PARTICIP foreign key (participant_id)
      references participants (participant_id)
      on delete restrict on update restrict;

alter table results
   add constraint FK_RESULTS_RESULT_IN_TYPESOFS foreign key (type_id)
      references types_of_sport (type_id)
      on delete restrict on update restrict;

/*==============================================================*/
/* Table: Attempts                                              */
/*==============================================================*/
create table attempts (
   attempt_id            BIGSERIAL            not null,
   result_id             BIGINT               not null,
   actual_result         NUMERIC(8, 3)        null,
   attempt               BOOLEAN              null,
   constraint PK_ATTEMPTS primary key (attempt_id)
);

create  index result_in_attempts_FK on attempts (result_id);

alter table attempts
   add constraint FK_ATTEMPTS_RESULTS foreign key (result_id)
      references results (result_id)
      on delete restrict on update restrict;

/*==============================================================*/
/* Table: wmaParameters                                         */
/*==============================================================*/
create table wma_parameters (
   type_id              SERIAL                  not null,
   m35                  NUMERIC(6, 4)           null,
   m40                  NUMERIC(6, 4)           null,
   m45                  NUMERIC(6, 4)           null,
   m50                  NUMERIC(6, 4)           null,
   m55                  NUMERIC(6, 4)           null,
   m60                  NUMERIC(6, 4)           null,
   m65                  NUMERIC(6, 4)           null,
   m70                  NUMERIC(6, 4)           null,
   m75                  NUMERIC(6, 4)           null,
   m80                  NUMERIC(6, 4)           null,
   m85                  NUMERIC(6, 4)           null,
   m90                  NUMERIC(6, 4)           null,
   m95                  NUMERIC(6, 4)           null,
   m100                 NUMERIC(6, 4)           null,
   w35                  NUMERIC(6, 4)           null,
   w40                  NUMERIC(6, 4)           null,
   w45                  NUMERIC(6, 4)           null,
   w50                  NUMERIC(6, 4)           null,
   w55                  NUMERIC(6, 4)           null,
   w60                  NUMERIC(6, 4)           null,
   w65                  NUMERIC(6, 4)           null,
   w70                  NUMERIC(6, 4)           null,
   w75                  NUMERIC(6, 4)           null,
   w80                  NUMERIC(6, 4)           null,
   w85                  NUMERIC(6, 4)           null,
   w90                  NUMERIC(6, 4)           null,
   w95                  NUMERIC(6, 4)           null,
   w100                 NUMERIC(6, 4)           null,
   constraint PK_WMAPARAMETERS primary key (type_id)
);

create unique index wma_parameters_PK on wma_parameters (type_id);
create  index parameter_by_sports_discipline_ on wma_parameters (type_id);

alter table wma_parameters
   add constraint FK_WMAPARAM_PARAMETER_TYPESOFS foreign key (type_id)
      references types_of_sport (type_id)
      on delete restrict on update restrict;
