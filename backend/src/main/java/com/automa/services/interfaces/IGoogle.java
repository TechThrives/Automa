package com.automa.services.interfaces;

import com.automa.dto.MessageResponse;

import jakarta.servlet.http.HttpServletRequest;

public interface IGoogle {

    public MessageResponse googleCallback(String code, HttpServletRequest request);
}
