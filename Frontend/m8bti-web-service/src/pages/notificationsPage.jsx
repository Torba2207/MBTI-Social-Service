import React, { useEffect, useState, useContext } from "react";
import Head from "next/head";
import AuthRoute from "@/hooks/route";
import { MBTIColors } from "@/components/MBTIColors";
import { getMBTIGroupIndex } from "@/components/MBTIMap";
import { AuthContext } from "@/hooks/auth";
import { Header } from "@/components/Header";
import acceptFriendship from "@/hooks/acceptFriendship";
import deleteFriendship from "@/hooks/deleteFriendship";
import { Button } from "@/components/Button";
import UserDataBlock from "@/components/UserDataBlock";


export default function NotificationsPage() {
    const { currentUser, userData } = useContext(AuthContext);
    const [pendingRequests, setPendingRequests] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    
    const groupIndex = userData?.mbtiType
        ? getMBTIGroupIndex(userData.mbtiType.toString())
        : 0;
    const primaryColor = MBTIColors({ colorDest: "Primary", mbti: groupIndex });

    const fetchRequestsWithUserData = async () => {
        try {
            setLoading(true);
            setError(null);
            
            const response = await fetch("http://localhost:8080/api/friendships/me/pending", {
                method: "GET",
                credentials: "include"
            });
            
            if (!response.ok) throw new Error("Failed to fetch friendship requests");
            const requests = await response.json();

            const enrichedRequests = await Promise.all(
                requests.map(async (req) => {
                    try {
                        const userRes = await fetch(`http://localhost:8080/api/users/${req.senderNickname}`, {
                            method: "GET",
                            credentials: "include"
                        });
                        
                        if (!userRes.ok) {
                            console.error(`Failed to fetch user data for ${req.senderNickname}`);
                            return {
                                ...req,
                                senderData: null 
                            };
                        }
                        
                        const userInfo = await userRes.json();
                        return {
                            ...req,
                            senderData: userInfo
                        };
                    } catch (err) {
                        console.error(`Error fetching user ${req.senderNickname}:`, err);
                        return {
                            ...req,
                            senderData: null
                        };
                    }
                })
            );

            setPendingRequests(enrichedRequests.filter(req => req.senderData !== null));
        } catch (error) {
            console.error("Error fetching requests:", error);
            setError("Failed to load friend requests");
        } finally {
            setLoading(false);
        }
    };

    const handleAccept = async (nickname) => {
        try {
            await acceptFriendship(nickname);
            await fetchRequestsWithUserData();
        } catch (err) {
            console.error("Error accepting request:", err);
            setError("Failed to accept friend request");
        }
    };

    const handleReject = async (nickname) => {
        try {
            await deleteFriendship(nickname);
            await fetchRequestsWithUserData();
        } catch (err) {
            console.error("Error rejecting request:", err);
            setError("Failed to reject friend request");
        }
    };

    useEffect(() => {
        if (currentUser) {
            fetchRequestsWithUserData();
        }
    }, [currentUser]);

    if (currentUser === null) {
        return (
            <AuthRoute>
                <></>
            </AuthRoute>
        );
    }

    return (
        <AuthRoute>
            <Head>
                <title>M8TI - Notifications</title>
            </Head>
            <Header
                mbti={groupIndex}
                userName={`${userData?.name || ''} ${userData?.surname || ''}`}
                currentUser={currentUser}
            />
            <div className="flex flex-col items-center justify-start min-h-screen bg-gray-100 py-10">
                <h1 className="text-3xl font-bold mb-6" style={{ color: primaryColor }}>
                    Friend Requests
                </h1>
                <div className="w-full space-y-4 px-4">
                    {loading ? (
                        <p>Loading...</p>
                    ) : error ? (
                        <p className="text-red-500">{error}</p>
                    ) : pendingRequests.length === 0 ? (
                        <p>No pending friend requests.</p>
                    ) : (
                        pendingRequests.map((req) => (
                            req.senderData && (
                                <UserDataBlock key={req.senderNickname} userData={req.senderData}>
                                    <div className="flex gap-2">
                                        <Button color={groupIndex} onClick={() => handleAccept(req.senderNickname)}>
                                            Accept
                                        </Button>
                                        <Button color={groupIndex} onClick={() => handleReject(req.senderNickname)}>
                                            Reject
                                        </Button>
                                    </div>
                                </UserDataBlock>
                            )
                        ))
                    )}
                </div>
            </div>
        </AuthRoute>
    );
}
