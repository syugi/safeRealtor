package com.loadone.saferealtor.model.entity;

public enum SignupType {
    ADMIN("관리자"),
    EMAIL("이메일"),
    KAKAO("카카오"),
    NAVER("네이버");

    private final String displayName;

    SignupType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}