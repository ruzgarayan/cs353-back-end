package cs353.proje.usecases.customer.controller;

import cs353.proje.usecases.common.dto.Order;
import cs353.proje.usecases.common.dto.Response;
import cs353.proje.usecases.customer.dto.Customer;
import cs353.proje.usecases.customer.dto.OrderFromCustomer;
import cs353.proje.usecases.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("CustomerController")
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    @Autowired
    CustomerService customerService;


    @GetMapping("/allRestaurants")
    public Response getAllRestaurants()
    {
        return customerService.getAllRestaurants();
    }

    //Ege
    @GetMapping("/customerData/id={id}")
    public Response getCustomerData(@PathVariable(value="id") int customer_id)
    {
        return customerService.getCustomerData(customer_id);
    }

    //Ege
    @PostMapping("/customerData/id={id}")
    public Response updateCustomerData(@PathVariable(value="id") int customer_id, @RequestBody Customer newCustomerData)
    {
        return customerService.updateCustomerData(customer_id, newCustomerData);
    }

    //Ege
    @GetMapping("/orders/id={id}")
    public Response getOldOrders(@PathVariable(value="id") int customer_id)
    {
        return customerService.getOldOrders(customer_id);
    }

    //Ege
    @GetMapping("/restaurants/id={id}")
    public Response getRestaurants(@PathVariable(value="id") int customer_id)
    {
        return customerService.getRestaurants(customer_id);
    }

    @GetMapping("/restaurants/id={id}/search={searchkey}")
    public Response getRestaurantsWithSearch(@PathVariable(value="id") int customer_id,
                                             @PathVariable(value="searchkey") String searchkey)
    {
        return customerService.getRestaurantsWithSearch(customer_id, searchkey);
    }

    //Kaan
    @GetMapping("/restaurants/id={id}/open={open}/rating={min}to{max}")
    public Response getRestaurantsWithFilter(@PathVariable(value="id") int customer_id,
                                             @PathVariable(value="open") boolean open,
                                             @PathVariable(value="min") double minRating,
                                             @PathVariable(value="max") double maxRating)
    {
        return customerService.getRestaurantsWithFilter(customer_id, open, minRating, maxRating);
    }

    //Kaan
    @GetMapping("/restaurantInfo/id={id}")
    public Response getRestaurantInfo(@PathVariable(value="id") int restaurant_id)
    {

        return customerService.getRestaurantInfo(restaurant_id);
    }

    @GetMapping("/restaurantMenu/id={id}")
    public Response getRestaurantMenu(@PathVariable(value="id") int restaurant_id)
    {
        return customerService.getRestaurantMenu(restaurant_id);
    }

    @GetMapping("/restaurantMenuByCategory/id={id}")
    public Response getRestaurantMenuByCategory(@PathVariable(value="id") int restaurant_id)
    {
        return customerService.getRestaurantMenuByCategory(restaurant_id);
    }

    //Returns List<Ingredient> in the Response
    @GetMapping("/ingredients/id={menu_item_id}")
    public Response getIngredients(@PathVariable(value="menu_item_id") int menu_item_id)
    {

        return customerService.getIngredients(menu_item_id);
    }

    //Kaan
    //Returns the
    @PostMapping("/order")
    public Response createNewOrder(@RequestBody OrderFromCustomer order)
    {
        return customerService.createNewOrder(order);
    }

    @GetMapping("/getOrderDetails/order_id={order_id}")
    public Response getOrderDetails(@PathVariable(value="order_id") int order_id)
    {
        return customerService.getOrderDetails(order_id);
    }

    //Kaan
    @PostMapping("/addFavorite/customer_id={customer_id}/restaurant_id={restaurant_id}")
    public Response addFavorite(@PathVariable(value="customer_id") int customer_id,
                                @PathVariable(value="restaurant_id") int restaurant_id)
    {
        System.out.println(restaurant_id);
        return customerService.addFavorite(customer_id, restaurant_id);
    }

    @PostMapping("/removeFavorite/customer_id={customer_id}/restaurant_id={restaurant_id}")
    public Response removeFavorite(@PathVariable(value="customer_id") int customer_id,
                                   @PathVariable(value="restaurant_id") int restaurant_id)
    {
        return customerService.removeFavorite(customer_id, restaurant_id);
    }

    @GetMapping("/getFavorite/customer_id={customer_id}")
    public Response getFavorite(@PathVariable(value="customer_id") int customer_id)
    {
        return customerService.getFavorite(customer_id);
    }

    @PostMapping("/approveOrder/order_id={order_id}")
    public Response approveOrder(@PathVariable(value="order_id") int order_id)
    {
        return customerService.approveOrder(order_id);
    }

}
