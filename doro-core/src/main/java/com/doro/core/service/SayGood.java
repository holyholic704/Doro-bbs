package com.doro.core.service;

public class SayGood implements HelloService{
    @Override
    public String say() {
        System.out.println("非常好");
        return "非常好";
    }
}
