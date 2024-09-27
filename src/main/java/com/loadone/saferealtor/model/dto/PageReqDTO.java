package com.loadone.saferealtor.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class PageReqDTO {
    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int perPage = 10;


    public Pageable getPageable() {
        // 최신 등록순으로 정렬
        return this.getPageable(Sort.by("createdAt").descending());
    }

    public Pageable getPageable(Sort sort) {
        int pageNum = Math.max(page - 1, 0);  // 0 이하의 페이지는 0으로 처리
        int perPageNum = Math.max(perPage, 10);  // 최소 10개의 항목을 반환하도록 설정

        return PageRequest.of(pageNum, perPageNum, sort);
    }

}
