package com.collosteam.simplesitereader.api;

/**
 *
 */
public class NotSameCountItemsArray extends Exception {
    public NotSameCountItemsArray(String detailMessage) {
        super("NotSameCountItemsArray please check this problem " + detailMessage);
    }
}
