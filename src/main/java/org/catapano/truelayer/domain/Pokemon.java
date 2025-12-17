package org.catapano.truelayer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class Pokemon {

    String name;
    String habitat;
    String description;
    Boolean isLegendary;


}
