package com.rdbprojects.cryptowallet.controllers;

import com.rdbprojects.cryptowallet.dao.CryptoCurrenciesDao;
import com.rdbprojects.cryptowallet.dao.UsersDao;
import com.rdbprojects.cryptowallet.dao.WalletsDao;
import com.rdbprojects.cryptowallet.entities.CryptoCurrencies;
import com.rdbprojects.cryptowallet.entities.Users;
import com.rdbprojects.cryptowallet.entities.Wallets;
import com.rdbprojects.cryptowallet.requests.CryptoInWalletRequest;
import com.rdbprojects.cryptowallet.udt.CryptoCurrenciesModel;
import com.rdbprojects.cryptowallet.utils.JsonWebToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class WalletsController {
    @Autowired
    WalletsDao walletsDao;

    @Autowired
    CryptoCurrenciesDao cryptoCurrenciesDao;

    @Autowired
    UsersDao usersDao;

    @Autowired
    JsonWebToken jsonWebToken;

    @Value("${jwt.secret}")
    private String secret;

    @CrossOrigin
    @GetMapping("/wallets")
    public ResponseEntity<List<Wallets>> getWalletsByUserId(@RequestHeader("token") String token, @RequestParam(value = "email") String email) {
        if (email == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        // check token
        Users user = jsonWebToken.getUserInformationFromToken(token);
        if(user == null) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }
        try {
            user = usersDao.findUserByEmail(user);
            if (user == null) {
                return ResponseEntity.status(400).eTag("Bad token ...").body(null);
            }
            if (!(user.getIsAdmin() || user.getEmail().equals(email))) {
                return ResponseEntity.status(400).eTag("Not authorized ...").body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }
        List<CryptoCurrencies> cryptocurrencyList = cryptoCurrenciesDao.findAll();

        List<Wallets> wallets = walletsDao.findWalletByUserId(email);

        if (wallets == null || cryptocurrencyList == null) {
            return ResponseEntity.ok(wallets);
        }
        updateAllWallets(wallets, cryptocurrencyList);


        return ResponseEntity.ok(wallets);
    }

    @GetMapping("/allwallets")
    public ResponseEntity<List<Wallets>> getAllWallets(@RequestHeader(value="${json.token}") String token) {
        // check token
        Users user = jsonWebToken.getUserInformationFromToken(token);
        if(user == null) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }
        try {
            user = usersDao.findUserByEmail(user);
            if (user == null) {
                return ResponseEntity.status(400).eTag("Bad token ...").body(null);
            }
            if (!user.getIsAdmin()) {
                return ResponseEntity.status(400).eTag("Not authorized ...").body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }

        List<Wallets> wallets = walletsDao.findAll();
        return ResponseEntity.ok(wallets);
    }

    @GetMapping("/wallet")
    public ResponseEntity<Wallets> getWalletsByWalletName(
            @RequestHeader(value="${json.token}") String token,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "walletName") String walletName) {

        // check request
        if (email == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        if (walletName == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        // check token
        Users user = jsonWebToken.getUserInformationFromToken(token);
        if(user == null) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }
        try {
            user = usersDao.findUserByEmail(user);
            if (user == null) {
                return ResponseEntity.status(400).eTag("Bad token ...").body(null);
            }
            if (!(user.getIsAdmin() || user.getEmail().equals(email))) {
                return ResponseEntity.status(400).eTag("Not authorized ...").body(null);
            }

        } catch (Exception e) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }

        Wallets findWallet = new Wallets();
        findWallet.setUserId(email);
        findWallet.setWalletName(walletName);
        // try to find wallet
        Wallets findedWallet = walletsDao.findWalletByWalletNameAndUserId(findWallet);
        if (findedWallet == null) {
            return ResponseEntity.status(400).eTag("Wallet didn't find ...").body(null);
        }

        return ResponseEntity.ok(findedWallet);
    }

    @PostMapping("/wallet")
    public ResponseEntity<Wallets> addWallets(@RequestHeader(value="${json.token}") String token, @RequestBody Wallets walletDetails) {

        // check request
        if (walletDetails == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        if (walletDetails.getWalletName() == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        // check token
        Users user = jsonWebToken.getUserInformationFromToken(token);
        if(user == null) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }
        try {
            user = usersDao.findUserByEmail(user);
            if (user == null) {
                return ResponseEntity.status(400).eTag("Bad token ...").body(null);
            }
            if (!(user.getIsAdmin() || user.getEmail().equals(walletDetails.getUserId()))) {
                return ResponseEntity.status(400).eTag("Not authorized ...").body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }
        //
        Wallets findedWallet = walletsDao.findWalletByWalletNameAndUserId(walletDetails);
        if (findedWallet == null) {
            if (walletDetails.getCryptocurrenciesList() == null) {
                walletDetails.setCryptocurrenciesList(new ArrayList<>());
            }

            Wallets addedWallet = walletsDao.addWallet(walletDetails);
            if (addedWallet == null) {
                return ResponseEntity.status(500).eTag("Internal server error ...").body(null);
            }

            return ResponseEntity.ok(addedWallet);
        }
        return ResponseEntity.status(400).eTag("Wallet already exist ...").body(null);
    }

    @PutMapping("/wallet")
    public ResponseEntity<Wallets> updateWallets(@RequestHeader(value="${json.token}") String token, @RequestBody Wallets walletDetails) {

        // check request
        if (walletDetails == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        if (walletDetails.getWalletName() == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        // check token
        Users user = jsonWebToken.getUserInformationFromToken(token);
        if(user == null) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }
        try {
            user = usersDao.findUserByEmail(user);
            if (user == null) {
                return ResponseEntity.status(400).eTag("Bad token ...").body(null);
            }
            if (!(user.getIsAdmin() || user.getEmail().equals(walletDetails.getUserId()))) {
                return ResponseEntity.status(400).eTag("Not authorized ...").body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }
        //

        Wallets findedWallet = walletsDao.findWalletByWalletNameAndUserId(walletDetails);




        if (findedWallet != null) {
            findedWallet.setWalletDescription(walletDetails.getWalletDescription());
            findedWallet.setWalletName(walletDetails.getWalletName());

            Wallets updatedWallet = walletsDao.updateWallet(walletDetails);
            if (updatedWallet == null) {
                return ResponseEntity.status(500).eTag("Internal server error ...").body(null);
            }

            return ResponseEntity.ok(updatedWallet);
        }
        return ResponseEntity.status(400).eTag("Wallet didn't find ...").body(null);

    }

    @DeleteMapping("/wallet")
    public ResponseEntity<String> deleteWallet(@RequestHeader(value="${json.token}") String token, @RequestBody Wallets walletDetails) {

        // check request
        if (walletDetails == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body("Bad request ...");
        }
        if (walletDetails.getWalletName() == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body("Bad request ...");
        }
        // check token
        Users user = jsonWebToken.getUserInformationFromToken(token);
        if(user == null) {
            return ResponseEntity.status(400).eTag("Bad token ...").body("Bad token ...");
        }
        try {
            user = usersDao.findUserByEmail(user);
            if (user == null) {
                return ResponseEntity.status(400).eTag("Bad token ...").body("Bad token ...");
            }
            if (!(user.getIsAdmin() || user.getEmail().equals(walletDetails.getUserId()))) {
                return ResponseEntity.status(400).eTag("Not authorized ...").body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).eTag("Bad token ...").body("Bad token ...");
        }
        //
        Wallets deletedWallet = walletsDao.deleteWallet(walletDetails);
        if (deletedWallet != null) {
            return ResponseEntity.ok(walletDetails.getWalletName());
        }
        return ResponseEntity.status(400).eTag("Wallet didn't find ...").body("Wallet didn't find ...");


    }

    @PostMapping("/wallet/crypto")
    public ResponseEntity<Wallets> addCryptoToWallet(@RequestHeader(name="${json.token}") String token,
                                                      @RequestBody CryptoInWalletRequest cryptoDetails) {


        // check request
        if (cryptoDetails == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        if (cryptoDetails.getCryptoName() == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        Wallets searchWallet = new Wallets();
        searchWallet.setWalletName(cryptoDetails.getWalletName());
        searchWallet.setUserId(cryptoDetails.getUserId());
        Wallets wallet = walletsDao.findWalletByWalletNameAndUserId(searchWallet);
        if (wallet == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        // check token
        Users user = jsonWebToken.getUserInformationFromToken(token);
        if(user == null) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }
        try {
            user = usersDao.findUserByEmail(user);
            if (user == null) {
                return ResponseEntity.status(400).eTag("Bad token ...").body(null);
            }
            if (!(user.getIsAdmin() || user.getEmail().equals(wallet.getUserId()))) {
                return ResponseEntity.status(400).eTag("Not authorized ...").body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }
        //******************************************************************************
        List<CryptoCurrencies> cryptocurrencyList = cryptoCurrenciesDao.findAll();


        if (cryptocurrencyList == null) {
            return ResponseEntity.status(400).eTag("Crypto + is not in the available cryptocurrencies list").body(null);
        }
        if (cryptocurrencyList.isEmpty()) {
            return ResponseEntity.status(400).eTag("Crypto + is not in the available cryptocurrencies list").body(null);
        }
        boolean isInAvailable = false;
        CryptoCurrencies availableCrypto = new CryptoCurrencies();
        for (int i = 0; i < cryptocurrencyList.size(); i++) {
            availableCrypto = cryptocurrencyList.get(i);
            if (availableCrypto.getCryptoName().equals(cryptoDetails.getCryptoType())) {
                isInAvailable = true;
                break;
            }
        }

        if (!isInAvailable) {
            return ResponseEntity.status(400).eTag("Crypto is not in the available cryptocurrencies list").body(null);
        }

        List<CryptoCurrenciesModel> cryptocurrencyInWallet = wallet.getCryptocurrenciesList();
        if (cryptocurrencyInWallet == null) {
            cryptocurrencyInWallet = new ArrayList<>();
        }
        boolean isInWallet = false;
        for (int i = 0; i < cryptocurrencyInWallet.size(); i++) {
            if (cryptocurrencyInWallet.get(i).getCryptoName().equals(cryptoDetails.getCryptoName())) {
                isInWallet = true;
                break;
            }
        }

        if (isInWallet) {
            return ResponseEntity.status(400).eTag("Crypto is already in wallet").body(null);
        }
        cryptoDetails.setCryptoCost(cryptoDetails.getCryptoAmount() * availableCrypto.getCryptoCost());
        CryptoCurrenciesModel cryptoModel = new CryptoCurrenciesModel(cryptoDetails.getCryptoName(), cryptoDetails.getCryptoType(),
                cryptoDetails.getImage(), cryptoDetails.getImageUrl(),cryptoDetails.getCryptoDescription(), cryptoDetails.getCryptoAmount(),
                cryptoDetails.getCryptoCost());
        cryptocurrencyInWallet.add(cryptoModel);
        wallet.setCryptosCount((double) cryptocurrencyInWallet.size());
        wallet.setCryptosCost(wallet.getCryptosCost() + cryptoDetails.getCryptoCost());
        wallet.setCryptocurrenciesList(cryptocurrencyInWallet);



        Wallets walletAfterAdd = walletsDao.updateWallet(wallet);
        if (walletAfterAdd == null) {
            return ResponseEntity.status(500).eTag("Internal server error").body(null);
        }

        List<Wallets> wallets = walletsDao.findWalletByUserId(wallet.getUserId());
        updateAllWallets(wallets, cryptocurrencyList);



        walletAfterAdd = walletsDao.findWalletByWalletNameAndUserId(wallet);

        if (walletAfterAdd == null) {
            return ResponseEntity.status(500).eTag("Internal server error").body(null);
        }




        return ResponseEntity.ok(walletAfterAdd);

    }

    @PutMapping("/wallet/crypto")
    public ResponseEntity<Wallets> updateCryptoInWallet(@RequestHeader(value="${json.token}") String token,
                                                        @RequestBody CryptoInWalletRequest cryptoDetails) {
        // check request
        if (cryptoDetails == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        if (cryptoDetails.getCryptoName() == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        Wallets searchWallet = new Wallets();
        searchWallet.setWalletName(cryptoDetails.getWalletName());
        searchWallet.setUserId(cryptoDetails.getUserId());
        Wallets wallet = walletsDao.findWalletByWalletNameAndUserId(searchWallet);
        if (wallet == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        // check token
        Users user = jsonWebToken.getUserInformationFromToken(token);
        if(user == null) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }
        try {
            user = usersDao.findUserByEmail(user);
            if (user == null) {
                return ResponseEntity.status(400).eTag("Bad token ...").body(null);
            }
            if (!(user.getIsAdmin() || user.getEmail().equals(wallet.getUserId()))) {
                return ResponseEntity.status(400).eTag("Not authorized ...").body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }
        //******************************************************************************
        List<CryptoCurrencies> cryptocurrencyList = cryptoCurrenciesDao.findAll();


        boolean isInAvailable = false;
        CryptoCurrencies availableCrypto = new CryptoCurrencies();
        if (cryptocurrencyList != null) {
            if (!cryptocurrencyList.isEmpty()) {

                for (int i = 0; i < cryptocurrencyList.size(); i++) {
                    availableCrypto = cryptocurrencyList.get(i);
                    if (availableCrypto.getCryptoName().equals(cryptoDetails.getCryptoType())) {
                        isInAvailable = true;
                        break;
                    }
                }

            }
        }

        List<CryptoCurrenciesModel> cryptocurrencyInWallet = wallet.getCryptocurrenciesList();
        if (cryptocurrencyInWallet == null) {
            cryptocurrencyInWallet = new ArrayList<>();
        }
        int index = -1;
        for (int i = 0; i < cryptocurrencyInWallet.size(); i++) {
            if (cryptocurrencyInWallet.get(i).getCryptoName().equals(cryptoDetails.getCryptoName())) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            return ResponseEntity.status(400).eTag("Crypto is not in a wallet").body(null);
        }

        double previosCost = cryptocurrencyInWallet.get(index).getCryptoCost();
        if (isInAvailable) {
            cryptoDetails.setCryptoCost(cryptoDetails.getCryptoAmount() * availableCrypto.getCryptoCost());
        } else {
            cryptoDetails.setCryptoCost(cryptoDetails.getCryptoAmount() * cryptoDetails.getCryptoCost());
        }
        CryptoCurrenciesModel cryptoModel = new CryptoCurrenciesModel(cryptoDetails.getCryptoName(), cryptoDetails.getCryptoType(),
                cryptoDetails.getImage(), cryptoDetails.getImageUrl(),cryptoDetails.getCryptoDescription(), cryptoDetails.getCryptoAmount(),
                cryptoDetails.getCryptoCost());
        cryptocurrencyInWallet.set(index,cryptoModel);
        wallet.setCryptosCount((double) cryptocurrencyInWallet.size());
        wallet.setCryptosCost(wallet.getCryptosCost() - previosCost + cryptoDetails.getCryptoCost());
        wallet.setCryptocurrenciesList(cryptocurrencyInWallet);

        Wallets walletAfterUpdate = walletsDao.updateWallet(wallet);
        if (walletAfterUpdate == null) {
            return ResponseEntity.status(500).eTag("Internal server error").body(null);
        }

        List<Wallets> wallets = walletsDao.findWalletByUserId(wallet.getUserId());
        updateAllWallets(wallets, cryptocurrencyList);

        walletAfterUpdate = walletsDao.findWalletByWalletNameAndUserId(wallet);

        if (walletAfterUpdate == null) {
            return ResponseEntity.status(500).eTag("Internal server error").body(null);
        }


        return ResponseEntity.ok(walletAfterUpdate);

    }

    @DeleteMapping("/wallet/crypto")
    public ResponseEntity<Wallets> deleteCryptoInWallet(@RequestHeader(value="${json.token}") String token,
                                                        @RequestBody CryptoInWalletRequest cryptoDetails) {
        // check request
        if (cryptoDetails == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        if (cryptoDetails.getCryptoName() == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        Wallets searchWallet = new Wallets();
        searchWallet.setWalletName(cryptoDetails.getWalletName());
        searchWallet.setUserId(cryptoDetails.getUserId());
        Wallets wallet = walletsDao.findWalletByWalletNameAndUserId(searchWallet);
        if (wallet == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        // check token
        Users user = jsonWebToken.getUserInformationFromToken(token);
        if(user == null) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }
        try {
            user = usersDao.findUserByEmail(user);
            if (user == null) {
                return ResponseEntity.status(400).eTag("Bad token ...").body(null);
            }
            if (!(user.getIsAdmin() || user.getEmail().equals(wallet.getUserId()))) {
                return ResponseEntity.status(400).eTag("Not authorized ...").body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }
        //******************************************************************************
        List<CryptoCurrencies> cryptocurrencyList = cryptoCurrenciesDao.findAll();

        List<CryptoCurrenciesModel> cryptocurrencyInWallet = wallet.getCryptocurrenciesList();
        if (cryptocurrencyInWallet == null) {
            return ResponseEntity.status(400).eTag("Crypto is not in a wallet").body(null);
        }
        if (cryptocurrencyInWallet.isEmpty()) {
            return ResponseEntity.status(400).eTag("Crypto is not in a wallet").body(null);
        }
        int index = -1;
        for (int i = 0; i < cryptocurrencyInWallet.size(); i++) {
            if (cryptocurrencyInWallet.get(i).getCryptoName().equals(cryptoDetails.getCryptoName())) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            return ResponseEntity.status(400).eTag("Crypto is not in a wallet").body(null);
        }

        double previosCost = cryptocurrencyInWallet.get(index).getCryptoCost();

        cryptocurrencyInWallet.remove(index);


        wallet.setCryptosCount((double) cryptocurrencyInWallet.size());
        wallet.setCryptosCost(wallet.getCryptosCost() - previosCost);
        wallet.setCryptocurrenciesList(cryptocurrencyInWallet);

        Wallets walletAfterUpdate = walletsDao.updateWallet(wallet);
        if (walletAfterUpdate == null) {
            return ResponseEntity.status(500).eTag("Internal server error").body(null);
        }

        List<Wallets> wallets = walletsDao.findWalletByUserId(wallet.getUserId());
        updateAllWallets(wallets, cryptocurrencyList);

        walletAfterUpdate = walletsDao.findWalletByWalletNameAndUserId(wallet);

        if (walletAfterUpdate == null) {
            return ResponseEntity.status(500).eTag("Internal server error").body(null);
        }


        return ResponseEntity.ok(walletAfterUpdate);

    }

    @PutMapping("/wallets")
    public ResponseEntity<List<Wallets>> updateCryptoInAllWallets(@RequestHeader(value="${json.token}") String token,
                                                        @PathVariable String walletId, @RequestBody Users userWallets) {
        // check request
        if (userWallets == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        if (userWallets.getEmail() == null) {
            return ResponseEntity.status(400).eTag("Bad request ...").body(null);
        }
        List<Wallets> wallets = walletsDao.findWalletByUserId(userWallets.getEmail());
        if (wallets == null) {
            return ResponseEntity.status(400).eTag("This user doesn't have wallets ...").body(null);
        }
        // check token
        Users user = jsonWebToken.getUserInformationFromToken(token);
        if(user == null) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }
        try {
            user = usersDao.findUserByEmail(user);
            if (user == null) {
                return ResponseEntity.status(400).eTag("Bad token ...").body(null);
            }
            if (!(user.getIsAdmin() || user.getEmail().equals(userWallets.getEmail()))) {
                return ResponseEntity.status(400).eTag("Not authorized ...").body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).eTag("Bad token ...").body(null);
        }
        //******************************************************************************
        List<CryptoCurrencies> cryptocurrencyList = cryptoCurrenciesDao.findAll();

        updateAllWallets(wallets, cryptocurrencyList);

        return ResponseEntity.ok(wallets);

    }

    // ***************************************************************
    public void updateAllWallets(List<Wallets> wallets, List<CryptoCurrencies> cryptocurrencyList) {
        Map<String, CryptoCurrencies> map = new HashMap<>();
        if (cryptocurrencyList != null) {
            if (!cryptocurrencyList.isEmpty()) {
                for (CryptoCurrencies crypto : cryptocurrencyList) {
                    map.put(crypto.getCryptoName(), crypto);
                }
                for (int j = 0; j < wallets.size(); j++) {
                    Wallets wallet = wallets.get(j);

                    List<CryptoCurrenciesModel> cryptos = wallet.getCryptocurrenciesList();
                    if (cryptos != null) {
                        if (!cryptos.isEmpty()) {
                            for (int i = 0; i < cryptos.size(); i++) {
                                CryptoCurrenciesModel crypto = cryptos.get(i);
                                if (map.containsKey(crypto.getCryptoType())) {
                                    CryptoCurrencies cryptoCurrencies = map.get(crypto.getCryptoType());
                                    crypto.setCryptoCost(crypto.getCryptoAmount() * cryptoCurrencies.getCryptoCost());
                                    cryptos.set(i, crypto);
                                }

                            }
                        } else {
                            wallet.setCryptosCount(0.0);
                            wallet.setCryptosCost(0.0);
                        }
                    } else {
                        wallet.setCryptosCount(0.0);
                        wallet.setCryptosCost(0.0);

                    }
                    wallet.setCryptocurrenciesList(cryptos);
                    wallets.set(j, wallet);
                    walletsDao.updateWallet(wallet);

                }
            }
        }
    }

}
