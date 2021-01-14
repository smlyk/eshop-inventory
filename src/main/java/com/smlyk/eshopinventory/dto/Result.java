package com.smlyk.eshopinventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: always
 * @Date: 2021/1/2 7:35 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    private int code;

    private String msg;

    private T data;

    public Result<T> success(T data){
        return new Result(0, "SUCCESS", data);
    }

    public Result<T> fail(){
        return new Result(-1, "FAILED", null);
    }
}

