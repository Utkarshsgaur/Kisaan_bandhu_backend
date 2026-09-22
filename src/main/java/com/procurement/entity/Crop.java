package com.procurement.entity;
import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity @Table(name="crops") public class Crop {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="crop_id") private Long id;
 @Column(name="crop_name",nullable=false,length=100) private String cropName;
 @Column(name="crop_code",nullable=false,unique=true,length=20) private String cropCode;
 @Column(name="is_active",nullable=false) private boolean active=true;
 @Column(name="created_at",nullable=false) private LocalDateTime createdAt; @Column(name="updated_at",nullable=false) private LocalDateTime updatedAt;
 @PrePersist void prePersist(){createdAt=LocalDateTime.now();updatedAt=createdAt;} @PreUpdate void preUpdate(){updatedAt=LocalDateTime.now();}
 public Long getId(){return id;} public void setId(Long v){id=v;} public String getCropName(){return cropName;} public void setCropName(String v){cropName=v;} public String getCropCode(){return cropCode;} public void setCropCode(String v){cropCode=v;} public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
}
