package com.contfiable.dto.notification;

import com.contfiable.model.Notification;

public class NotificationCreateRequest {
    private Notification.Type type;
    private String title;
    private String message;
    private Long invoiceId;

    public NotificationCreateRequest() {}

    public Notification.Type getType() {
        return type;
    }

    public void setType(Notification.Type type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }
}
