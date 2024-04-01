package com.nhnacademy.account.user.entity;

import com.nhnacademy.account.user.dto.JoinResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Members")
public class Member {

    @Id
    @Column(name = "member_id")
    private String id;
    @Column(name = "member_password")
    private String pw;
    @Column(name = "member_email")
    private String email;

    public static Member createMember(String id, String pw, String email) {
        return new Member(id, pw, email);
    }

    public JoinResponseDto toDto() {
        return JoinResponseDto.builder()
                .id(id)
                .password(pw)
                .email(email)
                .build();
    }
}