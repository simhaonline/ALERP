package edu.nju.alerp.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 公司支出DTO
 *
 * @author luhailong
 * @date 2019/12/21
 */
@Data
@Builder
public class ExpenseDTO {
    /**
     * 单据名称
     */
    private String title;

    /**
     * 单据描述
     */
    private String description;

    /**
     * 支出金额
     */
    private double cash;

    /**
     * 支出时间
     */
    private String doneAt;
}
