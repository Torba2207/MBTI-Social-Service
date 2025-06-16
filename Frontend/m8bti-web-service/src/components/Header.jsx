import React, { useState, useRef, useEffect } from "react";
import { DefaultAvatar } from "./SVGComponents/DefaultAvatar";
import { MBTIColors } from "./MBTIColors";
import { Logo } from "./Logo";
import { SearchIcon } from "./SVGComponents/SearchIcon";
import { FriendsIcon } from "./SVGComponents/FriendsIcon";
import { NotificationBellIcon } from "./SVGComponents/NotificationBellIcon";
import { HomeIcon } from "./SVGComponents/HomeIcon";
import { useRouter } from "next/router";
import axios from "axios";


export function Header({ mbti, userName = "Lionel Messi", newInvites = 10, ...props }) {
  const [avatarUrl, setAvatarUrl] = useState(null);
  const fileInputRef = useRef(null);
  const bgColor = MBTIColors({ colorDest: "Secondary", mbti });
  const primColor = MBTIColors({ colorDest: "Primary", mbti });
  const [newInvites, setNewInvites] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  let router = useRouter();

  const fetchPendingRequests = async () => {
    try {
      setError(null);
      setLoading(true);
      
      const response = await fetch("http://localhost:8080/api/friendships/me/pending", {
        method: "GET",
        credentials: "include"
      });
      
      if (!response.ok) throw new Error("Failed to fetch friendship requests");
      
      const requests = await response.json();
      setNewInvites(requests.length); 
      
    } catch (error) {
      console.error("Error fetching requests:", error);
      setError("Failed to load friend requests");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchPendingRequests();
   
    const interval = setInterval(fetchPendingRequests, 3000);
    return () => clearInterval(interval);
  }, []);
    
  useEffect(() => {
    async function fetchAvatar() {
      try {
        const res = await axios.get("http://localhost:8080/api/user/me/photo", {
          responseType: 'blob',
          withCredentials: true,
        });

        if (res.status === 200) {
          const blob = res.data;
          const url = URL.createObjectURL(blob);
          setAvatarUrl(url);
        }
      } catch (error) {
        if (error.response && error.response.status === 404) {
          console.log("No profile photo found for the user.");
          setAvatarUrl(null);
        } else {
          console.error("Error fetching avatar:", error);
        }
      }
    }
    fetchAvatar();
  }, []);

  const handleAvatarClick = () => {
    fileInputRef.current?.click();
  };

  const handleFileChange = async (event) => {
    const file = event.target.files[0];
    if (!file) return;

    const formData = new FormData();
    formData.append("image", file);

    try {
      const res = await axios.post("http://localhost:8080/api/user/me/photo/upload", formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
        withCredentials: true,
      });

      if (res.status === 200) {
        alert("Photo uploaded successfully!");
        const updatedRes = await axios.get("http://localhost:8080/api/user/me/photo", {
          responseType: 'blob',
          withCredentials: true,
        });
        if (updatedRes.status === 200) {
          const newBlob = updatedRes.data;
          const newUrl = URL.createObjectURL(newBlob);
          setAvatarUrl(newUrl);
        }
      } else {
        alert("Error uploading photo: " + res.data);
        console.error('Error while loading photo:', res.data);
      }
    } catch (error) {
      console.error("Error uploading:", error);
      if (error.response) {
          alert(`Error uploading photo: ${error.response.status} - ${error.response.data}`);
      } else {
          alert("Error uploading photo. Please try again.");
      }
    }
  };

  return (
    <header
      className="flex flex-col w-full border-b-2 border-gray-500"
      style={{ backgroundColor: bgColor }}
    >
      {/* Logo name avatar */}
      <div className="flex items-center justify-between h-20 px-4">
        <div className="flex items-center space-x-4">
          <Logo className="h-19 w-auto" />
          <div className="flex flex-col"></div>
        </div>
        <div className="relative cursor-pointer" onClick={handleAvatarClick}>
          {avatarUrl ? (
            <img
              src={avatarUrl}
              alt="User avatar"
              className="h-15 w-15 rounded-full object-cover"
            />
          ) : (
            <DefaultAvatar className="h-10 w-10 rounded-full" fill={primColor} />
          )}
          <input
            ref={fileInputRef}
            type="file"
            accept="image/*"
            onChange={handleFileChange}
            className="hidden"
          />
        </div>
      </div>

      {/* icons */}
      <div className="flex justify-center items-center h-16 pb-2 w-full">
        <div className="flex justify-between w-[900%]">
          {/* Home Icon */}
          <div className="flex flex-col items-center group w-[20%]">
            <div
              onClick={() => (window.location.href = `/loginPage`)}
              className="relative p-2 rounded-full transition-all duration-200 group-hover:brightness-108 group-hover:bg-opacity-100"
              style={{ backgroundColor: bgColor, backgroundOpacity: 0 }}
            >
              <HomeIcon className="h-6 w-6" fill={primColor} />
            </div>
            <span
              className="text-xs font-medium opacity-0 group-hover:opacity-100 transition-opacity duration-200 mt-1"
              style={{ color: primColor }}
            >
              Home
            </span>
          </div>

          {/* Search Icon */}
          <div className="flex flex-col items-center group w-[20%]">
            <div
              onClick={() => router.push("/searchPage")}
              className="relative p-2 rounded-full transition-all duration-200 group-hover:brightness-108 group-hover:bg-opacity-50"
              style={{ backgroundColor: bgColor, backgroundOpacity: 0 }}
            >
              <SearchIcon bgColor={primColor} className="h-6 w-6" fill={bgColor} />
            </div>
            <span
              className="text-xs font-medium opacity-0 group-hover:opacity-100 transition-opacity duration-200 mt-1"
              style={{ color: primColor }}
            >
              Search
            </span>
          </div>

          {/* Friends Icon */}
           <div onClick={()=>router.push("/friendsPage")} className="flex flex-col items-center group w-[20%]">
            <div className="relative p-2 rounded-full transition-all duration-200 
                          group-hover:brightness-108 group-hover:bg-opacity-50"
                          style={{ backgroundColor: bgColor, backgroundOpacity: 0 }}>
              <FriendsIcon className="h-6 w-6" fill={primColor} />
            </div>
            <span
              className="text-xs font-medium opacity-0 group-hover:opacity-100 transition-opacity duration-200 mt-1"
              style={{ color: primColor }}
            >
              Friends
            </span>
          </div>

          {/* Notification Icon */}
          <div
            onClick={() => router.push("/notificationsPage")}
            className="flex flex-col items-center group w-[20%]"
          >
            <div
              className="relative p-2 rounded-full transition-all duration-200 group-hover:brightness-108 group-hover:bg-opacity-50"
              style={{ backgroundColor: bgColor, backgroundOpacity: 0 }}
            >
              <NotificationBellIcon bgColor={primColor} className="h-6 w-6" fill={bgColor} />
              {newInvites > 0 && (
                <span
                  className="absolute bottom-0 right-0 bg-white text-xs font-bold rounded-full h-5 w-5 flex items-center justify-center shadow"
                  style={{ color: primColor }}
                >
                  {newInvites > 9 ? "9+" : newInvites}
                </span>
              )}
            </div>
            <span
              className="text-xs font-medium opacity-0 group-hover:opacity-100 transition-opacity duration-200 mt-1"
              style={{ color: primColor }}
            >
              Notifications
            </span>
          </div>
        </div>
      </div>
    </header>
  );
}