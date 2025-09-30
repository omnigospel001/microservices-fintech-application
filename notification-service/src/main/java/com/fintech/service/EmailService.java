package com.fintech.service;

import com.fintech.mailcontent.EmailBody;
import com.fintech.response.DepositNotificationResponse;
import com.fintech.response.TransferNotificationResponse;
import com.fintech.response.WithdrawalNotificationResponse;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailBody emailBody;

    @Value("${spring.mail.username}")
    private String companyEmail;

    @Value("${spring.mail.subject}")
    private String subjectEmail;

    @Value("${spring.company.name}")
    private String companyName;



    public void sendDepositEmailNotification(DepositNotificationResponse depositNotificationResponse) {

        try {

            MimeMessage message = mailSender.createMimeMessage();
            var messageHelper = new MimeMessageHelper(message);

            messageHelper.setFrom(companyEmail, companyName);
            messageHelper.setTo(depositNotificationResponse.userEmail());
            messageHelper.setSubject(subjectEmail);
            messageHelper.setText(emailBody.depositCreditAlertEmailBody(depositNotificationResponse), true);

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendWithdrawalEmailNotification(WithdrawalNotificationResponse withdrawalNotificationResponse) {

        try {

            MimeMessage message = mailSender.createMimeMessage();
            var messageHelper = new MimeMessageHelper(message);

            messageHelper.setFrom(companyEmail, companyName);
            messageHelper.setTo(withdrawalNotificationResponse.userEmail());
            messageHelper.setSubject(subjectEmail);
            messageHelper.setText(emailBody.withdrawalDebitAlertEmailBody(withdrawalNotificationResponse), true);

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendTransferEmailNotificationToReceiver(TransferNotificationResponse transferNotificationResponse) {

        try {

            MimeMessage message = mailSender.createMimeMessage();
            var messageHelper = new MimeMessageHelper(message);

            messageHelper.setFrom(companyEmail, companyName);
            messageHelper.setTo(transferNotificationResponse.receiverEmail());
            messageHelper.setSubject(subjectEmail);
            messageHelper.setText(emailBody.creditAlertEmailBody(transferNotificationResponse), true);

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void sendTransferEmailNotificationToSender(TransferNotificationResponse transferNotificationResponse) {

        try {

            MimeMessage message = mailSender.createMimeMessage();
            var messageHelper = new MimeMessageHelper(message);

            messageHelper.setFrom(companyEmail, companyName);
            messageHelper.setTo(transferNotificationResponse.senderEmail());
            messageHelper.setSubject(subjectEmail);
            messageHelper.setText(emailBody.debitAlertEmailBody(transferNotificationResponse), true);

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
