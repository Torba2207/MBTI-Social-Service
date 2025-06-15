import React, { useEffect, useState, useContext } from "react";
import Head from "next/head";
import AuthRoute from "@/hooks/route";
import { MBTIColors } from "@/components/MBTIColors";
import { getMBTIGroupIndex } from "@/components/MBTIMap";
import { AuthContext } from "@/hooks/auth";
import { Header } from "@/components/Header";
import UserDataBlock from "@/components/UserDataBlock";
import { Button } from "@/components/Button";

export default function FriendsPage() {
    const { currentUser, userData } = useContext(AuthContext);
    const [friends, setFriends] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    
    const groupIndex = userData?.mbtiType
        ? getMBTIGroupIndex(userData.mbtiType.toString())
        : 0;
    const primaryColor = MBTIColors({ colorDest: "Primary", mbti: groupIndex });
    const secondaryColor=MBTIColors({colorDest:"Secondary",mbti:groupIndex});
    const extraColor=MBTIColors({colorDest:"Extra",mbti:groupIndex});

    
    const fetchFriends = async () => {
        try {
            setLoading(true);
            setError(null);
            
            const response = await fetch("http://localhost:8080/api/friendships/me/accepted", {
                method: "GET",
                credentials: "include"
            });
            
            if (!response.ok) throw new Error("Failed to fetch friends");
            const friendships = await response.json();

            
            const friendPromises = friendships.map(async (friendship) => {
                try {
                    
                    const friendNickname = 
                        friendship.senderNickname === currentUser.nickname 
                            ? friendship.receiverNickname 
                            : friendship.senderNickname;
                    
                    const userRes = await fetch(`http://localhost:8080/api/users/${friendNickname}`, {
                        method: "GET",
                        credentials: "include"
                    });
                    
                    if (!userRes.ok) return null;
                    const friendData = await userRes.json();
                    
                    return {
                        ...friendData,
                        friendshipId: friendship.id, 
                        
                    };
                } catch (err) {
                    console.error(`Error loading friend data:`, err);
                    return null;
                }
            });

            const loadedFriends = (await Promise.all(friendPromises)).filter(Boolean);
            setFriends(loadedFriends);
        } catch (err) {
            console.error("Error:", err);
            setError("Failed to load friends");
        } finally {
            setLoading(false);
        }
    };

    // remove friend 
    const handleRemoveFriend = async (friendshipId, nickname) => {
        try {
            const response = await fetch(`http://localhost:8080/api/friendships/me/${nickname}`, {
                method: "DELETE",
                credentials: "include"
            });
            
            if (!response.ok) throw new Error("Failed to remove friend");
            
            setFriends(friends.filter(f => f.nickname !== nickname));
        } catch (err) {
            console.error("Error removing friend:", err);
            setError("Failed to remove friend");
        }
    };

    useEffect(() => {
        if (currentUser) fetchFriends();
    }, [currentUser]);

    if (currentUser === null) {
        return <AuthRoute><></></AuthRoute>;
    }

    return (
        <AuthRoute>
            <Head>
                <title>M8TI - Friends</title>
            </Head>
            <Header
                mbti={groupIndex}
                userName={`${userData?.name || ''} ${userData?.surname || ''}`}
                currentUser={currentUser}
            />
            
            <div className="flex flex-col items-center min-h-screen py-8 px-4" style={{ backgroundColor: secondaryColor}}>
                <h1 className="text-3xl font-bold mb-8" style={{ color: primaryColor }}>
                    My Friends ({friends.length})
                </h1>

                {loading ? (
                    <div className="text-center py-12">Loading friends...</div>
                ) : error ? (
                    <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-6 max-w-md">
                        {error}
                    </div>
                ) : friends.length === 0 ? (
                    <div className="text-gray-500 py-8">You don't have any friends yet</div>
                ) : (
                    <div className="w-full space-y-4">
                        {friends.map((friend) => (
                            <UserDataBlock 
                                key={friend.nickname} 
                                userData={friend}
                            >
                                <div className="flex flex-col items-end">
                                    
                                    <Button 
                                        onClick={(e) => {
                                            e.stopPropagation();
                                            handleRemoveFriend(friend.friendshipId, friend.nickname);
                                        }}
                                        className="px-3 py-1 text-sm bg-red-100 border border-red-300 text-red-700 hover:bg-red-200"
                                    >
                                        Remove Friend
                                    </Button>
                                </div>
                            </UserDataBlock>
                        ))}
                    </div>
                )}
            </div>
        </AuthRoute>
    );
}