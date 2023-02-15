import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import axios from "axios";
import jwtDecode from "jwt-decode";
import {setHeaders, url} from "./api";

const initialState = {
    token: "", //localStorage.getItem("token"),
    name: "",
    email: "",
    avatar: "",
    role: "",
    isAdmin: false,
    userLoaded: false,
    registerStatus: "",
    registerError: "",
    loginStatus: "",
    loginError: "",
    updateUserStatus: "",
    updateUserError: "",
};

export const registerUser = createAsyncThunk(
    "auth/registerUser",
    async (values, {rejectWithValue}) => {
        try {

            const token = await axios.post(`${url}/user/register`, {
                name:  values.name,
                email: values.email,
                password: values.password,
                avatar: values.avatar,
                avatarUrl: values.avatarUrl,
                role: values.role,
                isAdmin: false
            });

           // localStorage.setItem("token", token.data);

            return token.data;
        } catch(err) {
            return rejectWithValue(err.response.data);
        }
    }
);


export const loginUser = createAsyncThunk(
    "auth/loginUser",
    async (values, {rejectWithValue}) => {
        try {


            const token = await axios.post(`${url}/user/login`, {
                email: values.email,
                password: values.password
            });

           // localStorage.setItem("token", token.data);

            return token.data;
        } catch(err) {

            return rejectWithValue(err.response.data);
        }
    }
);


export const updateUser = createAsyncThunk(
    "auth/updateUser",
    async (values, {rejectWithValue}) => {
        console.log(values);
        try {


            const response = await axios.put(
                `${url}/user`,
                {
                    name:  values.name,
                    email: values.email,
                    password: values.password,
                    avatar: values.avatar,
                    avatarUrl: values.avatarUrl,
                    role: values.role,
                    isAdmin: values.isAdmin,
                },
                {
                  headers: setHeaders(values.token)
              }
              
            );

           // localStorage.setItem("token", token.data);
           console.log(response.data);

            return response.data;
        } catch(err) {
            console.log(err);
            return rejectWithValue(err.response.data);
        }
    }
);

const authSlice = createSlice({
    name: "auth",
    initialState,
    reducers: {
        loadUser(state, action) {            
            const token = state.token;

            if (token) {

                const user = jwtDecode(token);
                return {
                    ...state, token,
                    name: user.name,
                    email: user.email,
                    isAdmin: user.isAdmin,
                    avatar: user.avatar,
                    role: user.role,
                    userLoaded: true,
                };
            }

        },
        logoutUser(state, action) {
            //localStorage.removeItem("token");
            return {
                ...state,
                token: "",
                name: "",
                email: "",
                avatar: "",
                role: "",
                isAdmin: false,
                userLoaded: false,
                registerStatus: "",
                registerError: "",
                loginStatus: "",
                loginError: "",
            };
        }
    },
    extraReducers: (builder) => {
        builder.addCase(registerUser.pending, (state, action) => {
            return {...state, registerStatus: "pending"};
        });
        builder.addCase(registerUser.fulfilled, (state, action) => {
            if (action.payload) {
                const user = jwtDecode(action.payload);


                return {
                    ...state,
                    token: action.payload,
                    name: user.name,
                    email: user.email,
                    isAdmin: user.isAdmin,
                    avatar: user.avatar,
                    role: user.role,
                    userLoaded: true,
                    registerStatus: "success"
                }
            } else {
                return state;
            };
        });
        builder.addCase(registerUser.rejected, (state, action) => {
            return {
                ...state,
                registerStatus: "rejected",
                registerError: action.payload,
            }
        });
        builder.addCase(loginUser.pending, (state, action) => {

            return {...state, loginStatus: "pending"};
        });
        builder.addCase(loginUser.fulfilled, (state, action) => {
            if (action.payload) {
                const user = jwtDecode(action.payload);


                return {
                    ...state,
                    token: action.payload,
                    name: user.name,
                    email: user.email,
                    isAdmin: user.isAdmin,
                    avatar: user.avatar,
                    role: user.role,
                    userLoaded: true,
                    loginStatus: "success"
                }
            } else {
                return state;
            };
        });
        builder.addCase(loginUser.rejected, (state, action) => {

            return {
                ...state,
                loginStatus: "rejected",
                loginError: action.payload,
            }
        });


        builder.addCase(updateUser.pending, (state, action) => {
            console.log("start");
            return {...state, updateUserStatus: "pending"};
        });
        builder.addCase(updateUser.fulfilled, (state, action) => {
            console.log("fullfield");
            return {
                ...state,
                updateUserStatus: "success",
                updateUserError: action.payload,
            }
        });
        builder.addCase(updateUser.rejected, (state, action) => {
            console.log(action.payload);
            return {
                ...state,
                updateUserStatus: "rejected",
                updateUserError: action.payload,
            }
        });
    },
    
});

export default authSlice.reducer;
export const {loadUser, logoutUser} = authSlice.actions;