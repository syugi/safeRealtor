package com.loadone.saferealtor.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PagedResponseDTO<T> {
    private List<T> properties;  // 매물 목록
    private int totalPages;    // 전체 페이지 수
    private int currentPage;   // 현재 페이지
    private long totalItems;   // 전체 아이템 수
}
