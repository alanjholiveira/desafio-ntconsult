package br.com.sicredi.vote.schedule;

import br.com.sicredi.vote.service.SessionServices;
import br.com.sicredi.vote.service.VoteService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class ResultSchedule {

    private final VoteService service;
    private final SessionServices sessionServices;

    @Scheduled(cron = "${event.get-result.cron}")
    @SchedulerLock(name = "TaskScheduler_ResultPollVotes")
    public void getResultPoll() {
        log.info("Generating poll result accounting event");
        service.countingVotesEvent();
    }

    @Scheduled(cron = "${event.check-session-valid.cron}")
    @SchedulerLock(name = "TaskScheduler_CheckSessionValid")
    public void checkSessionValid() {
        log.info("Checking session validity");
        sessionServices.checkSessionValid();
    }

}