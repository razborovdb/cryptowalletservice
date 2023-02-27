import {createSlice, createAsyncThunk} from "@reduxjs/toolkit";
import axios from "axios";
import { url, setHeaders } from "./api";
import { toast } from "react-toastify";


const initialState = {
    wallets: [],
    wallet: null,
    status: null,
    error: null,
    statusGetOne: null,
    errorGetOne: null,
    createStatus: null,
    createError: null,
    deleteStatus: null,
    deleteError: null,
    editStatus: null,
    editError: null,
    createCryptoStatus: null,
    createCryptoError: null,
    deleteCryptoStatus: null,
    deleteCryptoError: null,
    editCryptoStatus: null,
    editCryptoError: null,
};


export const walletsFetch = createAsyncThunk(
  "wallets/walletsFetch",
  async (values, {rejectWithValue}) => {

      try {
            const response = await axios.get(
                `${url}/wallets`, 
              {                 
                headers: setHeaders(values.token),
                params: {
                  email: values.userId,
              },
            });

         // localStorage.setItem("token", token.data);

          return response.data;
      } catch(err) {

          return rejectWithValue(err.response.data);
      }
  }
);

export const walletsGetOne = createAsyncThunk(
    "wallets/walletsGetOne",
    async (values, {rejectWithValue}) => {
        

        try {
              const response = await axios.get(
                  `${url}/wallet`, 
                {                 
                  headers: setHeaders(values.token),
                  params: {
                    email: values.userId,
                    walletName: values.walletName,
                },
              });
  
           // localStorage.setItem("token", token.data);
  
            return response.data;
        } catch(err) {
  
            return rejectWithValue(err.response.data);
        }
    }
  );

export const walletsCreate = createAsyncThunk(
    
    "wallets/walletsCreate",
    async (values, {rejectWithValue}) => {
  
      try {
        const response = await axios.post(
            `${url}/wallet`,
            {
                "userId": values.userId,
                "walletName": values.walletName,
                "walletDescription": values.walletDescription,
                "cryptosCount": values.cryptosCount,
                "cryptosCost": values.cryptosCost,
                "cryptocurrenciesList": values.cryptocurrenciesList,
              },
              {
                headers: setHeaders(values.token)
            }
        );

        return response.data;
      } catch (error) {
        return rejectWithValue(err.response.data);

      }
    }
  );

  export const walletsCreateCrypto = createAsyncThunk(
    
    "wallets/walletsCreateCrypto",
    async (values, {rejectWithValue}) => {
  

      try {
        const response = await axios.post(
            `${url}/wallet/crypto`,
            {
                userId: values.userId,
                walletName: values.walletName,
                cryptoName: values.cryptoName,
                cryptoType: values.cryptoType,
                image: values.image,
                imageUrl: values.imageUrl,
                cryptoDescription: values.cryptoDescription,
                cryptoAmount: values.cryptoAmount,
                cryptoCost: values.cryptoCost,                
            },

            {
                headers: setHeaders(values.token)
            }
        );

        return response.data;
      } catch (error) {
        return rejectWithValue(err.response.data);

      }
    }
  );

  export const walletsUpdateCrypto = createAsyncThunk(
    
    "wallets/walletsUpdateCrypto",
    async (values, {rejectWithValue}) => {
  

      try {
        const response = await axios.put(
            `${url}/wallet/crypto`,
            {
                userId: values.userId,
                walletName: values.walletName,
                cryptoName: values.cryptoName,
                cryptoType: values.cryptoType,
                image: values.image,
                imageUrl: values.imageUrl,
                cryptoDescription: values.cryptoDescription,
                cryptoAmount: values.cryptoAmount,
                cryptoCost: values.cryptoCost,                
            },

            {
                headers: setHeaders(values.token)
            }
        );

        return response.data;
      } catch (error) {
        return rejectWithValue(err.response.data);

      }
    }
  );

  export const walletsDelete = createAsyncThunk(
    
    "wallets/walletsDelete",
    async (values, {rejectWithValue}) => {
        
      try {
        const response = await axios.delete(
            `${url}/wallet`, {
                data: {
                    userId: values.userId,
                    walletName: values.walletName,
                    cryptosCount: 0.0,
                    cryptosCost: 0.0,
                    cryptocurrenciesList: [],
                    
                },
                headers: setHeaders(values.token),
            },
        );
        toast.warning("Wallet Deleted");
        return response.data;
      } catch (error) {
        return rejectWithValue(err.response.data);

      }
    }
  );

  export const walletsDeleteCrypto = createAsyncThunk(
    
    "wallets/walletsDeleteCrypto",
    async (values, {rejectWithValue}) => {
        
      try {
        const response = await axios.delete(
            `${url}/wallet/crypto`, {
                data: {
                    userId: values.userId,
                    walletName: values.walletName,
                    cryptoName: values.cryptoName,
                    image: "",
                    imageUrl: "",
                    cryptoDescription: "",
                    cryptoAmount: 0,
                    cryptoCost: 0,
                    
                },
                headers: setHeaders(values.token),
            },
        );
        toast.warning("Wallet Deleted");
        return response.data;
      } catch (error) {
        return rejectWithValue(err.response.data);

      }
    }
  );

  export const walletsEdit = createAsyncThunk(
    
    "wallets/walletsEdit",
    async (values, {rejectWithValue}) => {

      try {
        const response = await axios.put(
            `${url}/wallet`,
            {
                userId: values.userId,
                walletName: values.walletName,
                walletDescription: values.walletDescription,
                cryptosCount: values.cryptosCount,
                cryptosCost: values.cryptosCost,
                cryptocurrenciesList: values.cryptocurrenciesList,
              },
              {
                headers: setHeaders(values.token)
            }
        );
        
        return response.data;
      } catch (error) {
        return rejectWithValue(err.response.data);
      }
    }
  );

