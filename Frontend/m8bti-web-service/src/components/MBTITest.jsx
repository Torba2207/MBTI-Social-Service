import AuthLayout from "./AuthLayout";
import trainTree from "@/hooks/trainTree";
import { useState, useEffect, use } from "react";
import getMbtiQuestion from "@/hooks/getMbtiQuestion";
import { Button } from "@/components/Button";

export default function MBTITest() {

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
    return (
        <>

        {currentQuestion.questionId&&<div>
            <span>
                {currentQuestion.questionText || "Loading question..."}
            </span>
            <Button
               onClick={() => handleAnswer({questionId:currentQuestion.questionId, isYes:true})} 
            >
                Yes
            </Button>
            <Button
                onClick={() => handleAnswer({questionId:currentQuestion.questionId, isYes:true})}
            
            >
                No
            </Button>
        </div>}

        {currentQuestion.mbti&&<div>
            <span>
                Your MBTI type is: {currentQuestion.mbti}
            </span>
        </div>}

        </>
    );
}