package com.rdbprojects.cryptowallet.dao;

import com.rdbprojects.cryptowallet.entities.Users;
import com.rdbprojects.cryptowallet.entities.Wallets;
import com.rdbprojects.cryptowallet.repositories.WalletsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletsDao {
    @Autowired
    WalletsRepository walletsRepository;

    public Wallets addWallet(Wallets wallet) {
        try {
            Wallets savedWallet = walletsRepository.save(wallet);
            return savedWallet;
        } catch (Exception e) {
            return null;
        }

    }

    public Wallets findWalletByWalletNameAndUserId(Wallets wallet) {
        try {
            Wallets findedWallet = walletsRepository.findWalletByWalletNameAndUserId(wallet.getUserId(), wallet.getWalletName());


            return findedWallet;
        } catch (Exception e) {

            return null;
        }

    }

    public List<Wallets> findWalletByUserId(String email) {
        try {
            List<Wallets> findedWallets = walletsRepository.findWalletsByUserId(email);

            return findedWallets;
        } catch (Exception e) {

            return null;
        }

    }

    public Wallets updateWallet(Wallets wallet) {
        try {

            Wallets updatedWallet = walletsRepository.save(wallet);
            return updatedWallet;
        } catch (Exception e) {

            return null;
        }

    }

    public Wallets deleteWallet(Wallets wallet) {
        try {
            walletsRepository.delete(wallet);
            return wallet;
        } catch (Exception e) {
            return null;
        }

    }

    public List<Wallets> findAll() {
        try {
            List<Wallets> findedWallets = walletsRepository.findAll();
            return findedWallets;
        } catch (Exception e) {
            return null;
        }

    }
}
