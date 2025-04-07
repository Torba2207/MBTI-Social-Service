import { AuthContext } from "@/hooks/auth";
import React, { useContext, useState } from "react";
import Head from "next/head";
import AuthRoute from "@/hooks/route";

export default function ProfilePage(){
    const {currentUser}=useContext(AuthContext)
    const {userData}=useContext(AuthContext)
    const {logout}=useContext(AuthContext)
    
    return(
        <AuthRoute>
            <Head>
                <title>M8TI - Profile Page</title>
            </Head>
            <h1>{userData!==null?userData.nickname:"Users"}</h1>
            <button
      onClick={logout}
      className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600 transition"
    >
      Log Out
    </button>
        </AuthRoute>
    )
}

