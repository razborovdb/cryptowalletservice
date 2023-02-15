import * as React from 'react';
import styled from "styled-components";
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';

import { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { PrimaryButton } from "./CommonStyled";
import { cryptosUpdate } from "../slices/CryptoCurrenciesSlice";
import { toast } from "react-toastify";


export default function EditCryptoTemplate({cryptoName}) {
    const [open, setOpen] = React.useState(false);

    const dispatch = useDispatch();
    const {cryptos} = useSelector((state) => state.cryptos);

    const auth = useSelector((state) => state.auth);

    const [currentCrypto, setCurrentCrypto] = useState({});
    const [previewImg, setPreviewImg] = useState("");
    const { editStatus } = useSelector((state) => state.cryptos);

    const [cryptoImg, setCryptoImg] = useState("");

    const [cryptoImgUrl, setCryptoImgUrl] = useState("");
    const [name, setName] = useState("");
    const [desc, setDesc] = useState("");
    const [amount, setAmount] = useState(0.0);
    const [cost, setCost] = useState(0.0);

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
        setPreviewImg(reader.result);
      };
    } else {
        setCryptoImgUrl("");
    }
  };

  const navigate = useNavigate();

  const handleSubmit = async (e) => {

    e.preventDefault();

    dispatch(
      cryptosUpdate({
        name,
        image: cryptoImg,
        imageUrl: cryptoImgUrl,
        desc,
        amount,
        cost,
        token: auth.token,
      })
      
    );

    setOpen(false);
  };

  const handleClickOpen = () => {
    setOpen(true);
    let selectedCrypto = cryptos.filter((crypto) => crypto.cryptoName === cryptoName);
    selectedCrypto = selectedCrypto[0];


    setCurrentCrypto(selectedCrypto);
    setPreviewImg(selectedCrypto.imageUrl);
    setCryptoImgUrl("");
    setCryptoImg(selectedCrypto.image);
    setName(selectedCrypto.cryptoName);
    setDesc(selectedCrypto.cryptoDescription);
    setAmount(selectedCrypto.cryptoAmount);
    setCost(selectedCrypto.cryptoCost);



  };

  const handleClose = () => {
    setOpen(false);
  };

  return (
    <div>
      <Edit onClick={handleClickOpen}>
        Edit
      </Edit>
      <Dialog open={open} onClose={handleClose} fullWidth={true} maxWidth={"md"}>
        <DialogTitle>Edit Crypto</DialogTitle>
        <DialogContent>
            <StyledEditCrypto>
                <StyledForm onSubmit={handleSubmit}>
                    <h3>Edit a Crypto: {name}</h3>
                    <input
                    id="imgUpload"
                    accept="image/*"
                    type="file"
                    onChange={handleCryptoImageUpload}
                    />
                    <input
                    type="text"
                    placeholder="Description"
                    onChange={(e) => setDesc(e.target.value)}
                    value={desc}
                    required
                    />
                    <input
                    type="number"
                    step="0.000001"
                    placeholder="Amount"
                    onChange={(e) => setAmount(e.target.value)}
                    value={amount}
                    required
                    />
                    <input
                    type="number"
                    step="0.01"
                    placeholder="Cost"
                    onChange={(e) => setCost(e.target.value)}
                    value={cost}
                    required
                    />

                    <PrimaryButton type="submit">
                    {editStatus === "pending" ? "Submitting" : "Submit"}
                    </PrimaryButton>
                </StyledForm>
                <ImagePreview>
                    {previewImg ? (
                    <>
                        <img src={previewImg} alt="error!" />
                    </>
                    ) : (
                    <p>Crypto image upload preview will appear here!</p>
                    )}
                </ImagePreview>
            </StyledEditCrypto>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}

const Edit = styled.div`
  border: none;
  outline: none;
  padding: 3px 5px;
  color: white;
  border-radius: 3px;
  cursor: pointer;
  background-color: #4b70e2;
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

const StyledEditCrypto = styled.div`
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