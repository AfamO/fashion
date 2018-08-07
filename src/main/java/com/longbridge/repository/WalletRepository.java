package com.longbridge.repository;

import com.longbridge.models.User;
import com.longbridge.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Longbridge on 03/08/2018.
 */
@Repository
public interface WalletRepository extends JpaRepository<Wallet,Long>{
    Wallet findByUser(User user);
}
