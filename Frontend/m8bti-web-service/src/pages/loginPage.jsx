import Head from 'next/head'
import Link from 'next/link'
import AuthLayout from '@/components/AuthLayout';
import { TextField } from '@/components/Fields';
import { Button } from '@/components/Button';
import React, { useContext, useState} from "react";
import clsx from 'clsx';
import useColorCycle from '@/hooks/useColorCycle';
import { MBTIColors } from '@/components/MBTIColors';
import { useRouter } from "next/router";
import { AuthContext } from '@/hooks/auth.js'

const tfClassName="w-[80%] mx-auto"
const bgColors=MBTIColors({colorDest:"Primary",mbti:5});
const secondaryColors=MBTIColors({colorDest:"Secondary",mbti:5});
const extraColors=MBTIColors({colorDest:"Extra",mbti:5});
export default function Login(){
    //console.log(bgColors);

    const bgColor=useColorCycle(bgColors,3000);
    const secColor=useColorCycle(secondaryColors,3000);
    const extColor=useColorCycle(extraColors,3000);
    const {currentUser} = useContext(AuthContext)
    const {userDat} = useContext(AuthContext)
    const [usernameOrEmail, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState('');
    //const [success, setSuccess] = useState(false);
    const [fieldErrors, setFieldErrors] = useState({
        usernameOrEmail: '',
        password: ''
    });
    const [triedSubmit, setTriedSubmit] = useState(false);

    let router=useRouter();

    const validateFields = () => {
        const errors = {
            usernameOrEmail: '',
            password: ''
        };
        let isValid = true;

        if (!usernameOrEmail.trim()) {
            errors.usernameOrEmail = 'Please enter your email or username';
            isValid = false;
        }
        if (!password.trim()) {
            errors.password = 'Please enter your password';
            isValid = false;
        }

        setFieldErrors(errors);
        return isValid;
    };

    const handleLogIn = async (event) => {
        event.preventDefault();
        setError('');

        setTriedSubmit(true); 
        if (!validateFields()) {
           // setError("Please fill in both fields.");
            return;
        }
        try {
            const response = await fetch('http://localhost:8080/api/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ usernameOrEmail, password }),
                credentials: "include",
            });
            console.log(response.status);
            if (response.ok){
                window.location.href = `/loginPage`;
            }


            //router.push("/profilePage")
            /*
            if (response.ok) {
                const meRes=await fetch("http://localhost:8080/api/user/me", {
                    method:'GET',
                    credentials:"include",
                });
                if(meRes.ok){
                    const uData=await meRes.json;
                    console.log(uData)
                    window.location.href = `profile/${uData.nickname}`;
                    //router.push(`/profile/${uData.nickname}`);
                }else{
                    setError("Could not retrieve user info after login.")
                }
                // Option 1: reload the whole page (quick + easy)
                //window.location.href = "/profilePage";
          
                // Option 2 (better): set loading=true again and refetch user in AuthContext
              } else {
                const data = await response.json();
                setError(data.message || "Login failed.");
              }
                */
            
        } catch (err) {
            setError(err.response?.data?.message || 'Login failed. Please try again.');
        }
        
    };
    if(currentUser){
        router.push(`/profile/${currentUser}`)
        return(<></>)
    }else
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
                        className={clsx('pb-[2%]', tfClassName)}
                        labelColor={bgColor}
                        fieldBGColor={secColor}
                        isDynamic={true}
                        label="Email or Profile Name"
                        id="usernameOrEmail"
                        name="usernameOrEmail"
                        type="text"
                        autoComplete="email"
                        value={usernameOrEmail}
                        onChange={(e)=>{setEmail(e.target.value), setFieldErrors({...fieldErrors, usernameOrEmail: ''})}}
                        onKeyDown={(e) => {
                            if (e.key === "Enter") {
                                e.preventDefault();
                                handleLogIn(e);
                            }
                        }}
                      />
                      {triedSubmit && fieldErrors.usernameOrEmail && (
                        <p className="text-red-500 text-sm w-[80%] mx-auto pb-[5%]">
                            {fieldErrors.usernameOrEmail}
                        </p>
                      )}

                      <TextField
                        className={tfClassName}
                        label="Password"
                        labelColor={bgColor}
                        fieldBGColor={secColor}
                        isDynamic={true}
                        id="password"
                        name="password"
                        type="password"
                        autoComplete="current-password"
                        value={password}
                        onChange={(e)=>{setPassword(e.target.value), setFieldErrors({...fieldErrors, password: ''})}}
                        onKeyDown={(e) => {
                            if (e.key === "Enter") {
                                e.preventDefault();
                                handleLogIn(e);
                            }
                        }}
                        required
                      />
                      {triedSubmit && fieldErrors.password && (
                        <p className="text-red-500 text-sm w-[80%] mx-auto pt-[2%]">
                            {fieldErrors.password}
                        </p>
                      )}
                    </div>
                    {error && <p className="text-red-500 text-center mt-2">{error}</p>}
                    <div className='pt-[10%] flex justify-between w-[80%] mx-auto'>
                        <Button 
                            variant="solid"
                            type="submit"
                            color='none'
                            className='w-[45%] py-3 rounded-lg font-medium text-lg'
                            isDynamic={true}
                            currentBG={bgColor}
                            currentText={secColor}
                            style={{
                            border: `2px solid ${bgColor}`,
                            transition: "all 0.3s ease-in-out"
                            }}
                        >
                            Sign in
                        </Button>
                        
                        <Button 
                            variant="solid"
                            color='none'
                            className='w-[45%] py-3 rounded-lg font-medium text-lg'
                            isDynamic={true}
                            currentBG={secColor}
                            currentText={bgColor}
                            style={{
                            border: `2px solid ${bgColor}`,
                            transition: "all 0.3s ease-in-out"
                            }}
                            onClick={() => window.location.href = "http://localhost:3000/signupPage"}
                        >
                            Sign up
                        </Button>
                    </div>
                </form>
            </AuthLayout>
        </>
    )
}