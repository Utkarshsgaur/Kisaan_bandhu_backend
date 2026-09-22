package com.procurement.repository; import com.procurement.entity.ProcurementRecord; import java.util.Optional; import org.springframework.data.jpa.repository.JpaRepository;
public interface ProcurementRecordRepository extends JpaRepository<ProcurementRecord,Long>{ Optional<ProcurementRecord> findByBookingId(Long bookingId); }
