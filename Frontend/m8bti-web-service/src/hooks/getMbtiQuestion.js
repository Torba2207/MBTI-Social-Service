import axios from "axios";

const getMbtiQuestion = async (answerArray) => {
    try{
        const response = await axios.post('http://localhost:8080/api/mbti/step', 
            JSON.stringify(answerArray), // Send the answers as part of the request body
        {
            headers: {
                'Content-Type': 'application/json', // Ensure the backend expects this content type
            },
            withCredentials: true // Include credentials if your backend requires them
        });
        
        
        if (response.status !== 200) {
            throw new Error(`Failed to fetch MBTI question, status code: ${response.status}`);
        }
        
        return response.data; // Assuming the response contains the question dataq
    }
    catch (error) {
        console.error("Error fetching MBTI question:", error);
        throw error; // Re-throw to allow calling code to handle it
    }
}



export default getMbtiQuestion;