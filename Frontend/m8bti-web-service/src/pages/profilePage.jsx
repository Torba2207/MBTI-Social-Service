import { AuthContext } from "@/hooks/auth";
import React, { useContext, useState } from "react";
import Head from "next/head";
import AuthRoute from "@/hooks/route";
import { MBTIColors } from "@/components/MBTIColors";
import { getMBTIGroupIndex } from "@/components/MBTIMap";
import clsx from "clsx";
import { Button } from "@/components/Button";
import { Header } from "@/components/Header";

export default function ProfilePage(){
    
    const {currentUser}=useContext(AuthContext)
    if(currentUser===null)
        return(
        <AuthRoute>
            <></>
        </AuthRoute>)
    const {userData}=useContext(AuthContext)
    const {logout}=useContext(AuthContext)
    const groupIndex=userData?.mbtiType
        ? getMBTIGroupIndex(userData.mbtiType.toString())
        : 0;
    console.log(userData)
    const primaryColor=MBTIColors({colorDest:"Primary",mbti:groupIndex})
    const secondaryColor=MBTIColors({colorDest:"Secondary",mbti:groupIndex})
    const extraColor=MBTIColors({colorDest:"Extra",mbti:groupIndex})
    
    return(
        <AuthRoute>
            <Head>
                <title>M8TI - Profile Page</title>
            </Head>
            <Header mbti={groupIndex} userName={userData.name+" "+userData.surname}/>
            <Button color={groupIndex} onClick={logout}> Log Out</Button>
        </AuthRoute>
    )
}

