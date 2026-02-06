package com.library.dto;

import com.library.model.MemberType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MemberDto {
    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    private String phone;

    @NotNull(message = "Member type is required")
    private MemberType memberType;

    public MemberDto() {}

    public MemberDto(String name, String email, String phone, MemberType memberType) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.memberType = memberType;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public MemberType getMemberType() { return memberType; }
    public void setMemberType(MemberType memberType) { this.memberType = memberType; }
}
