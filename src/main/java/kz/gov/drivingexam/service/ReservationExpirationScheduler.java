package kz.gov.drivingexam.service;

import kz.gov.drivingexam.enums.ReservationStatus;
import kz.gov.drivingexam.repository.ExamReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationExpirationScheduler {

    private final ExamReservationRepository reservationRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void expireReservations() {
        var expired = reservationRepository.findByStatusAndExamDateTimeBefore(
                ReservationStatus.ACTIVE, LocalDateTime.now());

        if (!expired.isEmpty()) {
            expired.forEach(r -> r.setStatus(ReservationStatus.EXPIRED));
            reservationRepository.saveAll(expired);
            log.info("Переведено в EXPIRED: {} бронирований", expired.size());
        }
    }
}