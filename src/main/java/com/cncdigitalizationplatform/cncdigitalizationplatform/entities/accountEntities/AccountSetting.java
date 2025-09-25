package com.cncdigitalizationplatform.cncdigitalizationplatform.entities.accountEntities;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "setting")
public class AccountSetting {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "CHAR(36)")
    private String id;
    
    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;
    
    private String theme = "light";
    private String language = "EN";
    private String status;
    private Long createdTime = System.currentTimeMillis();
    private Long updatedTime;  
}
