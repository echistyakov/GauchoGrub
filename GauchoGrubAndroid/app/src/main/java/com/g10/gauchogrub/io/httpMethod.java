package com.g10.gauchogrub.io;


public enum httpMethod {
    GET, POST;

    public String toString(){
        switch (this){
            case GET:
                return "GET";
            case POST:
                return "POST";
        }
        return null;
    }
}

