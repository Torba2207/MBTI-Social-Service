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
import SearchMain from "@/components/SearchMain";

export default function SearchPage() {
    const router = useRouter();
    const { currentUser } = useContext(AuthContext);
    const { setLoading } = useContext(AuthContext);
    const {userData} = useContext(AuthContext);
    const [searchQuery, setSearchQuery] = useState("");
    const [error, setError] = useState(null);
    const groupIndex = userData?.mbtiType
        ? getMBTIGroupIndex(userData.mbtiType.toString())
        : 0;
    const primaryColor=MBTIColors({colorDest:"Primary",mbti:groupIndex});
    const secondaryColor=MBTIColors({colorDest:"Secondary",mbti:groupIndex});
    const extraColor=MBTIColors({colorDest:"Extra",mbti:groupIndex});

    if(currentUser === null)
        return (
            <AuthRoute>
                <></>
            </AuthRoute>
        );
    
    return(
        <AuthRoute>
            <Head>
                <title>M8TI - Search</title>
            </Head>
            <Header mbti={groupIndex} userName={userData.name+" "+userData.surname} currentUser={currentUser}/>
            <SearchMain primaryColor={primaryColor} secondaryColor={secondaryColor} extraColor={extraColor} 
            mbti={groupIndex} mbtiType={userData.mbtiType} userData={userData} />
        </AuthRoute>
    )
}