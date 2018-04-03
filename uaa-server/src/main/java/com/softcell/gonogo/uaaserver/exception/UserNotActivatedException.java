package com.softcell.gonogo.uaaserver.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * This exception is throw in case of a not activated user trying to authenticate.
 */
public class UserNotActivatedException extends UsernameNotFoundException {

    public UserNotActivatedException(String message) {
        super(message);
    }

}
