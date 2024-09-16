package com.loadone.saferealtor.util;

import com.loadone.saferealtor.exception.BaseException;
import com.loadone.saferealtor.exception.ErrorCode;
import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SMSUtil {

    @Value("${sms.enable}")
    private boolean isSmsEnabled;

    @Value("${coolsms.api-key}")
    private String apiKey;

    @Value("${coolsms.api-secret}")
    private String apiSecret;

    @Value("${coolsms.from-number}")
    private String fromNumber;

    DefaultMessageService messageService; // 메시지 서비스를 위한 객체

    @PostConstruct // 의존성 주입이 완료된 후 초기화를 수행하는 메서드
    public void init(){
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr"); // 메시지 서비스 초기화
    }

    // 단일 메시지 발송
    public String sendSMS(String to, String text){

        if (!isSmsEnabled) {
            System.out.println("SMS 발송이 비활성화되었습니다. 메시지를 발송하지 않습니다.");
            System.out.println("text = " + text);
            return "SMS 발송이 비활성화되었습니다.";
        }

        Message message = new Message(); // 새 메시지 객체 생성
        message.setFrom(fromNumber); // 발신자 번호 설정
        message.setTo(to); // 수신자 번호 설정
        message.setText(text); // 메시지 내용 설정

        try {
            this.messageService.sendOne(new SingleMessageSendingRequest(message)); // 메시지 발송 요청
            return "SMS 전송 성공: " + text;
        } catch (Exception e) {
            throw new BaseException(ErrorCode.FAILED_TO_SEND_SMS, e);
        }
    }
}
