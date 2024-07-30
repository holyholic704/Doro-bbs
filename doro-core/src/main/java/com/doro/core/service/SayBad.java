package com.doro.core.service;

public class SayBad implements HelloService{
    @Override
    public String say() {
        System.out.println("滚啊");
        return "滚啊";
    }
}
