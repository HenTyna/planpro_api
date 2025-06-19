package com.planprostructure.planpro.domain.proTalk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageMetadata implements Serializable {
    private Boolean edited;
    private List<String> reactions;
    private Long replyTo;
}
