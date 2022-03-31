package com.valeos.rest.members;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.regex.Pattern;

@Component
public class MemberValidator {

    private final static String RGX_NUMBER_PATTERN = "^[0-9]*$";

    private final static String RGX_PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$";

    private final static String RGX_KOR_ENG_PATTERN = "^[가-힣a-zA-Z]*$";

    private final static String RGX_ENGLISH_SMALL_PATTERN = "^[a-z]*$";

    private final static String RGX_EMAIL_PATTERN = "^[a-zA-Z0-9]+@[a-zA-Z0-9.]+$";

    public void validate(MemberDto memberDto, Errors errors) {

        //name: 한글, 영문 대소문자만 허용
        boolean hasOnlyKorEng = Pattern.matches(RGX_KOR_ENG_PATTERN, memberDto.getName());
        if (!hasOnlyKorEng) {
            // Field error
            errors.rejectValue("name", "wrongValue", "name can have korean or english small letters");
            // Global error
            // errors.reject("wrongName", "Values to Name Is Wrong");
        }
        //nickname: 영문 소문자만 허용
        boolean hasOnlySmallEng = Pattern.matches(RGX_ENGLISH_SMALL_PATTERN, memberDto.getNickname());
        if (!hasOnlySmallEng) {
            // Field error
            errors.rejectValue("nickname", "wrongValue", "nickname can have only english small letters");
            // Global error
            // errors.reject("wrongNickName", "Values to NickName Is Wrong");
        }
        //password:   영문 대문자, 영문 소문자, 특수 문자, 숫자 각 1개 이상씩 포함
        boolean isPassword = Pattern.matches(RGX_PASSWORD_PATTERN, memberDto.getPassword());
        if (!isPassword) {
            // Field error
            errors.rejectValue("password", "wrongValue", "password must have one or more english capitals, english small letters, numbers and special characters");
            // Global error
            // errors.reject("wrongPassword", "Values to Password Is Wrong");
        }
        //cellphone: 숫자
        boolean hasOnlyNumber = Pattern.matches(RGX_NUMBER_PATTERN, memberDto.getCellphone());
        if (!hasOnlyNumber) {
            // Field error
            errors.rejectValue("cellphone", "wrongValue", "cellphone can has only numbers");
            // Global error
            // errors.reject("wrongCellphone", "Values to Cellphone Is Wrong");
        }
        //email: 이메일 형식
        boolean isEmail = Pattern.matches(RGX_EMAIL_PATTERN, memberDto.getEmail());
        if (!isEmail) {
            // Field error
            errors.rejectValue("email", "wrongValue", "email format is wrong");
            // Global error
            // errors.reject("wrongEmail", "Values to Email Is Wrong");
        }
        // Todo
    }
}
