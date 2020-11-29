package com.example.nytimes.data;

public class SearchApiResponse {

    private String status;
    private String copyright;
    private Docs response;

    public Docs getResponse() {
        return response;
    }

    public void setResponse(Docs response) {
        this.response = response;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }



}
