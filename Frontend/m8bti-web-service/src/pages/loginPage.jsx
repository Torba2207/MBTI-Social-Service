import Head from 'next/head'
import Link from 'next/link'
import AuthLayout from '@/components/AuthLayout';
import { TextField } from '@/components/Fields';
import { Button } from '@/components/Button';
import React, { useContext, useState} from "react";
import clsx from 'clsx';


const tfClassName="w-[80%] mx-auto"


export default function Login(){
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [correctFields, setCorrectFields] = useState(true)


    return(
        <>
            <Head>
                <title>M8TI - Sign In</title>
            </Head>
            <AuthLayout className="pb-[5%]">
                <h1 className='text-center text-3xl font-bold pt-[3%] text-[#785D87]'>Sign In</h1>
                <form className='mt-[10%]'>
                    <div>
                      <TextField
                        className={clsx('pb-[10%]', tfClassName)}
                        label="Email or Profile Name"
                        id="email"
                        name="email"
                        type="email"
                        autoComplete="email"
                        value={email}
                        onChange={(e)=>{setEmail(e.target.value), setCorrectFields(true)}}
                        required/>
                        <TextField
                        className={tfClassName}
                        label="Password"
                        id="password"
                        name="password"
                        type="password"
                        autoComplete="current-password"
                        value={password}
                        onChange={(e)=>{setPassword(e.target.value), setCorrectFields(true)}}
                        required/>
                    </div>
                    <div className='pt-[10%] flex justify-between w-[80%] mx-auto'>
                        <Button color='purple' className='w-[30%]'>Sign in</Button>
                        <Button color='purple' className='w-[30%]'>Sign up</Button>
                    </div>
                </form>
            </AuthLayout>
        </>
    )
}