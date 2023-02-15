import { useNavigate, useParams, Link } from "react-router-dom";
import {useState, useEffect} from "react";
import styled from "styled-components";
import { setHeaders, url } from "../../slices/api";
import axios from "axios";
import { useDispatch, useSelector } from "react-redux";
import { DataGrid } from '@mui/x-data-grid';


const WalletTemplate = () => {

    const params = useParams();

    const [walletName, setWalletName] = useState(params.walletName);

    const dispatch = useDispatch();

    const navigate = useNavigate();
    

    const [wallet, setWallet] = useState({});
    const [walletDescription, setWalletDescription] = useState("");

    const [newRows, setRows] = useState([]);
    const [newCols, setCols] = useState([]);

    const [crypto, setCrypto] = useState({});
    const [loading, setLoading] = useState(false);

    const auth = useSelector((state) => state.auth);
    
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
    
                    
                setWallet(res.data);
                setWalletDescription(res.data.walletDescription);
                createTable(res.data.cryptocurrenciesList);




            } catch (error) {
              
            }
            setLoading(false);
          };       
        fetchData();
    }, []);

    const createTable = (data) => {

      const rows = data ? (
        data.map(crypto => {
          return {
              id: crypto.cryptoName,
              cryptoType: crypto.cryptoType,
              image: crypto.image,
              imageUrl: crypto.imageUrl,
              desc: crypto.cryptoDescription,
              amount: crypto.cryptoAmount ,
              cost: crypto.cryptoCost,
          }
      })) : [];
      setRows(rows);
  
      
      const columns = [
          { field: 'id', headerName: 'Crypto Name', width: 200 },
          { field: 'cryptoType', headerName: 'Crypto Type', width: 200 },
          
  
          { field: 'imageUrl', headerName: 'Crypto Image', width: 80, 
              renderCell: (params) => {
  
                  return (
                      <ImageContainer>
                          <img src={params.row.imageUrl} alt=""/>
                      </ImageContainer>
                  );
              } 
          },
  
          {
              field: 'desc',
              headerName: 'Cryptos Description',
              width: 120,
            },
            {
              field: 'amount',
              headerName: 'Cryptos Amount',
              width: 120,
            },
            {
              field: 'cost',
              headerName: 'Cryptos Cost',
              width: 120,
            },
          
      ];
      setCols(columns);




    };

  

    return ( 
        <StyledWallet>
            <WalletContainer>
                {loading ? (<p>Loading...</p>) :
                    
                      <WalletDetails >
                        <h3>View Wallet: {wallet.walletName}</h3>
                        <h2>Wallet description: {wallet.walletDescription}</h2>
                        <h2>Cryptos cost: ${wallet.cryptosCost}</h2>
                        <CryptoCountDiv>
                          <h2>Cryptos count: {wallet.cryptosCount?.toLocaleString()}</h2>
                          
                        </CryptoCountDiv>


                        <GridDiv>
                          <DataGrid
                                  rows={newRows}
                                  columns={newCols}
                                  pageSize={5}
                                  rowsPerPageOptions={[5]}
                                  checkboxSelection
                                  disableSelectionOnClick
                          />
                        </GridDiv>

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
                      </WalletDetails>

                }
            </WalletContainer>
        </StyledWallet>
     );
}
 
export default WalletTemplate;


const CryptoCountDiv = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

const GridDiv = styled.div`
  height: 400px;
  width: 100%;
  max-width: 1000px;
`;

const AddButton = styled.button`
  padding: 9px 12px;
  border-radius: 5px;
  width: fit-content;
  font-weight: 400;
  letter-spacing: 1.15px;
  background-color: #4b70e2;
  color: #f9f9f9;
  border: none;
  outline: none;
  cursor: pointer;
  margin: 0.5rem 0;
`;

const PrimaryButton = styled.button`
  padding: 9px 12px;
  border-radius: 5px;
  width: fit-content;
  font-weight: 400;
  letter-spacing: 1.15px;
  background-color: #4b70e2;
  color: #f9f9f9;
  border: none;
  outline: none;
  cursor: pointer;
  margin: 0.5rem 0;
`;

const StyledWallet = styled.div`
    margin: 3rem;
    display: flex;
    justify-content: center;
`;

const WalletContainer = styled.div`
    max-width: 1000px;
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
    height: 70%;
    width: 70%;
  }  
`;

const WalletDetails = styled.div`
    flex: 2;
    margin-left: 2rem;
    h2 {
        font-size: 15px   ;
        margin-top: 1rem;
    }
    h3 {
        font-size: 25px   ;
        margin-top: 1rem;
    }
    input {
        font-size: 15px   ;
        margin-top: 1rem;
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

const Actions = styled.div`
  width: 100%;
  display: flex;
  justify-content: space-between;
  button {
    border: none;
    outline: none;
    padding: 3px 5px;
    color: white;
    border-radius: 3px;
    cursor: pointer;
  }
`;

const Delete = styled.button`
    background-color: rgb(255,77, 73);

`;

const Edit = styled.button`
    background-color: #4b70e2;
`;

const View = styled.button`
    background-color: rgb(114,225,40);
`;
