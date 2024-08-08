package com.example.spring2.dto.request;

import com.example.spring2.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min= 6, message ="USERNAME_INVALID")
     String username;
    @Size(min = 8, message = "INVALID_PASSSWORD")
     String password;
     String firstName;
     String lastName;
     @DobConstraint(min = 18, message = "INVALID_DOB")
     Date ngaysinh;

}
