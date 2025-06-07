import axios from "axios";


//Version with Axios
const trainTree = async (depth) => {
    try {
        const response = await axios.post('http://localhost:8080/api/mbti/train',
            null,
            {
                params: { depth: depth },
                headers: {
                    'Content-Type': 'application/json', // Ensure the backend expects this content type
                },
                withCredentials: true // Include credentials if your backend requires them
            }
        );
        console.log("Training response:", response.data);
        console.log("Training completed successfully with depth:", depth);
        console.log("Response status:", response.status);   
        return response.data;
    } catch (error) {
        // Axios provides detailed error information
        if (axios.isAxiosError(error)) {
            console.error('Error training MBTI model:', error.message);
            if (error.response) {
                // The server responded with a status outside of 2xx
                console.error('Server responded with:', error.response.status);
                // Try to log the error message from the server if available
                console.error('Server error data:', error.response.data);
            } else if (error.request) {
                // The request was made but no response was received
                console.error('No response received from server.');
            }
        } else {
            // Something else entirely went wrong
            console.error('An unexpected error occurred:', error);
        }
        throw error; // Re-throw to allow calling code to handle it
    }

}

/*
//Version with fetch API
const trainTree = async(depth) => {
    try{
        const url = new URL('http://localhost:8080/api/mbti/train');
        url.searchParams.append('depth', depth); // Adds ?depth=X to the URL
        const response = await fetch(url.toString(), {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json', // Ensure the backend expects this content type
            },
            body: JSON.stringify({}), // Send depth as part of the request body
            //credentials: 'include' // Include credentials if your backend requires them
        });
        if (!response.ok) {
            let errorText = 'Something went wrong on the server.';
            // Try to read the response body as text for more specific error messages
            try {
                errorText = await response.text();
            } catch (e) {
                // Ignore if response.text() fails (e.g., no body)
            }
            throw new Error(`Request failed with status ${response.status}: ${errorText}`);
        }
        const data = await response.json(); // This will parse the JSON string, e.g., "Model successfully trained"

        console.log('Model training successful:', data);
        console.log('Response status:', response.status);

        return data;
    }catch(error){
        console.error("Error during training:", error);
        throw error; // Re-throw to allow calling code to handle it
    }
}
    */

export default trainTree;