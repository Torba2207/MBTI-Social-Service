import Head from 'next/head'
import AuthLayout from '@/components/AuthLayout';
import { SelectField, TextField } from '@/components/Fields';
import { Button } from '@/components/Button';
import React, { useState } from "react";
import clsx from 'clsx';

const tfClassName = "w-[80%] mx-auto";

export default function SignUp() {
    const [step, setStep] = useState(0);
    const [animate, setAnimate] = useState(true);

    const [name, setName] = useState("");
    const [surname, setSurname] = useState("");
    const [nickname, setNickname] = useState("");
    
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [location, setLocation] = useState("");

    const [mbtiType, setMbtiType] = useState("");
    const [gender, setGender] = useState("");
    const [birthDate, setBirthDate] = useState("");

    const handleDateInput = (e) => {
        const value = e.target.value;

        if (/^[0-9/]*$/.test(value)) {
            let formattedValue = value
                .replace(/^(\d{2})$/, "$1/")
                .replace(/^(\d{2})\/(\d{2})$/, "$1/$2/");

            setBirthDate(formattedValue);
        }
    };

    const [errors, setErrors] = useState({});

    const validateStep = () => {
    let newErrors = {};

    if (step === 0) {
        if (!name) newErrors.name = alert("Name is required");
        else if (/^[\d-]+$/.test(name)) newErrors.name = alert("Name cannot contain numbers");

        if (!surname) newErrors.surname = alert("Surname is required");
        else if (/^[\d-]+$/.test(surname)) newErrors.surname = alert("Surname cannot contain numbers");
        
        if (!nickname) newErrors.nickname = alert("Nickname is required");
    } 
    else if (step === 1) {
        if (!email) newErrors.email = alert("Email is required"); 
        else if (!email.includes("@") || !email.includes(".")) newErrors.email = alert("Invalid email format");

        if (!password) newErrors.password = alert("Password is required");
        if (!location) newErrors.location = alert("Location is required");
    } 
    else if (step === 2) {
        if (!mbtiType) newErrors.mbtiType = alert("MBTI type is required");
        if (!birthDate) newErrors.birthDate = alert("Birth date is required");
        else if (!isValidDate(birthDate)) newErrors.birthDate = alert("Invalid date");

        if (!gender) newErrors.gender = alert("Gender is required");
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
};

const isValidDate = (date) => {
    const [day, month, year] = date.split("/").map(Number);
    const d = new Date(year, month - 1, day);
    return d.getFullYear() === year && d.getMonth() === month - 1 && d.getDate() === day;
};


    const handleNext = () => {
        if (validateStep()) changeStep(step + 1);
    };

    const handleFinish = () => {
        if (validateStep()) alert("Registration complete!");
    };




    const totalSteps = 3;

    // Animation
    const changeStep = (newStep) => {
        setAnimate(false);
        setTimeout(() => {
            setStep(newStep);
            setAnimate(true);
        }, 100);
    };

    return (
        <>
            <Head>
                <title>M8TI - Sign Up</title>
            </Head>
            <AuthLayout className="pb-[5%]">
                <h1 className='text-center text-3xl font-bold pt-[3%] text-[#785D87]'>Sign Up</h1>
                <form className='mt-[10%]'>
                    <div id="controls-carousel" className="relative w-full">
                        <div className={`relative h-56 overflow-hidden rounded-lg md:h-96 transition-opacity duration-300 ease-in-out ${animate ? 'opacity-100' : 'opacity-0'}`}>
                            
                            {step === 0 && (
                                <div>
                                    <TextField className={clsx('pb-[10%]', tfClassName, errors.name && 'border-red-500')}
                                        label="Name" id="name" name="name" value={name}
                                        onChange={(e) => setName(e.target.value)} required />
                                        {errors.name && <p className="text-red-500 text-xs">{errors.name}</p>}
                                    
                                    <TextField className={clsx('pb-[10%]', tfClassName, errors.surname && 'border-red-500')}
                                        label="Surname" id="surname" name="surname" value={surname}
                                        onChange={(e) => setSurname(e.target.value)} required />
                                        {errors.surname && <p className="text-red-500 text-xs">{errors.surname}</p>}

                                    <TextField className={clsx('pb-[10%]', tfClassName, errors.nickname && 'border-red-500')}
                                        label="Nickname" id="nickname" name="nickname" value={nickname}
                                        onChange={(e) => setNickname(e.target.value)} required />
                                        {errors.nickname && <p className="text-red-500 text-xs">{errors.nickname}</p>}
                                </div>
                            )}

                            {step === 1 && (
                                <div>
                                    <TextField className={clsx('pb-[10%]', tfClassName, errors.email && 'border-red-500')}
                                        label="Email" id="email" name="email" type="email" value={email}
                                        onChange={(e) => setEmail(e.target.value)} required />
                                        {errors.email && <p className="text-red-500 text-xs">{errors.email}</p>}

                                    <TextField className={clsx('pb-[10%]', tfClassName, errors.password && 'border-red-500')}
                                        label="Password" id="password" name="password" type="password" value={password}
                                        onChange={(e) => setPassword(e.target.value)} required />
                                        {errors.password && <p className="text-red-500 text-xs">{errors.password}</p>}

                                    <TextField className={clsx('pb-[10%]', tfClassName, errors.location && 'border-red-500')}
                                        label="Location" id="location" name="location" value={location}
                                        onChange={(e) => setLocation(e.target.value)} required />
                                        {errors.location && <p className="text-red-500 text-xs">{errors.location}</p>}
                                </div>
                            )}

                            {step === 2 && (
                                <div>
                                    <SelectField 
                                        className={clsx('pb-[10%]', tfClassName, errors.mbtiType && 'border-red-500')}
                                        label="MBTI Type" 
                                        id="mbtiType" 
                                        name="mbtiType" 
                                        value={mbtiType}
                                        onChange={(e) => setMbtiType(e.target.value)} 
                                        required
                                        >
                                        <option value="">Choose Type</option>
                                        {[
                                            "ISTJ", "ISFJ", "INFJ", "INTJ", 
                                            "ISTP", "ISFP", "INFP", "INTP", 
                                            "ESTP", "ESFP", "ENFP", "ENTP", 
                                            "ESTJ", "ESFJ", "ENFJ", "ENTJ"
                                        ].map((type) => (
                                            <option key={type} value={type}>
                                            {type}
                                            </option>
                                        ))}
                                    </SelectField>
                                    {errors.mbtiType && <p className="text-red-500 text-xs">{errors.mbtiType}</p>}

                                    <TextField 
                                    className={clsx('pb-[10%]', tfClassName, errors.birthDate && 'border-red-500')}
                                    label="Date of Birth (DD/MM/YYYY)" 
                                    id="birthDate" 
                                    name="birthDate" 
                                    value={birthDate} 
                                    onChange={handleDateInput} 
                                    placeholder="DD/MM/YYYY"
                                    maxLength={10}
                                    required
                                    />
                                    {errors.birthDate && <p className="text-red-500 text-xs">{errors.birthDate}</p>}

                                    <SelectField 
                                        className={clsx('pb-[10%]', tfClassName, errors.gender && 'border-red-500')}
                                        label="Gender" 
                                        id="gender" 
                                        name="gender" 
                                        value={gender}
                                        onChange={(e) => setGender(e.target.value)} 
                                        required
                                        >
                                        <option value="">Choose Gender</option>
                                        {["male", "female", "other"].map((type) => (
                                            <option key={type} value={type}>
                                            {type}
                                            </option>
                                        ))}
                                    </SelectField>
                                    {errors.gender && <p className="text-red-500 text-xs">{errors.gender}</p>}
                                </div>
                            )}

                        </div>

                        <div className="flex justify-between mt-6">
                            {step > 0 && (
                                <button type="button" 
                                    className="px-4 py-2 bg-gray-500 text-white rounded ml-6"
                                    onClick={() => changeStep(step - 1)}
                                >
                                    Previous
                                </button>
                            )}
                            
                            {step < totalSteps - 1 ? (
                                <button 
                                    type="button" 
                                    className={`px-4 py-2 bg-purple-600 text-white rounded ${step === 0 ? 'ml-auto' : 'mr-6'}`}
                                    onClick={handleNext}
                                >
                                    Next
                                </button>
                            ) : (
                                <button 
                                    type="button" 
                                    className="px-4 py-2 bg-green-600 text-white rounded mr-6"
                                    onClick={handleFinish}
                                >
                                    Finish
                                </button>
                            )}

                        </div>
                    </div>
                </form>
            </AuthLayout>
        </>
    );
}
