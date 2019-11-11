package comp.project.backend.jpa.persistence;

//import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;

/**
 *
 * Author: TOS
 */
@Entity
@Table(name = "Parts")
@EqualsAndHashCode()
public class Part {

    @Id
    @Column(name = "_Part Number")
    // Currently this is defined as "_PartNumber", and Jackson gets the JSON
    // property from the GETTER method. We could also define it with:
    // @JsonProperty("_PartNumber")
    private String _PartNumber;
    
    @NotNull
    @Column(name = "Part_Type")
    @JsonProperty("part_Type")
    private String partType;
    
    @NotNull
    @Column(name = "Sub_Type")
    @JsonProperty("sub_Type")
    private String subType;
    
    @NotNull
    @Column(name = "Value")
    private String value;
    

    public Part() {
    }

    public Part(String _PartNumber) {
        this._PartNumber = _PartNumber;
    }

    public String get_PartNumber() {
        return _PartNumber;
    }

    public void set_PartNumber(String _PartNumber) {
        this._PartNumber = _PartNumber;
    }
    
    public String getPartType() {
        return partType;
    }

    public void setPartType(String partType) {
        this.partType = partType;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "backend.Parts[_PartNumber=" + _PartNumber + "]";
    }

}
