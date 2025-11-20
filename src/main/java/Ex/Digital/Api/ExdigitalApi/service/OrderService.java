package Ex.Digital.Api.ExdigitalApi.service;

import Ex.Digital.Api.ExdigitalApi.dto.*;
import Ex.Digital.Api.ExdigitalApi.entity.*;
import Ex.Digital.Api.ExdigitalApi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<OrderDto> getOrdersByUserId(Integer userId) {
        return orderRepository.findByUserOrderByCreatedAtDesc(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<OrderDto> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public OrderDto getOrderById(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        return convertToDto(order);
    }

    public OrderDto getOrderByOrderNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        return convertToDto(order);
    }

    @Transactional
public OrderDto createOrder(CreateOrderRequest request) {
    User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    Order order = new Order();
    order.setUser(user);
    order.setOrderNumber(generateOrderNumber());
    order.setStatus("PENDING");
    order.setPaymentMethod(request.getPaymentMethod());
    order.setShippingAddress(request.getShippingAddress());
    order.setNotes(request.getNotes());

    Integer total = 0;
    List<OrderItem> orderItems = new ArrayList<>();

    for (OrderItemRequest itemRequest : request.getItems()) {
        Product product = productRepository.findById(itemRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + itemRequest.getProductId()));

        if (product.getStock() < itemRequest.getQuantity()) {
            throw new RuntimeException("Stock insuficiente para el producto: " + product.getName());
        }

        Integer price = product.getValue();
        Integer discountApplied = 0;

        if (product.getDiscount() != null && product.getDiscount().getActive()) {
            // Calcular descuento
            discountApplied = (price * product.getDiscount().getDiscount().intValue()) / 100;
            price = price - discountApplied;
        }

        Integer subtotal = price * itemRequest.getQuantity();

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(itemRequest.getQuantity());
        orderItem.setPriceAtPurchase(product.getValue());
        orderItem.setDiscountApplied(discountApplied);

        orderItems.add(orderItem);
        total += subtotal;

        productService.updateStock(product.getIdProduct(), -itemRequest.getQuantity());
    }

    order.setTotal(total);
    order.setOrderItems(orderItems);

    Order savedOrder = orderRepository.save(order);
    return convertToDto(savedOrder);
}

    @Transactional
    public OrderDto updateOrderStatus(Integer orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        // Validar estados válidos
        List<String> validStatuses = List.of("PENDING", "PROCESSING", "SHIPPED", "DELIVERED", "CANCELLED");
        if (!validStatuses.contains(newStatus)) {
            throw new RuntimeException("Estado inválido");
        }

        // Si se cancela, devolver stock
        if (newStatus.equals("CANCELLED") && !order.getStatus().equals("CANCELLED")) {
            for (OrderItem item : order.getOrderItems()) {
                productService.updateStock(item.getProduct().getIdProduct(), item.getQuantity());
            }
        }

        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);

        return convertToDto(updatedOrder);
    }

    private String generateOrderNumber() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "ORD-" + timestamp;
    }

    private OrderDto convertToDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setIdOrder(order.getIdOrder());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setUserId(order.getUser().getIdUser());
        dto.setUserName(order.getUser().getName());
        dto.setTotal(new BigDecimal(order.getTotal()));
        dto.setStatus(order.getStatus());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setNotes(order.getNotes());
        dto.setCreatedAt(order.getCreatedAt());

        // Convertir items
        List<OrderItemDto> itemDtos = order.getOrderItems().stream()
                .map(this::convertItemToDto)
                .collect(Collectors.toList());
        dto.setItems(itemDtos);

        return dto;
    }

    private OrderItemDto convertItemToDto(OrderItem item) {
        OrderItemDto dto = new OrderItemDto();
        dto.setIdOrderItem(item.getIdOrderItems());
        dto.setProductId(item.getProduct().getIdProduct());
        dto.setProductName(item.getProduct().getName());
        dto.setQuantity(item.getQuantity());
        dto.setPriceAtPurchase(new BigDecimal(item.getPriceAtPurchase()));
        dto.setDiscountApplied(new BigDecimal(item.getDiscountApplied()));
        // Calcular subtotal: (precio - descuento) * cantidad
        BigDecimal subtotal = new BigDecimal(item.getPriceAtPurchase() - item.getDiscountApplied())
                .multiply(new BigDecimal(item.getQuantity()));
        dto.setSubtotal(subtotal);
        return dto;
    }
}