import { Outlet, useNavigate } from "react-router-dom";
import { UserHeaders, PrimaryButton } from "./CommonStyled";
const CryptosTemplate = () => {
    const navigate = useNavigate();
    return ( 
        <div>
            <UserHeaders>
                <h2>Cryptos</h2>
                <PrimaryButton
                onClick={() => navigate("/panel/cryptos/create-crypto")}
                >
                    Add Crypto
                </PrimaryButton>
            </UserHeaders>
            <Outlet />
        </div>
     );
}
 
export default CryptosTemplate;