import axios from "axios";

const acceptFriendship = async (reciever) => {
    try {
        const response = await axios.get(`http://localhost:8080/api/friendships/me/${reciever}/accept`, {
            headers: {
                'Content-Type': 'text/plain', // Ensure the backend expects this content type
            },
            withCredentials: true // Include credentials if your backend requires them
        });

        if (response.status !== 200) {
            throw new Error(`Failed to accept friendship, status code: ${response.status}`);
        }
        
        console.log('Friendship accepted successfully:', reciever);
        console.log('Response status:', response.status);
        return response.data; // Assuming the response contains confirmation of acceptance

    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            console.error(`Error accepting friendship: Status ${error.response.status}`, error.response.data || error.message);
            throw error; // Re-throw to allow calling code to handle it
        } else {
            console.error('An unexpected error occurred:', error);
            throw error;
        }
    }

}


export default acceptFriendship;