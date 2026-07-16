package com.fintech.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "transactions")
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String senderFirstName;
    private String senderLastName;
    private String senderEmail;
    private Long senderAccountNumber;
    private String receiverFirstName;
    private String receiverLastName;
    private String receiverEmail;
    private Long receiverAccountNumber;
    private BigDecimal transferAmount;

    private LocalDateTime transferDate;

}
