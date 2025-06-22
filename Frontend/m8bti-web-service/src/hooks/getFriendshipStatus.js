import axios from "axios";

const getFriendshipStatus = async (friendNickname) => {
    try {
        const response = await axios.get(`http://localhost:8080/api/friendships/me/${friendNickname}`,
            {
                headers: {
                    'Content-Type': 'application/json', // Ensure the backend expects this content type
                },
                withCredentials: true // Include credentials if your backend requires them
            }
        );

        // This check `if (response.status !== 200)` is technically redundant for Axios
        // because Axios automatically throws an error for non-2xx statuses.
        // If the code reaches here, `response.status` is guaranteed to be 2xx.
        // You can keep it if you want specific handling for non-200 success codes (e.g., 201, 204).
        // But for typical "fetch data" scenarios, if `response.ok` is what you care about,
        // you might just rely on Axios's default error throwing.
        if (response.status !== 200) {
            // This block will practically never be reached for non-2xx due to Axios's behavior
            // unless you have a custom `validateStatus` config.
            throw new Error(`Failed to fetch friendship status, status code: ${response.status}`);
        }

        return response.data; // Assuming the response contains the friendship status

    } catch (error) {
        // Check if it's an Axios error and if a response was received from the server
        if (axios.isAxiosError(error) && error.response) {
            // Now you can safely access error.response.status
            if (error.response.status === 404) {
                console.log("Friendship status not found for user:", friendNickname);
                // Return null or a specific status to indicate "not found"
                return null;
            } else {
                // Handle other HTTP errors (400, 401, 403, 500, etc.)
                console.error(`Error fetching friendship status: Status ${error.response.status}`, error.response.data || error.message);
                throw error; // Re-throw to allow calling code to handle it
            }
        } else if (axios.isAxiosError(error) && error.request) {
            // The request was made but no response was received (network error, server down)
            console.error('Network error or server unreachable:', error.message);
            throw error;
        } else {
            // Any other unexpected error (e.g., syntax error in your code)
            console.error('An unexpected error occurred:', error);
            throw error;
        }
    }
}

export default getFriendshipStatus;