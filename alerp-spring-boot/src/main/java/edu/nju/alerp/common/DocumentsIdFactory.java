package edu.nju.alerp.common;

import edu.nju.alerp.repo.IdGeneratorRepository;
import edu.nju.alerp.entity.IdGenerator;
import edu.nju.alerp.enums.DocumentsType;
import edu.nju.alerp.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description: 单据Id生成器
 * @Author: yangguan
 * @CreateDate: 2019-12-22 17:26
 */
@Configuration
@Slf4j
@EnableScheduling
public class DocumentsIdFactory implements InitializingBean {

    @Autowired
    private IdGeneratorRepository idGeneratorRepository;

    private Map<DocumentsType, AtomicInteger> idGenerator = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        List<IdGenerator> documents = idGeneratorRepository.findAll();
        documents.forEach(id -> idGenerator.put(DocumentsType.getDocumentsType(id.getDocuments()), buildCurrentCount(id)));
    }

    public String generateNextCode(DocumentsType documentsType) {
        int year = TimeUtil.getNowYear();
        int month = TimeUtil.getNowMonth();
        int day = TimeUtil.getNowDay();
        return String.valueOf(year) + String.format("%02", month) + String.format("%02", day) + String.format("%03", getNext(documentsType));
    }

    private int getNext(DocumentsType documentsType) {
        int nextCount = idGenerator.get(documentsType).getAndIncrement();
        IdGenerator idGenerator = IdGenerator.builder().currentCount(nextCount)
                            .documents(documentsType.getName())
                            .updateTime(TimeUtil.dateFormat(new Date())).build();
        idGeneratorRepository.saveAndFlush(idGenerator);
        return nextCount;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    private void resetCount() {
        idGenerator.forEach( (d, c) -> c.set(0));
    }

    private AtomicInteger buildCurrentCount(IdGenerator idGenerator) {
        long days = TimeUtil.getDays(TimeUtil.dateFormat(new Date()), idGenerator.getUpdateTime());
        if ( days == 0 )
            return new AtomicInteger(idGenerator.getCurrentCount());
        return new AtomicInteger();
    }
}