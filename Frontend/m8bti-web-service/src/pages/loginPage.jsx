import Head from 'next/head'
import Link from 'next/link'
import AuthLayout from '@/components/AuthLayout';
import { TextField } from '@/components/Fields';
import { Button } from '@/components/Button';
import React, { useContext, useState} from "react";


export default function Login(){
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [correctFields, setCorrectFields] = useState(true)


    return(
        <>
            <Head>
                <title>M8TI - Sign In</title>
            </Head>
            <AuthLayout>
                <form>
                    <div>
                      <TextField
                        label="Email or Profile Name"
                        id="email"
                        name="email"
                        type="email"
                        autoComplete="email"
                        value={email}
                        onChange={(e)=>{setEmail(e.target.value), setCorrectFields(true)}}
                        required/>
                        <TextField
                        label="Password"
                        id="password"
                        name="password"
                        type="password"
                        autoComplete="current-password"
                        value={password}
                        onChange={(e)=>{setPassword(e.target.value), setCorrectFields(true)}}
                        required/>
                    </div>
                </form>
            </AuthLayout>
        </>
    )
}