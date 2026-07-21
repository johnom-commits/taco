package tacos.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import tacos.TacoOrder;
import tacos.data.OrderRepository;

@Slf4j
@Controller
@RequiredArgsConstructor
@SessionAttributes("tacoOrder")
public class OrderController {

    private final OrderRepository orderRepo;

    @GetMapping("/orders/current")
    public String orderForm() {
        return "orderForm";
    }
    
    @PostMapping("/orders")
    public String processOrder(@Valid TacoOrder order, Errors errors,
                               SessionStatus sessionStatus) {
        if (errors.hasErrors()) {
            errors.getFieldErrors().forEach(e -> {
                log.error("{} : {}", e.getField(), e.getDefaultMessage());
            });
            return "orderForm";
        }
        orderRepo.save(order);
        log.info("Order submitted: {}", order);
        sessionStatus.setComplete();
        return "redirect:/";
    }
}
