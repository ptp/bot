package me.imsean.ptpbot.exceptions;

public class NotBannedFromGroupException extends Exception {

    public NotBannedFromGroupException() {
        super("This user is banned from this group");
    }

}
