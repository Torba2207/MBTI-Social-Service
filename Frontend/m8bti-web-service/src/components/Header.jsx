import { DefaultAvatar } from "./SVGComponents/DefaultAvatar";
import { MBTIColors } from "./MBTIColors";
import { Logo } from "./Logo";
import { SearchIcon } from "./SVGComponents/SearchIcon";
import { FriendsIcon } from "./SVGComponents/FriendsIcon";
import { NotificationBellIcon } from "./SVGComponents/NotificationBellIcon";

export function Header({ mbti, userName = "Lionel Messi", newInvites = 10 }) {
  return (
    <header 
      className="flex flex-col w-full border-b-2 border-gray-500"
      style={{ backgroundColor: MBTIColors({ colorDest: "Secondary", mbti }) }}
    >
      {/* Logo name avatar */}
      <div className="flex items-center justify-between h-20 px-4">
        <div className="flex items-center space-x-4">
          <Logo className="h-19 w-auto" />
          <div className="flex flex-col">
            <span className="text-3xl font-semibold">{userName}</span>
          </div>
        </div>
        <DefaultAvatar className="h-10 w-auto" />
      </div>

      {/* icons */}
      <div className="flex justify-center items-center h-12 pb-2">
        <div className="flex space-x-124"> {/* distance between the icons*/}
          <div className="relative">
            <SearchIcon className="h-6 w-6" />
            
          </div>
          
          <div className="relative">
            <FriendsIcon className="h-6 w-6" />
          </div>
          
          <div className="flex flex-col items-center space-y-1">
              <div className="relative rounded-full px-4 py-2"
              style={{ backgroundColor: MBTIColors({ colorDest: "Primary", mbti }) }}>
                <NotificationBellIcon className="h-5 w-5 " />
                {newInvites > 0 && (
                  <span className="absolute bottom-0 right-0 bg-white text-purple-700 text-xs font-bold rounded-full h-5 w-5 flex items-center justify-center shadow">
                    {newInvites > 9 ? "9+" : newInvites}
                  </span>
                )}
              </div>
            <span className="text-xs text-purple-900 font-medium">New Invites</span>
          </div>

        </div>
      </div>
    </header>
  );
}