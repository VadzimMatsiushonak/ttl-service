package by.vadzimmatsiushonak.ttlservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TTLPayload {

    private LoggingType type;
    private String message;

}
