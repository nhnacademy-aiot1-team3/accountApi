package com.nhnacademy.account.user.entity;

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

}
