package com.loadone.saferealtor.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PagingResultDTO<T> {
    private List<T> items;
    private int totalPages;    // 전체 페이지 수
    private int currentPage;   // 현재 페이지
    private long totalItems;   // 전체 아이템 수

    public PagingResultDTO(List<T> items, int totalPages, int currentPage, long totalItems) {
        this.items = items;
        this.totalPages = totalPages;
        this.currentPage = currentPage + 1; // 0부터 시작하는 페이지 번호를 1부터 시작하는 페이지 번호로 변경
        this.totalItems = totalItems;
    }
}