const walletsSlice = createSlice({
    name: "wallets",
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder.addCase(walletsFetch.pending, (state, action) =>  {

            return {...state, status: "pending"};
        });
        builder.addCase(walletsFetch.fulfilled, (state, action) => {

            return {
                ...state,
                wallets: action.payload,
                status: "success"
            };
            
        });
        builder.addCase(walletsFetch.rejected, (state, action) => {

            return {
                ...state,
                status: "rejected",
                error: action.payload,
            }
        });

        builder.addCase(walletsGetOne.pending, (state, action) =>  {

            return {...state, status: "pending"};
        });
        builder.addCase(walletsGetOne.fulfilled, (state, action) => {

            return {
                ...state,
                wallet: action.payload,
                statusGetOne: "success"
            };
            
        });
        builder.addCase(walletsGetOne.rejected, (state, action) => {

            return {
                ...state,
                statusGetOne: "rejected",
                errorGetOne: action.payload,
            }
        });

        builder.addCase(walletsCreate.pending, (state, action) =>  {

            return {...state, createStatus: "pending"};
        });
        builder.addCase(walletsCreate.fulfilled, (state, action) => {

            const newList = state.wallets.filter((wallet) => true);
            newList.push(action.payload);
            toast.success("Wallet: " + action.payload.walletName + " created", {
                position: "bottom-left",
            });
            return {
                ...state,
                wallets: newList,
                createStatus: "success"
            };
            
        });
        builder.addCase(walletsCreate.rejected, (state, action) => {

            return {
                ...state,
                createStatus: "rejected",
                createError: action.payload,
            }
        });
        builder.addCase(walletsDelete.pending, (state, action) =>  {
            return {...state, deleteStatus: "pending"};
        });
        builder.addCase(walletsDelete.fulfilled, (state, action) => {
            const newList = state.wallets.filter((wallet) => wallet.walletName !== action.payload);
            toast.success("Wallet: " + action.payload + " deleted", {
                position: "bottom-left",
            });
            return {
                ...state,
                wallets: newList,
                deleteStatus: "success"
            };
            
        });
        builder.addCase(walletsDelete.rejected, (state, action) => {
            return {
                ...state,
                deleteStatus: "rejected",
                deleteError: action.payload,
            }
        });

        builder.addCase(walletsCreateCrypto.pending, (state, action) =>  {

            return {...state, createCryptoStatus: "pending"};
        });
        builder.addCase(walletsCreateCrypto.fulfilled, (state, action) => {

            const newList = state.wallets.filter((wallet) => wallet.walletName !== action.payload.walletName);
            newList.push(action.payload);
            toast.success("Crypto added to wallet: " + action.payload.walletName, {
                position: "bottom-left",
            });
            return {
                ...state,
                // wallets: newList,
                createCryptoStatus: "success"
            };
            
        });
        builder.addCase(walletsCreateCrypto.rejected, (state, action) => {

            return {
                ...state,
                createCryptoStatus: "rejected",
                createCryptoError: action.payload,
            }
        });

        builder.addCase(walletsUpdateCrypto.pending, (state, action) =>  {

            return {...state, editCryptoStatus: "pending"};
        });
        builder.addCase(walletsUpdateCrypto.fulfilled, (state, action) => {

            const newList = state.wallets.filter((wallet) => wallet.walletName !== action.payload.walletName);

            newList.push(action.payload);
            toast.success("Crypto updated in wallet: " + action.payload.walletName, {
                position: "bottom-left",
            });
            return {
                ...state,
                wallets: newList,
                editCryptoStatus: "success"
            };
            
        });
        builder.addCase(walletsUpdateCrypto.rejected, (state, action) => {

            return {
                ...state,
                editCryptoStatus: "rejected",
                editCryptoError: action.payload,
            }
        });


        builder.addCase(walletsDeleteCrypto.pending, (state, action) =>  {

            return {...state, deleteCryptoStatus: "pending"};
        });
        builder.addCase(walletsDeleteCrypto.fulfilled, (state, action) => {

            const newList = state.wallets.filter((wallet) => wallet.walletName !== action.payload.walletName);
                newList.push(action.payload);
                toast.success("Crypto deleted in wallet: " + action.payload.walletName, {
                    position: "bottom-left",
                });
            return {
                ...state,
                wallets: newList,
                deleteCryptoStatus: "success"
            };
            
        });
        builder.addCase(walletsDeleteCrypto.rejected, (state, action) => {

            return {
                ...state,
                deleteCryptoStatus: "rejected",
                deleteCryptoError: action.payload,
            }
        });




        builder.addCase(walletsEdit.pending, (state, action) =>  {
            return {...state, editStatus: "pending"};
        });
        builder.addCase(walletsEdit.fulfilled, (state, action) => {


            const newList = state.wallets.filter((wallet) => wallet.walletName !== action.payload.walletName);
                newList.push(action.payload);
                toast.success("Wallet: " + action.payload.walletName + " updated", {
                    position: "bottom-left",
                });
            return {
                ...state,
                wallets: newList,
                editStatus: "success"
            };
            
        });
        builder.addCase(walletsEdit.rejected, (state, action) => {
            return {
                ...state,
                editStatus: "rejected",
                editError: action.payload,
            }
        });
    },
});

export default walletsSlice.reducer;