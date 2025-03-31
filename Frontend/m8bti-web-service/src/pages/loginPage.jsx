import Head from 'next/head'
import Link from 'next/link'
import AuthLayout from '@/components/AuthLayout';
import { TextField } from '@/components/Fields';
import { Button } from '@/components/Button';
import React, { useContext, useState} from "react";
import clsx from 'clsx';
import useColorCycle from '@/hooks/useColorCycle';
import { MBTIColors } from '@/components/MBTIColors';


const tfClassName="w-[80%] mx-auto"
const bgColors=MBTIColors({colorDest:"Primary",mbti:5});
const secondaryColors=MBTIColors({colorDest:"Secondary",mbti:5});
const extraColors=MBTIColors({colorDest:"Extra",mbti:5});
export default function Login(){
    //console.log(bgColors);
    const bgColor=useColorCycle(bgColors,3000);
    const secColor=useColorCycle(secondaryColors,3000);
    const extColor=useColorCycle(extraColors,3000);
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState('');
    const [success, setSuccess] = useState(false);
    const [correctFields, setCorrectFields] = useState(true);
    const handleLogIn = async (event) => {
        event.preventDefault();
        setError('');

        try {
            const response = await axios.post(
                'https://localhost:8080/api/auth/login', // Replace with the actual endpoint from the documentation
                {
                    email, // Assuming the backend expects 'email'
                    password
                },
                {
                    headers: {
                        'Content-Type': 'application/json',
                    },
                }
            );

            if (response.status === 200) {
                // Handle success (e.g., save token, redirect user)
                setSuccess(true);
                localStorage.setItem('token', response.data.token); // Replace 'token' with the actual key from the response
                alert('Logged in successfully!');
            }
        } catch (err) {
            // Handle error
            setError(err.response?.data?.message || 'An error occurred. Please try again.');
        }
    };


    return(
        <>
            <Head>
                <title>M8TI - Sign In</title>
            </Head>
            <AuthLayout mainBGColor={secColor} className="pb-[5%]" style={{
                borderColor: bgColor,
                background: extColor,
                transition: "border-color 1s ease-in-out, background-color 1s ease-in-out"
            }}>
                <h1 className='text-center text-3xl font-bold pt-[3%]' style={{
                    color:bgColor,
                    transition: "color 1s ease-in-out" 
                    }}>Sign In</h1>
                <form className='mt-[10%]' onSubmit={handleLogIn}>
                    <div>
                      <TextField
                        className={clsx('pb-[10%]', tfClassName)}
                        labelColor={bgColor}
                        fieldBGColor={secColor}
                        isDynamic={true}
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
                        labelColor={bgColor}
                        fieldBGColor={secColor}
                        isDynamic={true}
                        inputClassName={`border-[#785D87]`}
                        id="password"
                        name="password"
                        type="password"
                        autoComplete="current-password"
                        value={password}
                        onChange={(e)=>{setPassword(e.target.value), setCorrectFields(true)}}
                        required/>
                    </div>
                    <div className='pt-[10%] flex justify-between w-[80%] mx-auto'>
                        <Button type={"submit"} color='none' className='w-[30%]' 
                            style={{
                                background:bgColor,
                                color:secColor,
                                transition:"background 1s ease-in-out, color 1s ease-in-out"
                            }}
                        >Sign in</Button>
                        <Button color='none' className='w-[30%]'
                            style={{
                                background:bgColor,
                                color:secColor,
                                transition:"background 1s ease-in-out, color 1s ease-in-out"
                            }}
                        >Sign up</Button>
                    </div>
                </form>
            </AuthLayout>
        </>
    )
}