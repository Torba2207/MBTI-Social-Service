import { MBTIColors } from "./MBTIColors";
import { Logo } from "./Logo";

export function LogHeader({mbti}){
    return(
        <header className={"flex w-full h-1/5 border-b-2 border-gray-500"}
            style={{ backgroundColor: MBTIColors({ colorDest: "Secondary", mbti }) }}>
            {/**Logo Div */}
            <div className=" flex w-15/100 h-2/2 flex items-center text-center ml-[5%]">
                <Logo className="w-69 h-28" />
            </div>
            
        </header>
    )
}