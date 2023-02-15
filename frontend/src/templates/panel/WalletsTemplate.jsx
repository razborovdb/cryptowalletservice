import { Outlet, useNavigate } from "react-router-dom";
import { UserHeaders, PrimaryButton } from "./CommonStyled";

const WalletsTemplate = () => {
    const navigate = useNavigate();

  return (
    <div>
      <UserHeaders>
        <h2>Wallets</h2>
        <PrimaryButton
          onClick={() => navigate("/panel/wallets/add-wallet")}
        >
          Add Wallet
        </PrimaryButton>
      </UserHeaders>
      <Outlet />
    </div>
  );
}
 
export default WalletsTemplate;