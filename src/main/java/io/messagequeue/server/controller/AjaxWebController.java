package io.messagequeue.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AjaxWebController {
    @GetMapping("/web/products-ajax")
    public String showAjaxPage() {
        return "products_ajax"; // maps to templates/products_ajax.html
    }

    @GetMapping("/web/orders-ajax")
    public String showOrdersAjaxPage() {
        return "orders_ajax"; // maps to templates/orders_ajax.html
    }
}
