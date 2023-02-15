# Cryptocurrency Wallet Service Design

## Description
This service provides:

* Creating, retrieving, and updating cryptocurrency wallets
* Retrieving all customer wallets in the list 

This project uses authorization to validate users.

This project uses Cassandra as Database, Spring Boot as Server and RESET controller, Node JS and React as frontend.


## 1. Problem Statement

Today, many people have cryptocurrencies as an asset. At the same time,
the number of types of cryptocurrencies that they can own may be in the tens.
They can mine, buy and exchange cryptocurrencies and also store cryptocurrencies
on different cryptocurrency exchanges. Users want to have information about the assets
that they store in cryptocurrencies in one place.
Users also want to distribute their assets to different wallets in order
to accumulate the necessary assets for different projects.

This design document describes the Cryptocurrency Wallet Service, a new service 
that will provide the custom wallet functionality to meet customers' needs. 
It is designed to interact with the Cryptocurrency Wallet Service client,
which allows customers to get information about their wallets to their
phone or computer.


## 2. Top Questions to Resolve in Review

1. Will we have a common cryptocurrency exchange rate table?
2. Will we have a cryptocurrency table, which will store the list of available cryptocurrencies
in The Cryptocurrency Wallet Service?
3. How will we update cryptocurrencies' exchange rate?

## 3. Use Cases

U1. As a customer, I want to create a new, empty wallet with a given name and
a description.

U2. As a customer, I want to retrieve all my wallets list.

U3. As a customer, I want to retrieve my wallet with a given name.

U4. As a customer, I want to update my wallet description.

U5. As a customer, I want to add cryptocurrency to my wallet.

U6. As a customer, I want to update a cryptocurrency amount in my wallet.

U7. As a customer, I want to update the cryptocurrency exchange rate in all my wallets.

U8. As a customer, I want to retrieve all available cryptocurrencies that I can use
in my wallet.

## 4. Project Scope

### 4.1. In Scope

* Creating, retrieving, and updating a wallet
* Retrieving all customer wallets in the list
* Retrieving all available cryptocurrencies that we can use in our wallet

### 4.2. Out of Scope

* Updating cryptocurrencies exchange rate from external API (at this stage, 
the available cryptocurrencies list and the cryptocurrencies exchange rate will be updated 
by the system administrator). 

# 5. Proposed Architecture Overview

This initial iteration will provide the minimum lovable product (MLP) including:
* Creating, retrieving, and updating a wallet
* Retrieving all customer wallets in the list
* Retrieving all available cryptocurrencies that we can use in our wallet

We will use API Gateway and Lambda to create nine endpoints (`GetAllWallets`, `GetWallet`,
`CreateWallet`, `UpdateWallet`, `AddCryptocurrencyToWallet`, `UpdateCryptocurrencyInWallet`, 
`UpdateExchangeRateInWallets`, and `GetAvailableCryptocurrencies`) that will handle the creation, update, 
and retrieval of wallets to satisfy our requirements.

We will store available cryptocurrencies that we can use in our wallet in a table in DynamoDB. Wallets
themselves will also be stored in DynamoDB. For simpler cryptocurrency list retrieval, we
will store the list of cryptocurrencies in a given wallet directly in the wallet
table.

Cryptocurrency Wallet Service will also provide a web interface for users to manage their wallets. 
A home page providing a list view of all of customer wallets will let them create new wallets and link off 
to pages, per-wallet to update metadata, add cryptocurrencies and change their amount in the wallet.

# 6. API

## 6.1. Public Models

```
// UsersModel

String email;
String name;
String avatar;
String avatarUrl;
String password;
String role;
Boolean isAdmin;
```

```
// CryptoCurrenciesModel

String cryptoName;
String cryptoType;
String image;
String imageUrl;
String cryptoDescription;
Double cryptoAmount;
Double cryptoCost;
```

```
// WalletModel

String userId;
String walletName;
String walletDescription;
Double cryptosCount;
Double cryptosCost;
List   CryptoCurrenciesModel;
```

```
// ProjectsModel

String projectName;
String image;
String imageUrl;
String projectDescription;
Double projectCost;
```

