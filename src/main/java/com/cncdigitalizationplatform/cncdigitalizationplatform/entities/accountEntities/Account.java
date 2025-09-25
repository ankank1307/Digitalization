package com.cncdigitalizationplatform.cncdigitalizationplatform.entities.accountEntities;

import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "CHAR(36)")
    private String id;
    
    @ManyToOne
    @JoinColumn(name = "role_id")
    @JsonBackReference(value = "role-account")
    private Role role;
    
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private AccountSetting accountSetting; 
    
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PasswordResetToken> passwordResetTokens; 
    
    private String fullName;
    private String phoneNumber;
    private String password = "123";
    private String email;
    private Long kpi;
    private String avatarUrl;
    private String status = "active";
    private Long createdTime = System.currentTimeMillis();
    private Long updatedTime;
}
