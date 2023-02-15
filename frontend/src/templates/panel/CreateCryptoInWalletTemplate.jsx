import { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate, useParams, Link } from "react-router-dom";
import styled from "styled-components";
import { getAllCryptos } from "../slices/CryptoCurrenciesSlice";

import {walletsCreateCrypto} from "../slices/WalletsSlice"


const CreateCryptoInWalletTemplate = () => {
    const dispatch = useDispatch();
    const { createStatus } = useSelector((state) => state.wallets);
    const auth = useSelector((state) => state.auth);
    const params = useParams();

    const [cryptoImg, setCryptoImg] = useState("");
    const [cryptoImgUrl, setCryptoImgUrl] = useState("");
    const [name, setName] = useState("");
    const [desc, setDesc] = useState("");
    const [amount, setAmount] = useState(0);
    const [cost, setCost] = useState(0);
    const [crypto, setCrypto] = useState("");
    const [cryptoName, setCryptoName] = useState("");
    const [walletName, setWalletName] = useState(params.walletName);
    const {cryptos} = useSelector((state) => state.cryptos);
    const [cryptoParams, setCryptoParams] = useState("");
    

    const navigate = useNavigate();

    useEffect(() => {
  
      dispatch(
        getAllCryptos(
          {
            token: auth.token
          }
        )
      );
    }, [dispatch]);
   

    const handleSubmit = (e) => {
        e.preventDefault();
        
        dispatch(
          walletsCreateCrypto({
            userId: auth.email,
              walletName: walletName,
              cryptoName: cryptoName,
              cryptoType: crypto,
              image: "",
              imageUrl: cryptoImgUrl,
              cryptoDescription: desc,
              cryptoAmount: amount,
              cryptoCost: cost,
              token: auth.token,
          })
        );

        navigate(`/edit-wallet/${walletName}`);

    };

    const handleSetCrypto = (e) => {
      const curCrypto = e.target.value;

      setCrypto(curCrypto);
      let findCrypto = cryptos.filter((crypto) => crypto.cryptoName === curCrypto);
      findCrypto = findCrypto[0];

      setCryptoParams(findCrypto);

      setCost(amount * findCrypto.cryptoCost);

      setCryptoImgUrl(findCrypto.imageUrl);

    };

    const handleChangeAmount = (e) => {
      const changeAmount = e.target.value;

      setAmount(changeAmount);
      setCost(changeAmount * cryptoParams.cryptoCost);

    };

    return (
    <StyledCreateCrypto>
      <CryptoContainer>
      <StyledForm onSubmit={(e) => handleSubmit(e)}>
        <h3>Add a Crypto to Wallet: {walletName}</h3>


        <input
          type="text"
          placeholder="Crypto Name"
          onChange={(e) => setCryptoName(e.target.value)}
          required
        />

        <select onChange={(e) => handleSetCrypto(e)} required>
          <option value="">Select Crypto Type</option>
          {cryptos?.map((crypto) => (
            <option key={crypto.cryptoName}>
              {crypto.cryptoName}
            </option>
          ))}
        </select>

        <input
          type="text"
          placeholder="Description"
          onChange={(e) => setDesc(e.target.value)}
          required
        />
        <input
          type="number"
          step="0.000001"
          defaultValue = {amount}
          placeholder="Amount"
          onChange={(e) => handleChangeAmount(e)}
          required
        />
        <input
          type="number"
          step="0.01"
          value = {cost}
          placeholder="0"
          disabled={true}          
          required
        />

        <PrimaryButton type="submit">
          {createStatus === "pending" ? "Submitting" : "Submit"}
        </PrimaryButton>
        <div className="back-to-wallet">
          <Link to={`/edit-wallet/${walletName}`}>
            <svg xmlns="http://www.w3.org/2000/svg" 
              width="20" 
              height="20" 
              fill="currentColor" 
              className="bi bi-arrow-left" 
              viewBox="0 0 16 16">
              <path fillRule="evenodd" d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8z"/>
            </svg>
            <span>Back To Wallet</span>
          </Link>
        </div>
      </StyledForm>
      <ImagePreview>
        {cryptoImgUrl ? (
          <>
            <img src={cryptoImgUrl} alt="error!" />
          </>
        ) : (
          <p>Crypto image will appear here!</p>
        )}
      </ImagePreview>
      </CryptoContainer>
    </StyledCreateCrypto>
  );
}
 
export default CreateCryptoInWalletTemplate;

const CryptoContainer = styled.div`
    max-width: 800px;
    width: 100%;
    height: auto;
    display: flex;
    box-shadow: rgba(100,100,111,0.2) 0px 7px 29px 0px;
    border-radius: 5px;
    padding: 2rem;
`;

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

const StyledCreateCrypto = styled.div`


    margin: 3rem;
    display: flex;
    justify-content: center;
`;

const ImagePreview = styled.div`
  margin: 2rem 0 2rem 2rem;
  margin-right: 1rem;
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

const PrimaryButton = styled.button`
  padding: 9px 12px;
  border-radius: 5px;
  font-weight: 400;
  width: fit-content;
  letter-spacing: 1.15px;
  background-color: #4b70e2;
  color: #f9f9f9;
  border: none;
  outline: none;
  cursor: pointer;
  margin: 0.5rem 0;
`;