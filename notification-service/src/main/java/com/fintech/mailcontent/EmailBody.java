package com.fintech.mailcontent;

import com.fintech.response.DepositNotificationResponse;
import com.fintech.response.SettlementNotificationResponse;
import com.fintech.response.TransferNotificationResponse;
import com.fintech.response.WithdrawalNotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;


@Component
@RequiredArgsConstructor
public class EmailBody {

    @Value("${spring.mail.company.name}")
    private String companyName;


    public String depositCreditAlertEmailBody(DepositNotificationResponse depositNotificationResponse){

        String formattedDate = LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
        String formattedTime = LocalTime.now().format(DateTimeFormatter.ofPattern("h:mm a"));

        String mailContent = "<p> Credit Alert On: "+ depositNotificationResponse.accountNumber() + " </p>"+
                "<p> Amount: "+"USD "+depositNotificationResponse.depositAmount()+" CR"+"</p>" +
                "<p> Reference: "+depositNotificationResponse.firstName()+" "+depositNotificationResponse.lastName()+""+"</p>" +
                "<p> Date & Time: "+formattedDate+"  "+formattedTime+"</p>"+
                "<p> Thank you</p> <br> "+companyName+"";

        return mailContent;

    }

    public String withdrawalDebitAlertEmailBody(WithdrawalNotificationResponse withdrawalNotificationResponse){

        String formattedDate = LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
        String formattedTime = LocalTime.now().format(DateTimeFormatter.ofPattern("h:mm a"));

        String mailContent = "<p> Debit Alert On: "+ withdrawalNotificationResponse.accountNumber()+ " </p>"+
                "<p> Amount: "+"USD "+withdrawalNotificationResponse.withdrawalAmount()+" DR"+"</p>" +
                "<p> Reference: "+withdrawalNotificationResponse.firstName()+" "+withdrawalNotificationResponse.lastName()+""+"</p>" +
                "<p> Date & Time: "+formattedDate+"  "+formattedTime+"</p>"+
                "<p> Thank you</p> <br> "+companyName+"";

        return mailContent;

    }


    public String creditAlertEmailBody(TransferNotificationResponse transferNotificationResponse){

        String formattedDate = LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
        String formattedTime = LocalTime.now().format(DateTimeFormatter.ofPattern("h:mm a"));

        String mailContent = "<p> Credit Alert On: "+ transferNotificationResponse.receiverAccountNumber() + " </p>"+
                "<p> Amount: "+"USD "+transferNotificationResponse.transferAmount()+" CR"+"</p>" +
                "<p> Sender: "+transferNotificationResponse.senderFirstName()+" "+transferNotificationResponse.senderLastName()+" </p>"+
                "<p> Date & Time: "+formattedDate+"  "+formattedTime+"</p>"+
                "<p> Thank you</p> <br> "+companyName+"";

        return mailContent;

    }


    public String debitAlertEmailBody(TransferNotificationResponse transferNotificationResponse){

        String formattedDate = LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
        String formattedTime = LocalTime.now().format(DateTimeFormatter.ofPattern("h:mm a"));

        String mailContent = "<p> Debit Alert On: "+ transferNotificationResponse.senderAccountNumber() + " </p>"+
                "<p> Amount: "+"USD "+transferNotificationResponse.transferAmount()+" DR"+"</p>" +
                "<p> Recipient: "+transferNotificationResponse.receiverFirstName()+" "+transferNotificationResponse.receiverLastName()+""+"</p>" +
                "<p> Date & Time: "+formattedDate+"  "+formattedTime+"</p>"+
                "<p> Thank you</p> <br> "+companyName+"";

        return mailContent;

    }

    public String settlementAlertEmailBody(SettlementNotificationResponse settlementNotificationResponse){

        String formattedDate = LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
        String formattedTime = LocalTime.now().format(DateTimeFormatter.ofPattern("h:mm a"));

        String status = settlementNotificationResponse.status() != null ? settlementNotificationResponse.status().name() : "UNKNOWN";

        String mailContent = "<p> Settlement Alert On: "+ settlementNotificationResponse.accountNumber() + " </p>"+
                "<p> Reference: "+ settlementNotificationResponse.referenceId() +"</p>" +
                "<p> Type: "+ settlementNotificationResponse.transactionType() +"</p>" +
                "<p> Amount: "+"USD "+settlementNotificationResponse.amount()+"</p>" +
                "<p> Status: "+ status +"</p>" +
                "<p> Date & Time: "+formattedDate+"  "+formattedTime+"</p>"+
                "<p> Thank you</p> <br> "+companyName+"";

        return mailContent;

    }
}
