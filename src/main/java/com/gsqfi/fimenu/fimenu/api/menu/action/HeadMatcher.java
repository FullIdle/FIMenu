package com.gsqfi.fimenu.fimenu.api.menu.action;

public class HeadMatcher implements IMatcher{
    public final String head;

    public HeadMatcher(String head){
        this.head = head;
    }

    @Override
    public boolean matcher(String desc) {
        return desc.startsWith(head);
    }
}
