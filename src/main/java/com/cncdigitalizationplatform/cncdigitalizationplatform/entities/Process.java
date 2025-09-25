package com.cncdigitalizationplatform.cncdigitalizationplatform.entities;

import org.hibernate.annotations.GenericGenerator;

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
@Table(name = "process")
public class Process {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "CHAR(36)")
    private String id;
    
    @ManyToOne
    @JoinColumn(name = "drawing_id")
    private DrawingCode drawingCode;
    
    @ManyToOne
    @JoinColumn(name = "machine_id")
    private Machine machine;
    
    private String name;
    private String approved;
    private Long predictedDuration;
    private Long startTime;
    private Long endTime;
    private String qcRemarks;
    private String taskStatus;
    private String status;
    private Long createdTime = System.currentTimeMillis();
    private Long updatedTime;
    
    public Long getPredictedEndTime() {
        return this.startTime + this.predictedDuration;
    }
}
