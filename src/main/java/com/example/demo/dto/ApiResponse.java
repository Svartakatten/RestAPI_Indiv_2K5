package com.example.demo.dto;

public class ApiResponse<T> {
    private T data;
    private String version = "v1";

    public ApiResponse(T data) {
        this.data = data;
    }

    // Gets
    public T getData() { return data; }
    
    public String getVersion() { return version; }

    // Sets
    // Då den är onödig i detta kontext sparar jag den som referens för framtiden
    public void setVersion(String version) { this.version = version; }

    public void setData(T data) { this.data = data; }

}
