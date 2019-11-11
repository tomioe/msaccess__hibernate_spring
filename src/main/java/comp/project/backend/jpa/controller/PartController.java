package comp.project.backend.jpa.controller;

import java.beans.PropertyDescriptor;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import comp.project.backend.jpa.persistence.Part;
import comp.project.backend.jpa.persistence.PartRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 *
 * Author: TOS
 */
@RestController
@Transactional(isolation = Isolation.READ_UNCOMMITTED)
@CrossOrigin( origins = {"http://localhost:3000",  "http://localhost:3001"} )
@Slf4j
public class PartController {

    @Autowired
    private PartRepository partRepository;

    @PersistenceContext
    EntityManager entityManager;

    @GetMapping(value = "/parts", headers = "Accept=application/json")
    public List<Part> getParts() {
        return partRepository.findAll();
    }

    @GetMapping(value = "/parts/{partNumber}", headers = "Accept=application/json")
    public ResponseEntity<Part> getPart(@PathVariable String partNumber) throws NoSuchFieldException {
        Optional<Part> retPart = partRepository.findById(partNumber);

        if (!retPart.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(retPart.get());
    }

    @PostMapping("/parts")
    public ResponseEntity<Part> createPart(@RequestBody @Valid Part newPart) {

        Optional<Part> exisitingPart = partRepository.findById(newPart.get_PartNumber());
        if (exisitingPart.isPresent()) {
            //return ResponseEntity.status(409).body(presentPart.get());
            log.info("User tried to add new part '" + exisitingPart.get().get_PartNumber() + "' but it was already present.");
            // '409': 'Conflict'
            return ResponseEntity.status(409).build();
        }

        // TODO: Sanity check, e.g. are all required fields present, correctly formatted, etc.
        // Small work-around is that @NonNull is specified in Part now.
        String newPartNumber = newPart.get_PartNumber();
        System.out.println("new part number: '"+newPartNumber+"'");
        // This is broken in the public code ... tweak as required!
        // (Left in intentionally to demonstrate how a parameter can be sanity checked)
        if(newPartNumber.length() <= 3 || !newPartNumber.substring(0, 3).equals("_")) {
            log.info("User tried to add new part, but the Part Number was invalid.");
            // '422': 'Unprocessable Entity'
            return ResponseEntity.unprocessableEntity().body(newPart);
        }
        
        Part savedPart = partRepository.save(newPart);

        URI newPartLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{partNumber}")
                .buildAndExpand(savedPart.get_PartNumber())
                .toUri();

        log.info("User added new part '" + savedPart.get_PartNumber() + "'.");
        return ResponseEntity.created(newPartLocation).build();
    }

    @PutMapping("/parts/{partNumber}")
    public ResponseEntity<Object> updatePart(@RequestBody @Valid Part updatedPart, @PathVariable String partNumber) {

        Optional<Part> oldPart = partRepository.findById(partNumber);

        //if (!partRepository.existsById(partNumber)) {
        if (!oldPart.isPresent()) {
            log.info("User tried updating part '" + partNumber + "', but it was not found.");
            return ResponseEntity.notFound().build();
        }

        mergeParts(oldPart.get(), updatedPart);
        updatedPart.set_PartNumber(partNumber);

        partRepository.save(updatedPart);
        log.info("User updated part '" + partNumber + "'.");

        return ResponseEntity.noContent().build();
    }
    
    
    // This method 'breaks' the idea behind JPA / Persistency.
    // However, it is included as a design feature of the project.
    private void mergeParts(Part oldPart, Part newPart) {
        
        BeanWrapper oldPartProperties = new BeanWrapperImpl(oldPart),
                    newPartProperties = new BeanWrapperImpl(newPart);
        PropertyDescriptor[] pds = oldPartProperties.getPropertyDescriptors();
        for(PropertyDescriptor pd : pds) {
            String propertyName = pd.getName();
            if(propertyName.equalsIgnoreCase("class"))
                continue;
            Object oldPropertyValue = oldPartProperties.getPropertyValue(propertyName);
            Object newPropertyValue = newPartProperties.getPropertyValue(propertyName);
            
            
            // Should be noted that this type of merge, a 'newPart' field of null
            // will NOT lead to the value being deleted. However, this is part
            // of the design for the project.
            if(newPropertyValue != null) {
                newPartProperties.setPropertyValue(propertyName, newPropertyValue);
            } else {
                newPartProperties.setPropertyValue(propertyName, oldPropertyValue);
            }
        }
    }
    
    @DeleteMapping("/parts/{partNumber}")
    public ResponseEntity<Part> deletePart(@PathVariable String partNumber) {
        Optional<Part> partOptional = partRepository.findById(partNumber);

        if (partOptional.isPresent()) {
            log.info("User deleted part '" + partNumber + "'.");
            partRepository.deleteById(partNumber);
            return ResponseEntity.ok().build();
        } else {
            log.info("User tried to delete part '" + partNumber + "', but it was not found.");
            return ResponseEntity.notFound().build();
        }
    }

}
