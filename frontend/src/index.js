import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import {configureStore} from "@reduxjs/toolkit";
import {Provider} from "react-redux";

import authReducer, {loadUser} from "./templates/slices/AuthSlice";
import WalletsReducer from './templates/slices/WalletsSlice';
import CryptosReducer from './templates/slices/CryptoCurrenciesSlice';


const store = configureStore({
  reducer: {
    auth: authReducer,
    wallets: WalletsReducer,
    cryptos: CryptosReducer,
  },

});

store.dispatch(loadUser(null));

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <Provider store={store}>
      <App />
    </Provider>
  </React.StrictMode>
);


