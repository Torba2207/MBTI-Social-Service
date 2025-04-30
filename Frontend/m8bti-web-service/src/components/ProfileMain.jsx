import clsx from "clsx";
import { TextField } from "./Fields";
import useScreenSize from "@/hooks/useScreenSize";
import { Button } from "./Button";
import { useState, useEffect, use } from "react";
import axios from "axios";
import TagPopUp from "./TagPopUp";
export function ProfileMain({primaryColor,secondaryColor,extraColor,mbti,nickname,currentUser, userAbout, userTags,
    ...props}){
    const { width, height } = useScreenSize();
    const [aboutText, setAboutText] = useState(userAbout || "");
    const [oldAboutText, setOldAboutText] = useState(aboutText || "");
    
    //console.log(aboutText)
    const handleSave = async () => {
        try {
            const response = await axios.put("http://localhost:8080/api/user/me", {
                description: aboutText
            },
            {
                withCredentials: true,
            });
            console.log("Profile updated:", response.data);
            //alert("Profile successfully updated!");
            setOldAboutText(aboutText);
            //console.log(userAbout, aboutText)
        } catch (error) {
            console.error("Error updating profile:", error);
            alert("Failed to update profile.");
        }
    };
    useEffect(() => {
        setAboutText(userAbout || "");
        console.log(userAbout, aboutText)
    }, [userAbout]);
    
    //console.log(width, height);
    return(
        <div  className={`w-f h-f`}>
            <div 
                className={`pl-[15%] pt-[5%]`}
                style={{
                    height:height*0.7,
                }}
                >
                <h1 
                    className="text-2xl font-bold"
                    style={{
                        color:primaryColor,
                    }}
                >
                    About {currentUser===nickname?"You":nickname}
                </h1>
                <textarea 
                    name="about-section" 
                    id="about-section"
                    value={aboutText}
                    onChange={(e) => setAboutText(e.target.value)}
                    className={`mt-2 w-[70%] rounded-lg border-2 p-2
                    placeholder:text-gray-400 focus:border-cyan-500 focus:outline-none focus:ring-cyan-500 sm:text-sm`}
                    placeholder="Write something about yourself..."
                    style={
                        {
                            color:primaryColor,
                            background:extraColor,
                            borderColor:primaryColor,
                            height:height*0.5,
                        }
                    }

                    
                />
                <div className={currentUser!==nickname||aboutText===oldAboutText?"hidden":""}> 
                    <Button color={mbti} onClick={handleSave}> Save</Button>
                </div>
            </div>
            <div>
                <TagPopUp primaryColor={primaryColor} secondaryColor={secondaryColor} extraColor={extraColor} 
                    mbti={mbti} nickname={nickname} currentUser={currentUser} userTags={userTags}/>
            </div>
        </div>
    );
}