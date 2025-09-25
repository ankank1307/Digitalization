package com.cncdigitalizationplatform.cncdigitalizationplatform.entities;

import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.CascadeType;
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
@Table(name = "machine")
public class Machine {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "CHAR(36)")
    private String id;
    private long isActive;
    private String imageUrl;
    private String name;
    private String status;
    private Long createdTime = System.currentTimeMillis();
    private Long updatedTime;

    @OneToMany(mappedBy = "machine", cascade = CascadeType.ALL)
    private List<Log> logs;
}
