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
    const [usersTags, setUsersTags] = useState(userTags || []);
    const [oldAboutText, setOldAboutText] = useState(aboutText || "");
    const [isPopUpOpen, setIsPopUpOpen] = useState(false);
    //const [hoveredTag, setHoveredTag] = useState(null);
    
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
    useEffect(() => {
        setUsersTags(userTags || []);
        console.log(userTags, usersTags)
    }, [userTags]);
    
    //console.log(width, height);
    return(
        <div  className={`w-f h-f`}>
            <div 
                className={`pl-[15%] pt-[5%] h-[80%]`}
                >
                <h1 
                    className="text-2xl font-bold"
                    style={{
                        color:primaryColor,
                    }}
                >
                    About {currentUser===nickname?"You":nickname}
                </h1>
                <textarea readOnly={currentUser!==nickname}
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
                            resize: "none",
                        }
                    }
                    

                    
                />
                <div className={currentUser!==nickname||aboutText===oldAboutText?"hidden":""}> 
                    <Button color={mbti} onClick={handleSave}> Save</Button>
                </div>
            </div>
            

            <div>
                {usersTags!==null&&usersTags!==undefined&&usersTags.length>0&&(
                    <div className="w-[70%] flex flex-row flex-wrap items-center ml-[14%]">
                        {usersTags.map((tag, id) => (
                            <div 
                            className="w-max-[10%] mx-[1%] mt-[1%] rounded-lg border-2 px-[1%] py-[0.2%]" key={id}
                            /*
                            onMouseEnter={() => setHoveredTag(id)}
                            onMouseLeave={() => setHoveredTag(null)}
                            */
                            style={{
                                backgroundColor: extraColor,//hoveredTag === id&&currentUser===nickname ? primaryColor : extraColor,
                                color: primaryColor,//hoveredTag === id&&currentUser===nickname ? extraColor : primaryColor,
                                borderColor: primaryColor,
                            }}>
                                {tag.name}
                            </div>
                        ))}
                    </div>
                )}
                <div className={currentUser!==nickname?"hidden":""+"flex flex-row items-center ml-[14.6%] mt-[1%]"}> 
                    <Button color={mbti} onClick={() => setIsPopUpOpen(!isPopUpOpen)}> Edit Tags</Button>
                </div>
            </div>
            
            <div className={isPopUpOpen?"":"hidden"}>
                <TagPopUp primaryColor={primaryColor} secondaryColor={secondaryColor} extraColor={extraColor} 
                    mbti={mbti} nickname={nickname} currentUser={currentUser} userTags={usersTags}
                    setIsPopUpOpen={setIsPopUpOpen} isPopUpOpen={isPopUpOpen} screenHeight={height} screenWidth={width}
                    onTagsUpdated={(newTags) => setUsersTags(newTags)}/>
            </div>
        </div>
    );
}