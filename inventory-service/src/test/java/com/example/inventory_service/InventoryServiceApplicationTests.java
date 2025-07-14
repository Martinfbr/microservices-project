package com.example.inventory_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class InventoryServiceApplicationTests {

    @Test
    void contextLoads() {
        // Este test verifica que la aplicación Spring se puede levantar correctamente
    }
}
