package com.enigma.orderin.service.impl;

import com.enigma.orderin.dto.response.CashierResponse;
import com.enigma.orderin.entity.Cashier;
import com.enigma.orderin.repository.CashierRepository;
import com.enigma.orderin.service.CashierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CashierServiceImpl implements CashierService {



    private final CashierRepository cashierRepository;


    @Override
    public CashierResponse createCashier(Cashier cashier) {
        Cashier cashiers = cashierRepository.saveAndFlush(cashier);
        return CashierResponse.builder()
                .id(cashiers.getId())
                .name(cashiers.getName())
                .mobilePhone(cashiers.getPhoneNumber())
                .build();
    }

    @Override
    public CashierResponse getById(Integer id) {
        Cashier customer = cashierRepository.findById(id).orElse(null);

        if (customer == null) {
            return null;
        }
        return CashierResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .mobilePhone(customer.getPhoneNumber())
                .build();
    }

    @Override
    public List<CashierResponse> getAllCashier() {
        List<Cashier> listCashier = cashierRepository.findAll();
        return listCashier.stream().map(
                cashier -> CashierResponse.builder()
                .id(cashier.getId())
                .name(cashier.getName())
                .mobilePhone(cashier.getPhoneNumber())
                .build())
                .collect(Collectors.toList());
    }

    @Override
    public CashierResponse updateCashier(Cashier cashier) {
       Cashier cashiers = cashierRepository.findById(cashier.getId()).orElse(null);
       if (cashiers != null) {
           cashiers = Cashier.builder()
                .id(cashier.getId())
                .name(cashier.getName())
                .phoneNumber(cashier.getPhoneNumber())
                .build();
           cashierRepository.save(cashiers);
           return CashierResponse.builder()
                   .id(cashiers.getId())
                   .name(cashiers.getName())
                   .mobilePhone(cashiers.getPhoneNumber())
                   .build();
       }
       return null;
    }

    @Override
    public void deleteCashier(Integer id) {
        cashierRepository.deleteById(id);
    }
}
