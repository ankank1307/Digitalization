package com.cncdigitalizationplatform.cncdigitalizationplatform.entities;

import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "drawing code")
public class DrawingCode {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "CHAR(36)")
    private String id;

    private String name;
    private String pdfUrl;
    private String description;
    private String status;
    private Long createdTime = System.currentTimeMillis();
    private Long updatedTime;
}
