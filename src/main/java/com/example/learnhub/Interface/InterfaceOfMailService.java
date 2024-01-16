package com.example.learnhub.Interface;

import com.example.learnhub.MailConfig.MailDetail;

public interface InterfaceOfMailService {
    String sendMail(MailDetail mailDetail);

    String sendMailWithAttachment(MailDetail mailDetail);
}
