import clsx from "clsx";

export function ProfileMain({primaryColor,secondaryColor,extraColor,mbti,nickname,currentUser, userAbout,
    ...props}){
    return(
        <div  className={clsx(`w-screen h-f min-h-screen bg-[${secondaryColor}]`)}>
            <h1 className={clsx(`text-2xl font-bold text-[${primaryColor}]`)}>About {currentUser===nickname?"You":nickname}</h1>
        </div>
    );
}