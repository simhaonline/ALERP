package edu.nju.alerp.customer.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @Description: 客户特惠价格DTO
 * @Author: qianen.yin
 * @CreateDate: 2019-12-18 17:22
 */
@Data
@Builder
public class SpecialPricesDTO {
    private Integer id;
    private int productId;
    private double price;
}
