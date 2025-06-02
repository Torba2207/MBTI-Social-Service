import { TextField } from "./Fields";
import {useEffect, useState} from "react";
import SelectDropdown from "./SelectDropdown";
import { MBTIMap } from "./MBTIMap";
import UserDataBlock from "./UserDataBlock";

export default function SearchMain({primaryColor, secondaryColor, extraColor, mbti, mbtiType, ...props}) {
    /*
    const [selectedMBTI, setSelectedMBTI] = useState("");
    const [mbtiDropdownState, setMbtiDropdownState] = useState(false);
    const [mbtiDropdownValue, setMbtiDropdownValue] = useState("");
    const [selectedGender, setSelectedGender] = useState("");
    const [genderDropdownState, setGenderDropdownState] = useState(false);
    const [genderDropdownValue, setGenderDropdownValue] = useState("");
    const handleTypeDropdownClick = () => {
        setMbtiDropdownState(!mbtiDropdownState);
    }
    const handleSetTypeDropdownValue = (value) => {
        setMbtiDropdownValue(value);
        setSelectedMBTI(value);
        setMbtiDropdownState(false);
    }
    const handleGenderDropdownClick = () => {
        setGenderDropdownState(!genderDropdownState);
    }
    const handleSetGenderDropdownValue = (value) => {
        setGenderDropdownValue(value);
        setSelectedGender(value);
        setGenderDropdownState(false);
    }
        */
    const fetchUsers=async()=>{
        try{
            console.log("Current User Type:", mbtiType);
            const response = await fetch("http://localhost:8080/api/users/search", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    sortBy: "compatibility",
                    referenceType: mbtiType,                    
                }),
                credentials: "include"
            })
            if (!response.ok) {
                throw new Error("Failed to fetch users:", response.status);
            }
            const data = await response.json();
            console.log("Fetched users:", data);
        }
        catch (error) {
            console.error(error);
        }
    }
    
    useEffect(() => {
        //fetchUsers();
        console.log(props.userData);
    }, []);
    
    
    return (
        <div className="w-full h-full min-h-screen"
            style={{
                backgroundColor: secondaryColor,
            }}
        >
            Sugestions:
            <div>
            <UserDataBlock userData={props.userData} />
            <UserDataBlock userData={props.userData} />
            <UserDataBlock userData={props.userData} />
            <UserDataBlock userData={props.userData} />
            </div>
            {/*
            <div className="flex justify-around items-center">
                <div className="flex h-full">
                    <TextField
                        placeholder="Search..."
                        label={"Name"}
                        primaryColor={primaryColor}
                        secondaryColor={secondaryColor}
                        extraColor={extraColor}
                        mbti={mbti}
                    />
                </div>
                <div className="flex h-full">
                    <TextField
                        placeholder="Search by MBTI"
                        label={"Surname"}
                        primaryColor={primaryColor}
                        secondaryColor={secondaryColor}
                        extraColor={extraColor}
                        mbti={mbti}
                    />
                </div>
                
            </div>
            <div className="grid-cols-2 grid min-h-[40%] max-h-[40%] pt-[2%] w-1/4">
                <div className="flex">
                    <SelectDropdown
                        dropdownName="Select MBTI Type"
                        handleDropdownClick={handleTypeDropdownClick}
                        dropdownState={mbtiDropdownState}
                        primaryColor={primaryColor}
                        extraColor={extraColor}
                        options={MBTIMap}
                        handleSetDropdownValue={handleSetTypeDropdownValue}
                        dropdownValue={mbtiDropdownValue}
                    />
                </div>
                <div className="flex">
                    <SelectDropdown
                        dropdownName="Select Gender"
                        handleDropdownClick={handleGenderDropdownClick}
                        dropdownState={genderDropdownState}
                        primaryColor={primaryColor}
                        extraColor={extraColor}
                        options={["MALE", "FEMALE", "PEDIK"]}
                        handleSetDropdownValue={handleSetGenderDropdownValue}
                        dropdownValue={genderDropdownValue}
                    />
                </div>
            </div>
            */}
        </div>
    );
}