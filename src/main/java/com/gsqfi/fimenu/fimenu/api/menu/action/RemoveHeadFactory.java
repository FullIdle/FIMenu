package com.gsqfi.fimenu.fimenu.api.menu.action;

import lombok.SneakyThrows;

import java.lang.reflect.Constructor;

public class RemoveHeadFactory<T> implements IFactory<T>{
    public final String head;
    public final Constructor<T> constructor;
    @SneakyThrows
    public RemoveHeadFactory(String head, Class<T> targetClass) {
        this.head = head;
        this.constructor = targetClass.getDeclaredConstructor(String.class);
        this.constructor.setAccessible(true);
    }

    @SneakyThrows
    @Override
    public T create(String desc) {
        return constructor.newInstance(desc.substring(head.length()));
    }
}
