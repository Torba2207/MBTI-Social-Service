import clsx from "clsx";
import { TextField } from "./Fields";
import useScreenSize from "@/hooks/useScreenSize";
import { Button } from "./Button";
import { useState, useEffect, use } from "react";
import axios from "axios";
import TagPopUp from "./TagPopUp";
import sendFriendRequest from "@/hooks/sendFriendRequest";
import getFriendshipStatus from "@/hooks/getFriendshipStatus";
import deleteFriendship from "@/hooks/deleteFriendship";
import acceptFriendship from "@/hooks/acceptFriendship";

export function ProfileMain({ primaryColor, secondaryColor, extraColor, mbti, nickname, currentUser, userAbout, userTags, userLinks, ...props }) {
    const { width, height } = useScreenSize();
    const [aboutText, setAboutText] = useState(userAbout || "");
    const [usersTags, setUsersTags] = useState(userTags || []);
    const [oldAboutText, setOldAboutText] = useState(aboutText || "");
    const [isPopUpOpen, setIsPopUpOpen] = useState(false);
    const [friendshipStatus, setFriendshipStatus] = useState(null);
    const [instagramUrl, setInstagramUrl] = useState(userLinks[0] || "");
    const [facebookUrl, setFacebookUrl] = useState(userLinks[1] || "");
    const [oldInstagramUrl, setOldInstagramUrl] = useState(instagramUrl || "");
    const [oldFacebookUrl, setOldFacebookUrl] = useState(facebookUrl || "");

    const handleSave = async () => {
        try {
            const response = await axios.put("http://localhost:8080/api/user/me", {
                description: aboutText,
            },
            {
                withCredentials: true,
            });
            console.log("Profile updated:", response.data);
            setOldAboutText(aboutText);
        } catch (error) {
            console.error("Error updating profile:", error);
            alert("Failed to update profile.");
        }
    };

    const handleSaveSocialMediaLinks = async () => {
        try {
            const updatedLinks = [];
            if (instagramUrl) updatedLinks.push(instagramUrl);
            if (facebookUrl) updatedLinks.push(facebookUrl);

            if (userLinks) {
                userLinks.forEach(link => {
                    if (!link.includes("instagram.com") && !link.includes("facebook.com")) {
                        updatedLinks.push(link);
                    }
                });
            }

            const response = await axios.put("http://localhost:8080/api/user/me", {
                links: updatedLinks
            },
            {
                withCredentials: true,
            });
            console.log("Social media links updated:", response.data);
            setOldInstagramUrl(instagramUrl);
            setOldFacebookUrl(facebookUrl);
        } catch (error) {
            console.error("Error updating social media links:", error);
            alert("Failed to update social media links.");
        }
    };

    const handleFriendshipRequest = async (friendshipRequest) => {
        try {
            await friendshipRequest(nickname);
            const status = await getFriendshipStatus(nickname);
            setFriendshipStatus(status);
            console.log("Friend request sent and status updated:", status);
        } catch (error) {
            console.error("Failed to send friend request or update status:", error);
        }
    };

    useEffect(() => {
        setAboutText(userAbout || "");
    }, [userAbout]);

    useEffect(() => {
        setUsersTags(userTags || []);
    }, [userTags]);

    useEffect(() => {
        if (userLinks) {
            const instagramLink = userLinks.find(link => link.includes("instagram.com"));
            const facebookLink = userLinks.find(link => link.includes("facebook.com"));
            setInstagramUrl(instagramLink || "");
            setFacebookUrl(facebookLink || "");
        }
    }, [userLinks]);

    useEffect(() => {
        if (currentUser !== nickname) {
            getFriendshipStatus(nickname)
                .then((status) => {
                    console.log("Friendship status with", nickname, ":", status);
                    setFriendshipStatus(status);
                })
                .catch((error) => {
                    console.error("Error fetching friendship status:", error);
                });
        }   
    }, [currentUser, nickname]);
    
    return(
        <div className={`w-full h-full`}>
            {currentUser !== nickname && (
                <div className="mt-[1%] ml-[10%]">
                    {friendshipStatus === null && (
                        <Button 
                            onClick={() => (handleFriendshipRequest(sendFriendRequest))}
                            color={props.mbtiGroupIndex}>
                            Send Friend Request
                        </Button>
                    )}
                    {friendshipStatus && friendshipStatus.isPending && friendshipStatus.senderNickname === currentUser && (
                        <Button
                            onClick={() => (handleFriendshipRequest(deleteFriendship))}
                            color={props.mbtiGroupIndex}
                        >
                            Cancel Request
                        </Button>
                    )}
                    {friendshipStatus && friendshipStatus.isPending && friendshipStatus.receiverNickname === currentUser && (
                        <div>
                            <Button
                                onClick={() => handleFriendshipRequest(acceptFriendship)}
                                color={props.mbtiGroupIndex}
                            >
                                Accept Request
                            </Button>
                            <Button
                                onClick={() => handleFriendshipRequest(deleteFriendship)}
                                color={props.mbtiGroupIndex}
                            >
                                Reject Request
                            </Button>
                        </div>
                    )}
                    {friendshipStatus && !friendshipStatus.isPending && (
                        <Button
                            onClick={() => handleFriendshipRequest(deleteFriendship)}
                            color={props.mbtiGroupIndex}
                        >
                            Unfriend
                        </Button>
                    )}
                </div>
            )}
            <div 
                className={`pl-[15%] pt-[4%] h-[80%]`}
            >
                <h1 
                    className="md:text-2xl font-bold"
                    style={{
                        color:primaryColor,
                    }}
                >
                    About {currentUser === nickname ? "You" : props.name + " " + props.surname + "(" + nickname + ")"}
                </h1>
                <textarea readOnly={currentUser !== nickname}
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
                <div className={currentUser !== nickname || aboutText === oldAboutText ? "hidden" : ""}> 
                    <Button color={mbti} onClick={handleSave}>Save</Button>
                </div>
                    <div className="mt-4">
                        <h2 className="md:text-xl font-bold" style={{ color: primaryColor }}>Social Media Links</h2>
                        <TextField
                            readOnly={currentUser !== nickname}
                            label="Instagram URL"
                            value={instagramUrl}
                            onChange={(e) => setInstagramUrl(e.target.value)}
                            className="mt-2 w-[70%]"
                            placeholder="Share your Instagram"
                            labelColor={primaryColor}
                            fieldBGColor={extraColor}
                            inputBorderColor={primaryColor}
                        />
                        <TextField
                            readOnly={currentUser !== nickname}
                            label="Facebook URL"
                            value={facebookUrl}
                            onChange={(e) => setFacebookUrl(e.target.value)}
                            className="mt-2 w-[70%]"
                            placeholder="Share your Facebook"
                            labelColor={primaryColor}
                            fieldBGColor={extraColor}
                            inputBorderColor={primaryColor}
                        />
                        <div className={clsx(
                            "mt-2",
                            currentUser !== nickname || (instagramUrl === oldInstagramUrl && facebookUrl === oldFacebookUrl) ? "hidden" : ""
                        )}>
                            <Button color={mbti} onClick={handleSaveSocialMediaLinks}>Save Social Media Links</Button>
                        </div>
                    </div>
            </div>
            
            <div>
                {usersTags !== null && usersTags !== undefined && usersTags.length > 0 && (
                    <div className="w-[70%] flex flex-row flex-wrap items-center ml-[14%]">
                        {usersTags.map((tag, id) => (
                            <div 
                                className="w-max-[10%] mx-[1%] mt-[1%] rounded-lg border-2 px-[1%] py-[0.2%]" key={id}
                                style={{
                                    backgroundColor: extraColor,
                                    color: primaryColor,
                                    borderColor: primaryColor,
                                }}>
                                {tag.name}
                            </div>
                        ))}
                    </div>
                )}
                <div className={currentUser !== nickname ? "hidden" : "" + "flex flex-row items-center ml-[14.6%] mt-[1%]"}> 
                    <Button color={mbti} onClick={() => setIsPopUpOpen(!isPopUpOpen)}> Edit Tags</Button>
                </div>
            </div>
            
            <div className={isPopUpOpen ? "" : "hidden"}>
                <TagPopUp primaryColor={primaryColor} secondaryColor={secondaryColor} extraColor={extraColor} 
                    mbti={mbti} nickname={nickname} currentUser={currentUser} userTags={usersTags}
                    setIsPopUpOpen={setIsPopUpOpen} isPopUpOpen={isPopUpOpen} screenHeight={height} screenWidth={width}
                    onTagsUpdated={(newTags) => setUsersTags(newTags)}/>
            </div>
        </div>
    );
}