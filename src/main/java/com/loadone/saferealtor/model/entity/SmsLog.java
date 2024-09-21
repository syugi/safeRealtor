package com.loadone.saferealtor.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "message")
@Entity
@Table(name = "sms_logs")
public class SmsLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phoneNumber;   // 발송된 전화번호
    private String message;       // 발송된 메시지 내용
    private boolean success;      // 발송 성공 여부
    private String errorMessage;  // 실패 시 오류 메시지
    private LocalDateTime sentAt; // 발송 시각
}
