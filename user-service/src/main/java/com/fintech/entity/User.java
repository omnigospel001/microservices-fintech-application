package com.fintech.entity;

import org.springframework.data.annotation.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document(collation = "User")
public class User {

    @Id
    private String id;

    @Field(targetType = FieldType.STRING, name = "firstName")
    private String firstName;

    @Field(targetType = FieldType.STRING, name = "lastName")
    private String lastName;

    @Field(targetType = FieldType.STRING, name = "email")
    private String email;

    @Field(targetType = FieldType.STRING, name = "password")
    private String password;

    @Field(targetType = FieldType.INT64, name = "accountNumber")
    private Long accountNumber;

}
