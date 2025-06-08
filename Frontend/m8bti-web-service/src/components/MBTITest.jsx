import AuthLayout from "./AuthLayout";
import trainTree from "@/hooks/trainTree";
import { useState, useEffect, use } from "react";
import getMbtiQuestion from "@/hooks/getMbtiQuestion";
import { Button } from "@/components/Button";

export default function MBTITest({primaryColor, secondaryColor, extraColor, transitionDefinition, setMbtiTestState,
    setMbtiType,  ...props}) {

    const [answerArray, setAnswerArray] = useState([]);
    const [currentQuestion, setCurrentQuestion] = useState(0);
    const [isTreeReady, setIsTreeReady] = useState(false);

    const handleAnswer = (answer) => {
        const newAnswerArray = [...answerArray, answer];
        console.log("New answer received:", answer);
        console.log("Updated answer array:", newAnswerArray);
        setAnswerArray(newAnswerArray);
        getMbtiQuestion(newAnswerArray)
                .then((question) => {
                    console.log("Current question:", question);
                    setCurrentQuestion(question);
                    // Here you can set the question in your state or display it
                })
                .catch((error) => {
                    console.error("Error fetching question:", error);
                });

    }

    useEffect(() => {
        trainTree(5)
            .then((response) => {
                console.log("Training completed successfully:", response);
                setIsTreeReady(true);
            })
            .catch((error) => {
                console.error("Error during training:", error);
            });
    },[]);
    useEffect(() => {
        if (isTreeReady) {
            getMbtiQuestion(answerArray)
                .then((question) => {
                    console.log("Current question:", question);
                    setCurrentQuestion(question);
                    // Here you can set the question in your state or display it
                })
                .catch((error) => {
                    console.error("Error fetching question:", error);
                });
        }
    }, [isTreeReady]);
    useEffect(() => {
        if (currentQuestion.mbti) {
            
            console.log("MBTI type determined:", currentQuestion.mbti);
            setMbtiType(currentQuestion.mbti);
            
        }
    }, [currentQuestion]);
    return (
        <>

        {currentQuestion.questionId&&<div className="py-[10%]">
            <div className="text-center text-2xl font-bold mb-4 h-1/2"
                style={{
                    color: primaryColor,
                    transition: transitionDefinition,
                }}
                    >
                {currentQuestion.questionText || "Loading question..."}
            </div>
            <div className="flex justify-between mt-4 h-1/2">
                <Button
                    onClick={() => handleAnswer({questionId:currentQuestion.questionId, isYes:true})}
                    isDynamic={true}
                    currentBG={primaryColor}
                    currentText={secondaryColor}
                    className="ml-[5%]"            
                >
                    Yes
                </Button>
                <Button
                    onClick={() => handleAnswer({questionId:currentQuestion.questionId, isYes:true})}
                    isDynamic={true}
                    currentBG={primaryColor}
                    currentText={secondaryColor}
                    className="mr-[5%]"
                >
                    No
                </Button>
            </div>
        </div>}

        {currentQuestion.mbti&& <div className="h-full w-full">
                <div className="text-center text-2xl font-bold my-auto py-[20%]"
                    style={{
                        color: primaryColor,
                        transition: transitionDefinition,
                    }}>
                    Your MBTI type is: {currentQuestion.mbti}
                </div>
                
                <div className="flex justify-center py-[10%]">
                 <Button
                    onClick={() => setMbtiTestState(false)}
                    isDynamic={true}
                    currentBG={primaryColor}
                    currentText={secondaryColor}
                >
                    Back To Registration Process
                </Button>
                </div>
            </div>
            
        }

        </>
    );
}