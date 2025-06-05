import React, { useContext } from "react";
import Head from "next/head";
import AuthRoute from "@/hooks/route";
import { MBTIColors } from "@/components/MBTIColors";
import { getMBTIGroupIndex } from "@/components/MBTIMap";
import { useRouter } from "next/router";
import { AuthContext } from "@/hooks/auth";
import { Header } from "@/components/Header";

export default function NotificationsPage() {
    const router = useRouter();
    const { currentUser } = useContext(AuthContext);
    const { setLoading } = useContext(AuthContext);
    const {userData} = useContext(AuthContext);
    const groupIndex = userData?.mbtiType
        ? getMBTIGroupIndex(userData.mbtiType.toString())
        : 0;
    const primaryColor = MBTIColors({ colorDest: "Primary", mbti: groupIndex });
    const secondaryColor = MBTIColors({ colorDest: "Secondary", mbti: groupIndex });
    const extraColor = MBTIColors({ colorDest: "Extra", mbti: groupIndex });
    if (currentUser === null)
        return (
            <AuthRoute>
                <></>
            </AuthRoute>
        );
    
    return(
        <div></div>
    )
}