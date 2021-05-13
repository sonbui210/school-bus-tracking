package com.hoanganhbk.schoolBus.api.auth.payload;

import com.hoanganhbk.schoolBus.constant.Message;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class RegisterRequest {
    @NotBlank(message = Message.INVALID_USERNAME)
    @Length(min = 3, max = 20, message = Message.INVALID_USERNAME)
    private String username;

    @Length(min = 6, max = 20, message = Message.INVALID_PASSWORD)
    private String password;

    private String email;

    private String phone;
}
