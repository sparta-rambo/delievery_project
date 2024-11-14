package com.delivery_project.common;

import com.delivery_project.entity.*;
import com.delivery_project.enums.OrderStatus;
import com.delivery_project.enums.UserRoleEnum;
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
                    .role(UserRoleEnum.CUSTOMER)
                    .password("1234")
                    .username("김성호")
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

            Menu menu1 = Menu.builder()
                    .id(UUID.randomUUID())
                    .name("양념치킨")
                    .isHidden(false)
                    .restaurant(restaurant)
                    .price(24000)
                    .description("양념치킨 입니다.")
                    .build();

            Menu menu2 = Menu.builder()
                    .id(UUID.randomUUID())
                    .name("간장치킨")
                    .isHidden(false)
                    .restaurant(restaurant)
                    .price(24000)
                    .description("간장치킨 입니다.")
                    .build();
            menuRepository.save(menu1);
            menuRepository.save(menu2);

            Order order = new Order(UUID.fromString("54661171-6767-433e-bb0f-d5295056872c"),user,restaurant,"온라인",OrderStatus.CONFIRM.getStatus(), 48000,"서울특별시 노원구","문앞에 놓아주세요");
            orderRepository.save(order);

            OrderItem orderItem1 = OrderItem.builder()
                    .id(UUID.randomUUID())
                    .order(order)
                    .menu(menu1)
                    .quantity(1)
                    .build();
            OrderItem orderItem2 = OrderItem.builder()
                    .id(UUID.randomUUID())
                    .order(order)
                    .menu(menu2)
                    .quantity(1)
                    .build();
            orderItemRepository.save(orderItem1);
            orderItemRepository.save(orderItem2);

            Review review = Review.builder()
                    .id(UUID.randomUUID())
                    .order(order)
                    .rating(5)
                    .build();
            reviewRepository.save(review);

            Payment payment = Payment.builder()
                    .id(UUID.randomUUID())
                    .amount(48000)
                    .order(order)
                    .paymentMethod("CARD")
                    .build();
            paymentRepository.save(payment);
        }
    }

}
