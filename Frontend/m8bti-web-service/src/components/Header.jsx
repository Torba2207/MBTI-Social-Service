import { DefaultAvatar } from "./SVGComponents/DefaultAvatar";
import { MBTIColors } from "./MBTIColors";
import { Logo } from "./Logo";

export function Header({mbti}){
    return(
        <header className={"flex w-full h-1/5 border-b-2 border-gray-500"}
            style={{ backgroundColor: MBTIColors({ colorDest: "Secondary", mbti }) }}>
            {/**Logo Div */}
            <div className=" flex w-15/100 h-2/2 flex items-center text-center ml-[5%]">
                <Logo className="auto" />
            </div>
            {/**Full Name Div */}
            <div className="flex h-1/2 w-7/10 items-end">
                <h1 className="w-9/10 text-3xl">Name SureName</h1>
            </div>

            {/**Avatar Div */}
            <div className="w-1/10 h-1/2 flex items-end">
                <DefaultAvatar className="w-8 h-8 mb=2"/>
            </div>

        </header>
    )
}