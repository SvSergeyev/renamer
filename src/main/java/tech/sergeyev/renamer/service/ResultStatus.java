package tech.sergeyev.renamer.service;

public enum ResultStatus {
    SUCCESS("Успешно"),
    ERROR("Ошибка");

    private final String text;

    ResultStatus(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
