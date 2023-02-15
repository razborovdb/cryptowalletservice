import { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate, Link } from "react-router-dom";
import styled from "styled-components";
import { PrimaryButton } from "./CommonStyled";
import { cryptosCreate } from "../slices/CryptoCurrenciesSlice";

const CreateCryptosTemplate = () => {
    const dispatch = useDispatch();
    const { createStatus } = useSelector((state) => state.cryptos);
    const auth = useSelector((state) => state.auth);

    const [cryptoImg, setCryptoImg] = useState("");
    const [cryptoImgUrl, setCryptoImgUrl] = useState("");
    const [name, setName] = useState("");
    const [desc, setDesc] = useState("");
    const [amount, setAmount] = useState("");
    const [cost, setCost] = useState("");

    const handleCryptoImageUpload = (e) => {
        const file = e.target.files[0];

        TransformFileData(file);
    };

    const TransformFileData = (file) => {
        const reader = new FileReader();

        if (file) {
            reader.readAsDataURL(file);
            reader.onloadend = () => {
                setCryptoImgUrl(reader.result);
            };
        } else {
            setCryptoImgUrl("");
        }
    };

    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        dispatch(
        cryptosCreate({
            name,
            image: "",
            imageUrl: cryptoImgUrl,
            desc,
            amount,
            cost,
            token: auth.token,
        })
        );

        //navigate("/panel/cryptos");

    };

    return (
    <StyledCreateCrypto>
      <StyledForm onSubmit={handleSubmit}>
        <h3>Create a Crypto</h3>
        <input
          id="imgUpload"
          accept="image/*"
          type="file"
          onChange={handleCryptoImageUpload}
          required
        />
        <input
          type="text"
          placeholder="Name"
          onChange={(e) => setName(e.target.value)}
          required
        />
        <input
          type="text"
          placeholder="Description"
          onChange={(e) => setDesc(e.target.value)}
          required
        />
        <input
          type="number"
          step="0.000001"
          placeholder="Amount"
          onChange={(e) => setAmount(e.target.value)}
          required
        />
        <input
          type="number"
          step="0.01"
          placeholder="Cost"
          onChange={(e) => setCost(e.target.value)}
          required
        />

        <PrimaryButton type="submit">
          {createStatus === "pending" ? "Submitting" : "Submit"}
        </PrimaryButton>
        <div className="back-to-cryptos">
          <Link to="/panel/cryptos">
            <svg xmlns="http://www.w3.org/2000/svg" 
              width="20" 
              height="20" 
              fill="currentColor" 
              className="bi bi-arrow-left" 
              viewBox="0 0 16 16">
              <path fillRule="evenodd" d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8z"/>
            </svg>
            <span>Back To Cryptos</span>
          </Link>
        </div>
      </StyledForm>
      <ImagePreview>
        {cryptoImgUrl ? (
          <>
            <img src={cryptoImgUrl} alt="error!" />
          </>
        ) : (
          <p>Crypto image upload preview will appear here!</p>
        )}
      </ImagePreview>
    </StyledCreateCrypto>
  );
}
 
export default CreateCryptosTemplate;

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