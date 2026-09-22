package com.procurement.repository; import com.procurement.entity.ProcurementSeason; import java.util.List; import org.springframework.data.jpa.repository.JpaRepository;
public interface SeasonRepository extends JpaRepository<ProcurementSeason,Long>{ List<ProcurementSeason> findByActiveTrue(); }
