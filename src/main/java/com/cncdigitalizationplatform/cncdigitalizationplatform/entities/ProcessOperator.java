package com.cncdigitalizationplatform.cncdigitalizationplatform.entities;

import org.hibernate.annotations.GenericGenerator;

import com.cncdigitalizationplatform.cncdigitalizationplatform.entities.accountEntities.Account;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "process_operator")
public class ProcessOperator {
    
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "CHAR(36)")
    private String id;
    
    @ManyToOne
    @JoinColumn(name = "process_id")
    private Process process;
    
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
    
    private Long createdTime = System.currentTimeMillis();
}
