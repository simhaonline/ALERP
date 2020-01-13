package edu.nju.alerp.controller;

import edu.nju.alerp.common.ResponseResult;
import edu.nju.alerp.common.aop.InvokeControl;
import edu.nju.alerp.service.SummaryService;
import edu.nju.alerp.vo.SummaryInfoVO;
import edu.nju.alerp.vo.SummaryProductInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 汇总信息controller
 *
 * @author luhailong
 * @date 2020/01/12
 */
@Slf4j
@RestController
@Validated
@RequestMapping(value = "/api/summary")
public class SummaryController {

    @Autowired
    private SummaryService summaryService;

    /**
     * 获取汇总信息
     *
     * @return
     */
    @InvokeControl
    @ResponseBody
    @RequestMapping(value = "/info", method = RequestMethod.GET, name = "获取汇总信息")
    public ResponseResult<SummaryInfoVO> getSummaryInfo() {
        return ResponseResult.ok(summaryService.getSummaryInfo());
    }

    /**
     * 获取商品汇总信息
     *
     * @return
     */
    @InvokeControl
    @ResponseBody
    @RequestMapping(value = "/product", method = RequestMethod.GET, name = "获取商品汇总信息")
    public ResponseResult<SummaryProductInfoVO> getSummaryProductInfo() {
        return ResponseResult.ok(summaryService.getSummaryProductInfo());
    }

}
