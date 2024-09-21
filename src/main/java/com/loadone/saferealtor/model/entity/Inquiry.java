package com.loadone.saferealtor.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // 문의한 유저 ID

    @Lob
    private String inquiryContent; // 문의 내용

    @Lob
    private String detailRequest; //상세 요청사항

    private LocalDateTime inquiryDateTime; // 문의한 시간


    @ManyToMany
    @JoinTable(
            name = "inquiry_property",
            joinColumns = @JoinColumn(name = "inquiry_id"),
            inverseJoinColumns = @JoinColumn(name = "property_id")
    )

    @JsonManagedReference // 직렬화 시 이 참조가 포함됨
    private List<Property> properties;  // 문의한 매물 목록 (선택적)
}
