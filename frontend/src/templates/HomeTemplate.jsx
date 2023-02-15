import { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate} from "react-router-dom";
//import { toast } from "react-toastify";

import { setHeaders, url } from "../templates/slices/api";
import axios from "axios";



//import { useGetAllProductsQuery } from "../features/productApi";


const HomeTemplate = () => {
    const [project, setProject] = useState("");
    
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);
    const auth = useSelector((state) => state.auth);

    const [showButton, setShowButton] = useState(true);


    useEffect(() => {

        async function fetchData() {
            setLoading(true);
  
              try {
                const res = await axios.get(
                    `${url}/project`, 
                    
                );
  
                setProject(res.data);
  
            } catch (error) {
              
            }
            setLoading(false);
          };       
        fetchData();
    }, []);


    const handleDonate = () => {
        //navigate(`/edit-user/${user.email}`);
    }

    return (
    <div className="project-container">
        {loading ? (<p>Loading...</p>) :
            <div>
                <h2>Project Info</h2>
                <div className="projects">
                    
                    <div className="project">
                        <h3>{project.projectName}</h3>
                        <img src={project.imageUrl} alt={project.projectName} />
                        <div className="details">
                            <span>{project.projectDescription}</span>
                        </div>
                        { (auth.userLoaded && showButton) ? (
                            <>
                                <button onClick = {() => handleDonate()}>Donate</button>
                            </>
                        ) : (<p></p>)
                        }
                    </div>

                
                </div>
            </div>
        }
    </div>
    );
}


export default HomeTemplate;