package cs353.proje.usecases.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Favorite {
    private int customerId;
    private int restaurantId;
}
