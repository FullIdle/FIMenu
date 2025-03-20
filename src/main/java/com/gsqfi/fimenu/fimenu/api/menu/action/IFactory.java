package com.gsqfi.fimenu.fimenu.api.menu.action;

public interface IFactory<T> {
    T create(String desc);
}
