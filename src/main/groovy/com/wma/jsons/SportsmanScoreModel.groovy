package com.wma.jsons

class SportsmanScoreModel {
    String teamName
    List<String> title
    List<List<String>> data
    BigDecimal totalPoint
    String teamPlace

    SportsmanScoreModel(String teamName, List<String> title, List<List<String>> data, BigDecimal totalPoint) {
        this.teamName = teamName
        this.title = title
        this.data = data
        this.totalPoint = totalPoint
    }
}
