package me.study.inflearnjavatest.junit;

import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderTest {

    int orderValue = 1;

    @Test
    @Order(1)
    void order1_1() {
        System.out.println("OrderTest.order1_1");
        System.out.println("orderValue++ = " + orderValue++);
    }

    @Test
    @Order(1)
    void order1_2() {
        System.out.println("OrderTest.order1_2");
        System.out.println("orderValue++ = " + orderValue++);
    }

    @Test
    @Order(2)
    void order2() {
        System.out.println("OrderTest.order2");
        System.out.println("orderValue++ = " + orderValue++);
    }

    @Test
    @Order(3)
    void order3() {
        System.out.println("OrderTest.order3");
        System.out.println("orderValue++ = " + orderValue++);
    }
}
