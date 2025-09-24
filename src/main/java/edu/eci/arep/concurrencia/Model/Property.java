package edu.eci.arep.concurrencia.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "properties")
public class Property{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer size;

    private String description;

    public Property() {}

    public Property(String address, Double price, Integer size, String description) {
        this.address = address;
        this.price = price;
        this.size = size;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

