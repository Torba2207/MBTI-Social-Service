import Link from 'next/link'
import { Logo } from '@/components/Logo'
import clsx from 'clsx'

export default function AuthLayout({children, ...props}){
    return(
        <main className='w-screen h-screen bg-[#E7DFEA] min-h-screen'>
            <div className='w-screen h-[30%] mb-[1%]'>
                <Link href="/" arial-label="Home">
                    <Logo className="w-auto h-auto mx-auto"/>
                </Link>
            </div>
            <div className={clsx(`w-[30%] min-w-[350px] mx-auto bg-[#F4EDF6] border-5 border-[#785D87] rounded-lg`, props.className)}>
                {children}
            </div>
        </main>
    )
}