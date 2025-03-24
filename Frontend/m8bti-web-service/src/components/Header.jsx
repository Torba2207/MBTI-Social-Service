import { DefaultAvatar } from "./SVGComponents/DefaultAvatar";
import { MBTIColors } from "./MBTIColors";
import { Logo } from "./Logo";

export function Header({mbti}){
    return(
        <header className={"flex w-full h-1/5 border-b-2 border-gray-500"}
            style={{ backgroundColor: MBTIColors({ colorDest: "Secondary", mbti }) }}>
            {/**Logo Div */}
            <div className="flex w-1/3 min-w-[200px] h-1/2 min-h-[40px]">
                <Logo className="w-auto h-auto" />
            </div>
            {/**Full Name Div */}
            <div className="flex h-1/2 w-7/10 align-top">
                <h1 className="w-9/10 text-center text-2xl sm:text-3xl sm:text-left">Name SureName</h1>
            </div>

            {/**Avatar Div */}
            <div className="w-1/10 h-1/2 flex items-top">
                <DefaultAvatar className="w-8 h-8 mb=2"/>
            </div>

        </header>
    )
}