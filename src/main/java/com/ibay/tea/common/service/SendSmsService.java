package com.ibay.tea.common.service;

public interface SendSmsService {

    boolean sendVerificationCode(String phoneNum,String smsCode);

    void cacheVerificationCode(String phoneNum, String verificationCode);

    boolean checkVerificationCode(String phoneNum, String verificationCode);
}
