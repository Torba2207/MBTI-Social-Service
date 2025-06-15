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
          </div>
        </div>
        <DefaultAvatar className="h-10 w-auto" fill={primColor}/>
      </div>

      {/* icons */}
      <div className="flex justify-center items-center h-16 pb-2 w-full">
        <div className="flex justify-between w-[900%]"> 
          {/* Home Icon */}
          <div className="flex flex-col items-center group w-[20%]">
            <div onClick={()=>window.location.href = `/loginPage`} className="relative p-2 rounded-full transition-all duration-200 
                          group-hover:brightness-108 group-hover:bg-opacity-100"
                          style={{ backgroundColor: bgColor, backgroundOpacity: 0 }}>
              <HomeIcon className="h-6 w-6" fill={primColor}/>
            </div>
            <span className="text-xs font-medium opacity-0 
                           group-hover:opacity-100 transition-opacity duration-200 mt-1"
                           style={{ color: primColor }}>
              Home
            </span>
          </div>
          
          {/* Search Icon */}
          <div className="flex flex-col items-center group w-[20%]">
            <div onClick={()=>router.push("/searchPage")} className="relative p-2 rounded-full transition-all duration-200 
                          group-hover:brightness-108 group-hover:bg-opacity-50"
                          style={{ backgroundColor: bgColor, backgroundOpacity: 0 }}>
              <SearchIcon bgColor={primColor} className="h-6 w-6" fill={bgColor}/>
            </div>
            <span className="text-xs font-medium opacity-0 
                           group-hover:opacity-100 transition-opacity duration-200 mt-1"
                           style={{ color: primColor }}>
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
            <span className="text-xs font-medium opacity-0 
                           group-hover:opacity-100 transition-opacity duration-200 mt-1"
                           style={{ color: primColor }}>
              Friends
            </span>
          </div>
          
          {/* Notification Icon */}
          <div onClick={()=>router.push("/notificationsPage")} className="flex flex-col items-center group w-[20%]">
            <div className="relative p-2 rounded-full transition-all duration-200 
                          group-hover:brightness-108 group-hover:bg-opacity-50"
                          style={{ backgroundColor: bgColor, backgroundOpacity: 0 }}
                  >
              <NotificationBellIcon bgColor={primColor} className="h-6 w-6" fill={bgColor} />
              {newInvites > 0 && (
                <span className="absolute bottom-0 right-0 bg-white 
                               text-xs font-bold rounded-full h-5 w-5 flex items-center 
                               justify-center shadow"
                               style={{ color: primColor}}>
                  {newInvites > 9 ? "9+" : newInvites}
                </span>
              )}
            </div>
            <span className="text-xs font-medium opacity-0 
                           group-hover:opacity-100 transition-opacity duration-200 mt-1"
                           style={{color: primColor}}>
              Notifications
            </span>
          </div>
        </div>
      </div>
    </header>
  );
}