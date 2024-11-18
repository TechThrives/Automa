package com.automa.entity.action.mail;

import java.util.List;

import com.automa.entity.action.Action;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SendMail extends Action {

    @Column(nullable = false)
    private List<String> sendTo;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String body;
}
