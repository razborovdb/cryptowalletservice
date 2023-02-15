import {Link, useNavigate} from "react-router-dom";
import {useSelector, useDispatch} from "react-redux";
import styled from "styled-components";
import { toast } from "react-toastify";
import { logoutUser } from "../templates/slices/AuthSlice";

const NavBarTemplate = () => {
    const auth = useSelector(state => state.auth);
    const dispatch = useDispatch();
    const navigate = useNavigate();

    return ( 
        <nav className="nav-bar">
            <Link to="/">
                <h2>CryptoWallet</h2>
            </Link>
            {
                auth.userLoaded ? 
                (
                    <Links>
                        <div>
                            <Link to="/panel/userinfo">
                                <h2>{auth.name}</h2>
                            </Link>
                        </div>
                        
                        <div onClick={() => {
                            dispatch(logoutUser(null));
                            navigate("/");
                            toast.warning("Logged out!", {position: "bottom-left"});
                        }}>
                            Logout
                        </div>
                    </Links>
                )
                : <AuthLinks>
                    <Link to="/login">Login</Link>
                    <Link to="/register">Register</Link>
                </AuthLinks>
            }
        </nav>
     );
}
 
export default NavBarTemplate;

const AuthLinks = styled.div`
    a{
        &:last-child{
            margin-left: 2rem;
        }
    }
`

const Links = styled.div`
    color: white;
    display: flex;
    div{
        cursor: pointer;
        &:last-child{
            margin-left: 2rem;
        }
    }
    h2{
        color:   aqua;
        font-size: large;
    }
`