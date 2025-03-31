import Link from 'next/link'
import { Logo } from '@/components/Logo'
import clsx from 'clsx'

export default function AuthLayout({children, ...props}){
    return(
        <main className='w-screen h-f min-h-screen'
            style={{
                background:props.mainBGColor,
                transition:'background-color 1s ease-in-out'
            }}
        >
            <div className='w-screen h-[5%] lg:h-[30%] mb-[1%]'>
                <Link href="/" arial-label="Home">
                    <Logo className="w-auto h-auto mx-auto"/>
                </Link>
            </div>
            <div className={clsx(`w-[30%] min-w-[350px] mx-auto border-5 rounded-lg`, props.className)} style={props.style}>
                {children}
            </div>
        </main>
    )
}