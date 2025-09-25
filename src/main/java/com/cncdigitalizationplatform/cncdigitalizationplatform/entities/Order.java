package com.cncdigitalizationplatform.cncdigitalizationplatform.entities;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`order`")
public class Order {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "CHAR(36)")
    private String id;
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
        name = "order_drawing_code",
        joinColumns = @JoinColumn(name = "order_id"),
        inverseJoinColumns = @JoinColumn(name = "drawing_code_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<DrawingCode> drawingCodes = new HashSet<>();
    
    private String orderNumber;
    private Long orderDate;
    private Long deliveryDate;
    private Long drawingOfNumber;
    private Long drawingPartCount;
    private String productionStatus;
    private Long estimateTime;
    private Long completionDate;
    private Long updatedDeliveryDate;
    private String shippingMethod;
    private Long overdueDeliveryDate;
    private String manufacturingOrder;
    private String remarks;
    
    private String status;
    private Long createdTime = System.currentTimeMillis();
    private Long updatedTime;
}
