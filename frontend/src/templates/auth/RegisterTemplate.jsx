import { useState, useEffect } from "react";
import {useDispatch, useSelector} from "react-redux";
import { useNavigate } from "react-router-dom";
import { registerUser } from "../slices/AuthSlice";
import { StyledForm } from "./StyledForm";

const RegisterTemplate = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const auth = useSelector((state) => state.auth);

    useEffect(() => {
        if(auth.userLoaded){
            navigate("/panel/userinfo");
        }
    }, [auth.userLoaded, navigate]);

    const [user, setUser] = useState({
        name: "",
        email: "",
        password: "",
        avatar: "",
        avatarUrl: "",
        role: "",
        isAdmin: false,
    });

    const handleSubmit = (e) => {
        e.preventDefault();
        dispatch(registerUser(user));

    }
    return ( 
        <StyledForm onSubmit={handleSubmit}>
            <h2>Register</h2>
            <input type = "name" placeholder="name"
                onChange={(e) => setUser({...user, name: e.target.value})}/>
            <input type = "email" placeholder="email"
                onChange={(e) => setUser({...user, email: e.target.value})}/>
            <input type = "password" placeholder="password"
                onChange={(e) => setUser({...user, password: e.target.value})}/>
            <button>{auth.registerStatus === "pending" ? "Submitting" : "Register"}</button>
            {auth.registerStatus === "rejected" ? (<p>{auth.registerError}</p>) : null}
        </StyledForm>
     );
}
 
export default RegisterTemplate;