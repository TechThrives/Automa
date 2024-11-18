package com.automa.entity.credential;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Github extends Credential {

    @Column(nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String access;

    @Column(nullable = false)
    private String refresh;
}

