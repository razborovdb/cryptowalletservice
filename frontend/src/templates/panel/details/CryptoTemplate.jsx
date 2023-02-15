import { useNavigate, useParams } from "react-router-dom";
import {useState, useEffect} from "react";
import styled from "styled-components";
import { setHeaders, url } from "../../slices/api";
import axios from "axios";
import { useDispatch, useSelector } from "react-redux";

const CryptoTemplate = () => {
    const params = useParams();

    const dispatch = useDispatch();

    const navigate = useNavigate();
    

    const [crypto, setCrypto] = useState({});
    const [loading, setLoading] = useState(false);

    const auth = useSelector((state) => state.auth);
    
    useEffect(() => {

        async function fetchData() {
            setLoading(true);


            try {
              const res = await axios.get(
                  `${url}/crypto`, 
                {                 
                  headers: setHeaders(auth.token),
                  params: {
                    cryptoName: params.id,
                },
              },
              );

      
              setCrypto(res.data);
            } catch (error) {
              
            }
            setLoading(false);
          };       
        fetchData();
    }, []);

    const handleClose = () => {
        navigate("/panel/cryptos");
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
                        <h3>{crypto.cryptoName}</h3>
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
 
export default CryptoTemplate;

const StyledCrypto = styled.div`
    margin: 3rem;
    display: flex;
    justify-content: center;
`;

const CryptoContainer = styled.div`
    max-width: 500px;
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