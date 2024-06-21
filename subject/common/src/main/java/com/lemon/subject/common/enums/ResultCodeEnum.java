package com.lemon.subject.common.enums;

import lombok.Getter;

@Getter
public enum ResultCodeEnum {
    SUCCESS(200,"成功"),
    FAILURE(500," 失败");

    public int code;
    public String message;

    ResultCodeEnum(int code,String message){
        this.code=code;
        this.message=message;
    }

    public static ResultCodeEnum getByCode(int value){
        for(ResultCodeEnum resultCodeEnum:ResultCodeEnum.values()){
            if(resultCodeEnum.code==value){
                return resultCodeEnum;
            }
        }
        return  null;
    }
}
