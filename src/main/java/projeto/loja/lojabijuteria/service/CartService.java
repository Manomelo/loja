package projeto.loja.lojabijuteria.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import projeto.loja.lojabijuteria.dto.CartItemRequest;
import projeto.loja.lojabijuteria.dto.CartItemResponse;
import projeto.loja.lojabijuteria.dto.CartResponse;
import projeto.loja.lojabijuteria.entity.Cart;
import projeto.loja.lojabijuteria.entity.CartItem;
import projeto.loja.lojabijuteria.entity.Product;
import projeto.loja.lojabijuteria.entity.User;
import projeto.loja.lojabijuteria.repository.CartItemRepository;
import projeto.loja.lojabijuteria.repository.CartRepository;
import projeto.loja.lojabijuteria.repository.ProductRepository;
import projeto.loja.lojabijuteria.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private User getLoggedUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));
    }



    private Cart getOrCreateCart(){
        User user = getLoggedUser();
        return cartRepository.findUserById(user.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    private void validateCartOwnership(Cart cart){
        User loggedUser = getLoggedUser();
        if (!cart.getUser().getId().equals(loggedUser.getId())){
            throw new RuntimeException("Acesso negado");
        }
    }

    private CartItemResponse toCartItemResponse(CartItem item){
        CartItemResponse response = new CartItemResponse();
        response.setId(item.getId());
        response.setProductId(item.getProduct().getId());
        response.setProductName(item.getProduct().getName());
        response.setProductImageUrl(item.getProduct().getImageUrl());
        response.setProductPrice(item.getProduct().getPrice());
        response.setQuantity(item.getQuantity());
        response.setSubTotal(item.getProduct().getPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity())));

        return response;
    }

    private CartResponse toCartResponse(Cart cart){
        List<CartItemResponse> items = cart.getItems().stream()
                .map(this::toCartItemResponse)
                .toList();

        BigDecimal total = items.stream()
                .map(CartItemResponse::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setItems(items);
        response.setTotal(total);
        response.setItemCount(items.size());
        return response;
    }

    public CartResponse getCart(){
        Cart cart = getOrCreateCart();
        return toCartResponse(cart);
    }

    public CartResponse addItem(CartItemRequest request){
        Cart cart = getOrCreateCart();

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Produto nao encontrado"));

        if(product.getStock() < request.getQuantity()){
            throw new RuntimeException("Estoque insuficiente");
        }

        cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId())
                .ifPresentOrElse(
                        existingItem -> {
                            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
                            cartItemRepository.save(existingItem);
                        },
                        () -> {
                            CartItem newItem = new CartItem();
                            newItem.setCart(cart);
                            newItem.setProduct(product);
                            newItem.setQuantity(request.getQuantity());
                            cartItemRepository.save(newItem);
                        }
                );
        return toCartResponse(cartRepository.findById(cart.getId()).get());
    }

    public CartResponse updateItem(Long itemId, CartItemRequest request){
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item nao encontrado"));

        validateCartOwnership(item.getCart());

        if(item.getProduct().getStock() < request.getQuantity()){
            throw new RuntimeException("Estoque Insuficiente");
        }

        item.setQuantity(request.getQuantity());
        cartItemRepository.save(item);

        return toCartResponse(item.getCart());
    }

    public CartResponse removeItem(Long itemId){
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item nao encontrado")  );

        validateCartOwnership(item.getCart());

        cartItemRepository.delete(item);

        return toCartResponse(cartRepository.findById(item.getCart().getId()).get());
    }

    public void clearCart(){
        Cart cart = getOrCreateCart();
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
