package com.stewart.teste.Model;

import java.util.List;

public class JwtPayload {
    private String subject;
    private String roles;

    public JwtPayload(String subject, String roles) {
        this.subject = subject;
        this.roles = roles;
    }

    public String getSubject() {
        return subject;
    }

    public String getRoles() {
        return roles;
    }
}
