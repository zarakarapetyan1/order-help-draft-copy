package com.platform.controller;


import com.platform.constants.RoutConstants;
import com.platform.model.Account;
import com.platform.repository.AccountRepository;
import com.platform.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(RoutConstants.BASE_URL + RoutConstants.VERSION + RoutConstants.ACCOUNTS)
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Account create(@RequestBody Account account) {

        return accountService.createAccount(account);
    }


    @GetMapping("/{id}")
    public @ResponseBody Account getAccountById(@PathVariable UUID id) {

        return accountService.getAccount(id);
    }

    @PutMapping("/{id}")
    public @ResponseBody Account updateAccount(@PathVariable UUID id, @RequestBody Account account) {
        return accountService.updateAccount(id, account);
    }

    @GetMapping
    public @ResponseBody List<Account> getAll() {
        return accountService.getAll();
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@PathVariable UUID id) {
        accountService.deleteAccount(id);
    }

}
