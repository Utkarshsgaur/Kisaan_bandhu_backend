package com.procurement.repository; import com.procurement.entity.Crop; import java.util.List; import org.springframework.data.jpa.repository.JpaRepository;
public interface CropRepository extends JpaRepository<Crop,Long>{ List<Crop> findByActiveTrue(); }
