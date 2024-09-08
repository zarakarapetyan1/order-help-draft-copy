package com.platform.repository;

import com.platform.entity.AccountEntity;
import com.platform.enums.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {

    AccountEntity getByAccountName(String name);

    AccountEntity getByAccountNameAndAccountTypeAndAccountIdNot(String name, AccountType accountType, UUID accountId);
}
