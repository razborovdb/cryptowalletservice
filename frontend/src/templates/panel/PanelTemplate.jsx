import styled from "styled-components";
import {NavLink, Outlet} from "react-router-dom";
import {useSelector} from "react-redux";
import {FaUsers, FaWallet, FaBitcoin, FaUserCircle} from "react-icons/fa";

const PanelTemplate = () => {
    const auth = useSelector((state) => state.auth);

    if (!auth.userLoaded) return (<p>Access denied.</p>);
    return ( 
        <StyledPanel>
            <SideNav>
                <h3>Quick Links</h3>
                <NavLink
                    className={
                        ({isActive}) => isActive ? "link-active" : "link-inactive"
                    }
                    to="/panel/userinfo">
                        <FaUserCircle/> User Info
                </NavLink>
                <NavLink
                    className={
                        ({isActive}) => isActive ? "link-active" : "link-inactive"
                    }
                    to="/panel/wallets">
                        <FaWallet/> Wallets
                </NavLink>
                {auth.isAdmin ? (
                    <>
                    <NavLink
                        className={
                            ({isActive}) => isActive ? "link-active" : "link-inactive"
                        }
                        to="/panel/cryptos">
                            <FaBitcoin/> Cryptos
                    </NavLink>
                    </>
                ) : (
                    <div></div>
                )}
                
            </SideNav>
            <Content>
                <Outlet/>
            </Content>
        </StyledPanel>
    );
}
 
export default PanelTemplate;

const StyledPanel = styled.div`
    display: flex;
    height: 100%;
`;

const SideNav = styled.div`
  border-right  : 1px solid gray;
  height: 100%;
  position: fixed;
  overflow-y: auto;
  width: 200px;
  display: flex;
  flex-direction: column;
  padding: 2rem;
  h3 {
    margin: 0 0 1rem 0;
    padding: 0;
    text-transform: uppercase;
    font-size: 17px;
  }
  a {
    text-decoration: none;
    margin-bottom: 1rem;
    font-size: 14px;
    display: flex;
    align-items: center;
    font-weight: 700;
  }
  svg {
    margin-right: 0.5rem;
    font-size: 18px;
  }
`;

const Content = styled.div`
  margin-left  : 200px;
  padding: 2rem 3rem;
  width: 100%;
`;