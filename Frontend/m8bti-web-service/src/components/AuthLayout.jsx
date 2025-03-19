import Link from 'next/link'
import { Logo } from '@/components/Logo'

export default function AuthLayout({children}){
    return(
        <main>
            <div className='flex w-full h-41/100 px-[20%]'>
                <Link href="/" arial-label="Home" className='mx-auto'>
                    <Logo className="h-full"/>
                </Link>
            </div>
            <div className='mx-[30%] px-[5%] bg-[#D6ECE3]'>
                {children}
            </div>
        </main>
    )
}