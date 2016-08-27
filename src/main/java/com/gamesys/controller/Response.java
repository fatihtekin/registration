package com.gamesys.controller;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Response {

    private Set<String> messages = new HashSet<>();
    
    private Status status;
    
    public Response() {}
    
    public enum Status{
        SUCCESS,ERROR
    }
    
    public Set<String> getMessages() {
        return messages;
    }

    public void setMessages(Set<String> messages) {
        this.messages = messages;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Response(Status status, String message) {
        messages.add(message);
        this.status = status;
    }    

    public Response(Status status) {
        this.status = status;
    }    
    
    public Response(Status status, String... message) {
        messages.addAll(Arrays.asList(message));
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Response other = (Response) obj;
        if (messages == null) {
            if (other.messages != null)
                return false;
        } else if (!messages.equals(other.messages))
            return false;
        if (status != other.status)
            return false;
        return true;
    }    
        
    
}
