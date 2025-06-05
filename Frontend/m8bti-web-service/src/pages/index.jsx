import Head from 'next/head'
import { Header } from '@/components/Header'

export default function Home(){
    return(
        <>
        <Head>
            <title>M8TI - Find your TypeM8</title>
            <meta
                name="description"
                content="Social Service To Find New Friends based on Your personality type"
            />
        </Head>
        <div className="h-screen w-full">
        {/*/<Header mbti={2} />*/}
        </div>
        </>
    )
}