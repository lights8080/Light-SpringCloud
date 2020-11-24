package com.light.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@EnableAsync
@EnableScheduling
@Component
@Slf4j
public class LightTask {
//    @Async
//    public Future<String> testAsync() throws InterruptedException {
//        Thread.sleep(2000);
//        log.info("test async");
//        return new AsyncResult<>("test");
//    }
//
//    @Scheduled(cron = "0 */10 * * * ?")
//    public void testSchedule() {
//        log.info("test schedule");
//    }
}
