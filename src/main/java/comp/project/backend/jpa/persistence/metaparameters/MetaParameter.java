package comp.project.backend.jpa.persistence.metaparameters;

import javax.persistence.Table;
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
public class MetaParameter {
    
    private String parameterName, parameterValue;
    private int parameterOrder;

    public MetaParameter(String parameterValue) {
        this.parameterValue = parameterValue;
    }

    public MetaParameter(String parameterName, String parameterValue) {
        this.parameterName = parameterName;
        this.parameterValue = parameterValue;
    }
    
}
