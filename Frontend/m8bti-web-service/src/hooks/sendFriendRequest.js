const sendFriendRequest = async (reciever) => {
    try {
        const response = await fetch(`http://localhost:8080/api/friendships/me/${reciever}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json', // Keep this if your backend expects it for the request, though body is empty.
            },
            body: JSON.stringify({}), // You can also omit the body if the backend truly doesn't need it.
            credentials: 'include'
        });

        if (!response.ok) {
            // It's good practice to read the error response as text too,
            // in case the server sends a plain text error message.
            const errorText = await response.text();
            throw new Error(`Network response was not ok: ${response.status} - ${errorText}`);
        }

        // *** CHANGE THIS LINE ***
        const data = await response.text(); // Use .text() because the backend sends text/plain

        console.log('Friend request sent successfully:', data); // data will now be the plain text string
        console.log('Friend request sent successfully to:', reciever);
        console.log('Response status:', response.status);
    } catch (error) {
        console.error('There was a problem with the fetch operation:', error);
    }
};

export default sendFriendRequest;