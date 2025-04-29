import Head from 'next/head'
import AuthLayout from '@/components/AuthLayout';
import { SelectField, TextField } from '@/components/Fields';
import { Button } from '@/components/Button';
import React, { useState } from "react";
import clsx from 'clsx';
import useColorCycle from '@/hooks/useColorCycle';
import { MBTIColors } from '@/components/MBTIColors';

const tfClassName = "w-[80%] mx-auto";
const bgColors = MBTIColors({ colorDest: "Primary", mbti: 5 });
const secondaryColors = MBTIColors({ colorDest: "Secondary", mbti: 5 });
const extraColors = MBTIColors({ colorDest: "Extra", mbti: 5 });

export default function SignUp() {
    const bgColor = useColorCycle(bgColors, 3000);
    const secColor = useColorCycle(secondaryColors, 3000);
    const extColor = useColorCycle(extraColors, 3000);

    const [step, setStep] = useState(0);
    const [animate, setAnimate] = useState(true);
    const [triedSubmit, setTriedSubmit] = useState(false);

    const [name, setName] = useState("");
    const [surname, setSurname] = useState("");
    const [nickname, setNickname] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [location, setLocation] = useState("");
    const [mbtiType, setMbtiType] = useState("");
    const [gender, setGender] = useState("");
    const [birthDate, setBirthDate] = useState("");

    const [errors, setErrors] = useState({
        name: '',
        surname: '',
        nickname: '',
        email: '',
        password: '',
        location: '',
        mbtiType: '',
        gender: '',
        birthDate: ''
    });

    const handleDateInput = (e) => {
        const value = e.target.value;
        if (/^[0-9/]*$/.test(value)) {
            let formattedValue = value
                .replace(/^(\d{2})$/, "$1/")
                .replace(/^(\d{2})\/(\d{2})$/, "$1/$2/");
            setBirthDate(formattedValue);
            setErrors({...errors, birthDate: ''});
        }
    };

    const validateStep = () => {
        let isValid = true;
        const newErrors = {
            name: '',
            surname: '',
            nickname: '',
            email: '',
            password: '',
            location: '',
            mbtiType: '',
            gender: '',
            birthDate: ''
        };

        if (step === 0) {
            if (!name.trim()) {
                newErrors.name = 'Please enter your name';
                isValid = false;
            } else if (/^[\d-]+$/.test(name)) {
                newErrors.name = 'Name cannot contain numbers';
                isValid = false;
            }

            if (!surname.trim()) {
                newErrors.surname = 'Please enter your surname';
                isValid = false;
            } else if (/^[\d-]+$/.test(surname)) {
                newErrors.surname = 'Surname cannot contain numbers';
                isValid = false;
            }

            if (!nickname.trim()) {
                newErrors.nickname = 'Please enter your nickname';
                isValid = false;
            }
        } 
        else if (step === 1) {
            if (!email.trim()) {
                newErrors.email = 'Please enter your email';
                isValid = false;
            } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
                newErrors.email = 'Invalid email format';
                isValid = false;
            }

            if (!password.trim()) {
                newErrors.password = 'Please enter your password';
                isValid = false;
            }

            if (!location.trim()) {
                newErrors.location = 'Please enter your location';
                isValid = false;
            }
        } 
        else if (step === 2) {
            if (!mbtiType) {
                newErrors.mbtiType = 'Please select your MBTI type';
                isValid = false;
            }

            if (!birthDate.trim()) {
                newErrors.birthDate = 'Please enter your birth date';
                isValid = false;
            } else if (!isValidDate(birthDate)) {
                newErrors.birthDate = 'Invalid date format (DD/MM/YYYY)';
                isValid = false;
            }

            if (!gender) {
                newErrors.gender = 'Please select your gender';
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
        setTriedSubmit(true);
        if (validateStep()) changeStep(step + 1);
    };

    const handleFinish = () => {
        setTriedSubmit(true);
        if (validateStep()) alert("Registration complete!");
    };

    const totalSteps = 3;

    const changeStep = (newStep) => {
        setAnimate(false);
        setTimeout(() => {
            setStep(newStep);
            setAnimate(true);
            setTriedSubmit(false);
        }, 100);
    };

    const handleFieldChange = (field, setter) => (e) => {
        setter(e.target.value);
        if (triedSubmit) {
            setErrors({...errors, [field]: ''});
        }
    };

    const FieldWrapper = ({ children, error }) => (
        <div className="mb-8 w-[80%] mx-auto relative">
            {children}
            {triedSubmit && error && (
                <p className="absolute text-red-500 text-xs mt-1">
                    {error}
                </p>
            )}
        </div>
    );

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
                <h1 className='text-center text-3xl font-bold pt-[3%]' style={{
                    color: bgColor,
                    transition: "color 1s ease-in-out"
                }}>
                    Sign Up
                </h1>
                <form className='mt-[5%]'>
                    <div id="controls-carousel" className="relative w-full">
                        <div className={`relative overflow-hidden rounded-lg transition-opacity duration-300 ease-in-out ${animate ? 'opacity-100' : 'opacity-0'}`}>
                            {step === 0 && (
                                <div className="space-y-2">
                                    <FieldWrapper error={errors.name}>
                                        <TextField
                                            className={clsx('w-full', errors.name && 'border-red-500')}
                                            labelColor={bgColor}
                                            fieldBGColor={secColor}
                                            isDynamic={true}
                                            label={
                                                <>
                                                    Name <span className="text-red-500">*</span>
                                                </>
                                            }
                                            id="name"
                                            name="name"
                                            value={name}
                                            onChange={handleFieldChange('name', setName)}
                                            required
                                        />
                                    </FieldWrapper>

                                    <FieldWrapper error={errors.surname}>
                                        <TextField
                                            className={clsx('w-full', errors.surname && 'border-red-500')}
                                            labelColor={bgColor}
                                            fieldBGColor={secColor}
                                            isDynamic={true}
                                            label={
                                                <>
                                                    Surname <span className="text-red-500">*</span>
                                                </>
                                            }
                                            id="surname"
                                            name="surname"
                                            value={surname}
                                            onChange={handleFieldChange('surname', setSurname)}
                                            required
                                        />
                                    </FieldWrapper>

                                    <FieldWrapper error={errors.nickname}>
                                        <TextField
                                            className={clsx('w-full', errors.nickname && 'border-red-500')}
                                            labelColor={bgColor}
                                            fieldBGColor={secColor}
                                            isDynamic={true}
                                            label={
                                                <>
                                                    Nickname <span className="text-red-500">*</span>
                                                </>
                                            }
                                            id="nickname"
                                            name="nickname"
                                            value={nickname}
                                            onChange={handleFieldChange('nickname', setNickname)}
                                            required
                                        />
                                    </FieldWrapper>
                                </div>
                            )}

                            {step === 1 && (
                                <div className="space-y-2">
                                    <FieldWrapper error={errors.email}>
                                        <TextField
                                            className={clsx('w-full', errors.email && 'border-red-500')}
                                            labelColor={bgColor}
                                            fieldBGColor={secColor}
                                            isDynamic={true}
                                            label={
                                                <>
                                                    Email <span className="text-red-500">*</span>
                                                </>
                                            }
                                            id="email"
                                            name="email"
                                            type="email"
                                            value={email}
                                            onChange={handleFieldChange('email', setEmail)}
                                            required
                                        />
                                    </FieldWrapper>

                                    <FieldWrapper error={errors.password}>
                                        <TextField
                                            className={clsx('w-full', errors.password && 'border-red-500')}
                                            labelColor={bgColor}
                                            fieldBGColor={secColor}
                                            isDynamic={true}
                                            label={
                                                <>
                                                    Password <span className="text-red-500">*</span>
                                                </>
                                            }
                                            id="password"
                                            name="password"
                                            type="password"
                                            value={password}
                                            onChange={handleFieldChange('password', setPassword)}
                                            required
                                        />
                                    </FieldWrapper>

                                    <FieldWrapper error={errors.location}>
                                        <TextField
                                            className={clsx('w-full', errors.location && 'border-red-500')}
                                            labelColor={bgColor}
                                            fieldBGColor={secColor}
                                            isDynamic={true}
                                            label={
                                                <>
                                                    Location <span className="text-red-500">*</span>
                                                </>
                                            }
                                            id="location"
                                            name="location"
                                            value={location}
                                            onChange={handleFieldChange('location', setLocation)}
                                            required
                                        />
                                    </FieldWrapper>
                                </div>
                            )}

                            {step === 2 && (
                                <div className="space-y-2">
                                    <FieldWrapper error={errors.mbtiType}>
                                        <SelectField
                                            className={clsx('w-full', errors.mbtiType && 'border-red-500')}
                                            labelColor={bgColor}
                                            fieldBGColor={secColor}
                                            isDynamic={true}
                                            label={
                                                <>
                                                    MBTI Type <span className="text-red-500">*</span>
                                                </>
                                            }
                                            id="mbtiType"
                                            name="mbtiType"
                                            value={mbtiType}
                                            onChange={handleFieldChange('mbtiType', setMbtiType)}
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
                                    </FieldWrapper>

                                    <FieldWrapper error={errors.birthDate}>
                                        <TextField
                                            className={clsx('w-full', errors.birthDate && 'border-red-500')}
                                            labelColor={bgColor}
                                            fieldBGColor={secColor}
                                            isDynamic={true}
                                            label={
                                                <>
                                                    Date of Birth (DD/MM/YYYY) <span className="text-red-500">*</span>
                                                </>
                                            }
                                            id="birthDate"
                                            name="birthDate"
                                            value={birthDate}
                                            onChange={handleDateInput}
                                            placeholder="DD/MM/YYYY"
                                            maxLength={10}
                                            required
                                        />
                                    </FieldWrapper>

                                    <FieldWrapper error={errors.gender}>
                                        <SelectField
                                            className={clsx('w-full', errors.gender && 'border-red-500')}
                                            labelColor={bgColor}
                                            fieldBGColor={secColor}
                                            isDynamic={true}
                                            label={
                                                <>
                                                    Gender <span className="text-red-500">*</span>
                                                </>
                                            }
                                            id="gender"
                                            name="gender"
                                            value={gender}
                                            onChange={handleFieldChange('gender', setGender)}
                                            required
                                        >
                                            <option value="">Choose Gender</option>
                                            {["male", "female", "other"].map((type) => (
                                                <option key={type} value={type}>
                                                    {type}
                                                </option>
                                            ))}
                                        </SelectField>
                                    </FieldWrapper>
                                </div>
                            )}
                        </div>

                        <div className="flex justify-between mt-8">
                            {step > 0 && (
                                <Button
                                    type="button"
                                    isDynamic
                                    dynamicStyle={{
                                        backgroundColor: secColor,
                                        color: bgColor,
                                        transition: 'all 1s ease-in-out'
                                    }}
                                    className="ml-6 px-4 py-2 rounded"
                                    onClick={() => changeStep(step - 1)}
                                >
                                    Previous
                                </Button>
                            )}
                            {step === 0 && (
                                <Button
                                    isDynamic={true}
                                    currentBG={bgColor}
                                    currentText={secColor}
                                    className="ml-10 px-4 py-2 rounded"
                                    onClick={() => window.location.href = "/loginPage"}
                                >
                                    Sign in
                                </Button>
                            )}
                            {step < totalSteps - 1 ? (
                                <Button
                                    type="button"
                                    isDynamic
                                    dynamicStyle={{
                                        backgroundColor: bgColor,
                                        color: extColor,
                                        transition: 'all 1s ease-in-out'
                                    }}
                                    className="ml-auto mr-6 px-4 py-2 rounded"
                                    onClick={handleNext}
                                >
                                    Next
                                </Button>
                            ) : (
                                <Button
                                    type="button"
                                    isDynamic
                                    dynamicStyle={{
                                        backgroundColor: extColor,
                                        color: bgColor,
                                        transition: 'all 1s ease-in-out'
                                    }}
                                    className="mr-6 px-4 py-2 rounded"
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