import { TextField } from "./Fields";
import {useState} from "react";
import SelectDropdown from "./SelectDropdown";
import { MBTIMap } from "./MBTIMap";

export default function SearchMain({primaryColor, secondaryColor, extraColor, mbti, ...props}) {
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

    return (
        <div className="w-full h-full min-h-screen"
            style={{
                backgroundColor: secondaryColor,
            }}
        >
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