package com.procurement.entity;
import jakarta.persistence.*; import java.time.*;
@Entity @Table(name="procurement_seasons") public class ProcurementSeason {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="season_id") private Long id;
 @Column(name="season_name",nullable=false,unique=true,length=100) private String seasonName;
 @Column(name="start_date",nullable=false) private LocalDate startDate; @Column(name="end_date",nullable=false) private LocalDate endDate;
 @Column(name="is_active",nullable=false) private boolean active=true; @Column(name="created_at",nullable=false) private LocalDateTime createdAt; @Column(name="updated_at",nullable=false) private LocalDateTime updatedAt;
 @PrePersist void prePersist(){createdAt=LocalDateTime.now();updatedAt=createdAt;} @PreUpdate void preUpdate(){updatedAt=LocalDateTime.now();}
 public Long getId(){return id;} public String getSeasonName(){return seasonName;} public LocalDate getStartDate(){return startDate;} public LocalDate getEndDate(){return endDate;} public boolean isActive(){return active;}
 public void setId(Long v){id=v;} public void setSeasonName(String v){seasonName=v;} public void setStartDate(LocalDate v){startDate=v;} public void setEndDate(LocalDate v){endDate=v;} public void setActive(boolean v){active=v;}
}
