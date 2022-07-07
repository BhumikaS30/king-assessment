package com.king.assessment.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class LevelId implements Serializable {

    private Integer levelId;

    private Integer userId;
}
