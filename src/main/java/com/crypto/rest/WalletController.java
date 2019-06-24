package com.crypto.rest;

import com.crypto.dto.CreateWalletDto;
import com.crypto.dto.CreateWalletResponse;
import com.crypto.dto.TransactionDto;
import com.crypto.dto.TransactionResponse;
import com.crypto.dto.WalletBalanceResponse;
import com.crypto.services.EthereumService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
public class WalletController {

	private final EthereumService service;

    @Autowired
    public WalletController(EthereumService service) {
        this.service = service;
    }

    @ApiOperation(value = "Create new wallet.", httpMethod = "POST")
    @PostMapping("/wallet/create")
	public ResponseEntity<CreateWalletResponse> create(@RequestBody CreateWalletDto dto) throws Exception {
        return ResponseEntity.ok(service.create(dto.getEmail()));
	}

    @ApiOperation(value = "Send ETH from one address to another.", httpMethod = "POST")
	@PostMapping("/wallet/send")
	public ResponseEntity<TransactionResponse> send(@RequestBody TransactionDto dto) throws Exception {
		return ResponseEntity.ok(service.send(dto));
	}

    @ApiOperation(value = "Get wallet balance.", httpMethod = "GET")
    @GetMapping("/wallet/balance")
	public ResponseEntity<WalletBalanceResponse> get(@RequestParam("email") String email) throws Exception {
		return ResponseEntity.ok(service.balanceOf(email));
	}
}