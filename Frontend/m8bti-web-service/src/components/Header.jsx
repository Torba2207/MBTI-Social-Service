import { DefaultAvatar } from "./SVGComponents/DefaultAvatar";
import { MBTIColors } from "./MBTIColors";
import { Logo } from "./Logo";
import { SearchIcon } from "./SVGComponents/SearchIcon";
import { FriendsIcon } from "./SVGComponents/FriendsIcon";
import { NotificationBellIcon } from "./SVGComponents/NotificationBellIcon";

export function Header({ mbti, userName = "Lionel Messi",newInvites = 10 }) {
    return (
      <header 
        className="flex items-center justify-between w-full h-32 px-4 border-b-2 border-gray-500"
        style={{ backgroundColor: MBTIColors({ colorDest: "Secondary", mbti }) }}
      >
        {/*Logo */}
        <div className="flex items-center space-x-4">
          <Logo className="h-20 w-auto" />
        </div>
  
        {/*"FIND YOUR TYPE" text */}
        <div className="flex justify-center">
          <h2 className="hidden sm:inline text-xl font-semibold">FIND YOUR TYPE</h2>
        </div>
  
        {/* Right section - User info and icons */}
        <div className="flex items-center space-x-4">
          <div className="flex space-x-3">
            <div className="relative">
              <SearchIcon className="h-6 w-auto" />
            </div>
            
            <div className="relative">
              <FriendsIcon className="h-6 w-auto" />
            </div>
            
            <div className="relative">
              <NotificationBellIcon className="h-6 w-auto" />
              {newInvites/10 < 1 && (
                <span className="absolute -top-2 -right-2 bg-red-500 text-white text-xs rounded-full h-5 w-5 flex items-center justify-center">
                  {newInvites}
                </span>
              )}
             {newInvites/10 >= 1 && (
                <span className="absolute -top-2 -right-2 bg-red-500 text-white text-xs rounded-full h-5 w-5 flex items-center justify-center">
                  {"9+"}
                </span>
              )}

            </div>
          </div>

          
          <div className="flex items-center space-x-2">
            <span className="text-xl font-medium">{userName}</span>
            <DefaultAvatar className="h-10 w-auto" />
          </div>
        </div>
      </header>
    );
}  