import Head from 'next/head'
import AuthLayout from '@/components/AuthLayout';
import { SelectField, TextField } from '@/components/Fields';
import { Button } from '@/components/Button';
import React, { useState } from "react";
import clsx from 'clsx';
import useColorCycle from '@/hooks/useColorCycle';
import { MBTIColors } from '@/components/MBTIColors';
import { useRouter } from "next/router";
import { QuestionMarkCircleIcon } from '@heroicons/react/24/outline';
import MBTITest from '@/components/MBTITest';

const tfClassName = "w-[80%] mx-auto";
const bgColors=MBTIColors({colorDest:"Primary",mbti:5});
const secondaryColors=MBTIColors({colorDest:"Secondary",mbti:5});
const extraColors=MBTIColors({colorDest:"Extra",mbti:5});

export default function SignUp() {
    const bgColor=useColorCycle(bgColors,3000);
    const secColor=useColorCycle(secondaryColors,3000);
    const extColor=useColorCycle(extraColors,3000);
    // Initialize MBTI test state
    //For test the test is set to true
    const [mbtiTestState, setMbtiTestState] = useState(false);
    //const [mbtiTestState, setMbtiTestState] = useState(true); // Set to true for testing purposes

    const [step, setStep] = useState(0);
    const [animate, setAnimate] = useState(true);
    
    const [name, setName] = useState("");
    const [surname, setSurname] = useState("");
    const [nickname, setNickname] = useState("");
    
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [shareLocation, setShareLocation] = useState(false);
    const [location, setLocation] = useState(null);
    const [locationError, setLocationError] = useState("");
    const [isLocationChecked, setIsLocationChecked] = useState(false);

    const [mbtiType, setMbtiType] = useState("");
    const [gender, setGender] = useState("");
    const [birthDate, setBirthDate] = useState("");

    let router=useRouter();
    const [error, setError] = useState('');

    const handleDateInput = (e) => {
        const value = e.target.value;

        if (/^[0-9/]*$/.test(value)) {
            let formattedValue = value
                .replace(/^(\d{2})$/, "$1/")
                .replace(/^(\d{2})\/(\d{2})$/, "$1/$2/");

            setBirthDate(formattedValue);
        }
        if (errors.birthDate) {
            setErrors(prev => ({...prev, birthDate: ''}));
        }
    };

    const [errors, setErrors] = useState({});

    const handleFieldChange = (field, setter, e) => {
        setter(e.target.value);
        if (errors[field]) {
            setErrors(prev => ({...prev, [field]: ''}));
        }
    };

    const handleLocationToggle = async (e) => {
        const wantsToShare = e.target.checked;
        setShareLocation(wantsToShare);
        setLocationError("");
        setIsLocationChecked(false);
        setErrors(prev => ({...prev, location: ''}));

        if (wantsToShare) {
            try {
                const position = await new Promise((resolve, reject) => {
                    navigator.geolocation.getCurrentPosition(resolve, reject, {
                        enableHighAccuracy: true,
                        timeout: 5000,
                        maximumAge: 0
                    });
                });
                
                const { latitude, longitude } = position.coords;
                setLocation({ latitude, longitude });
                setIsLocationChecked(true);
            } catch (err) {
                console.error("Error getting location:", err);
                setLocationError("Failed to get location. Please check your permissions or try again.");
                setShareLocation(false);
                setLocation(null);
                setIsLocationChecked(false);
            }
        } else {
            setLocation(null);
            setIsLocationChecked(false);
        }
    };

    const validateStep = () => {
        let newErrors = {};
        let isValid = true;
    
        const nicknameReg = /^[a-zA-Z0-9_]{3,20}$/;
        const passwordReg = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$/;
    
        if (step === 0) {
            if (!name) {
                newErrors.name = "Name is required";
                isValid = false;
            } else if (/^[\d-]+$/.test(name)) {
                newErrors.name = "Name cannot contain numbers";
                isValid = false;
            }
    
            if (!surname) {
                newErrors.surname = "Surname is required";
                isValid = false;
            } else if (/^[\d-]+$/.test(surname)) {
                newErrors.surname = "Surname cannot contain numbers";
                isValid = false;
            }
    
            if (!nickname) {
                newErrors.nickname = "Nickname is required";
                isValid = false;
            } else if (!nicknameReg.test(nickname)) {
                newErrors.nickname = "Nickname must be 3–20 characters, letters/numbers/_ only";
                isValid = false;
            }
        } 
        
        else if (step === 1) {
            if (!email) {
                newErrors.email = "Email is required";
                isValid = false;
            } else if (!email.includes("@") || !email.includes(".")) {
                newErrors.email = "Invalid email format";
                isValid = false;
            }
    
            if (!password) {
                newErrors.password = "Password is required";
                isValid = false;
            } else if (password.length < 8) {
                newErrors.password = "Password must be at least 8 characters long";
                isValid = false;
            } else if (!passwordReg.test(password)) {
                newErrors.password = "Password must contain uppercase, lowercase, digit, and special character";
                isValid = false;
            }

           if (!shareLocation) {
                newErrors.location = "You must agree to share your location";
                isValid = false;
            } else if (!isLocationChecked) {
                newErrors.location = "Please wait while we get your location";
                isValid = false;
            }
        
        } 
        
        else if (step === 2) {
            if (!mbtiType) {
                newErrors.mbtiType = "MBTI type is required";
                isValid = false;
            }
            if (!birthDate) {
                newErrors.birthDate = "Birth date is required";
                isValid = false;
            } else if (!isValidDate(birthDate)) {
                newErrors.birthDate = "Invalid date";
                isValid = false;
            }
    
            if (!gender) {
                newErrors.gender = "Gender is required";
                isValid = false;
            }
        }
    
        setErrors(newErrors);
        return isValid;
    };
    
    const isValidDate = (date) => {
        const [day, month, year] = date.split("/").map(Number);
        const d = new Date(year, month - 1, day);
        return d.getFullYear() === year && d.getMonth() === month - 1 && d.getDate() === day;
    };

    const handleNext = () => {
        if (validateStep()) changeStep(step + 1);
    };

    const handleSignUp = async () => {
        try {
            // Format date to be transferred to backend
            const [day, month, year] = birthDate.split('/');
            const formattedDate = `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`;
        
            const userData = {
                name,
                surname,
                nickname,
                password,
                email,
                latitude: location.latitude,
                longitude: location.longitude,
                mbtiType: mbtiType,
                birthday: formattedDate,
                gender,
                pronouns: "OTHER"

            };
            console.log("User data to send:", userData);
        
            const response = await fetch('http://localhost:8080/api/auth/register', {
                method: 'POST',
                headers: { 
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(userData),
                credentials: "include",
            });
            console.log("Response status:", response.status);
            const contentType = response.headers.get('content-type');
            if (!response.ok) {
                const errorData = contentType?.includes('application/json') 
                    ? await response.json() 
                    : await response.text();
                setError(errorData.message || "Registration failed");
                return;
            }
        
            router.push("/loginPage");
            
        } catch (err) {
            console.error("Registration error:", err);
            setError("Please check your data and try again");
        }
    };

    const handleFinish = () => {
        if (validateStep()) {
            handleSignUp();
        }
    };

    const totalSteps = 3;


    const changeStep = (newStep) => {
        setAnimate(false);
        setTimeout(() => {
            setStep(newStep);
            setAnimate(true);
        }, 100);
    };

    
    const RequiredStar = () => <span className="text-red-500">*</span>;


    const Tooltip = ({ children }) => (
        <div className="relative inline-flex ml-2 group">
          <QuestionMarkCircleIcon className="h-5 w-5 text-gray-400 hover:text-gray-500 cursor-pointer" />
          <div className="absolute bottom-full left-1/2 transform -translate-x-1/2 mb-2 hidden group-hover:block w-64 p-3 bg-white text-sm text-gray-700 rounded-md shadow-lg z-10 whitespace-normal">
            <div className="space-y-1">
              {children}
            </div>
          </div>
        </div>
      );
      const PasswordTooltip = () => (
        <div className="relative inline-flex ml-2 group">
          <QuestionMarkCircleIcon className="h-5 w-5 text-gray-400 hover:text-gray-500 cursor-pointer" />
          <div className="absolute left-full ml-2 top-1/2 transform -translate-y-1/2 hidden group-hover:block w-[300px] p-3 bg-white text-sm text-gray-700 rounded-md shadow-lg z-50 border border-gray-200">            <div className="space-y-1.5">
          Password requirements:
            <ul className="list-disc pl-5 mt-1">
                <li>At least 8 characters long</li>
                <li>At least one uppercase letter (A-Z)</li>
                <li>At least one lowercase letter (a-z)</li>
                <li>At least one number (0-9)</li>
                <li>At least one special character (@#$%^&+=!)</li>
            </ul>
            </div>
          </div>
        </div>
      );


    if (mbtiTestState) {
        return (
            <>
            <Head>
                <title>M8TI - MBTI Test</title>
            </Head>
            <AuthLayout mainBGColor={secColor} style={{
                borderColor: bgColor,
                background: extColor,
                transition: "border-color 1s ease-in-out, background-color 1s ease-in-out"
            }}>
                <MBTITest 
                primaryColor={bgColor} 
                secondaryColor={secColor}
                extraColor={extColor}
                transitionDefinition={'all 1s ease-in-out'}
                setMbtiTestState={setMbtiTestState}
                setMbtiType={setMbtiType}

                />
            </AuthLayout>
            </>
        );
    }

    
    return (
        <>
            <Head>
                <title>M8TI - Sign Up</title>
            </Head>
            <AuthLayout mainBGColor={secColor} className="pb-[5%]" style={{
                borderColor: bgColor,
                background: extColor,
                transition: "border-color 1s ease-in-out, background-color 1s ease-in-out"
            }}>
                <h1 className='text-center text-3xl font-bold pt-[3%] text-[#785D87]' style={{
                    color:bgColor,
                    transition: "color 1s ease-in-out" }}>
                    Sign Up</h1>
                <form className='mt-[5%]'>
                    <div id="controls-carousel" className="relative w-full">
                        <div className={`relative overflow-hidden rounded-lg transition-opacity duration-300 ease-in-out ${animate ? 'opacity-100' : 'opacity-0'}`}>
                            {step === 0 && (
                                <div className="space-y-6">
                                    <div>
                                        <TextField 
                                            className={clsx(tfClassName, errors.name && 'border-red-500')}
                                            labelColor={bgColor}
                                            fieldBGColor={secColor}
                                            isDynamic={true}
                                            label={<span>Name <RequiredStar /></span>} 
                                            id="name" 
                                            name="name" 
                                            value={name}
                                            onChange={(e) => handleFieldChange('name', setName, e)} 
                                            
                                        />
                                        {errors.name && <p className="text-red-500 text-xs mt-1 ml-[10%]">{errors.name}</p>}
                                    </div>
                                    
                                    <div>
                                        <TextField 
                                            className={clsx(tfClassName, errors.surname && 'border-red-500')}
                                            labelColor={bgColor}
                                            fieldBGColor={secColor}
                                            isDynamic={true}
                                            label={<span>Surname <RequiredStar /></span>} 
                                            id="surname" 
                                            name="surname" 
                                            value={surname}
                                            onChange={(e) => handleFieldChange('surname', setSurname, e)} 
                                            
                                        />
                                        {errors.surname && <p className="text-red-500 text-xs mt-1 ml-[10%]">{errors.surname}</p>}
                                    </div>

                                    <div>
                                        <TextField 
                                            className={clsx(tfClassName, errors.nickname && 'border-red-500')}
                                            labelColor={bgColor}
                                            fieldBGColor={secColor}
                                            isDynamic={true}
                                            label={
                                                <span> Nickname 
                                                    <RequiredStar />
                                                    <Tooltip>
                                                        Nickname requirements:
                                                        <ul>
                                                            <li>3-20 characters long</li>
                                                            <li>Can contain letters (a-z, A-Z)</li>
                                                            <li>Can contain numbers (0-9)</li>
                                                            <li>Can contain underscores (_)</li>
                                                            <li>No special characters or spaces</li>
                                                        </ul>
                                                    </Tooltip>
                                                </span>
                                                } 
                                            id="nickname" 
                                            name="nickname" 
                                            value={nickname}
                                            onChange={(e) => handleFieldChange('nickname', setNickname, e)} 
                                            
                                        />
                                        {errors.nickname && <p className="text-red-500 text-xs mt-1 ml-[10%]">{errors.nickname}</p>}
                                    </div>
                                </div>
                            )}

                            {step === 1 && (
                                <div className="space-y-6">
                                    <div>
                                        <TextField 
                                            className={clsx(tfClassName, errors.email && 'border-red-500')}
                                            labelColor={bgColor}
                                            fieldBGColor={secColor}
                                            isDynamic={true}
                                            label={<span>Email <RequiredStar /></span>} 
                                            id="email" 
                                            name="email" 
                                            type="email" 
                                            value={email}
                                            onChange={(e) => handleFieldChange('email', setEmail, e)} 
                                            required 
                                        />
                                        {errors.email && <p className="text-red-500 text-xs mt-1 ml-[10%]">{errors.email}</p>}
                                    </div>

                                    <div>
                                        <TextField 
                                            className={clsx(tfClassName, errors.password && 'border-red-500')}
                                            labelColor={bgColor}
                                            fieldBGColor={secColor}
                                            isDynamic={true}
                                            label={
                                            <span>Password <RequiredStar />
                                            <PasswordTooltip />
                                            </span>
                                            } 
                                            id="password" 
                                            name="password" 
                                            type="password" 
                                            value={password}
                                            onChange={(e) => handleFieldChange('password', setPassword, e)} 
                                            required 
                                        />
                                        {errors.password && <p className="text-red-500 text-xs mt-1 ml-[10%]">{errors.password}</p>}
                                    </div>

                                    <div className="flex items-center ml-[10%]">
                                        <input
                                            type="checkbox"
                                            id="shareLocation"
                                            checked={shareLocation}
                                            onChange={handleLocationToggle}
                                            className="h-4 w-4 rounded border-gray-300 text-indigo-600 focus:ring-indigo-500"
                                        />
                                        <label htmlFor="shareLocation" className="ml-2 block text-sm text-gray-900">
                                        <span>Share my current location <RequiredStar /></span>
                                            
                                        </label>
                                        </div>
                                            {errors.location && (
                                                <p className="text-red-500 text-xs mt-1 ml-[10%]">{errors.location}</p>
                                            )}
                                            {locationError && (
                                                <p className="text-red-500 text-xs mt-1 ml-[10%]">{locationError}</p>
                                            )}
                                            {location && (
                                                <p className="text-green-500 text-xs mt-1 ml-[10%]">
                                                    Location acquired successfully!
                                                </p>
                                            )}
                                        </div>
                            )}

                            {step === 2 && (
                                <div className="space-y-4">
                                    <div>
                                        <SelectField 
                                            className={clsx(tfClassName, errors.mbtiType && 'border-red-500')}
                                            labelColor={bgColor}
                                            fieldBGColor={secColor}
                                            isDynamic={true}
                                            label={<span>MBTI Type <RequiredStar /></span>} 
                                            id="mbtiType" 
                                            name="mbtiType" 
                                            value={mbtiType}
                                            onChange={(e) => handleFieldChange('mbtiType', setMbtiType, e)} 
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
                                        <div className='w-full justify-center flex'>
                                            <Button
                                                isDynamic={true}
                                                currentBG={bgColor}
                                                currentText={secColor}
                                                className="mt-[1%]"
                                                onClick={() => (setMbtiTestState(true))}
                                            >
                                                MBTI TEST
                                            </Button>
                                        </div>
                                        {errors.mbtiType && <p className="text-red-500 text-xs mt-1 ml-[10%]">{errors.mbtiType}</p>}
                                    </div>

                                    <div>
                                        <TextField 
                                            className={clsx(tfClassName, errors.birthDate && 'border-red-500')}
                                            labelColor={bgColor}
                                            fieldBGColor={secColor}
                                            isDynamic={true}
                                            label={<span>Date of Birth (DD/MM/YYYY) <RequiredStar /></span>} 
                                            id="birthDate" 
                                            name="birthDate" 
                                            value={birthDate} 
                                            onChange={handleDateInput} 
                                            placeholder="DD/MM/YYYY"
                                            maxLength={10}
                                            required
                                        />
                                        {errors.birthDate && <p className="text-red-500 text-xs mt-1 ml-[10%]">{errors.birthDate}</p>}
                                    </div>

                                    <div>
                                        <SelectField 
                                            className={clsx(tfClassName, errors.gender && 'border-red-500')}
                                            labelColor={bgColor}
                                            fieldBGColor={secColor}
                                            isDynamic={true}
                                            label={<span>Gender <RequiredStar /></span>} 
                                            id="gender" 
                                            name="gender" 
                                            value={gender}
                                            onChange={(e) => handleFieldChange('gender', setGender, e)} 
                                            required
                                        >
                                            <option value="">Choose Gender</option>
                                            {["MALE", "FEMALE", "OTHER"].map((type) => ( 
                                                <option key={type} value={type}>
                                                {type}
                                                </option>
                                            ))}
                                        </SelectField>
                                        {errors.gender && <p className="text-red-500 text-xs mt-1 ml-[10%]">{errors.gender}</p>}
                                    </div>
                                </div>
                            )}
                        </div>

                        <div className="flex justify-between mt-10">
                            {step === 0 ? (
                                <Button
                                    type='Button'
                                    isDynamic={true}
                                    currentBG={bgColor}
                                    currentText={secColor}
                                    className="ml-20 px-6 py-3 rounded"
                                    onClick={() => router.push("/loginPage")}
                                >
                                    Sign In
                                </Button>
                            ) : step > 0 ? (
                                <Button
                                    type='Button'
                                    isDynamic={true}
                                    currentBG={bgColor}
                                    currentText={secColor}
                                        className="ml-20 px-6 py-3 rounded"
                                        onClick={() => changeStep(step - 1)}
                                >
                                    Previous
                                </Button>
                            ) : null}
                            
                            {step < totalSteps - 1 ? (
                                <Button
                                    type='Button'
                                    isDynamic={true}
                                    currentBG={bgColor}
                                    currentText={secColor}  
                                        className="ml-auto mr-20 px-6 py-3 rounded"
                                        onClick={handleNext}
                                >
                                    Next
                                </Button>
                            ) : (
                                <Button
                                    type='Button'
                                    isDynamic={true}
                                    currentBG={bgColor}
                                    currentText={secColor}
                                        className="mr-20 ml-auto px-6 py-3 rounded"
                                        onClick={handleFinish}
                                >
                                    Finish
                                </Button>
                            )}
                        </div>
                    </div>
                </form>
            </AuthLayout>
        </>
    );
}