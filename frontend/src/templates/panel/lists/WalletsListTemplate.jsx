import styled from "styled-components";
import { useEffect } from "react";
import { DataGrid } from '@mui/x-data-grid';
import {useDispatch, useSelector} from "react-redux";
import { useNavigate } from "react-router-dom";
import { walletsDelete, walletsFetch } from "../../slices/WalletsSlice";


export default function WalletsListTemplate() {
  const auth = useSelector((state) => state.auth);
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const {wallets} = useSelector((state) => state.wallets);


    useEffect(() => {

      dispatch(
        walletsFetch(
          {
            userId: auth.email,
            walletName: "",
            walletDescription: "",
            cryptosCount: 0,
            cryptosCost: 0,
            cryptocurrenciesList: [],
            token: auth.token
          },
          auth.token
        )
      );


    }, [dispatch]);

    
    const rows = wallets ? (
      wallets.map(wallet => {
        return {
            id: wallet.walletName,
            walletDescription: wallet.walletDescription,
            cryptosCount: wallet.cryptosCount.toLocaleString(),
            cryptosCost: wallet.cryptosCost.toLocaleString()
        }
    })) : [];

    
    const columns = [
        { field: 'id', headerName: 'Wallet Name', width: 200 },
        {
          field: 'walletDescription',
          headerName: 'Wallet Description',
          width: 200,
        },
        {
            field: 'cryptosCount',
            headerName: 'Cryptos Count',
            width: 120,
          },
          {
            field: 'cryptosCost',
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
                        <Edit onClick={() => navigate(`/edit-wallet/${params.row.id}`)}>
                          Edit
                        </Edit>
                        <View onClick={() => navigate(`/wallet/${params.row.id}`)}>
                            View
                        </View>
                    </Actions>
                );
            },
        },
      ];

      const handleDelete = (walletName) => {
        dispatch(
          walletsDelete(
            {
              userId: auth.email,
              walletName: walletName,
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

const Edit = styled.button`
    background-color: #4b70e2;
`;

const View = styled.button`
    background-color: rgb(114,225,40);
`;
 
