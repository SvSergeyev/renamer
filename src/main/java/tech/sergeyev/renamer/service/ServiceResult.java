package tech.sergeyev.renamer.service;

import java.util.ArrayList;
import java.util.List;

public class ServiceResult {
    public ServiceResult() {
        this.status = ResultStatus.SUCCESS;
    }

    public ServiceResult(ResultStatus status, List<String> messages) {
        this.status = status;
        this.messages = messages;
    }

    public ResultStatus getStatus() {
        return status;
    }

    public List<String> getMessages() {
        return messages;
    }

    private ResultStatus status;

    private List<String> messages = new ArrayList<>();


    public void setStatus(ResultStatus status) {
        this.status = status;
    }
}
