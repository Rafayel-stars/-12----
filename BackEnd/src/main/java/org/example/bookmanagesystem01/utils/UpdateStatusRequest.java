package org.example.bookmanagesystem01.utils;

public class UpdateStatusRequest {
    private Integer id;
    private Integer status;

    public UpdateStatusRequest(Integer id, Integer status) {
        this.id = id;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public Integer getStatus() {
        return status;
    }
}