package com.automa.entity.credential;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Github extends Credential {
    
    @Column(nullable = false)
    private String access;

    @Column(nullable = false)
    private String refresh;
}

