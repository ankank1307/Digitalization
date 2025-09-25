package com.cncdigitalizationplatform.cncdigitalizationplatform.entities.accountEntities;

import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "CHAR(36)")
    private String roleId;
    
    private String roleName;
    private String roleShort;
    
    @OneToMany(mappedBy = "role")
    @JsonManagedReference(value = "role-account")
    private List<Account> accounts;
    
    private String status;
    private Long createdTime = System.currentTimeMillis();
    private Long updatedTime;
}
