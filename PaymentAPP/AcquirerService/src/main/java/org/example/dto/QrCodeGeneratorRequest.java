package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class QrCodeGeneratorRequest {
    @JsonProperty("K")
    String K;
    @JsonProperty("V")
    String V;
    @JsonProperty("C")
    String C;
    @JsonProperty("R")
    String R;
    @JsonProperty("N")
    String N;
    @JsonProperty("I")
    String I;
    @JsonProperty("P")
    String P;
    @JsonProperty("SF")
    String SF;
    @JsonProperty("S")
    String S;

    public QrCodeGeneratorRequest(){
        K = "PR";
        V = "01";
        C = "1";
        SF = "189";
        S = "Placanje obaveza za kupovinu";
    }
}
