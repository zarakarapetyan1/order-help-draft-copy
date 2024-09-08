package com.platform.springjpa;

import com.platform.entity.AccountEntity;
import com.platform.exceptions.accountexceptions.AccountAlreadyExistsException;
import com.platform.exceptions.accountexceptions.AccountApiException;
import com.platform.exceptions.accountexceptions.AccountBadRequestException;
import com.platform.exceptions.accountexceptions.AccountNotFoundException;
import com.platform.model.Account;
import com.platform.repository.AccountRepository;
import com.platform.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountSpringJpa implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account createAccount(Account account) {
        AccountEntity accountEntity;

        if (account.getAccountId() != null) {
            throw new AccountBadRequestException("Account ID must be null");
        }

        try {
            accountEntity = accountRepository.getByAccountName(account.getAccountName());
        } catch (Exception e) {
            throw new AccountApiException("Problem during creating account");
        }

        if (accountEntity != null) {
            throw new AccountAlreadyExistsException("Account already exists with given name");
        }

        try {
            accountEntity = AccountEntity.builder()
                    .accountName(account.getAccountName())
                    .accountType(account.getAccountType())
                    .build();
            accountEntity = accountRepository.save(accountEntity);
        } catch (Exception e) {
            throw new AccountApiException("Problem during creating account");
        }

        return accountEntity.toAccount();
    }


    @Override
    public Account getAccount(UUID accountId) {
         AccountEntity entity= accountRepository.findById(accountId)
                 .orElseThrow(()->new AccountNotFoundException("Account not found with given id"));
         return entity.toAccount();
    }

    @Override
    public Account updateAccount(UUID accountId, Account account) {
        if(account.getAccountId() != null && !accountId.equals(account.getAccountId())) {
            throw new AccountBadRequestException("not match");
        }
        Optional<AccountEntity> accountEntities;
        try{
            accountEntities = accountRepository.findById(accountId);
        }catch (Exception e) {
            throw new AccountApiException("Problem during updating account");
        }
        if (accountEntities.isPresent()) {
            throw new AccountAlreadyExistsException("Account not found with given id");
        }

        AccountEntity accountEntity=accountEntities.get();

        AccountEntity existingAccount = accountRepository.
                getByAccountNameAndAccountTypeAndAccountIdNot(account.getAccountName(), account.getAccountType(), accountId);

        if (existingAccount != null) {
            throw new AccountBadRequestException("Account already exists with given name an type");
        }
        accountEntity.setAccountName(account.getAccountName());
        accountEntity.setAccountType(account.getAccountType());

       return accountRepository.save(accountEntity).toAccount();
    }


    @Override
    public void deleteAccount(UUID accountId) {
        AccountEntity existingAccountEntity = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with given id"));

        try {
            accountRepository.delete(existingAccountEntity);
        } catch (Exception e) {
            throw new AccountApiException("Problem during deleting account");
        }
    }

    @Override
    public List<Account> getAll() {
        List<AccountEntity> accountEntities;
        try {
            accountEntities = accountRepository.findAll();
        } catch (Exception e) {
            throw new AccountApiException("Problem during getting account");
        }

        return accountEntities.stream().map(AccountEntity::toAccount).toList();
    }

}
