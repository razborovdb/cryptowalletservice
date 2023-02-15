import './App.css';
import "react-toastify/dist/ReactToastify.css";

import {ToastContainer} from "react-toastify";
import {BrowserRouter, Routes, Route, Navigate} from "react-router-dom";
import NavBarTemplate from './templates/NavBarTemplate';
import HomeTemplate from './templates/HomeTemplate';
import NotFoundTemplate from './templates/NotFoundTemplate';
import PanelTemplate from './templates/panel/PanelTemplate';
import LoginTemplate from './templates/auth/LoginTemplate';
import RegisterTemplate from './templates/auth/RegisterTemplate';
import UserInfoTemplate from './templates/panel/UserInfoTemplate';
import WalletsTemplate from './templates/panel/WalletsTemplate';
import WalletsListTemplate from './templates/panel/lists/WalletsListTemplate';
import AddWalletTemplate from './templates/panel/AddWalletTemplate';
import EditWalletTemplate from './templates/panel/EditWalletTemplate';
import CreateCryptoInWalletTemplate from './templates/panel/CreateCryptoInWalletTemplate';
import WalletTemplate from './templates/panel/details/WalletTemplate';
import CryptosTemplate from './templates/panel/CryptosTemplate';
import CreateCryptosTemplate from './templates/panel/CreateCryptosTemplate';
import CryptosListTemplate from './templates/panel/lists/CryptosListTemplate';
import CryptoTemplate from './templates/panel/details/CryptoTemplate';
import ViewCryptoInWalletTemplate from './templates/panel/ViewCryptoInWalletTemplate';
import EditCryptoInWalletTemplate from './templates/panel/EditCryptoInWalletTemplate';
import EditUserTemplate from './templates/panel/EditUserTemplate';


function App() {
  return (
    <div className="App">
      <BrowserRouter>
      <ToastContainer/>
        <NavBarTemplate/>
        <Routes>
          <Route path="/panel" element={<PanelTemplate />}>
            <Route path="userinfo" element={<UserInfoTemplate/>}/>
            <Route path="wallets" element={<WalletsTemplate />}>
              <Route index element={<WalletsListTemplate/>} />
              <Route path="add-wallet" element={<AddWalletTemplate />}/>             
            </Route>            
            <Route path="cryptos" element={<CryptosTemplate/>}>
              <Route index element={<CryptosListTemplate/>} />
              <Route path="create-crypto" element={<CreateCryptosTemplate />}/>
            </Route>
          
          </Route>
          <Route path="/wallet/:walletName" element={<WalletTemplate />}/>
          <Route path="/edit-user" element={<EditUserTemplate />}/>
          <Route path="/wallet/create-crypto/:walletName" element={<CreateCryptoInWalletTemplate />}/>
          <Route path="/wallet/view-crypto/:walletName/:cryptoName" element={<ViewCryptoInWalletTemplate />}/>
          <Route path="/wallet/edit-crypto-wallet/:walletName/:cryptoName" element={<EditCryptoInWalletTemplate />}/>
          <Route path="/edit-wallet/:walletName" element={<EditWalletTemplate />}/>
          <Route path="/crypto/:id" element={<CryptoTemplate />}/>
          <Route path="/login" exact element={<LoginTemplate />} />
          <Route path="/register" exact element={<RegisterTemplate />} />
          <Route path="/" exact element={<HomeTemplate />} />
          <Route path="/not-found" element={< NotFoundTemplate/>}/>
          <Route path="*" element={<Navigate replace to="/not-found" />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
