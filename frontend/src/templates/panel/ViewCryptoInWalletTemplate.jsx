import { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate, useParams, Link } from "react-router-dom";
import { setHeaders, url } from "../slices/api";
import axios from "axios";
import styled from "styled-components";



const ViewCryptoInWalletTemplate = () => {
    const dispatch = useDispatch();
    const { createStatus } = useSelector((state) => state.wallets);
    const auth = useSelector((state) => state.auth);
    const params = useParams();

    const [crypto, setCrypto] = useState("");
    const [walletName, setWalletName] = useState(params.walletName);
    const [cryptoName, setCryptoName] = useState(params.cryptoName);
    const {cryptos} = useSelector((state) => state.cryptos);
    const [cryptoParams, setCryptoParams] = useState("");
    const [loading, setLoading] = useState(false);
    

    const navigate = useNavigate();

    useEffect(() => {

      async function fetchData() {
          setLoading(true);

            try {
              const res = await axios.get(
                  `${url}/wallet`, 
                  {                 
                    headers: setHeaders(auth.token),
                    params: {
                      email: auth.email,
                      walletName: params.walletName,
                    },
                  },
              );

  
              let findCrypto = res.data.cryptocurrenciesList.filter((crypto) => crypto.cryptoName === params.cryptoName);
              findCrypto = findCrypto[0];
              setCrypto(findCrypto)

          } catch (error) {

          }
          setLoading(false);
        };       
      fetchData();
  }, []);

  const handleClose = () => {
    navigate(`/edit-wallet/${walletName}`);
};

  return ( 
    <StyledCrypto>
        <CryptoContainer>
            {loading ? (<p>Loading...</p>) :
                <>
                <ImageContainer>
                    <img src={crypto.imageUrl} alt="crypto" />
                </ImageContainer>
                <CryptoDetails>
                    <h3>{crypto.cryptoName} in {walletName}</h3>
                    <p><span>Crypto Type:</span> {crypto.cryptoType} </p>
                    <p><span>Description:</span> {crypto.cryptoDescription} </p>
                    <Price>{crypto.cryptoAmount?.toLocaleString()}</Price>
                    <Price>${crypto.cryptoCost?.toLocaleString()}</Price>
                    <button className="cryptodetails-close" onClick={() => 
                        handleClose()
                    }>
                        Close
                    </button>
                </CryptoDetails>
                </>
            }
        </CryptoContainer>
    </StyledCrypto>
 );
}
 
export default ViewCryptoInWalletTemplate;

const StyledCrypto = styled.div`
    margin: 3rem;
    display: flex;
    justify-content: center;
`;

const CryptoContainer = styled.div`
    max-width: 800px;
    width: 100%;
    height: auto;
    display: flex;
    box-shadow: rgba(100,100,111,0.2) 0px 7px 29px 0px;
    border-radius: 5px;
    padding: 2rem;
`;

const ImageContainer = styled.div`
    flex: 1;
  img {
    width: 100%;
  }  
`;

const CryptoDetails = styled.div`
    flex: 2;
    margin-left: 2rem;
    h3 {
        font-size: 35px;
    }
    p span {
        font-weight: bold;
    }
`;

const Price = styled.div`
  margin: 1rem 0;
  font-weight: bold;
  font-size: 25px;
`;