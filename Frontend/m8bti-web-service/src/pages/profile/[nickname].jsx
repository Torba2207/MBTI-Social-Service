import { AuthContext } from "@/hooks/auth";
import React, { useContext, useState } from "react";
import Head from "next/head";
import AuthRoute from "@/hooks/route";
import { MBTIColors } from "@/components/MBTIColors";
import { getMBTIGroupIndex } from "@/components/MBTIMap";
import clsx from "clsx";
import { Button } from "@/components/Button";
import { Header } from "@/components/Header";
import { useRouter } from "next/router";
import { ProfileMain } from "@/components/ProfileMain";

export default function ProfilePage(){
    const router = useRouter();
    const { nickname } = router.query;
    //console.log(nickname)
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
                <title>M8TI - {nickname}</title>
            </Head>
            <div className={clsx(`h-f min-h-screen bg-[${secondaryColor}]`)}>
            <Header mbti={groupIndex} userName={userData.name+" "+userData.surname}/>
            <ProfileMain primaryColor={primaryColor} secondaryColor={secondaryColor}
                extraColor={extraColor} mbti={groupIndex} nickname={nickname} currentUser={currentUser}
                userAbout={userData.description}/>
            <Button color={groupIndex} onClick={logout}> Log Out</Button>
            </div>
        </AuthRoute>
    )
}

