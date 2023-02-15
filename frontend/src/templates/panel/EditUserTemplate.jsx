import { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate, useParams, Link } from "react-router-dom";
import axios from "axios";
import { setHeaders, url } from "../slices/api";
import styled from "styled-components";
import { updateUser } from "../slices/AuthSlice";


const EditUserTemplate = () => {
    const dispatch = useDispatch();
    const { updateUserStatus } = useSelector((state) => state.auth);
    const auth = useSelector((state) => state.auth);
    const params = useParams();

    
    const [userImg, setUserImg] = useState("");
    const [userImgUrl, setUserImgUrl] = useState("");
    const [name, setName] = useState("");
    const [desc, setDesc] = useState("");
    const [amount, setAmount] = useState(0.0);
    const [cost, setCost] = useState(0.0);
    const [crypto, setCrypto] = useState("");
    const [findedCrypto, setFindedCrypto] = useState("");
    const [cryptoName, setCryptoName] = useState("");
    const [walletName, setWalletName] = useState(params.walletName);
    // const {cryptos} = useSelector((state) => state.cryptos);
    const [cryptoParams, setCryptoParams] = useState("");
    const [loading, setLoading] = useState(false);
    const [cryptos, setCryptos] = useState([]);

    const [user, setUser] = useState("");

    const [previewImg, setPreviewImg] = useState("");
    

    const navigate = useNavigate();

    useEffect(() => {

      async function fetchData() {
          setLoading(true);

            try {
              const res = await axios.get(
                  `${url}/user`, 
                  {                 
                    headers: setHeaders(auth.token),
                    params: {
                      email: auth.email,
                    },
                  },
              );


              setUser(res.data);
              setName(res.data.name);

              setPreviewImg(res.data.avatarUrl);
              setUserImgUrl("");
              setUserImg(res.data.avatar);


          } catch (error) {

          }
          setLoading(false);
        };       
      fetchData();
    }, []);

    const handleUserImageUpload = (e) => {
      const file = e.target.files[0];

    TransformFileData(file);
  };

  const TransformFileData = (file) => {
    const reader = new FileReader();

    if (file) {
      reader.readAsDataURL(file);
      reader.onloadend = () => {
        setUserImgUrl(reader.result);
        setPreviewImg(reader.result);
      };
    } else {
        setUserImgUrl("");
    }
  };


   

    const handleSubmit = async (e) => {
        e.preventDefault();

        
        
        // dispatch(
        //   updateUser({
        //       name:  name,
        //       email: user.email,
        //       password: user.password,
        //       avatar: userImg,
        //       avatarUrl: userImgUrl,
        //       role: user.role,
        //       isAdmin: user.isAdmin,
        //       token: auth.token,
        //   })
        // );

        await axios.put(
          `${url}/user`,
          {
              name:  name,
              email: user.email,
              password: user.password,
              avatar: userImg,
              avatarUrl: userImgUrl,
              role: user.role,
              isAdmin: user.isAdmin,
          },
          {
            headers: setHeaders(auth.token)
        }
        
      );

        navigate(`/panel/userinfo`);

    };

    

    return (
      <StyledUser>
        <UserContainer>
                <StyledForm onSubmit={handleSubmit}>
                    <h3>Edit User: {user.email}</h3>
                    <input
                    id="imgUpload"
                    accept="image/*"
                    type="file"
                    onChange={handleUserImageUpload}
                    />
                    <input
                    type="text"
                    defaultValue={user.name}
                    placeholder="Name"
                    onChange={(e) => setName(e.target.value)}
                    value={name}
                    disabled={true}
                    required
                    />
                    

                    <PrimaryButton type="submit">
                    {updateUserStatus === "pending" ? "Submitting" : "Submit"}
                    </PrimaryButton>
                    <div className="back-to-wallet">
                    <Link to={`/panel/userinfo`}>
                      <svg xmlns="http://www.w3.org/2000/svg" 
                        width="20" 
                        height="20" 
                        fill="currentColor" 
                        className="bi bi-arrow-left" 
                        viewBox="0 0 16 16">
                        <path fillRule="evenodd" d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8z"/>
                      </svg>
                      <span>Back To User Info</span>
                    </Link>
                  </div>
                </StyledForm>
                <ImagePreview>
                    {previewImg ? (
                    <>
                        <img src={previewImg} alt="error!" />
                    </>
                    ) : (
                    <p>User image upload preview will appear here!</p>
                    )}
                </ImagePreview>
                </UserContainer>
            </StyledUser>
    );
  }
 
export default EditUserTemplate;

const Edit = styled.div`
  border: none;
  outline: none;
  padding: 3px 5px;
  color: white;
  border-radius: 3px;
  cursor: pointer;
  background-color: #4b70e2;
`;

const PrimaryButton = styled.button`
  padding: 9px 12px;
  border-radius: 5px;
  font-weight: 400;
  letter-spacing: 1.15px;
  background-color: #4b70e2;
  color: #f9f9f9;
  border: none;
  outline: none;
  cursor: pointer;
  margin: 0.5rem 0;
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

const StyledUser = styled.div`
    margin: 3rem;
    display: flex;
    justify-content: center;
`;

const UserContainer = styled.div`
    max-width: 800px;
    width: 100%;
    height: auto;
    display: flex;
    box-shadow: rgba(100,100,111,0.2) 0px 7px 29px 0px;
    border-radius: 5px;
    padding: 2rem;
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