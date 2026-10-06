package com.planPurchase.Controller;

import com.planPurchase.Common.ApiResponse;
import com.planPurchase.Dto.InvoiceDto;
import com.planPurchase.Service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/invoice")
@Tag(name = "Invoice API", description = "Handles invoice generation and retrieval operations")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @Operation(summary = "Generate Invoice by Purchase ID", description = "Creates an invoice for the given purchase transaction.")
    @GetMapping("/preview/{purchaseId}")
    public ResponseEntity<ApiResponse<InvoiceDto>> previewInvoice(@PathVariable Long purchaseId) {
        InvoiceDto dto = invoiceService.getInvoiceByPurchaseId(purchaseId);
        ApiResponse<InvoiceDto> response = new ApiResponse<>(
                true,
                "Invoice fetched successfully!!",
                dto
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
