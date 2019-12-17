package comp.project.backend.jpa.persistence.metaparameters;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * Author: TOS
 */
@Getter
@Setter
@EqualsAndHashCode()
@Table(name = "MetaParts")
public class ClassParameter {
    
    @NotNull
    @Column(name = "Class I")
    private String classI;
    
    @Column(name = "Type")
    private String type;

    public ClassParameter(String classI, String type) {
        this.classI = classI;
        this.type = type;
    }

}
