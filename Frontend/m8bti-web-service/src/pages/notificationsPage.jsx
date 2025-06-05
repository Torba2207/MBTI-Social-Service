import React, { useEffect, useContext } from "react";
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

    const fetchNotifications = async () => {
        try {
            const response = await fetch(`http://localhost:8080/api/friendships/me/pending`, {
                method: "GET",
                credentials: "include"
            });
            if (!response.ok) throw new Error("Failed to fetch notifications");
            const data = await response.json();
            console.log(data);
        } catch (error) {
            console.error("Error fetching notifications:", error);
        }
    }
    useEffect(() => {
        if (currentUser) {
            fetchNotifications();
        }
    }, []);

    if (currentUser === null)
        return (
            <AuthRoute>
                <></>
            </AuthRoute>
        );
    
    return(
        <AuthRoute>
            <Head>
                <title>M8TI - Notifications</title>
            </Head>
            <Header mbti={groupIndex} userName={userData.name + " " + userData.surname} currentUser={currentUser}/>
            <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
                <h1 className="text-3xl font-bold mb-4" style={{ color: primaryColor }}>Notifications</h1>
                <p className="text-lg" style={{ color: secondaryColor }}>This feature is under development.</p>
            </div>
        </AuthRoute>
    )
}