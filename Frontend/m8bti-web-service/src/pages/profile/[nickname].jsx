import { AuthContext } from "@/hooks/auth";
import React, { useContext, useEffect, useState } from "react";
import Head from "next/head";
import AuthRoute from "@/hooks/route";
import { MBTIColors } from "@/components/MBTIColors";
import { getMBTIGroupIndex } from "@/components/MBTIMap";
import clsx from "clsx";
import { Button } from "@/components/Button";
import { Header } from "@/components/Header";
import { useRouter } from "next/router";
import { ProfileMain } from "@/components/ProfileMain";
import autoprefixer from "autoprefixer";

export default function ProfilePage(){
    const router = useRouter();
    const { nickname } = router.query;
    //console.log(nickname)
    const {currentUser}=useContext(AuthContext)
    const {setLoading}=useContext(AuthContext   )
    if(currentUser===null)
        return(
        <AuthRoute>
            <></>
        </AuthRoute>)
    const {userData}=useContext(AuthContext)
    const {logout}=useContext(AuthContext)
    const [userPageData, setUserPageData] =useState(userData)

    const fetchVisitedUserData=async()=>{
        try{
            const response = await fetch(`http://localhost:8080/api/users/${nickname}`, {
                method: "GET",
                credentials: "include"
            });
            if (!response.ok) throw new Error("Failed to fetch user data");
            const data = await response.json();
            setUserPageData(data);

        }
        catch{

        }
        finally{
        }
    }
    useEffect(()=>{
        if(nickname!==currentUser){
            
            fetchVisitedUserData();
        }
    
    },[])

    const groupIndex=userPageData?.mbtiType
    ? getMBTIGroupIndex(userPageData.mbtiType.toString())
    : 0;
    console.log(userPageData)
    const primaryColor=MBTIColors({colorDest:"Primary",mbti:groupIndex})
    const secondaryColor=MBTIColors({colorDest:"Secondary",mbti:groupIndex})
    const extraColor=MBTIColors({colorDest:"Extra",mbti:groupIndex})
    console.log(groupIndex);
    console.log(primaryColor);
    
    return(
        <AuthRoute>
            <Head>
                <title>M8TI - {nickname}</title>
            </Head>
            <div className="h-f min-h-screen"
                style={{
                    backgroundColor: secondaryColor,
                }}>
            <Header mbti={groupIndex} userName={userPageData.name+" "+userPageData.surname}/>
            <ProfileMain primaryColor={primaryColor} secondaryColor={secondaryColor}
                extraColor={extraColor} mbti={groupIndex} nickname={nickname} currentUser={currentUser}
                userAbout={userPageData.description} userTags={userPageData.tags} 
                name={userPageData.name} surname={userPageData.surname}/>
                <div className={currentUser!==nickname?"hidden":""}> 
                    <Button color={groupIndex} onClick={logout}> Log Out</Button>
                </div>
            </div>
        </AuthRoute>
    )
}

