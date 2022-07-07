package com.king.assessment.model;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    private Integer id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Level> levels = new ArrayList<>();

    @Column(name = "session_token", unique = true)
    private String sessionToken;

    @Column(name = "session_token_expiry")
    private LocalTime sessionTokenExpiry;
}
