export const url = "http://localhost:5000/api";
export const setHeaders = (token) => {
    const headers = {
        token: token,
      };
  
    return headers;
  };