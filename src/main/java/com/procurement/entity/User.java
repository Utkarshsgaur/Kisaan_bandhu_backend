package com.procurement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name="users")
public class User {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="user_id") private Long id;
    @Column(name="phone_number", nullable=false, unique=true, length=15) private String phoneNumber;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=30) private Role role;
    @Column(name="is_active", nullable=false) private boolean active = true;
    @Column(name="created_at", nullable=false) private LocalDateTime createdAt;
    @Column(name="updated_at", nullable=false) private LocalDateTime updatedAt;
    @PrePersist void prePersist(){createdAt=LocalDateTime.now(); updatedAt=createdAt;}
    @PreUpdate void preUpdate(){updatedAt=LocalDateTime.now();}
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getPhoneNumber(){return phoneNumber;} public void setPhoneNumber(String v){phoneNumber=v;}
    public Role getRole(){return role;} public void setRole(Role v){role=v;}
    public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
    public java.time.LocalDateTime getCreatedAt(){return createdAt;}
    public java.time.LocalDateTime getUpdatedAt(){return updatedAt;}
}
