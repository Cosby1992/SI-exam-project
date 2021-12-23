package dk.cosby.cph.si.exam.driverschedulesrestfulapi.dto;

import org.springframework.hateoas.RepresentationModel;

public class CreateScheduleResponseEntity extends RepresentationModel<CreateScheduleResponseEntity> {

    private boolean acknowledged;
    private boolean success;
    private String insertedId;
    private String errorMessage;

    public CreateScheduleResponseEntity() {
    }

    public CreateScheduleResponseEntity(boolean acknowledged, boolean success, String insertedId, String errorMessage) {
        this.acknowledged = acknowledged;
        this.success = success;
        this.insertedId = insertedId;
        this.errorMessage = errorMessage;
    }

    public boolean isAcknowledged() {
        return acknowledged;
    }

    public void setAcknowledged(boolean acknowledged) {
        this.acknowledged = acknowledged;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getInsertedId() {
        return insertedId;
    }

    public void setInsertedId(String insertedId) {
        this.insertedId = insertedId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
