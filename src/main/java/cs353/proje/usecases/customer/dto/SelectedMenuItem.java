package cs353.proje.usecases.customer.dto;

import cs353.proje.usecases.common.dto.Ingredient;
import cs353.proje.usecases.common.dto.MenuItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SelectedMenuItem {
    private MenuItem menuItem;
    private int quantity;

    //ingredient information of the selected ingredients for this menu item.
    private List<Ingredient> selectedIngredients;
}
