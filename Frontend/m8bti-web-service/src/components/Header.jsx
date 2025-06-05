import { DefaultAvatar } from "./SVGComponents/DefaultAvatar";
import { MBTIColors } from "./MBTIColors";
import { Logo } from "./Logo";
import { SearchIcon } from "./SVGComponents/SearchIcon";
import { FriendsIcon } from "./SVGComponents/FriendsIcon";
import { NotificationBellIcon } from "./SVGComponents/NotificationBellIcon";
import { HomeIcon } from "./SVGComponents/HomeIcon";
import { useRouter } from "next/router";

export function Header({ mbti, userName = "Lionel Messi", newInvites = 10, ...props }) {
  const bgColor = MBTIColors({ colorDest: "Secondary", mbti });
  const primColor = MBTIColors({ colorDest: "Primary", mbti });
  let router = useRouter();
  
  return (
    <header 
      className="flex flex-col w-full border-b-2 border-gray-500"
      style={{ backgroundColor: bgColor }}
    >
      {/* Logo name avatar */}
      <div className="flex items-center justify-between h-20 px-4">
        <div className="flex items-center space-x-4">
          <Logo className="h-19 w-auto" />
          <div className="flex flex-col">
            <span className="hidden md:block text-3xl font-semibold truncate max-w-[70vw] md:max-w-none">
              {userName}
            </span>
          </div>
        </div>
        <DefaultAvatar className="h-10 w-auto" />
      </div>

      {/* icons */}
      <div className="flex justify-center items-center h-16 pb-2 w-full">
        <div className="flex justify-between w-[900%]"> 
          {/* Home Icon */}
          <div className="flex flex-col items-center group w-[20%]">
            <div onClick={()=>window.location.href = `/loginPage`} className="relative p-2 rounded-full transition-all duration-200 
                          group-hover:brightness-150 group-hover:bg-opacity-50"
                          style={{ backgroundColor: bgColor }}>
              <HomeIcon className="h-6 w-6" />
            </div>
            <span className="text-xs text-purple-900 font-medium opacity-0 
                           group-hover:opacity-100 transition-opacity duration-200 mt-1">
              Home
            </span>
          </div>
          
          {/* Search Icon */}
          <div className="flex flex-col items-center group w-[20%]">
            <div onClick={()=>router.push("/searchPage")} className="relative p-2 rounded-full transition-all duration-200 
                          group-hover:brightness-150 group-hover:bg-opacity-50"
                          style={{ backgroundColor: bgColor }}>
              <SearchIcon className="h-6 w-6" />
            </div>
            <span className="text-xs text-purple-900 font-medium opacity-0 
                           group-hover:opacity-100 transition-opacity duration-200 mt-1">
              Search
            </span>
          </div>
          
          {/* Friends Icon */}
          <div className="flex flex-col items-center group w-[20%]">
            <div className="relative p-2 rounded-full transition-all duration-200 
                          group-hover:brightness-150 group-hover:bg-opacity-50"
                          style={{ backgroundColor: bgColor }}>
              <FriendsIcon className="h-6 w-6" />
            </div>
            <span className="text-xs text-purple-900 font-medium opacity-0 
                           group-hover:opacity-100 transition-opacity duration-200 mt-1">
              Friends
            </span>
          </div>
          
          {/* Notification Icon */}
          <div className="flex flex-col items-center group w-[20%]">
            <div className="relative p-2 rounded-full transition-all duration-200 
                          group-hover:brightness-150 group-hover:bg-opacity-50"
                          style={{ backgroundColor: bgColor }}
                  >
              <NotificationBellIcon className="h-6 w-6" />
              {newInvites > 0 && (
                <span className="absolute bottom-0 right-0 bg-white text-purple-700 
                               text-xs font-bold rounded-full h-5 w-5 flex items-center 
                               justify-center shadow">
                  {newInvites > 9 ? "9+" : newInvites}
                </span>
              )}
            </div>
            <span className="text-xs text-purple-900 font-medium opacity-0 
                           group-hover:opacity-100 transition-opacity duration-200 mt-1">
              Notifications
            </span>
          </div>
        </div>
      </div>
    </header>
  );
}