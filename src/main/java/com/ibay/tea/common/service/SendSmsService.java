package com.ibay.tea.common.service;

import com.ibay.tea.entity.TbSmsConfig;

public interface SendSmsService {

    boolean sendVerificationCode(String phoneNum,String smsCode);

    void cacheVerificationCode(String phoneNum, String verificationCode);

    boolean checkVerificationCode(String phoneNum, String verificationCode);

    void sendSmsContent(TbSmsConfig tbSmsConfig);
}
