import { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate, Link } from "react-router-dom";
import styled from "styled-components";
import { PrimaryButton } from "./CommonStyled";
import { walletsCreate } from "../slices/WalletsSlice";

const AddWalletTemplate = () => {
    const dispatch = useDispatch();
    const auth = useSelector((state) => state.auth);
    const { createStatus } = useSelector((state) => state.wallets);


  const [walletName, setWalletName] = useState("");
  const [walletDescription, setWalletDescription] = useState("");


  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();

    dispatch(
        walletsCreate({
        userId: auth.email,
        walletName: walletName,
        walletDescription: walletDescription,
        cryptosCount: 0,
        cryptosCost: 0,
        cryptocurrenciesList: [],
        token: auth.token
      })
    );

    navigate("/panel/wallets");

  };

  return (
    <StyledCreateWallet>
      <StyledForm onSubmit={handleSubmit}>
        <h3>Add a Wallet</h3>
        
        <input
          type="text"
          placeholder="Wallet Name"
          onChange={(e) => setWalletName(e.target.value)}
          required
        />
        <input
          type="text"
          placeholder="Wallet Description"
          onChange={(e) => setWalletDescription(e.target.value)}
          required
        />

        <PrimaryButton type="submit">
          {createStatus === "pending" ? "Submitting" : "Submit"}
        </PrimaryButton>

        <div className="back-to-wallets">
          <Link to="/panel/wallets">
            <svg xmlns="http://www.w3.org/2000/svg" 
              width="20" 
              height="20" 
              fill="currentColor" 
              className="bi bi-arrow-left" 
              viewBox="0 0 16 16">
              <path fillRule="evenodd" d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8z"/>
            </svg>
            <span>Back To Wallets</span>
          </Link>
        </div>
        
      </StyledForm>
      
    </StyledCreateWallet>
  );
}
 
export default AddWalletTemplate;

const StyledForm = styled.form`
  display: flex;
  flex-direction: column;
  max-width: 300px;
  margin-top: 2rem;
  select,
  input {
    padding: 7px;
    min-height: 30px;
    outline: none;
    border-radius: 5px;
    border: 1px solid rgb(182, 182, 182);
    margin: 0.3rem 0;
    &:focus {
      border: 2px solid rgb(0, 208, 255);
    }
  }
  select {
    color: rgb(95, 95, 95);
  }
`;

const StyledCreateWallet = styled.div`
  display: flex;
  justify-content: space-between;
`;

const ImagePreview = styled.div`
  margin: 2rem 0 2rem 2rem;
  padding: 2rem;
  border: 1px solid rgb(183, 183, 183);
  max-width: 300px;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 2rem;
  color: rgb(78, 78, 78);
  img {
    max-width: 100%;
  }
`;