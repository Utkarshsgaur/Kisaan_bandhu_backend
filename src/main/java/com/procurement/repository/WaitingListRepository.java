package com.procurement.repository;
import com.procurement.entity.*; import java.util.*; import org.springframework.data.jpa.repository.*;
public interface WaitingListRepository extends JpaRepository<WaitingListEntry,Long>{ List<WaitingListEntry> findByScheduleIdAndStatusOrderByQueuePositionAsc(Long scheduleId,WaitingStatus status); int countByScheduleIdAndStatus(Long scheduleId,WaitingStatus status); List<WaitingListEntry> findByFarmerIdOrderByJoinedAtDesc(Long farmerId); }
