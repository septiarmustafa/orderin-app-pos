package com.enigma.orderin.service;

import com.enigma.orderin.dto.response.CashierResponse;
import com.enigma.orderin.entity.Cashier;

import java.util.List;

public interface CashierService {

    CashierResponse createCashier (Cashier cashier);

    CashierResponse getById (Integer id);

    List<CashierResponse> getAllCashier ();

    CashierResponse updateCashier (Cashier cashier);
    void deleteCashier (Integer id);
}
