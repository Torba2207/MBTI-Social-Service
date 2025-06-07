import AuthLayout from "./AuthLayout";
import trainTree from "@/hooks/trainTree";
import { useState, useEffect, use } from "react";

export default function MBTITest() {

    const [answerArray, setAnswerArray] = useState([]);
    const [currentQuestion, setCurrentQuestion] = useState(0);
    useEffect(() => {
        trainTree(5)
            .then((response) => {
                console.log("Training completed successfully:", response);
            })
            .catch((error) => {
                console.error("Error during training:", error);
            });
    },[]);
    return (
        <>
        </>
    );
}