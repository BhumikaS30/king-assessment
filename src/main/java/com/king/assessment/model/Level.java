package com.king.assessment.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "level")
public class Level implements Serializable {

    @EmbeddedId
    private LevelId levelId;

    @ManyToOne
    @JoinColumn(name = "id")
    private User user;

    @Column(name = "score")
    private Integer score;

}
