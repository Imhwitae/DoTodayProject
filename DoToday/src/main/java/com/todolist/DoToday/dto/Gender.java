package com.todolist.DoToday.dto;

public enum Gender {
    Male("남"), Female("여");

    private final String genderSelect;

    Gender(String genderSelect) {
        this.genderSelect = genderSelect;
    }

    public String getGenderSelect() {
        return genderSelect;
    }
}
