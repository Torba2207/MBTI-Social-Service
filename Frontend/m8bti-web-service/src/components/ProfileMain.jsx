import clsx from "clsx";
import { TextField } from "./Fields";
export function ProfileMain({primaryColor,secondaryColor,extraColor,mbti,nickname,currentUser, userAbout,
    ...props}){
    return(
        <div  className="w-f h-f">
            <div className="pl-[15%] pt-[5%]">
                <h1 className={clsx(`text-2xl font-bold text-[${primaryColor}]`)}>
                    About {currentUser===nickname?"You":nickname}
                </h1>
                <TextField className={`w-[60%]`}/>
            </div>
        </div>
    );
}