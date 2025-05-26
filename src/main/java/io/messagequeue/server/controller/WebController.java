package io.messagequeue.server.controller;

import java.math.BigDecimal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.messagequeue.server.dto.ProductRequest;
import io.messagequeue.server.service.OrderService;
import io.messagequeue.server.service.ProductService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/web")
@RequiredArgsConstructor
public class WebController {
    private final ProductService productService;
    private final OrderService orderService;

    @GetMapping("/products")
    public String productPage(Model model) {
        model.addAttribute("products", productService.findAll());
        return "products";
    }

    @PostMapping("/products/create")
    public String createProduct(@RequestParam String name,
                                @RequestParam String description,
                                @RequestParam BigDecimal price,
                                @RequestParam String imgUrl) {
        ProductRequest request = new ProductRequest(name, description, price, imgUrl);
        productService.create(request);
        return "redirect:/web/products";
    }

    @GetMapping("/orders")
    public String orderPage(Model model) {
        model.addAttribute("products", productService.findAll());
        model.addAttribute("orders", orderService.getAllOrders());
        return "orders";
    }

    // @PostMapping("/orders/create")
    // public String createOrder(@RequestParam Long userId,
    //                           @RequestParam List<Long> productIds,
    //                           @RequestParam List<Integer> quantities) {

    //     orderService.createOrder(userId, productIds, quantities);
    //     return "redirect:/web/orders";
    // }
}
