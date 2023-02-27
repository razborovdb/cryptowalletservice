import styled from "styled-components";
import { useEffect } from "react";
import { DataGrid } from '@mui/x-data-grid';
import {useDispatch, useSelector} from "react-redux";
import { useNavigate } from "react-router-dom";
import EditCryptoTemplate from "../EditCryptoTemplate";
import { getAllCryptos, cryptosDelete } from "../../slices/CryptoCurrenciesSlice";


export default function CryptosListTemplate() {
    const auth = useSelector((state) => state.auth);
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const {cryptos} = useSelector((state) => state.cryptos);
  
  
    useEffect(() => {
  
        dispatch(
          getAllCryptos(
            {
              token: auth.token
            }
          )
        );
    }, [dispatch]);

    const rows = cryptos ? (cryptos?.map(crypto => {
        return {
            id: crypto.cryptoName,
            image: crypto.image,
            imageUrl: crypto.imageUrl,
            desc: crypto.cryptoDescription,
            amount: crypto.cryptoAmount ,
            cost: crypto.cryptoCost,
        }
    })) : [];

    const columns = [
        { field: 'id', headerName: 'Crypto Name', width: 200 },
        

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
        {
          field: 'actions',
          headerName: 'Actions',
          sortable: false,
          width: 170,
          renderCell: (params) => {
                return (
                    <Actions>
                        <Delete onClick={() => handleDelete(params.row.id)}>Delete</Delete>
                        <EditCryptoTemplate cryptoName={params.row.id}/>
                        <View onClick={() => navigate(`/crypto/${params.row.id}`)}>
                            View
                        </View>
                    </Actions>
                );
            },
        },
    ];

    const handleDelete = (cryptoName) => {
        dispatch(cryptosDelete(
            {
                cryptoName: cryptoName,
                token: auth.token
            }
            )
        );

    }

    return (
        <div style={{ height: 400, width: '100%' }}>
          <DataGrid
            rows={rows}
            columns={columns}
            pageSize={5}
            rowsPerPageOptions={[5]}
            checkboxSelection
            disableSelectionOnClick
          />
        </div>
      );

}
 
const ImageContainer = styled.div`
  img {
    height: 40px;
  }  
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

const View = styled.button`
    background-color: rgb(114,225,40);
`;