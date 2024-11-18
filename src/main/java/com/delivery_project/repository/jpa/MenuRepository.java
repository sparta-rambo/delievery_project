package com.delivery_project.repository.jpa;

import com.delivery_project.entity.Menu;
import com.delivery_project.repository.implement.MenuRepositoryCustom;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, UUID>, MenuRepositoryCustom {


}
