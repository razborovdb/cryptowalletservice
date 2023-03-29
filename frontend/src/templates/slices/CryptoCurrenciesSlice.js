import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import axios from "axios";
import {setHeaders, url} from "./api";
import jwtDecode from "jwt-decode";
import {toast} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';


const initialState = {
    cryptos: [],
    status: "",
    error: "",
    createStatus: "",
    createError: "",
    deleteStatus: "",
    deleteError: "",
    updateStatus: "",
    updateError: "",
};

export const getAllCryptos = createAsyncThunk(
    "cryptos/getAllCryptos",
    async (values, {rejectWithValue}) => {
        try {

            const cryptosList = await axios.get(`${url}/cryptos`, 
            {
              
                headers: setHeaders(values.token)
            }
            );

            return cryptosList?.data;
        } catch(err) {
            return rejectWithValue(err.response.data);
        }
    }
);

export const updateAllCryptos = createAsyncThunk(
  "cryptos/updateAllCryptos",
  async (values, { rejectWithValue }) => {

    console.log("here");
    
    try {

      const cryptosList = await axios.post(`${url}/cryptos`,
        {

        } ,    
        {

          headers: setHeaders(values.token)
        }
      );


      return cryptosList?.data;
    } catch (err) {
      console.log("----------------------error----------------");
      console.log(err);
      return rejectWithValue(err.response.data);
    }
  }
);

export const cryptosDelete = createAsyncThunk(
    
    "cryptos/cryptosDelete",
    async (values, {rejectWithValue}) => {
        
      try {
        const response = await axios.delete(
            `${url}/crypto`, 

          //setHeaders(values.token),
          {
            data: {
                cryptoName: values.cryptoName,
                image: values.image,
                imageUrl: values.imageUrl,
                cryptoDescription: "",
                cryptoAmount: 0,
                cryptoCost: 0,
            },
            headers: setHeaders(values.token),
        },
        );

        return values.cryptoName;
      } catch (error) {
        return rejectWithValue(err.response.data);
      }
    }
  );

  export const cryptosCreate = createAsyncThunk(
    
    "cryptos/cryptosCreate",
    async (values, {rejectWithValue}) => {
      
      try {
        const response = await axios.post(
            `${url}/crypto`,
          {
            "cryptoName": values.name,
            "image": values.image,
            "imageUrl": values.imageUrl,
            "cryptoDescription": values.desc,
            "cryptoAmount": values.amount,
            "cryptoCost": values.cost,
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

  export const cryptosUpdate = createAsyncThunk(
    
    "cryptos/cryptosUpdate",
    async (values, {rejectWithValue}) => {
      
      try {

        const response = await axios.put(
            `${url}/crypto`,
            {
              "cryptoName": values.name,
              "image": values.image,
              "imageUrl": values.imageUrl,
              "cryptoDescription": values.desc,
              "cryptoAmount": values.amount,
              "cryptoCost": values.cost,
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


const cryptosSlice = createSlice({
    name: "cryptos",
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder.addCase(getAllCryptos.pending, (state, action) =>  {

            return {...state, status: "pending"};
        });
        builder.addCase(getAllCryptos.fulfilled, (state, action) => {

            return {
                ...state,
                cryptos: action.payload,
                status: "success"
            };
            
        });
        builder.addCase(getAllCryptos.rejected, (state, action) => {


            return {
                ...state,
                status: "rejected",
                error: action.payload,
            }
        });
        builder.addCase(updateAllCryptos.pending, (state, action) => {

          return { ...state, status: "pending" };
        });
        builder.addCase(updateAllCryptos.fulfilled, (state, action) => {
          toast.success("Cryptos updated", {
            position: "bottom-left",
          });
    
          return {
            ...state,
            cryptos: action.payload,
            status: "success"
          };
    
        });
        builder.addCase(updateAllCryptos.rejected, (state, action) => {
    
    
          return {
            ...state,
            status: "rejected",
            error: action.payload,
          }
        });
        builder.addCase(cryptosDelete.pending, (state, action) =>  {
            return {...state, deleteStatus: "pending"};
        });
        builder.addCase(cryptosDelete.fulfilled, (state, action) => {
            const newList = state.cryptos.filter((crypto) => crypto.cryptoName !== action.payload);
            toast.success("Crypto: " + action.payload + " deleted", {
                position: "bottom-left",
            });
            return {
                ...state,
                cryptos: newList,
                deleteStatus: "success"
            };
            
        });
        builder.addCase(cryptosDelete.rejected, (state, action) => {
            return {
                ...state,
                deleteStatus: "rejected",
                deleteError: action.payload,
            }
        });
        builder.addCase(cryptosCreate.pending, (state, action) =>  {

            return {...state, createStatus: "pending"};
        });
        builder.addCase(cryptosCreate.fulfilled, (state, action) => {

            const newList = state.cryptos.filter((crypto) => true);
            newList.push(action.payload);
            return {
                ...state,
                cryptos: newList,
                createStatus: "success"
            };
            
        });
        builder.addCase(cryptosCreate.rejected, (state, action) => {

            return {
                ...state,
                createStatus: "rejected",
                createError: action.payload,
            }
        });

        builder.addCase(cryptosUpdate.pending, (state, action) =>  {

            return {...state, updateStatus: "pending"};
        });
        builder.addCase(cryptosUpdate.fulfilled, (state, action) => {

            const newList = state.cryptos.filter((crypto) => crypto.cryptoName !== action.payload.cryptoName);
            newList.push(action.payload);
            return {
                ...state,
                cryptos: newList,
                updateStatus: "success"
            };
            
        });
        builder.addCase(cryptosUpdate.rejected, (state, action) => {

            return {
                ...state,
                updateStatus: "rejected",
                updateError: action.payload,
            }
        });
    }
});

export default cryptosSlice.reducer;