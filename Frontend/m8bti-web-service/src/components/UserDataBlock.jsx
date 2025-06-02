import React from 'react';
import { MBTIMap } from './MBTIMap';
import { MBTIColors } from './MBTIColors';



export default function UserDataBlock({ userData }) {
    const groupIndex = userData?.mbtiType
        ? MBTIMap.indexOf(userData.mbtiType.toString())
        : 0;
    const primaryColor = MBTIColors({ colorDest: "Primary", mbti: groupIndex });
    const secondaryColor = MBTIColors({ colorDest: "Secondary", mbti: groupIndex });
    const extraColor = MBTIColors({ colorDest: "Extra", mbti: groupIndex });
    return(
        
            <div className="flex p-4 rounded-lg shadow-md w-2/3 mt-[2%] ml-[1%]"
                style={{
                    backgroundColor: secondaryColor,
                    borderColor: primaryColor,
                    borderWidth: "2px",
                    borderStyle: "solid",
                }}>
                <div className='w-full'>
                    <img src={userData.profilePicture || "/icon.png"} alt="Profile" className="w-16 h-16 rounded-full mr-4" />
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
                        {userData.tags.map((tag, index) => (
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
                </div>
            </div>

        
    )
}