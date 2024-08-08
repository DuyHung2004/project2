package com.example.spring2.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
     String password;
     String firstName;
    String lastName;
     Date ngaysinh;
    List<String> roles;
}
