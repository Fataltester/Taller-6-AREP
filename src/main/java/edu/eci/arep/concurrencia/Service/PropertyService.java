package edu.eci.arep.concurrencia.Service;

import edu.eci.arep.concurrencia.Model.Property;
import java.util.List;
import java.util.Optional;

public interface PropertyService {
    Property createProperty(Property property);
    List<Property> getAllProperties();
    Optional<Property> getPropertyById(Long id);
    Property updateProperty(Long id, Property property);
    void deleteProperty(Long id);
}

