package edu.nju.alerp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: 城市枚举值
 * @Author: qianen.yin
 * @CreateDate: 2019-12-29 23:00
 */
@Getter
@AllArgsConstructor
public enum CityEnum {
    SZ(1, "苏州"),

    KS(2, "昆山");

    private int code;
    private String message;


    public static CityEnum of(int code) {
        for (CityEnum item : CityEnum.values()) {
            if (item.code == code) {
                return item;
            }
        }
        return null;
    }
}
