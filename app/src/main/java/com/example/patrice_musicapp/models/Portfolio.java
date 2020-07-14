package com.example.patrice_musicapp.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;

@ParseClassName("Portfolio")
public class Portfolio extends Post {
    public static final String KEY_CERTIFICATES = "certificates";
    public Portfolio() {
        super();
    }

    public ParseFile[] getCertificates() {
        return (ParseFile[]) get(KEY_CERTIFICATES);
    }

    public void setCertificates(ParseFile[] certificates){
        put(KEY_CERTIFICATES, certificates);
    }

}
