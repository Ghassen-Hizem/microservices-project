package ghassen.cart.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ghassen.cart.cart.CartDAO;
import ghassen.cart.entities.Cart;
import ghassen.cart.entities.Item;
import ghassen.cart.item.ItemDAO;
import ghassen.cart.repositories.CartRepository;
import ghassen.cart.repositories.ItemRepository;

import java.util.List;

@Configuration
public class BeanConfiguration {
    @Bean
    public CartDAO getCartDao(CartRepository cartRepository) {
        return new CartDAO() {
            @Override
            public void delete(Cart cart) {
                cartRepository.delete(cart);
            }

            @Override
            public Cart save(Cart cart) {
                return cartRepository.save(cart);
            }

            @Override
            public List<Cart> findByCustomerId(String customerId) {
                return cartRepository.findByCustomerId(customerId);
            }
        };
    }

    @Bean
    public ItemDAO getItemDao(ItemRepository itemRepository) {
        return new ItemDAO() {
            @Override
            public Item save(Item item) {
                return itemRepository.save(item);
            }

            @Override
            public void destroy(Item item) {
                itemRepository.delete(item);
            }

            @Override
            public Item findOne(String id) {
                return itemRepository.findById(id).orElse(null);
            }
        };
    }
}
