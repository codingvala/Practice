package com.example.springannotationdemo.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GreetingComponent {
    @Value("${demo.greeting:Hello from Spring}")
    private String greeting;

    public String getGreeting() { return greeting; }
}
