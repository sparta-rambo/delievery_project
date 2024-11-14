package com.delivery_project.common;

import com.delivery_project.entity.*;
import com.delivery_project.enums.OrderStatus;
import com.delivery_project.repository.jpa.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final MenuRepository menuRepository;
    private final ReviewRepository reviewRepository;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if(userRepository.count()==0){
            User user = User.builder()
                    .id(UUID.randomUUID())
                    .address("서울특별시 노원구")
                    .role("ROLE_USER")
                    .password("1234")
                    .username("test")
                    .build();
            userRepository.save(user);

            Category category =Category.builder()
                    .id(UUID.randomUUID())
                    .name("치킨")
                    .build();
            categoryRepository.save(category);

            Restaurant restaurant = Restaurant.builder()
                    .id(UUID.randomUUID())
                    .address("서울특별시 노원구")
                    .name("또래오래")
                    .owner(user)
                    .isHidden(false)
                    .category(category)
                    .build();
            restaurantRepository.save(restaurant);

            Menu menu = Menu.builder()
                    .id(UUID.randomUUID())
                    .name("양념치킨")
                    .isHidden(false)
                    .restaurant(restaurant)
                    .price(24000)
                    .description("양념치킨 입니다.")
                    .build();
            menuRepository.save(menu);

            Order order = new Order(UUID.randomUUID(),user,restaurant,"온라인",OrderStatus.CONFIRM.getStatus(), 28000,"서울특별시 노원구","문앞에 놓아주세요");
            orderRepository.save(order);

            OrderItem orderItem = OrderItem.builder()
                    .id(UUID.randomUUID())
                    .order(order)
                    .menu(menu)
                    .quantity(1)
                    .build();
            orderItemRepository.save(orderItem);

            Review review = Review.builder()
                    .id(UUID.randomUUID())
                    .order(order)
                    .rating(5)
                    .build();
            reviewRepository.save(review);

            Payment payment = Payment.builder()
                    .id(UUID.randomUUID())
                    .amount(28000)
                    .order(order)
                    .paymentMethod("CARD")
                    .build();
            paymentRepository.save(payment);
        }
    }

}
