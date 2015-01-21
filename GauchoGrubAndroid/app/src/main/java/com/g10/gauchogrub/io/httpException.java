package com.g10.gauchogrub.io;

import android.util.AndroidRuntimeException;

/**
 * Created by elswenson on 1/20/2015.
 */
public class httpException extends AndroidRuntimeException {
    public httpException(String message){
        super(message);
    }
}
