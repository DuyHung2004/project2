package com.example.spring2.dto.reponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleReponse {
    String name;
    String description;
    Set<PermissionReponse> permissions;
}
