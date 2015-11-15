package me.imsean.ptpbot.exceptions;

public class NotAdminException extends Exception {

    public NotAdminException() {
        super("I am not ADMIN in this group");
    }

}
