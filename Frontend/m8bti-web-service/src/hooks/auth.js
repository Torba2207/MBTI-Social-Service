import React, { useEffect, useState, createContext } from "react";
import LoadingPage from "./LoadingScreen";

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [userData, setUserData] = useState(null);
    const [img, setImg] = useState(null);
    const [currentUser, setCurrentUser] = useState(null);
    const [loading,setLoading]=useState(true);
    const [error, setError] = useState(null); // Added missing state

    const fetchUserData = async () => {
        try {
            const response = await fetch("http://localhost:8080/api/user/me", {
                method: "GET",
                credentials: "include"
            });
            if (!response.ok) throw new Error("Failed to fetch user data");
            const data = await response.json();
            setUserData(data);
            setCurrentUser(data.nickname);
            console.log(currentUser)
            /*TODO Awaiting update from Backend
            // Fetch user image
            if (data?.profileImage) {
                setImg(data.profileImage);
            } else {
                const imgResponse = await fetch("http://localhost:8080/api/user/photo", {
                    method: "GET",
                    credentials: "include"
                });

                if (imgResponse.ok) {
                    const imgData = await imgResponse.blob();
                    setImg(URL.createObjectURL(imgData));
                }
            }
                */
        } catch (err) {
            setError(err.message || "Failed to fetch user data");
            setUserData(null);
            setCurrentUser(null);
        }finally{
            setLoading(false);
        }
    };
    const logout = async () => {
        try {
          const res = await fetch("http://localhost:8080/api/auth/logout", {
            method: "GET",
            credentials: "include", // needed to send session cookie
          });
      
          if (res.ok) {
            setUserData(null);
            setCurrentUser(null);
            //setImgUrl("");
            alert("Successfully logged out.");
          } else {
            alert("Logout failed.");
          }
        } catch (error) {
          console.error("Logout error:", error);
          alert("Logout error.");
        }
      };

    useEffect(() => {
        setLoading(true);
        fetchUserData();
    }, []);
    if(loading)
        return <LoadingPage/>

    return (
        <AuthContext.Provider
            value={{
                currentUser,
                userData,
                setLoading,
                logout,
                error,
                loading
            }}
        >
            {children}
        </AuthContext.Provider>
    );
};
