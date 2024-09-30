package com.loadone.saferealtor.model.entity;

import lombok.extern.log4j.Log4j2;

@Log4j2
public enum Role {
    ADMIN(0),
    AGENT(1),
    USER(2);

    private final int value;

    Role(int value) {
        this.value = value;
    }

    public String getName() {
        return name();
    }

    public int getValue() {
        return value;
    }

    // 정수 값으로부터 Role Name을 반환하는 메서드
    public static String getName(int value) {

        for(Role role : Role.values()) {
            if(role.getValue() == value) {
                return role.getName();
            }
        }

        log.error("[ERROR] Invalid role value: " + value);
        return "";
    }
}
