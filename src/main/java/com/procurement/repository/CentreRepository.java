package com.procurement.repository; import com.procurement.entity.ProcurementCentre; import java.util.List; import org.springframework.data.jpa.repository.JpaRepository;
public interface CentreRepository extends JpaRepository<ProcurementCentre,Long>{ List<ProcurementCentre> findByActiveTrue(); }
