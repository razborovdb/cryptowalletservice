package com.rdbprojects.cryptowallet.dao;

import com.rdbprojects.cryptowallet.entities.CryptoCurrencies;
import com.rdbprojects.cryptowallet.repositories.CryptoCurrenciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CryptoCurrenciesDao {
    @Autowired
    CryptoCurrenciesRepository cryptoCurrenciesRepository;

    public CryptoCurrencies addCrypto(CryptoCurrencies crypto) {
        try {
            CryptoCurrencies savedCrypto = cryptoCurrenciesRepository.save(crypto);

            return savedCrypto;
        } catch (Exception e) {

            return null;
        }

    }

    public CryptoCurrencies findCryptoByName(CryptoCurrencies crypto) {
        try {
            CryptoCurrencies findedCrypto = cryptoCurrenciesRepository.findCryptoByName(crypto.getCryptoName());

            return findedCrypto;
        } catch (Exception e) {

            return null;
        }

    }

    public CryptoCurrencies updateCrypto(CryptoCurrencies crypto) {
        try {
            CryptoCurrencies updatedCrypto = cryptoCurrenciesRepository.save(crypto);
            return updatedCrypto;
        } catch (Exception e) {
            return null;
        }

    }

    public CryptoCurrencies deleteCrypto(CryptoCurrencies crypto) {
        try {

            cryptoCurrenciesRepository.delete(crypto);
            return crypto;
        } catch (Exception e) {
            return null;
        }

    }

    public List<CryptoCurrencies> findAll() {
        try {
            List<CryptoCurrencies> findedCryptos = cryptoCurrenciesRepository.findAll();
            return findedCryptos;
        } catch (Exception e) {
            return null;
        }

    }
}
