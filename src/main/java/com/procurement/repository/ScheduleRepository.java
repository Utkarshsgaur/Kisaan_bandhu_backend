package com.procurement.repository;

import com.procurement.entity.ProcurementSchedule;
import com.procurement.entity.ScheduleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<ProcurementSchedule, Long> {

 @Lock(LockModeType.PESSIMISTIC_WRITE)
 @Query("SELECT s FROM ProcurementSchedule s WHERE s.id = :id")
 Optional<ProcurementSchedule> findByIdForUpdate(
         @Param("id") Long id
 );

 List<ProcurementSchedule> findByStatusAndScheduleDateGreaterThanEqual(
         ScheduleStatus status,
         LocalDate date
 );

 List<ProcurementSchedule> findByCentreId(Long centreId);

 List<ProcurementSchedule> findByCropId(Long cropId);

 List<ProcurementSchedule> findByScheduleDate(LocalDate date);
}