import Head from 'next/head'
import { Button } from '@/components/Button'
import Image from 'next/image'

export default function Home() {
  return (
    <>
      <Head>
        <title>Find Your Type</title>
      </Head>
      <div className="min-h-screen bg-gray-200 px-4 sm:px-8 py-8 sm:py-12 flex flex-col">
        <div className="text-xl sm:text-2xl font-black tracking-widest text-center sm:text-left mb-8 sm:mb-2">
          MBTI
        </div>
        <hr className="border-t-2 sm:border-t-4 border-gray-300 w-full my-6 sm:my-8" />
        
        {/* Mobile & Tablet */}
        <div className="lg:hidden w-full flex justify-center mb-6 sm:mb-8">
          <div className="relative w-full max-w-[300px] sm:max-w-[400px] aspect-square mx-auto">
            <Image 
              src="/icon.png" 
              alt="MBTI Illustration"
              fill
              className="object-contain"
              sizes="(max-width: 1024px) 100vw, 50vw"
            />
          </div>
        </div>
        
        <div className="flex flex-col lg:flex-row items-center justify-center flex-1 gap-8 lg:gap-12 mt-4 sm:mt-8 lg:mt-0">
          {/* Text */}
          <div className="max-w-2xl lg:max-w-4xl w-full flex flex-col items-center lg:items-start text-center lg:text-left">
            <h1 className="text-3xl sm:text-4xl md:text-5xl lg:text-6xl font-bold text-gray-900 mb-6 lg:mb-28 leading-tight sm:leading-tight">
              Find and connect with<br />
              people who truly get you.
            </h1>

            <p className="text-base sm:text-lg md:text-2xl lg:text-3xl font-semibold text-gray-800 mb-3 sm:mb-4">
              Tired of small talk? Connect with like-minded people.
            </p>
            <p className="text-sm sm:text-base md:text-xl lg:text-2xl text-gray-700 mb-6 sm:mb-8">
              Join a community where your unique personality is the starting point for meaningful
              conversations, friendships, and more – all based on your MBTI type.
            </p>

            <div className="flex flex-col sm:flex-row gap-3 sm:gap-4 w-full justify-center lg:justify-start">
              <Button 
                className="bg-black text-white px-4 sm:px-6 py-2 sm:py-3 rounded-md hover:bg-gray-900 text-sm sm:text-base"
                onClick={()=>window.location.href = `/loginPage`}>
                Login
              </Button>
              <Button 
                className="bg-black text-white px-4 sm:px-6 py-2 sm:py-3 rounded-md hover:bg-gray-900 text-sm sm:text-base"
                onClick={()=>window.location.href = `/signupPage`}>
                Sign Up
              </Button>
            </div>
          </div>

          {/* Desktop */}
          <div className="hidden lg:flex w-full max-w-md lg:max-w-2xl justify-center items-center">
            <div className="relative w-full max-w-[500px] aspect-square mx-auto">
              <Image 
                src="/icon.png" 
                alt="MBTI Illustration"
                fill
                className="object-contain"
                sizes="(max-width: 1024px) 100vw, 50vw"
              />
            </div>
          </div>
        </div>
      </div>
    </>
  )
}