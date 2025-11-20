package Ex.Digital.Api.ExdigitalApi.controller;

import Ex.Digital.Api.ExdigitalApi.dto.ApiResponse;
import Ex.Digital.Api.ExdigitalApi.dto.CreateOrderRequest;
import Ex.Digital.Api.ExdigitalApi.dto.OrderDto;
import Ex.Digital.Api.ExdigitalApi.security.CustomUserDetails;
import Ex.Digital.Api.ExdigitalApi.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<OrderDto>>> getAllOrders() {
        try {
            List<OrderDto> orders = orderService.getAllOrders();
            return ResponseEntity.ok(ApiResponse.success("Órdenes obtenidas exitosamente", orders));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.userId")
    public ResponseEntity<ApiResponse<List<OrderDto>>> getOrdersByUserId(@PathVariable Integer userId) {
        try {
            List<OrderDto> orders = orderService.getOrdersByUserId(userId);
            return ResponseEntity.ok(ApiResponse.success("Órdenes del usuario obtenidas exitosamente", orders));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/my-orders")
    public ResponseEntity<ApiResponse<List<OrderDto>>> getMyOrders(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            List<OrderDto> orders = orderService.getOrdersByUserId(userDetails.getUserId());
            return ResponseEntity.ok(ApiResponse.success("Mis órdenes obtenidas exitosamente", orders));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<OrderDto>>> getOrdersByStatus(@PathVariable String status) {
        try {
            List<OrderDto> orders = orderService.getOrdersByStatus(status);
            return ResponseEntity.ok(ApiResponse.success("Órdenes por estado obtenidas exitosamente", orders));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderDto>> getOrderById(
            @PathVariable Integer id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            OrderDto order = orderService.getOrderById(id);
            
            // Verificar que el usuario solo pueda ver sus propias órdenes (excepto admin)
            if (!userDetails.getRoleName().equals("ADMIN") && 
                !order.getUserId().equals(userDetails.getUserId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("No tiene permiso para ver esta orden"));
            }
            
            return ResponseEntity.ok(ApiResponse.success("Orden obtenida exitosamente", order));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/order-number/{orderNumber}")
    public ResponseEntity<ApiResponse<OrderDto>> getOrderByOrderNumber(
            @PathVariable String orderNumber,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            OrderDto order = orderService.getOrderByOrderNumber(orderNumber);
            
            // Verificar que el usuario solo pueda ver sus propias órdenes (excepto admin)
            if (!userDetails.getRoleName().equals("ADMIN") && 
                !order.getUserId().equals(userDetails.getUserId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("No tiene permiso para ver esta orden"));
            }
            
            return ResponseEntity.ok(ApiResponse.success("Orden obtenida exitosamente", order));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderDto>> createOrder(
            @Valid @RequestBody CreateOrderRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            // Verificar que el usuario solo pueda crear órdenes para sí mismo (excepto admin)
            if (!userDetails.getRoleName().equals("ADMIN") && 
                !request.getUserId().equals(userDetails.getUserId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("No puede crear órdenes para otros usuarios"));
            }
            
            OrderDto order = orderService.createOrder(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Orden creada exitosamente", order));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<OrderDto>> updateOrderStatus(
            @PathVariable Integer id,
            @RequestParam String status) {
        try {
            OrderDto order = orderService.updateOrderStatus(id, status);
            return ResponseEntity.ok(ApiResponse.success("Estado de orden actualizado exitosamente", order));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}