package Ex.Digital.Api.ExdigitalApi.service;

import Ex.Digital.Api.ExdigitalApi.dto.CreateDiscountRequest;
import Ex.Digital.Api.ExdigitalApi.dto.DiscountDto;
import Ex.Digital.Api.ExdigitalApi.entity.Discount;
import Ex.Digital.Api.ExdigitalApi.repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiscountService {

    @Autowired
    private DiscountRepository discountRepository;

    public List<DiscountDto> getAllDiscounts() {
        return discountRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<DiscountDto> getActiveDiscounts() {
        return discountRepository.findActiveDiscountsByDate(LocalDate.now()).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public DiscountDto getDiscountById(Integer id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Descuento no encontrado"));
        return convertToDto(discount);
    }

    @Transactional
    public DiscountDto createDiscount(CreateDiscountRequest request) {
        if (request.getEndDate().isBefore(request.getInitDate())) {
            throw new RuntimeException("La fecha de fin debe ser posterior a la fecha de inicio");
        }

        Discount discount = new Discount();
        discount.setName(request.getName());
        discount.setDiscount(request.getDiscount());
        discount.setInitDate(request.getInitDate());
        discount.setEndDate(request.getEndDate());
        discount.setDescription(request.getDescription());
        discount.setActive(true);

        Discount savedDiscount = discountRepository.save(discount);
        return convertToDto(savedDiscount);
    }

    @Transactional
    public DiscountDto updateDiscount(Integer id, CreateDiscountRequest request) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Descuento no encontrado"));

        if (request.getName() != null) {
            discount.setName(request.getName());
        }
        if (request.getDiscount() != null) {
            discount.setDiscount(request.getDiscount());
        }
        if (request.getInitDate() != null) {
            discount.setInitDate(request.getInitDate());
        }
        if (request.getEndDate() != null) {
            discount.setEndDate(request.getEndDate());
        }
        if (request.getDescription() != null) {
            discount.setDescription(request.getDescription());
        }

        Discount updatedDiscount = discountRepository.save(discount);
        return convertToDto(updatedDiscount);
    }

    @Transactional
    public void activateDiscount(Integer id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Descuento no encontrado"));
        discount.setActive(true);
        discountRepository.save(discount);
    }

    @Transactional
    public void deactivateDiscount(Integer id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Descuento no encontrado"));
        discount.setActive(false);
        discountRepository.save(discount);
    }

    private DiscountDto convertToDto(Discount discount) {
        return new DiscountDto(
                discount.getIdDiscount(),
                discount.getName(),
                discount.getActive(),
                discount.getDiscount(),
                discount.getInitDate(),
                discount.getEndDate(),
                discount.getDescription()
        );
    }
}