```
// PaymentsModel

String id;
Double payment;
String paymentDescription;
String userId;
String projectId;
Double paymentDate;
```
----------------------------------------------------
## 6.2. RegisterUser Endpoint

* Accepts `POST` requests to `/user/register`
* Accepts a User with all parameters and returns token.
  * If the given email in User is found, will return an error.
  * If user created, return token.

## 6.3. LoginUser Endpoint

* Accepts `POST` requests to `/user/login`
* Accepts a User with email and returns token.
  * If the User with given email is not found, will return an error.
  * If User is found, return token.

## 6.4. GetAvailableCryptocurrencies Endpoint

* Accepts `GET` requests to `/api/cryptos`
* Accepts token and returns the list of available cryptocurrencies.

## 6.4. DeleteCryptoInAvailableCryptocurrencies Endpoint

* Accepts `DELETE` requests to `/api/crypto`
* Accepts token and CryptoCurrencies with cryptoName returns cryptoName.

## 6.5. AddCryptoToAvailableCryptocurrencies Endpoint

* Accepts `POST` requests to `/api/crypto`
* Accepts token and CryptoCurrencies with all parameters returns added crypto.

## 6.6. UpdateCryptoInAvailableCryptocurrencies Endpoint

* Accepts `PUT` requests to `/api/crypto`
* Accepts token and CryptoCurrencies with cryptoName returns updated crypto.

## 6.7. GetCryptoFromAvailableCryptocurrencies Endpoint

* Accepts `GET` requests to `/api/crypto`
* Accepts token and cryptoName as request param, returns crypto.

## 6.8. GetUserWallets Endpoint

* Accepts `GET` requests to `/api/wallets`
* Accepts token and email as request param, returns user wallets list.

## 6.9. AddWallet Endpoint

* Accepts `POST` requests to `/api/wallet`
* Accepts token and Wallets with all parameters returns added wallet.


## 6.10. GetUserOneWallet Endpoint

* Accepts `GET` requests to `/api/wallet`
* Accepts token, email and walletName as request param, returns user wallet.

## 6.11. DeleteUserWallet Endpoint

* Accepts `DELETE` requests to `/api/wallet`
* Accepts token and Wallet with userId and walletName returns walletName.

## 6.12. AddCryptoToUserWallet Endpoint

* Accepts `POST` requests to `/api/wallet/crypto`
* Accepts token, walletName as path variable and CryptoCurrencies with all parameters returns CryptoCurrencies.

## 6.13. UpdateCryptoInUserWallet Endpoint

* Accepts `PUT` requests to `/api/wallet/crypto`
* Accepts token, walletName as path variable and CryptoCurrencies with all parameters returns CryptoCurrencies.

## 6.14. DeleteCryptoInUserWallet Endpoint

* Accepts `DELETE` requests to `/api/wallet/crypto`
* Accepts token, walletName as path variable and CryptoCurrencies with all parameters returns cryptoName.


## 6.15. GetUser Endpoint

* Accepts `GET` requests to `/api/user`
* Accepts token, email as path variable returns Users.


## 6.16. UpdateUser Endpoint

* Accepts `PUT` requests to `/api/user`
* Accepts token, Users as body returns Users.

## 6.17. GetProject Endpoint

* Accepts `GET` requests to `/api/project`
* Accepts no parameters returns Projects.

# 7. Tables

### 7.1. `users`

```
email // partition key, ordinal 0, text
name // text
avatar // text
avatarurl // text
password // text
role // text
isadmin // boolean
```

### 7.2. `cryptocurrency`

```
cryptoName // partition key, ordinal 0, text
image // text
imageurl // text
cryptodescription // text
cryptoamount // double
cryptocost // double
```

### 7.3. `wallets`

```
userid // partition key, ordinal 0, text
walletname // partition key, ordinal 1, text
walletdescription // text
cryptoscount // double
cryptoscost // double
cryptocurrencieslist // list, udt (cryptocurrenciesmodel)
```

### 7.4. `projects`

```
projectname // partition key, ordinal 0, text
image // text
imageurl // text
projectdescription // text
projectcost // double
```

### 7.5. `payments`

```
id // partition key, ordinal 0, text
payment // double
paymentdescription // text
userid // text
projectid // text
paymentdate // text
```
