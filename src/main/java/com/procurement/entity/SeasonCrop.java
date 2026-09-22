package com.procurement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name="season_crops")
public class SeasonCrop {
    @EmbeddedId private SeasonCropId id;
    @ManyToOne(fetch=FetchType.LAZY) @MapsId("seasonId") @JoinColumn(name="season_id") private ProcurementSeason season;
    @ManyToOne(fetch=FetchType.LAZY) @MapsId("cropId") @JoinColumn(name="crop_id") private Crop crop;
    @Column(name="created_at",nullable=false) private LocalDateTime createdAt;
    @PrePersist void prePersist(){createdAt=LocalDateTime.now();}
    public SeasonCropId getId(){return id;} public void setId(SeasonCropId v){id=v;} public ProcurementSeason getSeason(){return season;} public void setSeason(ProcurementSeason v){season=v;} public Crop getCrop(){return crop;} public void setCrop(Crop v){crop=v;}
}
