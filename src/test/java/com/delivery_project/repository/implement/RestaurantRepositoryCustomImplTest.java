package com.delivery_project.repository.implement;

import static org.assertj.core.api.Assertions.assertThat;

import com.delivery_project.config.QuerydslConfig;
import com.delivery_project.entity.Category;
import com.delivery_project.entity.QRestaurant;
import com.delivery_project.entity.Restaurant;
import com.delivery_project.entity.User;
import com.delivery_project.enums.UserRoleEnum;
import com.delivery_project.repository.jpa.CategoryRepository;
import com.delivery_project.repository.jpa.RestaurantRepository;
import com.delivery_project.repository.jpa.UserRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Import(QuerydslConfig.class)
class RestaurantRepositoryCustomImplTest {

    @Autowired
    private RestaurantRepositoryCustomImpl restaurantRepositoryCustom;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    @DisplayName("findRestaurants 메서드 테스트")
    void findRestaurantsTest() {
        // given
        Category category = categoryRepository.save(Category.builder()
            .id(UUID.randomUUID())
            .name("한식")
            .build());

        User owner = userRepository.save(User.builder()
            .id(UUID.randomUUID())
            .username("ownerTest")
            .role(UserRoleEnum.OWNER)
            .address("서울시")
            .password("password1234!")
            .build());

        Restaurant restaurant1 = restaurantRepository.save(Restaurant.builder()
            .id(UUID.randomUUID())
            .name("김밥천국")
            .category(category)
            .owner(owner)
            .address("서울시")
            .isHidden(false)
            .build());

        Restaurant restaurant2 = restaurantRepository.save(Restaurant.builder()
            .id(UUID.randomUUID())
            .name("김가네")
            .category(category)
            .owner(owner)
            .address("서울시")
            .isHidden(false)
            .build());

        // when
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        BooleanExpression predicate = QRestaurant.restaurant.isHidden.eq(false)
            .and(QRestaurant.restaurant.category.name.eq("한식"))
            .and(QRestaurant.restaurant.owner.username.eq("ownerTest"));

        List<Restaurant> result = restaurantRepositoryCustom.findRestaurants(predicate, pageRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("김가네");
        assertThat(result.get(1).getName()).isEqualTo("김밥천국");
    }
}
