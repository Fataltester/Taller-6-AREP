/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.eci.arep.concurrencia.Service;
import edu.eci.arep.concurrencia.Model.Property;
import edu.eci.arep.concurrencia.Repository.PropertyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;

    public PropertyServiceImpl(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @Override
    public Property createProperty(Property property) {
        return propertyRepository.save(property);
    }

    @Override
    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    @Override
    public Optional<Property> getPropertyById(Long id) {
        return propertyRepository.findById(id);
    }

    @Override
    public Property updateProperty(Long id, Property property) {
        return propertyRepository.findById(id).map(existing -> {
            existing.setAddress(property.getAddress());
            existing.setPrice(property.getPrice());
            existing.setSize(property.getSize());
            existing.setDescription(property.getDescription());
            return propertyRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Property not found with id " + id));
    }

    @Override
    public void deleteProperty(Long id) {
        if (!propertyRepository.existsById(id)) {
            throw new RuntimeException("Property not found with id " + id);
        }
        propertyRepository.deleteById(id);
    }
}

