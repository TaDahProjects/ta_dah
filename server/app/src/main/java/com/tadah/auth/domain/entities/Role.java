package com.tadah.auth.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 사용자의 권한을 정의한다.
 */
@Generated
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public final class Role {
    @Id
    @GeneratedValue
    private Long id;

    private Long userId;

    private String name;
}
