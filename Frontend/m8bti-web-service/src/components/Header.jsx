import { DefaultAvatar } from "./SVGComponents/DefaultAvatar";
import { MBTIColors } from "./MBTIColors";

export function Header({mbti}){
    return(
        <header className={"flex w-full h-1/5"}
            style={{ backgroundColor: MBTIColors({ colorDest: "Secondary", mbti }) }}>
            {/**Logo Div */}
            <div className=" flex w-2/10 h-1/2 flex items-center text-center ml-[5%]">
                <h2>
                    M8TI LOGO
                </h2>
            </div>
            {/**Full Name Div */}
            <div className="flex h-1/2 w-6/10">
                Name SureName
            </div>
            {/**Avatar Div */}
            <div className="w-2/10 h-1/2">
                <DefaultAvatar className="w-8 h-8"/>
            </div>

        </header>
    )
}