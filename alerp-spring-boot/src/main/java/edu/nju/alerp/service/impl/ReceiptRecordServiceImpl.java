package edu.nju.alerp.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.nju.alerp.dto.ReceiptRecordDTO;
import edu.nju.alerp.repo.ReceiptRecordRepository;
import edu.nju.alerp.service.ReceiptRecordServie;
import edu.nju.alerp.entity.ReceiptRecord;
import edu.nju.alerp.enums.ReceiptRecordStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author luhailong
 * @date 2019/12/23
 */
@Service
public class ReceiptRecordServiceImpl implements ReceiptRecordServie {

    @Autowired
    private ReceiptRecordRepository receiptRecordRepository;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");


    @Override
    public boolean addReceiptRecord(ReceiptRecordDTO receiptRecordDTO) {
        ReceiptRecord receiptRecord = ReceiptRecord.builder().
            createdAt(sdf.format(new Date())).
            status(ReceiptRecordStatus.CONFIRMED.getCode()).
            build();
        BeanUtils.copyProperties(receiptRecordDTO,receiptRecord);
        receiptRecordRepository.save(receiptRecord);
        return true;
    }
}