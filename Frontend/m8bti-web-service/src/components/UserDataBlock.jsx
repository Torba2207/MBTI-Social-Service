import React, { useEffect } from 'react';
import { MBTIMap, getMBTIGroupIndex } from './MBTIMap';
import { MBTIColors } from './MBTIColors';
import useScreenSize from '@/hooks/useScreenSize';
import { DefaultAvatar } from "./SVGComponents/DefaultAvatar";
import { FacebookIcon } from "./SVGComponents/FacebookIcon"; 
import { InstagramIcon } from './SVGComponents/InstagramIcon';
import { useRouter } from "next/router";


export default function UserDataBlock({ userData, children }) {
    const { width, height } = useScreenSize();
    const groupIndex = userData?.mbtiType
        ? getMBTIGroupIndex(userData.mbtiType.toString())
        : 0;
    const primaryColor = MBTIColors({ colorDest: "Primary", mbti: groupIndex });
    const secondaryColor = MBTIColors({ colorDest: "Secondary", mbti: groupIndex });
    const extraColor = MBTIColors({ colorDest: "Extra", mbti: groupIndex });
    let router = useRouter();

    const instagramUrl = userData.links?.find(link => link.includes("instagram.com"));
    const facebookUrl = userData.links?.find(link => link.includes("facebook.com"));

    const profilePictureUrl = userData.profilePicture ? `http://localhost:8080/api/photo/${userData.profilePicture}` : "/icon.png";

    useEffect(() => {
        if (userData.profilePicture) {
            console.log("User prof picture:", userData.profilePicture);
        }
    },[])
    const redirectToProfile = () => {
        router.push(`/profile/${userData.nickname}`);
    }
    useEffect(() => {
        console.log("Screen size:", width, height);
    }, [width, height]);
    return(
        
            <div onClick={redirectToProfile} 
            className="flex p-4 rounded-lg shadow-md w-full md:w-2/3 min-w-[285px] mt-[2%] md:ml-[1%]"
                style={{
                    backgroundColor: secondaryColor,
                    borderColor: primaryColor,
                    borderWidth: "2px",
                    borderStyle: "solid",
                }}>
                <div className='w-full'>
                    {userData.profilePicture !== "default.png" && userData.profilePicture !== "default.jpg" && (
                        <img 
                            src={profilePictureUrl} 
                            alt="Profile" 
                            className="w-16 h-16 rounded-full mr-4" 
                        />
                    )}
                    {(userData.profilePicture === "default.png" || userData.profilePicture === "default.jpg") && (
                        <DefaultAvatar className="w-8 h-8" />
                    )}
                    <h2 className="text-2xl font-bold mb-2" style={{ color: primaryColor }}>
                        {userData.name} {userData.surname}
                    </h2>
                    <p className="text-lg mb-2" style={{ color: primaryColor }}>
                        MBTI Type: {userData.mbtiType}
                    </p>
                </div>
                <div className='w-full'>
                { userData.tags && userData.tags.length > 0 && (
                    <div className="flex flex-wrap">
                        {userData.tags.map((tag, index) => index < 5 && (
                            <div key={index} className="w-max-[10%] mx-[1%] mt-[1%] rounded-lg border-2 px-[1%] py-[0.2%]"
                                style={{
                                    backgroundColor: extraColor,
                                    color: primaryColor,
                                    borderColor: primaryColor,
                                }}
                            >
                                <span>{tag.name}</span>
                            </div>
                        ))}
                    </div>
                )}
                {/* Social Media Links */}
                {(instagramUrl || facebookUrl) && (
                    <div className="flex mt-4">
                        {instagramUrl && (
                            <a href={instagramUrl} target="_blank" rel="noopener noreferrer" className="mr-3">
                                <InstagramIcon className="w-8 h-8" fill={primaryColor} />
                            </a>
                        )}
                        {facebookUrl && (
                            <a href={facebookUrl} target="_blank" rel="noopener noreferrer">
                                <FacebookIcon className="w-8 h-8" fill={primaryColor} />
                            </a>
                        )}
                    </div>
                )}
                </div>
                {children && (
                <div className="flex flex-col justify-center items-center ml-4">
                    {children}
                </div>
            )}
            </div>

        
    )
}