package com.procurement.repository;
import com.procurement.entity.*; import java.util.List; import org.springframework.data.jpa.repository.*; import org.springframework.data.repository.query.Param;
public interface SeasonCropRepository extends JpaRepository<SeasonCrop,SeasonCropId>{
 @Query("select sc.crop from SeasonCrop sc where sc.season.id=:seasonId and sc.crop.active=true") List<Crop> findActiveCropsBySeasonId(@Param("seasonId") Long seasonId);
}
