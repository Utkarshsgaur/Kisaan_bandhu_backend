package com.procurement.repository;
import com.procurement.entity.*; import java.util.*; import org.springframework.data.jpa.repository.*;
public interface BookingRepository extends JpaRepository<Booking,Long>{ List<Booking> findByFarmerIdOrderByBookedAtDesc(Long farmerId); List<Booking> findByScheduleIdOrderByBookedAtAsc(Long scheduleId); long countByScheduleIdAndStatusIn(Long scheduleId, Collection<BookingStatus> statuses); boolean existsByFarmerIdAndScheduleIdAndStatusIn(Long farmerId,Long scheduleId,Collection<BookingStatus> statuses); }
