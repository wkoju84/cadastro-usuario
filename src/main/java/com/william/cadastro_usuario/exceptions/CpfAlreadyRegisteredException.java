package com.william.cadastro_usuario.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) //400
public class CpfAlreadyRegisteredException extends RuntimeException{

    public CpfAlreadyRegisteredException(String message){
        super(message);
    }
}
