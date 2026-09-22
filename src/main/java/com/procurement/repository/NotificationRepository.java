package com.procurement.repository; import com.procurement.entity.Notification; import java.util.List; import org.springframework.data.jpa.repository.JpaRepository;
public interface NotificationRepository extends JpaRepository<Notification,Long>{ List<Notification> findByFarmerIdOrderByCreatedAtDesc(Long farmerId); }
